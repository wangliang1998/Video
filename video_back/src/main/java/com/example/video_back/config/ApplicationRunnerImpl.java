package com.example.video_back.config;

import com.example.video_back.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MovieService movieService;



    @Override
    public void run(ApplicationArguments args) throws Exception {
        //清除redis缓存
        Set<String> keys = redisTemplate.keys("*");
        // 如果存在key，则逐个删除
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        //获取所有电影
        List<String> allMovieNames = movieService.getAllMovieNames();
        //存入redis
        redisTemplate.opsForSet().add("movieNames",  allMovieNames.toArray(new String[0]));
        System.out.println("电影名称缓存完成");
    }
}
