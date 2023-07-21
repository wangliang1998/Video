package com.example.video_back.mapper;

import com.example.video_back.pojo.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    //用户登录
    @Select("select * from account where username=#{username} and password=#{password}")
    public Account accountLogin(String username, String password);

    //分页获取所有用户
    @Select("select * from account")
    public List<Account> getAllUsers();

    //搜索用户
    @Select("select * from account where username=#{username}")
    public Account getAccount(String username);

    //用户删除
    @Delete("delete from account where username=#{username}")
    public int delete(String username);

    //用户信息修改
    @Update("update account set password=#{password},access=#{access} where username=#{username}")
    public int update(Account account);

    //增加用户
    @Insert("insert into account values(#{username},#{password},#{access})")
    public int adduser(Account account);


}
