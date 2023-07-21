package com.example.video_back.service;

import com.example.video_back.mapper.KindMapper;
import com.example.video_back.mapper.MovieMapper;
import com.example.video_back.pojo.Kinds;
import com.example.video_back.pojo.Movie;
import com.example.video_back.pojo.movieKind;
import com.example.video_back.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class KindService {

    @Autowired
    private KindMapper mapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 得到所有分类电影
     */
    public List<Kinds> getAllKinds() throws JsonProcessingException {
        String keys = "select:movie:kinds";
        String result = redisTemplate.opsForValue().get(keys);
        if(result!=null){
            List<Kinds> list = new ObjectMapper().readValue(result, new TypeReference<List<Kinds>>() {});
            return list;
        }
        List<Kinds> list = mapper.getAllKinds();
        String json = new ObjectMapper().writeValueAsString(list);
        redisTemplate.opsForValue().set(keys,json,30, TimeUnit.MINUTES);
        return list;
    }

    /***
     * 增加电影分类
     */
    public int addKind(int id,String name){
        return mapper.addKind(id,name);
    }

    /**
     * 电影类别删除
     */
    public int deleteKind(int id){
        return mapper.deleteKind(id);
    }

    /**
     * 电影类别修改
     */
    public int updateKind(int id,String name){
        return mapper.updateKind(id,name);
    }

    /**
     * 根据id删除电影类别
     * @param id
     */
    public int deleteById(int id){
        return mapper.deleteById(id);
    }

    /**
     * 增加电影时，批量添加类别
     * 将电影类别添加到movie_kind表，用于分类查看
     */
    public int batchAddKinds(List<movieKind> lists){
        return mapper.batchAddKinds(lists);
    }


}
