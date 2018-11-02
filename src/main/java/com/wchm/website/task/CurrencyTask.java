package com.wchm.website.task;

import com.wchm.website.entity.Currency;
import com.wchm.website.service.CurrencyService;
import com.wchm.website.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 客户代币池，定时任务
 */
@Component
public class CurrencyTask {

    public final static Logger log = LoggerFactory.getLogger(CurrencyTask.class);

    @Autowired
    private CurrencyService currencyService;

    /**
     * 每个季度修改代币池表中的数据，每次解仓20%代币，剩余可用代币增加20%，代币总额减少20%。
     * <p>
     * 第一次解仓按照每个人的解仓时间
     * <p>
     * 以后按照每个用户解仓时间+3个月
     */
    @Scheduled(cron = "0 0 6 * * ?")
    private void updateUserCurrency() {
        // 查询代币池中数据
        List<Currency> poolList = currencyService.queryPoolList();

        if (poolList == null) {
            return;
        }

        /**
         * 如果第一次解仓，上一次解仓时间为null，直接根据锁仓结束时间解仓，然后修改上一次解仓时间字段值。
         *
         * 第二次解仓就判断上一次解仓时间到现在是否有3个月，有三个月就再次解仓20%，再修改上一次解仓时间字段值。
         *
         * 以后都是重复第二次步骤。
         */
        for (Currency item : poolList) {
            // 上一次解仓时间
            Date last_unlock_time = item.getLast_unlock_time();
            // 解仓时间
            Date lock_end_time = item.getLock_end_time();
            // 代币总额
            BigDecimal currency = item.getCurrency();
            // 可用代币
            BigDecimal surplus = item.getSurplus();
            surplus = surplus == null ? new BigDecimal("0") : surplus;

            // 解仓的代币金额 20%
            BigDecimal money = currency.multiply(new BigDecimal("0.2"));
            item.setSurplus(surplus.add(money));

            // 总额减少
            item.setCurrency(currency.subtract(money));

            String lock_end_time_str = DateUtil.formatDefaultDate(lock_end_time);
            String now = DateUtil.formatDefaultDate(new Date());
            item.setLast_unlock_time(DateUtil.parseDefaultDate(now));

            long result = -1;

            // 第一次解仓
            if (last_unlock_time == null) {
                if (now.equals(lock_end_time_str)) {
                    // 时间一致，解仓，修改数据库
                    result = currencyService.updateCurrency(item);
                }
            } else {
                String last_unlock_time_str = DateUtil.formatDefaultDate(
                        DateUtil.customTimeByAddMonth(last_unlock_time, 3));

                // 不是第一次解仓判断当前时间是否等于上一次解仓时间加3个月
                if (last_unlock_time_str.equals(now)) {
                    // 相等，解仓，修改数据库
                    result = currencyService.updateCurrency(item);
                }
            }

            if (result == 0) {
                log.error("-----error-----用户代币池解仓修改失败，代币池数据：currency：" + item.toString());
                return;
            }

        }


    }


}
