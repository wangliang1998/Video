package com.example.video_back.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

        private String code;
        private String msg;
        private Object data;

    public static Result succ(Object data) {
         Result m = new Result();
         m.setCode("200");
         m.setData(data);
         m.setMsg("操作成功");
         return m;
     }
     public static Result succ(String mess, Object data) {
         Result m = new Result();
         m.setCode("200");
         m.setData(data);
         m.setMsg(mess);
         return m;
     }
     public static Result fail(String mess) {
         Result m = new Result();
         m.setCode("400");
         m.setData(null);
         m.setMsg(mess);
         return m;
     }
    public static Result fail(String mess, Object data) {
            Result m = new Result();
            m.setCode("400");
            m.setData(data);
            m.setMsg(mess);
            return m;
     }
    public static Result fail(int code,String mess, Object data) {
        Result m = new Result();
        m.setCode(String.valueOf(code));
        m.setData(data);
        m.setMsg(mess);
        return m;
    }
}
