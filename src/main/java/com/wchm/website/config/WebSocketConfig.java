package com.wchm.website.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
// 开启使用STOMP协议来传输基于代理的消息。
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 注册STOMP协议的节点，并指定映射的URL。
     * @param stompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        // 注册STOMP协议节点，同时指定使用SockJS协议。
        stompEndpointRegistry.addEndpoint("/endpointSang").setAllowedOrigins("*").withSockJS();
    }

    /**
     * 配置消息代理，是实现推送功能，这里的消息代理是/topic
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
    }

}
