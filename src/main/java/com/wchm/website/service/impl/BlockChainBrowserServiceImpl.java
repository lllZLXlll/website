package com.wchm.website.service.impl;

import com.wchm.website.service.BlockChainBrowserService;
import com.wchm.website.util.DateUtil;
import com.wchm.website.util.HttpUtils;
import com.wchm.website.util.PCT;
import com.wchm.website.util.Result;
import com.wchm.website.vo.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 官网区块链浏览器模块所有接口的实现写在此类下
 */

@Service
public class BlockChainBrowserServiceImpl implements BlockChainBrowserService {

    /**
     * 查询合约交易记录api地址，固定不变
     */
    @Value("${wchm.address-transactions-url}")
    private String addressTransactionsUrl;

    @Value("${wchm.company-address}")
    private String companyAddress;

    @Value("${wchm.contract-address}")
    private String contractAddress;

    @Value("${wchm.keystore-address}")
    private String keystoreAddress;

    @Value("${wchm.keystore-password}")
    private String keystorePassword;

    @Value("${wchm.infura-api}")
    private String infuraApi;

    @Value("${wchm.method-id}")
    private String methodId;

    // 10的18次方
    private BigDecimal _10_18 = new BigDecimal(Math.pow(10, 18));

    @Override
    public Result queryIndexData() throws Exception {
        // 返回vo对象
        BlockChainVo vo = new BlockChainVo();

        // PCT持币人数
//        vo.setAddressCount(getPCTUserCount());

        // 查询最新交易信息
        List<com.wchm.website.vo.Transaction> tranList = queryIndexDataTransaction();
        vo.setTranList(tranList);

        /**
         * 查询最新区块，第一版暂时不要
         */
//        Set<Block> blockList = queryIndexDataBlock();
//        vo.setBlockList(blockList);

        return Result.create().success(vo);
    }


    /**
     * 业务需求：统计持有PCT总用户人数
     * 实现方法：
     * 1.以太坊中已有总计每个币种有多少持币人数，爬取页面中的数据返回到前端即可。
     * 2.利用第三方节点监听以太坊区块，把PCT交易的记录保存到数据库，在做出统计去重之后能得到人数。
     * <p>
     * 具体实现：
     * 选择使用方案1，原因是直接查询以太坊的数据，数据实时性高并且准确无误，而且开发时间成本、难度也很低。
     * 而方案2实现要更多时间成本，且数据不能做到完全实时性，因为有个统计的过程。
     */
    private Long getPCTUserCount() {
        // TODO

        return null;
    }


    /**
     * 查询区块链首页交易列表方法
     *
     * @return <p>返回交易记录数据集合</p>
     */
    public List<com.wchm.website.vo.Transaction> queryIndexDataTransaction() {
        // API
        String apiUrl = fomcatApiUrl(companyAddress, 1, 6);

        List<com.wchm.website.vo.Transaction> tran = new ArrayList<>();
        // 请求以太坊API返回数据
        String response = HttpUtils.get(apiUrl);
        JSONObject json = JSONObject.fromObject(response);

        if (json != null) {
            if ("OK".equals(json.get("message"))) { //查询成功
                // 取返回的交易列表的数组
                JSONArray result = (JSONArray) json.get("result");
                Object arr[] = result.toArray();
                List<Object> data = Arrays.asList(arr);

                // 转换成自己封装的对象返回前台
                for (Object item : data) {
                    JSONObject js = JSONObject.fromObject(item);
                    com.wchm.website.vo.Transaction t = new com.wchm.website.vo.Transaction();
                    t.setHash(js.get("hash").toString());
                    t.setBlockNumber(new BigInteger(js.get("blockNumber").toString()));
                    t.setTimeStamp(new BigInteger(js.get("timeStamp").toString()));
                    tran.add(t);
                }

                return tran;
            }
        }
        return null;
    }

    /**
     * 查询区块链首页最新区块列表方法
     *
     * @return <p>返回最新区块数据集合</p>
     */
    public Set<Block> queryIndexDataBlock() throws Exception {
        // 地址是以太坊节点,一个第三方的以太坊节点,通过此节点才能访问以太坊网络上的数据
        Web3j web3 = Web3j.build(new HttpService(infuraApi));

        // 查询最新区块
        DefaultBlockParameter param = DefaultBlockParameterName.LATEST;
        Set<Block> set = new TreeSet<>();

        // 这里每次请求只循环十次查询最新的区块
        for (int i = 0; i < 6; i++) {
            // 异步操作
            CompletableFuture completableFuture = web3.ethGetBlockByNumber(param, false).sendAsync();
            Future<EthBlock> future = completableFuture.whenComplete((v, e) -> {
//                web3.shutdown();
            });
            EthBlock ethBlock = future.get();
            EthBlock.Block block = ethBlock.getBlock();
            // 返回自己封装的对象给前台
            Block b = new Block();
            b.setBlockNumber(block.getNumber());
            b.setHash(block.getHash());
            b.setTotalDifficulty(block.getTotalDifficulty());
            b.setTimeStamp(block.getTimestamp());
            set.add(b);
        }

        return set;
    }

    @Override
    public Result searchData(String hash, Integer pageNum, Integer pageSize) throws Exception {
        DetailsVo vo = new DetailsVo();

        if (StringUtils.isEmpty(hash)) {
            return Result.create().fail("请输入正确的区块哈希或钱包地址");
        }

        // 这里需要通过哈希值的长度判断是区块哈希还是钱包地址，调用不同的方法查询数据
        int hashLength = hash.length();
        if (hashLength == 66) {
            // 通过区块哈希查询该笔交易详情
            vo.setTransactionVo(searchByHash(hash));
            return Result.create().success(vo);
        } else if (hashLength == 42) {
            // 通过钱包地址查询该账户下所有交易记录
            vo = searchByAddress(hash, pageNum, pageSize);
            return Result.create().success(vo);
        }

        return Result.create().fail("请输入正确的区块哈希或钱包地址");
    }

    private DetailsVo searchByAddress(String hash, Integer pageNum, Integer pageSize) throws Exception {
        // API
        String apiUrl = fomcatApiUrl(hash, pageNum, pageSize);

        DetailsVo vo = new DetailsVo();
        List<TransactionRecordVo> data = new ArrayList<>();
        Web3j web3 = Web3j.build(new HttpService(infuraApi));

        // 账户地址
        vo.setAddress(hash);

        // 请求以太坊API返回数据
        String response = HttpUtils.get(apiUrl);
        JSONObject json = JSONObject.fromObject(response);

        if (json != null) {
            if ("OK".equals(json.get("message"))) { // 查询成功
                // 取返回的交易列表的数组
                JSONArray result = (JSONArray) json.get("result");
                Object arr[] = result.toArray();

                for (int i = 0; i < arr.length; i++) {
                    TransactionRecordVo txVo = new TransactionRecordVo();
                    JSONObject j = JSONObject.fromObject(arr[i].toString());

                    String from = j.get("from").toString(); // 转账人
                    String to = j.get("to").toString(); // 接收人
                    String blockHash = j.get("hash").toString(); // 区块哈希
                    String gasPrice = j.get("gasPrice").toString();
                    String gasUsed = j.get("gasUsed").toString();
                    String value = j.get("value").toString(); // 交易额
                    String timeStamp = j.get("timeStamp").toString(); // 时间

                    // 判断是转出send还是收入receive
                    if (from.equals(hash)) {
                        txVo.setType("send");
                        txVo.setAddress(to);
                    } else {
                        txVo.setType("receive");
                        txVo.setAddress(from);
                    }

                    BigDecimal d_gasPrice = new BigDecimal(gasPrice);
                    BigDecimal d_gasUsed = new BigDecimal(gasUsed);

                    txVo.setMiner(d_gasPrice.multiply(d_gasUsed).divide(_10_18));
                    txVo.setValue(new BigDecimal(new BigInteger(value)).divide(_10_18));
                    txVo.setHash(blockHash);
                    txVo.setTimestamp(DateUtil.formatTimesTampDate(new Date(Long.parseLong(timeStamp) * 1000)));

                    data.add(txVo);
                }
            }
        }

        // 查询该地址下的交易记录数
        DefaultBlockParameter param = DefaultBlockParameterName.LATEST;
        CompletableFuture completableFuture = web3.ethGetTransactionCount(hash, param).sendAsync();
        Future<EthGetTransactionCount> future = completableFuture.whenComplete((v, e) -> {
        });
        // 这里查询出来的值和以太坊中的交易记录数量不一致，少了收入的，没有相应的接口可以获取
        BigInteger count = future.get().getTransactionCount();

        // 查询该地址的余额 ETH
//        CompletableFuture completableFuture1 = web3.ethGetBalance(hash, param).sendAsync();
//        Future<EthGetBalance> future1 = completableFuture1.whenComplete((v, e) -> {
//        });
//        BigInteger balance = future1.get().getBalance();
//        BigDecimal d_balance = new BigDecimal(balance).divide(_10_18); // wei转换ETH/PCT

        // 查询该地址的余额 PCT
        BigDecimal d_balance = getBalance(web3, hash);

        vo.setBalance(d_balance);
        vo.setTxCount(count);
        vo.setTransactionRecordVo(data);

        return vo;
    }

    private TransactionVo searchByHash(String hash) throws Exception {
        TransactionVo vo = new TransactionVo();

        Web3j web3 = Web3j.build(new HttpService(infuraApi));

        // 通过交易哈希查询交易详情
        Transaction tran = web3.ethGetTransactionByHash(hash).sendAsync().get().getTransaction().get();
        ;

        // 通过交易哈希获取交易收据，在收据中获取gasUsed
        TransactionReceipt receipt = web3.ethGetTransactionReceipt(hash).sendAsync().get().getTransactionReceipt().get();

        // 通过交易哈希查询区块，获取时间戳
        DefaultBlockParameter param = new DefaultBlockParameterNumber(tran.getBlockNumber());
        BigInteger time = web3.ethGetBlockByNumber(param, false).sendAsync().get().getBlock().getTimestamp();
        DateUtil.getTimeDifferenceMinute(new Date(), new Date(time.longValue()));
        vo.setTimestamp(DateUtil.formatTimesTampDate(new Date(time.longValue() * 1000)));

        // 计算矿工费: 矿工费=交易燃料费用*燃料价格/10^18, miner=gasPrice*gasUsed/10^18
        BigDecimal d_gasPrice = new BigDecimal(tran.getGasPrice());
        BigDecimal d_gasUsed = new BigDecimal(receipt.getGasUsed());

        // 交易的代币信息放在了input中
        String input = tran.getInput();
        // 取得to的地址, tran中的to是合约地址
        String to = "0x" + input.substring(34, 74);
        vo.setTo(to);

        // 最后的64位就是转账代币的16进制数值
        String value = input.substring(74);
        // 转换成10进制
        value = new BigInteger(value, 16).toString(10);
        vo.setValue(new BigDecimal(value).divide(_10_18));

        vo.setHash(tran.getHash());
        vo.setFrom(tran.getFrom());
        vo.setBlockNumber(tran.getBlockNumber());
        vo.setMiner(d_gasPrice.multiply(d_gasUsed).divide(_10_18));

        return vo;
    }

    /**
     * 拼装api url
     *
     * @return
     */
    private String fomcatApiUrl(String hash, Integer pageNum, Integer pageSize) {
        String url = addressTransactionsUrl;

        if (pageNum == null) {
            url = url.replace("[pageNum]", "1");
        } else {
            url = url.replace("[pageNum]", pageNum + "");
        }

        if (pageSize == null) {
            url = url.replace("[pageSize]", "20");
        } else {
            url = url.replace("[pageSize]", pageSize + "");
        }

        if (StringUtils.isEmpty(hash)) {
            url = url.replace("[address]", companyAddress);
        } else {
            url = url.replace("[address]", hash);
        }

        return url;
    }

    /**
     * 请求接口方式根据钱包地址查询账户PCT余额
     *
     * @param address 钱包地址
     * @return
     * @throws IOException
     */
    public BigDecimal getBalance(String address) throws IOException {
        String json = "{ " +
                " \"jsonrpc\": \"2.0\", " +
                " \"method\": \"eth_call\", " +
                " \"params\": [{ " +
                " \"from\": \"" + address + "\", " +
                " \"to\": \"" + "0x183630c3AfA08957E588eaa26b748cc5C2D42DC6" + "\", " +
                " \"gas\": \"0x0\", " +
                " \"gasPrice\": \"0x0\", " +
                " \"value\": \"0x0\", " +
                " \"data\": \"" + "0x70a08231000000000000000000000000" + address.substring(2) + "\", " +
                " \"nonce\": \"0x0\" " +
                " }, \"latest\"], " +
                " \"id\": 23 " +
                "} ";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(infuraApi)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            JSONObject object = JSONObject.fromObject(response.body().string());
            String result = object.getString("result");
            String text = result.substring(2);
            String str = new BigInteger(text, 16).toString(10);
            long d = (long) Math.pow(10, 18);
            BigDecimal pctmoney = new BigDecimal(str).divide(new BigDecimal(d + "")).setScale(4, RoundingMode.DOWN);
            return pctmoney;
        } else {
            return new BigDecimal("0");
        }
    }

    /**
     * 调用合约方式根据钱包地址查询账户PCT余额
     *
     * @param address 钱包地址
     * @return
     * @throws IOException
     */
    public BigDecimal getBalance(Web3j web3, String address) throws Exception {
        // gasPrice
        BigInteger gasPrice1 = web3.ethGasPrice().sendAsync().get().getGasPrice();
        // 读取keystore文件
        Credentials credentials = WalletUtils.loadCredentials(keystorePassword, keystoreAddress);
        // 实例化合约对象
        PCT pct = new PCT(contractAddress, web3, credentials, gasPrice1, BigInteger.valueOf(100000));
        // 调用合约方法查询余额
        BigInteger balance = pct.balanceOf(address).sendAsync().get();

        long d = (long) Math.pow(10, 18);
        return new BigDecimal(balance).divide(new BigDecimal(d + "")).setScale(4, RoundingMode.DOWN);
    }


}
