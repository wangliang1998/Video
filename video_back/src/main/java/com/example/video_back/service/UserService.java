package com.example.video_back.service;

import com.example.video_back.mapper.UserMapper;
import com.example.video_back.pojo.Account;
import com.example.video_back.pojo.Movie;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //用户登录
    public Account accountLogin(String username, String password){
        return userMapper.accountLogin(username,password);
    }

    //分页获取所有用户
    public Map<String,Object> getAllUsers(int page){
        PageHelper.startPage(page,30);
        List<Account> accounts = userMapper.getAllUsers();
        PageInfo<Account> pageInfo = new PageInfo<>(accounts);
        Map<String,Object> map = new HashMap<>();
        map.put("users",accounts);
        map.put("count",pageInfo.getTotal());
        return map;
    }

    //搜索用户
    public Account getAccount(String username){
        return userMapper.getAccount(username);
    }

    //用户删除
    public int delete(String username){
        return userMapper.delete(username);
    }

    //用户信息修改
    public int update(Account account){
        return userMapper.update(account);
    }

    //增加用户
    public int adduser(Account account){
        return userMapper.adduser(account);
    }

}
