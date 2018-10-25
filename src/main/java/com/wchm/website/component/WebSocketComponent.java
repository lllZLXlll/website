package com.wchm.website.component;

import com.wchm.website.service.BlockChainBrowserService;
import com.wchm.website.vo.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 通过web socket向订阅了消息的客户端推送消息
 *
 * 推送交易记录消息。
 */

@Component
public class WebSocketComponent {

    @Autowired
    private BlockChainBrowserService blockChainBrowserService;

    @Autowired
    private SimpMessagingTemplate simpMessageSendingOperations; // 消息发送模板

    /**
     * 查询最新交易记录6条，返回前端，如果没有最新的则不返回，
     */

    @Scheduled(fixedRate = 1000 * 10) // 每隔10秒向客户端发送一次数据
    public void sendMessage() {
        List<Transaction> trans = blockChainBrowserService.queryIndexDataTransaction();

        if (trans != null && !trans.isEmpty()) {
            simpMessageSendingOperations.convertAndSend("/topic/getTransactionRecord", trans);
        }

    }

}
