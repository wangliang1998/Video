package com.example.video_back.mapper;

import com.example.video_back.pojo.Kinds;
import com.example.video_back.pojo.movieKind;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface KindMapper {

    /**
     * 得到所有分类电影
     */
    @Select("select kind,name from kinds")
    public List<Kinds> getAllKinds();

    /***
     * 增加电影分类
     */
    @Insert("insert into kinds values(#{id},#{name})")
    public int addKind(int id,String name);

    /**
     * 电影类别删除
     */
    @Delete("delete from kinds where kind=#{id}")
    public int deleteKind(int id);

    /**
     * 电影类别修改
     */
    @Update("update kinds set name= #{name} where kind = #{id}")
    public int updateKind(int id,String name);

    /**
     * 根据id删除电影类别
     * @param id
     */
    @Delete("delete from movie_kind where movie_id=#{id}")
    public int deleteById(int id);

    /**
     * 增加电影时，批量添加类别
     * 将电影类别添加到movie_kind表，用于分类查看
     */
    public int batchAddKinds(List<movieKind> lists);


}
