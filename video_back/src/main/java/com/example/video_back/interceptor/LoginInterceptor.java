package com.example.video_back.interceptor;

import com.example.video_back.pojo.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;

    public LoginInterceptor(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        //查询redis，查看用户是否已经登录
        String token = request.getHeader("Token");
        Map<String,Object> map = new HashMap<>();
        String access = redisTemplate.opsForValue().get(token);

        if(token==null || access==null){
            map.put("code", "400");
            map.put("msg", "普通用户请先登录");
            String json = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            /* 允许跨域的请求方法GET, POST, HEAD 等 */
            response.setHeader("Access-Control-Allow-Methods", "*");
            /* 重新预检验跨域的缓存时间 (s) */
            response.setHeader("Access-Control-Max-Age", "3600");
            /* 允许跨域的请求头 */
            response.setHeader("Access-Control-Allow-Headers", "*");
            /* 是否携带cookie */
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.getWriter().println(json);
            return false;
        }
        //放行
        return true;
    }
}
