package com.wmw.websocket.controller;

import com.wmw.websocket.server.WebSocketServer;
import com.wmw.websocket.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @ClassName WebSocketController
 * @Description TODO
 * @Author wumingwang
 * @Date 2019/5/24 10:27
 * @Version 1.0
 */
@RestController
public class WebSocketController {

    //推送数据接口
    @GetMapping("/socket/push")
    public Result pushToWeb(String sid, String message) {
        try {
            WebSocketServer.sendInfo(message,sid);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(sid+"#"+e.getMessage());
        }
        return Result.ok(sid);
    }
}
