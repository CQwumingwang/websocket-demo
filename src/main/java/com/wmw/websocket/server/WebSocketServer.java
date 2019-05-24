package com.wmw.websocket.server;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName WebSocketServer
 * @Description TODO WebSocket Server
 *  当ServerEndpointExporter类通过Spring配置进行声明并被使用，它将会去扫描带有@ServerEndpoint注解的类
 *  被注解的类将被注册成为一个WebSocket端点 所有的配置项都在这个注解的属性中 ( 如:@ServerEndpoint("/ws") )
 * @Author wumingwang
 * @Date 2019/5/24 9:50
 * @Version 1.0
 */

@ServerEndpoint(prefix = "netty-websocket")
@Component
@Slf4j
public class WebSocketServer {

    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    /**接收sid*/
    private String sid="";

    /**
     * 当有新的WebSocket连接进入时，对该方法进行回调 注入参数的类型:Session、HttpHeaders、ParameterMap
     * @param session
     * @param headers
     * @param parameterMap
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
        this.session = session;
        String sid = parameterMap.getParameter("sid");
        webSocketSet.add(this);
        log.info("有新窗口开始监听:"+sid);
        this.sid=sid;
        sendMessage("连接成功");
    }

    /**
     * 当有WebSocket连接关闭时，对该方法进行回调 注入参数的类型:Session
     * @throws IOException
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        webSocketSet.remove(this);
        log.info("有一连接关闭！sid:{}" ,this.sid);
    }

    /**
     * 当接收到字符串消息时，对该方法进行回调 注入参数的类型:Session、String
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(Session session,String message) {
        log.info("收到来自窗口"+sid+"的信息:"+message);
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    /**
     * 当接收到二进制消息时，对该方法进行回调 注入参数的类型:Session、byte[]
     * @param session
     * @param bytes
     */
    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            log.info("byte:{}",b);
        }
        session.sendBinary(bytes);
    }

    /**
     * 当有WebSocket抛出异常时，对该方法进行回调 注入参数的类型:Session、Throwable
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误",error);
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        this.session.sendText(message);
    }

    /**
     * 当接收到Netty的事件时，对该方法进行回调 注入参数的类型:Session、Object
     * @param session
     * @param evt
     */
    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    log.info("read idle");
                    break;
                case WRITER_IDLE:
                    log.info("write idle");
                    break;
                case ALL_IDLE:
                    log.info("all idle");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message, String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServer item : webSocketSet) {
            /**这里可以设定只推送给这个sid的，为null则全部推送*/
            if(sid==null) {
                item.sendMessage(message);
            }else if(item.sid.equals(sid)){
                item.sendMessage(message);
            }
        }
    }
}
