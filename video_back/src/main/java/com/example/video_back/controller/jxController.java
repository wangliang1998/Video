package com.example.video_back.controller;

import com.example.video_back.pojo.Movie;
import com.example.video_back.service.JieXiService;
import com.example.video_back.service.MovieService;
import com.example.video_back.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class jxController {

    @Autowired
    private JieXiService jieXiService;

    //获取所有解析接口
    @GetMapping("/jx/front/getAllJieKou")
    public Result getAllJieKou(){
        return Result.succ(jieXiService.getAllJieXi());
    }

}
