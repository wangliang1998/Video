package com.example.video_back.controller;

import com.example.video_back.pojo.Movie;
import com.example.video_back.service.JieXiService;
import com.example.video_back.service.MovieService;
import com.example.video_back.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class InformationController {

    @Autowired
    private MovieService service;

    @Autowired
    private JieXiService jieXiService;

    @GetMapping("/movie/front/information/{id}")
    public Result getInformation(@PathVariable("id") int id) throws JsonProcessingException {
            Map<String,Object> map = new HashMap<>();
            Movie movie = service.getMoviesById(id);
            map.put("movie",movie);
            List<String> kinds= service.getMovieKinds(id);
            String ss="";
            for(int i=0;i<kinds.size();i++)
                ss=ss + kinds.get(i).toString();
            map.put("kind",ss);

            return Result.succ(map);
    }
}
