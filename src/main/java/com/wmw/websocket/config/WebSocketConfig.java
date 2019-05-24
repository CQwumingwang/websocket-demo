package com.wmw.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yeauty.standard.ServerEndpointExporter;

/**
 * @ClassName WebSocketConfig
 * @Description TODO  WebSocket配置
 * @Author wumingwang
 * @Date 2019/5/24 9:47
 * @Version 1.0
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
