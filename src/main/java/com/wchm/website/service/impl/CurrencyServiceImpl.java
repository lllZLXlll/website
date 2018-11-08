package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.*;
import com.wchm.website.mapper.CurrencyMapper;
import com.wchm.website.mapper.CurrencyRecordMapper;
import com.wchm.website.mapper.OperationMapper;
import com.wchm.website.service.CurrencyService;
import com.wchm.website.service.RedisService;
import com.wchm.website.util.HttpUtils;
import com.wchm.website.util.Result;
import com.wchm.website.vo.CurrencyAccountVo;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    public final static Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Value("${wchm.company-address}")
    private String companyAddress;

    @Value("${wchm.company-pri-key}")
    private String companyPriKey;

    @Value("${wchm.contract-address}")
    private String contractAddress;

    @Value("${wchm.method-id}")
    private String methodId;

    @Value("${wchm.infura-api}")
    private String infuraApi;

    @Value("${wchm.php.token-api}")
    private String phpTokenApi;


    @Autowired
    CurrencyMapper currencyMapper;

    @Autowired
    CurrencyRecordMapper currencyRecordMapper;

    @Autowired
    OperationMapper operationMapper;

    @Autowired
    RedisService redisService;

    // 前端查询用户的锁仓信息，提现记录
    @Override
    public Result queryCurrencyAccount(String token, String language) {
        // TODO 测试数据
//        token = "eyJpdiI6IlptUmhhMmx1Wld3N2FXNXFZV3BrYWc9PSIsInZhbHVlIjoiWUhYMk5kdlwvOFdCa1oya1lcL284eTFURjNDZUlsYzY2UDFPVndtZWZBc0JkdElwTlhMaVZCTWNxVjhKTDBNWnNwdkNCY2g2d3dQZmtsZlVkY2NQNGtxUT09In0=";

        // 请求php接口解密token，拿到用户id
        Long userId = getPhpUid(token);
        if (userId == null) {
            return Result.create().fail("登录信息失效，token不正确");
        }

        // 通过用户id查询信息
        CurrencyAccountVo vo = currencyMapper.queryCurrencyAccount(userId);

        if (vo == null) {
            return Result.create().fail("查询不到相关信息");
        }

        // 如果是英文就改成英文的锁仓规则描述
        if ("en".equals(language)) {
            vo.setLock_describe("After the private placement is completed, cornerstone round users unlock 10% of the purchased PCT, the remaining part of the lock warehouse for 1 year, after the expiration of the lock warehouse, according to the quarterly release of 20%");
        }

        // 查询提现记录
        List<ExtractApplyfor> extractList = currencyMapper.queryApplyforListByUid(userId);
        vo.setExtractList(extractList);

        return Result.create().success(vo);
    }

    @Override
    public Result extractApplyfor(String token, String address) {
        // 请求php接口解密token，拿到用户id
        Long userId = getPhpUid(token);
        if (userId == null) {
            return Result.create().fail("登录信息失效，token不正确");
        }

        // 校验地址是否正确
        CurrencyAccountVo vo = currencyMapper.queryCurrencyAccount(userId);

        if (!vo.getAddress().equals(address)) {
            return Result.create().fail("钱包地址不正确");
        }

        // 用户能提现的代币金额就是website_currency_pool表中的surplus字段的值，如果这里没有值说明不能提现
        if (vo.getSurplus().doubleValue() <= 0) {
            return Result.create().fail("剩余部分代币未到解仓时间，请到解仓时间再试");
        }

        // 往提现申请表中插入一条申请
        long result = currencyMapper.saveExtractApplyfor(userId, address, vo.getSurplus(), vo.getCurrency());
        if (result <= 0) {
            return Result.create().fail("提现申请提交失败，请联系客服处理");
        }

        return Result.create().fail("提现申请提交成功");
    }

    private Long getPhpUid(String token) {
        String response = HttpUtils.post(phpTokenApi, "token", token);
        JSONObject json = JSONObject.fromObject(response);
        String state = json.getString("status");
        String message = json.getString("message");
        json = json.getJSONObject("data");

        if (!"1".equals(state)) {
            return null;
        }

        return Long.parseLong(json.getString("uid"));
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

    @Transactional // 开启事务
    @Override
    public Result currencyTransfer(Long id, BigDecimal money) throws RuntimeException {
        try {
            // 获得admin
            Subject currentUser = SecurityUtils.getSubject();
            Admin admin = (Admin) currentUser.getPrincipals().getPrimaryPrincipal();

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
                int state = 1;

                // 调用web3j转账方法
                Map<String, String> resultMap = web3jTransaction(address, money);
                String txAddress = resultMap.get("address");
                String message = resultMap.get("message");

                if (StringUtils.isEmpty(txAddress)) {
                    // 转账失败
                    state = 0;
                }

                // 转账记录表中增加一条记录
                CurrencyRecord record = new CurrencyRecord();
                record.setPool_id(id);
                record.setFrom(companyAddress);
                record.setTo(address);
                record.setTxAddress(txAddress);
                record.setCurrency(money);
                record.setPeriods(periods + 1);
                record.setState(state);
                record.setDescribe(message);
                record.setAdmin(admin.getUsername());
                record.setTime(new Date());
                result = currencyRecordMapper.recordSave(record);
                if (result <= 0) {
                    log.error("-----error-----向转账记录表中增加一条记录失败，带币池表id：" + id + "，转账金额：" + money + "-----");
                    throw new RuntimeException("转账失败，转账出现异常，请联系技术！");
                }

                // 添加日志记录
                Operation operation = new Operation();
                operation.setAdmin_name(admin.getUsername());
                operation.setOperation_type("4");
                operation.setMoney(money);
                operation.setAddress(address);
                operation.setCreate_time(new Date());
                operation.setState(state);
                operationMapper.operationSave(operation);

                if (state == 0) {
                    // 转账失败返回错误信息给前台，不再往下执行修改余额操作
                    return Result.create().fail(message);
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
            } else {
                return Result.create().fail("没有找到此用户信息！");
            }
        } catch (Exception e) {
            log.error("-----error-----给用户转账出现异常，带币池表id：" + id + "，转账金额：" + money + "-----", e);
            throw new RuntimeException("转账失败，转账出现异常，请联系技术！");
        }
        return Result.create().success("转账成功！");
    }

    /**
     * web3j 转账方法
     *
     * @param to    <p>到账地址</p>
     * @param money <p>转账代币</p>
     * @return <p>返回结果：map中的error不为null则说明转账失败，address不为null则转账成功</p>
     */

    private Map<String, String> web3jTransaction(String to, BigDecimal money) throws Exception {
        Map<String, String> result = new HashMap<>();

        // web3j
        Web3j web3 = Web3j.build(new HttpService(infuraApi));

        // 转账交易参数
        BigInteger nonce; // 转账地址的交易数量
        BigInteger gasPrice; // gas价格
        BigInteger gasLimit = new BigInteger("100000"); // 矿工费
        BigInteger value = Convert.toWei(money + "", Convert.Unit.ETHER).toBigInteger(); // 转换成WEI单位
        String data; // 需要把私钥等信息拼接

        // 最新的
        DefaultBlockParameter defaultParam = DefaultBlockParameterName.LATEST;
        // 当前最新交易笔数
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(companyAddress, defaultParam).sendAsync().get();
        nonce = ethGetTransactionCount.getTransactionCount();
        // 邮费价格
        gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();
        // 转账人私钥
        Credentials credentialss = Credentials.create(companyPriKey);

        // value转换16进制
        String value_16 = value.toString(16);
        // 需要有64位，不够往前补零
        String value_64 = strFormat64(value_16);
        // 去掉0x
        String toAddress = to.substring(2);
        data = methodId + toAddress + value_64;

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentialss);
        final String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
        // 转账返回的交易哈希
        String address = ethSendTransaction.getTransactionHash();
        Response.Error error = ethSendTransaction.getError();

        if (StringUtils.isEmpty(address)) {
            log.error("-----error-----后台客户币池转账失败:" + error.getMessage() + ", 错误码:" + error.getCode());
            result.put("message", "转账失败: " + error.getMessage() + ", 错误码: " + error.getCode());
            result.put("address", null);
            return result;
        }

        result.put("message", "转账成功");
        result.put("address", address);
        return result;
    }

    // 将字符串补足64位，不够往前补零
    private static String strFormat64(String s) {
        while (s.length() < 64) {
            s = "0" + s;
        }
        return s;
    }

    @Override
    public Result queryApplyforByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<ExtractApplyfor> data = currencyMapper.queryApplyforByPage();
        PageInfo<Currency> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    @Override
    public Result applyforSubmit(Integer id) {
        if (id == null) {
            return Result.create().fail("参数为空，确认提现失败");
        }

        // 修改申请表状态为成功
        long result = currencyMapper.applyforUpdate(id);
        if (result <= 0) {
            return Result.create().fail("确认提现失败，请联系技术");
        }

        // 通过申请表id查询到该用户uid，通过uid查询user表中的电话，通过电话关联到代币池表中的数据
        String mobile = currencyMapper.queryUserUidById(id);

        // 修改代币池表中的用户可用代币总额为0
        result = currencyMapper.updatePoolUserSurplus(mobile);


        return Result.create().success("确认提现成功");
    }

    @Override
    public List<Currency> queryPoolList() {
        return currencyMapper.queryPoolList();
    }

    @Override
    public Long updateCurrency(Currency item) {
        return currencyMapper.updateCurrency(item);
    }

}
