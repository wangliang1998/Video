package com.example.video_back.service;

import com.example.video_back.mapper.JieXiMapper;
import com.example.video_back.pojo.Jiexi;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JieXiService {

    @Autowired
    private JieXiMapper mapper;

    //获取所有解析接口
    public List<Jiexi> getAllJieXi(){
        return mapper.getAllJieXi();
    }

    //增加解析接口
    public int addJieXi(Jiexi jiexi){
        return mapper.addJieXi(jiexi);
    }

    //修改解析接口
    public int updateJieXi(Jiexi jiexi){
        return mapper.updateJieXi(jiexi);
    }

    //删除解析接口
    public int deleteJieXi(int id){
        return mapper.deleteJieXi(id);
    }

}
