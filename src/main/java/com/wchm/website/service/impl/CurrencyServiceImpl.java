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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    public final static Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Value("${wchm.company-address}")
    private String companyAddress;

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
                // 查询当前最大期数
                int periods = currencyRecordMapper.queryCurrencyRecordMaxPeriods(id);

                // TODO 调用web3j转账方法


                // 转账记录表中增加一条记录
                CurrencyRecord record = new CurrencyRecord();
                record.setPool_id(id);
                record.setFrom(companyAddress);
                record.setTo(currency.getAddress());
                record.setCurrency(money);
                record.setPeriods(periods + 1);
                record.setState(1); // TODO 此处要根据web3j的接口返回结果修改此处
                record.setDescribe("交易成功"); // TODO 此处要根据web3j的接口返回结果修改此处
                record.setAdmin(admin.getUsername());
                record.setTime(new Date());
                result = currencyRecordMapper.recordSave(record);
                if (result <= 0) {
                    log.error("-----error-----向转账记录表中增加一条记录失败，带币池表id：" + id +"，转账金额：" + money + "-----");
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
                    log.error("-----error-----修改代币池表中记录失败，带币池表id：" + id +"，修改字段：surplus，修改金额：" + s.doubleValue() + "-----");
                    throw new RuntimeException("转账失败，转账出现异常，请联系技术！");
                }

                // 添加日志记录
                Operation operation = new Operation();
                operation.setAdmin_name(admin.getUsername());
                operation.setOperation_type("4");
                operation.setMoney(money);
                operation.setAddress(currency.getAddress());
                operation.setCreate_time(new Date());
                operation.setState(1); // TODO 此处要根据web3j的接口返回结果修改此处
                operationMapper.operationSave(operation);

            } else {
                return Result.create().fail("没有找到此用户信息！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----error-----给用户转账出现异常，带币池表id：" + id +"，转账金额：" + money + "-----");
            throw new RuntimeException("转账失败，转账出现异常，请联系技术！");
        }
        return Result.create().success("转账成功！");
    }
}
