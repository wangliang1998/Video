package com.example.video_back.mapper;

import com.example.video_back.pojo.Jiexi;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JieXiMapper {

    //获取所有解析接口
    @Select("select * from jiexi order by `rank` desc")
    public List<Jiexi> getAllJieXi();

    //增加解析接口
    @Insert("insert into jiexi(name,url,`rank`) values(#{name},#{url},#{rank})")
    public int addJieXi(Jiexi jiexi);

    //修改解析接口
    @Update("update jiexi set name=#{name},url=#{url},`rank`=#{rank} where id=#{id}")
    public int updateJieXi(Jiexi jiexi);

    //删除解析接口
    @Delete("delete from jiexi where id = #{id}")
    public int deleteJieXi(int id);

}
