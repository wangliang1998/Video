package com.example.video_back.service;

import com.example.video_back.mapper.MovieMapper;
import com.example.video_back.pojo.Movie;
import com.example.video_back.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MovieService {

    @Autowired
    private MovieMapper mapper;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 获取所有电影名称
     *
     */
    public List<String> getAllMovieNames(){
        return mapper.getAllMovieNames();
    }


    /**
     * 获取下一位应插入id
     */
    public int get_auto_increase(){
        int id = mapper.get_auto_increase();
        return id+1;
    }

    /**
     * 增加电影
     */
    public int addMovieSource(Movie movie){
        return mapper.addMovieSource(movie);
    }

    /**
     * 批量增加电影数据
     */
    public int batchAddMovies(List<Movie> lists){
        return mapper.batchAddMovies(lists);
    }


    /**
     * 判断电影是否存在
     */
    public boolean isNameExist(String name){
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("movieNames", name));
    }

    /**
     * 通过电影id获取电影信息
     */
    public Movie getMoviesById(int id) throws JsonProcessingException {
        String keys = "select:movie:id:"+id;
        String result = redisTemplate.opsForValue().get(keys);
        Movie movie = null;
        if(result!=null){
            movie = new ObjectMapper().readValue(result, new TypeReference<Movie>() {});
            return movie;
        }
        movie = mapper.getMoviesById(id);

        String json = new ObjectMapper().writeValueAsString(movie);
        redisTemplate.opsForValue().set(keys,json,30, TimeUnit.MINUTES);

        return movie;
    }

    /**
     * 通过电影名称获取电影信息
     */
    public Movie getMoviesByName(String name){
        return mapper.getMoviesByName(name);
    }

    /**
     * 通过电影名称修改电影播放地址
     */
    public int updateMovieByName(String name,String jx){
        return mapper.updateMovieByName(name,jx);
    }

    /**
     * 通过电影名称添加movie_url
     */
    public int updateMovieUrlByName(String name,String movie_url){
        return mapper.updateMovieUrlByName(name,movie_url);
    }

    /**
     * 获取所有电影数量
     */
    public int getAllMoviesCount(){
        return mapper.getAllMoviesCount();
    }

    /**
     * 获取所有电影:按日期排序
     */
    public Map<String,Object> getAll_Movies_Date(int page){
        PageHelper.startPage(page,30);
        List<Movie> movies = mapper.getAll_Movies_Date();
        PageInfo<Movie> pageInfo = new PageInfo<>(movies);
        Map<String,Object> map = new HashMap<>();
        map.put("movies",movies);
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /***
     * 获取所有电影：按评分排序
     */
    public Map<String,Object> getAll_Movies_Score(int page){
        PageHelper.startPage(page,30);
        List<Movie> movies = mapper.getAll_Movies_Score();
        PageInfo<Movie> pageInfo = new PageInfo<>(movies);
        Map<String,Object> map = new HashMap<>();
        map.put("movies",movies);
        map.put("count",pageInfo.getTotal());
        return map;
    }


    /***
     * 根据电影id获取电影类型
     */
    public List<String> getMovieKinds(int id) throws JsonProcessingException {

        String keys = "select:movie_kind:id:"+id;
        String result = redisTemplate.opsForValue().get(keys);
        List<String> res= null;
        if(result!=null){
            res = new ObjectMapper().readValue(result, new TypeReference<List<String>>() {});
            return res;
        }

        res = mapper.getMovieKinds(id);
        String json = new ObjectMapper().writeValueAsString(res);
        redisTemplate.opsForValue().set(keys,json,30, TimeUnit.MINUTES);

        return res;
    }

    /**
     * 电影搜索：通过电影名称、电影简介
     */
    public Map<String,Object> search_Movies(String keys, int page){
        PageHelper.startPage(page,30);
        List<Movie> movies = mapper.search_Movies(keys);
        PageInfo<Movie> pageInfo = new PageInfo<>(movies);
        Map<String,Object> map = new HashMap<>();
        map.put("movies",movies);
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /**
     * 获取分类电影
     */
    public Map<String,Object> get_kind_Movies(int kind,int page){
        PageHelper.startPage(page,30);
        List<Movie> movies = mapper.get_kind_Movies(kind);
        PageInfo<Movie> pageInfo = new PageInfo<>(movies);
        Map<String,Object> map = new HashMap<>();
        map.put("movies",movies);
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /***
     * 通过地区获取电影
     * @return
     */
    public Map<String,Object> get_Area_Movies(int aid,int page){
        PageHelper.startPage(page,30);
        List<Movie> movies = mapper.get_Area_Movies(aid);
        PageInfo<Movie> pageInfo = new PageInfo<>(movies);
        Map<String,Object> map = new HashMap<>();
        map.put("movies",movies);
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /***
     * 通过语言获取电影
     * @return
     */
    public Map<String,Object> get_Language_Movies(int lid,int page){
        PageHelper.startPage(page,30);
        List<Movie> movies = mapper.get_Language_Movies(lid);
        PageInfo<Movie> pageInfo = new PageInfo<>(movies);
        Map<String,Object> map = new HashMap<>();
        map.put("movies",movies);
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /**
     * 根据id删除电影
     * @param id
     */
    public int deleteMovieById(int id){
        return mapper.deleteMovieById(id);
    }

    /**
     * 根据id删除电影类型
     * @param id
     */
    public int deleteMovieKindById(int id){
        return mapper.deleteMovieKindById(id);
    }

    /***
     * 电影信息修改
     */
    public int updateMovie(Movie movie){
        //数据一致性，先写MYSQL，再删除Redis
        int count = mapper.updateMovie(movie);
        String keys = "select:movie:id:"+movie.getId();
        redisTemplate.delete(keys);
        return count;
    }


}
