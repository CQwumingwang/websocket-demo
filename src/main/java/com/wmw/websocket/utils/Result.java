package com.wmw.websocket.utils;


import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Result
 * @Description:返回结果类
 * @Author wumingwang
 * @Date 2018/9/9 22:27
 * @Version 1.0
 */
@Data
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result error() {
        return error("fail", "未知异常，请联系管理员");
    }

    public static Result error(String msg) {
        return error("fail", msg);
    }

    public static Result error(String code, String msg) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
    public static Result error(String code, String msg,Object data) {
        if(StringUtils.isEmpty(code)){
            code = "fail";
        }
        if(StringUtils.isEmpty(msg)){
            msg = "未知异常，请联系管理员";
        }
        return Result.ok(code,msg,data);
    }
    public static Result ok(String code,String msg,Object data) {
        if(StringUtils.isEmpty(code)){
            code = "success";
        }
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result ok(String msg) {
        return ok("success",msg,null);
    }

    public static Result ok() {
        return ok("success","",null);
    }

    public static Result ok(Object data){
        return ok("success","",data);
    }

    public static Result resData(Object data){
        return ok("success","",data);
    }
}
