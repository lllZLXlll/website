package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Currency;
import com.wchm.website.entity.CurrencyRecord;
import com.wchm.website.entity.Operation;
import com.wchm.website.mapper.CurrencyMapper;
import com.wchm.website.mapper.CurrencyRecordMapper;
import com.wchm.website.mapper.OperationMapper;
import com.wchm.website.service.CurrencyService;
import com.wchm.website.service.RedisService;
import com.wchm.website.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    public final static Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Value("${wchm.company-address}")
    private String companyAddress;

    @Value("${wchm.infura-api}")
    private String infuraApi;

    // 10的18次方
    private static BigDecimal _10_18 = new BigDecimal(Math.pow(10, 18));


    @Autowired
    CurrencyMapper currencyMapper;

    @Autowired
    CurrencyRecordMapper currencyRecordMapper;

    @Autowired
    OperationMapper operationMapper;

    @Autowired
    RedisService redisService;

    @Override
    public List<Currency> queryCurrency() {
        return null;
    }

    /**
     * 分页
     *
     * @param pageNum
     * @param pageSize
     * @param user_name
     * @return
     */
    @Override
    public Result queryCurrencyByPage(Integer pageNum, Integer pageSize, String user_name) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Currency> data;
        if (StringUtil.isEmpty(user_name)) {
            data = currencyMapper.queryCurrencyByPage();
        } else {
            data = currencyMapper.queryCurrencyByPageName(user_name);
        }
        PageInfo<Currency> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    //保存
    @Override
    public Result currencySave(Currency currency) {
        // 设置，用户剩余代币总额
        currency.setSurplus(currency.getCurrency());

        long result = currencyMapper.currencySave(currency);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }

    //删除
    @Override
    public Result delCurrencyByID(Integer id) {
        long result = currencyMapper.delCurrencyByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");
        }
        return Result.create().success("删除成功");
    }

    //
    @Override
    public ModelAndView currencyInfo(Integer id) {
        Currency currency = currencyMapper.currencyInfo(id);
        ModelAndView mav = new ModelAndView("currency-record-edit");
        mav.getModel().put("currency", currency);
        return mav;
    }

    //修改
    @Override
    public Result currencyUpdate(Currency currency) {
        long result = currencyMapper.currencyUpdate(currency);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }

    @Override
    public void excelImport(Currency currency) {
        // 先查询数据库中是否有此地址, 有:修改; 无:插入
        Currency c = currencyMapper.queryCurrencyByAddress(currency.getAddress());

        if (c != null) {
            // update
            long result = currencyMapper.currencyUpdateByAddress(currency);
            if (result <= 0) {
                log.error("-----error--导入用户excel，修改失败！用户地址：" + currency.getAddress() + "，代币金额：" + currency.getCurrency().doubleValue() + "-----");
                throw new RuntimeException("导入用户excel，修改失败！");
            }
        } else {
            // insert
            long result = currencyMapper.currencySave(currency);
            if (result <= 0) {
                log.error("-----error--导入用户excel，新增失败！用户地址：" + currency.getAddress() + "，代币金额：" + currency.getCurrency().doubleValue() + "-----");
                throw new RuntimeException("导入用户excel，新增失败！");

            }
        }
    }

    @Override
    public ModelAndView recordInfo(Integer id) {
        ModelAndView mav = new ModelAndView("currency-record-list");
        mav.getModel().put("id", id);
        return mav;
    }

    @Override
    public Result queryCurrencyRecordByPage(Integer pageNum, Integer pageSize, Integer id) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<CurrencyRecord> data = currencyMapper.queryCurrencyRecordByPage(id);
        PageInfo<CurrencyRecord> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    @Override
    public Result currencyTransfer(String token, Long id, BigDecimal money) throws RuntimeException {
        try {
            // 获得admin
            String value = redisService.get(token);
            Admin admin = (Admin) redisService.strToBean(Admin.class, value);
            // 根据id查询信息
            Currency currency = currencyMapper.queryCurrencyById(id);
            // 判断是否有此信息
            if (currency != null) {
                // 判断转账代币
                if (money.doubleValue() > currency.getSurplus().doubleValue()) {
                    return Result.create().fail("转账代币不能大于剩余可转账代币！");
                }

                long result;
                // 用户钱包地址
                String address = currency.getAddress();
                // 查询当前最大期数
                int periods = currencyRecordMapper.queryCurrencyRecordMaxPeriods(id);

                // TODO 调用web3j转账方法
                int status = web3jTransaction(companyAddress, address, money);

                // 转账记录表中增加一条记录
                CurrencyRecord record = new CurrencyRecord();
                record.setPool_id(id);
                record.setFrom(companyAddress);
                record.setTo(address);
                record.setCurrency(money);
                record.setPeriods(periods + 1);
                record.setState(1); // TODO 此处要根据web3j的接口返回结果修改此处
                record.setDescribe("交易成功"); // TODO 此处要根据web3j的接口返回结果修改此处
                record.setAdmin(admin.getUsername());
                record.setTime(new Date());
                result = currencyRecordMapper.recordSave(record);
                if (result <= 0) {
                    log.error("-----error-----向转账记录表中增加一条记录失败，带币池表id：" + id + "，转账金额：" + money + "-----");
                    throw new RuntimeException("转账失败，转账出现异常，请联系技术！");
                }

                // 代币池用户剩余代币余额修改
                // 当前剩余总代币
                BigDecimal s = currency.getSurplus();
                // 减去转账的代币后的剩余代币
                s = s.subtract(money);
                currency.setSurplus(s);
                result = currencyMapper.currencyUpdate(currency);
                if (result <= 0) {
                    log.error("-----error-----修改代币池表中记录失败，带币池表id：" + id + "，修改字段：surplus，修改金额：" + s.doubleValue() + "-----");
                    throw new RuntimeException("转账失败，转账出现异常，请联系技术！");
                }

                // 添加日志记录
                Operation operation = new Operation();
                operation.setAdmin_name(admin.getUsername());
                operation.setOperation_type("4");
                operation.setMoney(money);
                operation.setAddress(address);
                operation.setCreate_time(new Date());
                operation.setState(1); // TODO 此处要根据web3j的接口返回结果修改此处
                operationMapper.operationSave(operation);

            } else {
                return Result.create().fail("没有找到此用户信息！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----error-----给用户转账出现异常，带币池表id：" + id + "，转账金额：" + money + "-----");
            throw new RuntimeException("转账失败，转账出现异常，请联系技术！");
        }
        return Result.create().success("转账成功！");
    }

    /**
     * web3j 转账方法
     *
     * @param from  <p>转账地址</p>
     * @param to    <p>到账地址</p>
     * @param money <p>转账代币</p>
     * @return <p>返回结果：1:成功，0:失败</p>
     */
    private static int web3jTransaction(String from, String to, BigDecimal money) throws Exception {

        /**
         * RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, dabi, new BigInteger("0"),data);
         * byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentialss);
         * final String hexValue = Numeric.toHexString(signedMessage);
         *
         * EthGetTransactionCount ethGetTransactionCount = null;
         * try {
         *     ethGetTransactionCount = web3js.ethGetTransactionCount(
         *             ownAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
         * } catch (InterruptedException e) {
         *     e.printStackTrace();
         * } catch (ExecutionException e) {
         *     e.printStackTrace();
         * }
         * BigInteger nonce = ethGetTransactionCount.getTransactionCount();
         *
         * BigInteger bi2 = new BigInteger("1");
         * //BigInteger text = nonce.add(bi2);
         * Log.e("datafunction",data.toString()+"---------");
         * RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, dabi, new BigInteger("0"),data);
         * byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentialss);
         * final String hexValue = Numeric.toHexString(signedMessage);
         *
         *
         * 私钥加密：Credentials credentialss = Credentials.create(priKey);
         *
         *
         * 私钥:         0x4bac2df2e83f1bdb4cb18fadb9777bf785c27dfa09cac7fa29cb6384e12497a9
         *
         * from:        0xEf34D9201b2E8dbf4dDd5072d708eA3cfd401a9d
         *
         * 代币地址:      0x107eff256b79fd5723c0499edd1120c303d73256
         *
         * to:          0xafc79afd163e192f144f9e48bb2739b35040e966
         *
         *
         */

        /**_________________________测试数据____________begin_________________*/
        // 账户私钥
        String priKey = "0x4bac2df2e83f1bdb4cb18fadb9777bf785c27dfa09cac7fa29cb6384e12497a9";
        // from
        from = "0xEf34D9201b2E8dbf4dDd5072d708eA3cfd401a9d";
        // to
        to = "0xafc79afd163e192f144f9e48bb2739b35040e966";
        // 代币地址
        String dabi = "0x107eff256b79fd5723c0499edd1120c303d73256";
        /**_________________________测试数据_____________end________________*/

        // web3j
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/c783bc77104c4b2d8d7cca07e85dcbc5"));

        // TODO 封装转账交易对象
        BigInteger nonce; // 转账地址的交易数量
        BigInteger gasPrice; // gas价格
        BigInteger gasLimit = null; // TODO gas 需要计算出来
        BigInteger value = new BigInteger(money.multiply(_10_18).setScale(0).toString()); // 转换成WEI单位
        String data = ""; // TODO 需要把私钥等信息拼接

        // 最新的
        DefaultBlockParameter defaultParam = DefaultBlockParameterName.LATEST;

        // nonce 0
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(from, defaultParam).sendAsync().get();
        nonce = ethGetTransactionCount.getTransactionCount();

        // gasPrice 5000000000
        CompletableFuture completableFuture = web3.ethGasPrice().sendAsync();
        Future<EthGasPrice> future1 = completableFuture.whenComplete((v, e) -> {
        });
        gasLimit = gasPrice = future1.get().getGasPrice();

        Credentials credentialss = Credentials.create(priKey);

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, dabi, new BigInteger("0"), data);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentialss);
        final String hexValue = Numeric.toHexString(signedMessage);


        Transaction tran = new Transaction(
                from,
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data
        );

        // 转账
        completableFuture = web3.ethSendTransaction(tran).sendAsync();
        Future<String> future2 = completableFuture.whenComplete((v, e) -> {
        });
        String address = future2.get();
        log.info("-----转账返回的交易哈希：" + address);

        if (StringUtils.isEmpty(address)) {
            return 0;
        }

        return 1;
    }

    public static void main(String[] args) throws Exception {
        web3jTransaction(null, null, new BigDecimal("1"));

        // 1000000000000000000  5000000000  200000000
    }
}
