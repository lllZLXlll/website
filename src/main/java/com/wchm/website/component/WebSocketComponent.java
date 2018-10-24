package com.wchm.website.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过web socket向订阅了消息的客户端推送消息
 *
 * 推送交易记录消息。
 */

@Component
public class WebSocketComponent {

    private int count;

    @Autowired
    private SimpMessagingTemplate simpMessageSendingOperations; // 消息发送模板

    /**
     * // TODO
     * 查询最新交易记录6条，返回前端，如果没有最新的则不返回，
     * 如果有但不满6条，则有多少返回多少。
     */

    @Scheduled(fixedRate = 1000 * 3) // 每隔3秒向客户端发送一次数据
    public void sendMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("key", ++count + "");

        // 将消息推送给订阅了‘topic/getResponse’的客户端
        simpMessageSendingOperations.convertAndSend("/topic/getTransactionRecord", map + " - 服务端消息！");
    }
}
