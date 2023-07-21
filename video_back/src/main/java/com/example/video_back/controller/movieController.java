package com.example.video_back.controller;

import com.example.video_back.config.CatchSchedule;
import com.example.video_back.pojo.Kinds;
import com.example.video_back.pojo.Movie;
import com.example.video_back.service.KindService;
import com.example.video_back.service.MovieService;
import com.example.video_back.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class movieController {

    @Autowired
    private MovieService service;

    @Autowired
    private KindService kindService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /***
     * 电影首页数据显示
     * @param page
     * @return
     */
    @GetMapping("/movie/front/{type}/{page}")
    public Result PageSplit(@PathVariable("page") int page, @PathVariable("type") String type) throws JsonProcessingException {
        String keys = "select:movie:"+type+":"+page;
        String result = redisTemplate.opsForValue().get(keys);
        if(result!=null){
            Map<String, Object> map = new ObjectMapper().readValue(result, new TypeReference<Map<String, Object>>() {});
            return Result.succ(map);
        }
        Map<String, Object> map = null;
        if ("default".equals(type)) {
            map = service.getAll_Movies_Date(page);
        } else if ("hot".equals(type)) {
            map = service.getAll_Movies_Score(page);
        } else if ("new".equals(type)) {
            map = service.getAll_Movies_Date(page);
        } else if (type.contains("kind")) {
            int kind = Integer.parseInt(type.split("_")[1]);
           map = service.get_kind_Movies(kind,page);
        } else if (type.contains("area")) {
            int aid = Integer.parseInt(type.split("_")[1]);
            map = service.get_Area_Movies(aid,page);
        } else if (type.contains("language")) {
            int lid = Integer.parseInt(type.split("_")[1]);
            map = service.get_Language_Movies(lid,page);
        }
        String json = new ObjectMapper().writeValueAsString(map);
        redisTemplate.opsForValue().set(keys,json,30, TimeUnit.MINUTES);
        return Result.succ(map);
    }

    //获取电影分类
    @PostMapping("/movie/getKinds")
    public Result getMovieKinds() throws JsonProcessingException {
        List<Kinds> kinds = null;
        kinds = kindService.getAllKinds();
        return Result.succ(kinds);
    }

    //电影搜索
    @RequestMapping ("/movie/front/search/{page}")
    public Result search(@RequestParam("search_name") String name, @PathVariable("page") int page) throws JsonProcessingException {

        String keys = "select:movie:search:" + name + ":" + page;
        String result = redisTemplate.opsForValue().get(keys);
        if (result != null) {
            Map<String, Object> map = new ObjectMapper().readValue(result, new TypeReference<Map<String, Object>>() {
            });
            return Result.succ(map);
        }

        Map<String,Object> map = service.search_Movies(name,page);

        String json = new ObjectMapper().writeValueAsString(map);
        redisTemplate.opsForValue().set(keys,json,30, TimeUnit.MINUTES);
        return Result.succ(map);
    }



}
