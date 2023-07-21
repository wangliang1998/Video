package com.example.video_back.mapper;

import com.example.video_back.pojo.Movie;
import org.apache.ibatis.annotations.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Mapper
public interface MovieMapper {

    /**
     * 获取所有电影名称
     *
     */
    @Select("select name from movies")
    public List<String> getAllMovieNames();

    /**
     * 获取下一位应插入id
     */
    @Select("SELECT MAX(id) max FROM movies")
    public int get_auto_increase();

    /**
     * 增加电影
     */
    @Insert("insert into movies(id,name,actors,information,picture_url,movie_url,jiexi_url,area,score,popular,language,time,director) " +
            "values(#{id},#{name},#{actors},#{information},#{picture_url},#{movie_url},#{jiexi_url}," +
            "#{area},#{score},#{popular},#{language},#{time},#{director})")
    public int addMovieSource(Movie movie);

    /**
     * 批量增加电影数据
     */
    public int batchAddMovies(List<Movie> lists);


    /**
     * 判断电影是否存在
     * @param name
     * @return
     */
    @Select("SELECT count(*) count FROM movies where name=#{name}")
    public int isNameExist(String name);

    /**
     * 通过电影id获取电影信息
     */
    @Select("SELECT * FROM movies where id =#{id}")
    public Movie getMoviesById(int id);

    /**
     * 通过电影名称获取电影信息
     */
    @Select("SELECT * FROM movies where name =#{name}")
    public Movie getMoviesByName(String name);

    /**
     * 通过电影名称修改电影播放地址
     */
    @Update("update movies set jiexi_url = #{jx} where name = #{name}")
    public int updateMovieByName(String name,String jx);

    /**
     * 通过电影名称添加movie_url
     */
    @Update("update movies set movie_url = #{movie_url} where name = #{name}")
    public int updateMovieUrlByName(String name,String movie_url);

    /**
     * 获取所有电影数量
     */
    @Select("SELECT count(id) count FROM movies")
    public int getAllMoviesCount();

    /**
     * 获取所有电影:按日期排序
     */
    @Select("select * from movies order by time desc")
    public List<Movie> getAll_Movies_Date();

    /***
     * 获取所有电影：按评分排序
     */
    @Select("select * from movies order by score desc")
    public List<Movie> getAll_Movies_Score();

    /***
     * 根据电影id获取电影类型
     */
    @Select("select name from movie_kind join kinds on(movie_kind.kind = kinds.kind) where movie_id=#{id}")
    public List<String> getMovieKinds(int id);

    /**
     * 电影搜索：通过电影名称、电影简介、电影演员
     */
    public List<Movie> search_Movies(String keys);

    /**
     * 获取分类电影
     */
    @Select("SELECT * from movies JOIN movie_kind on(movies.id = movie_kind.movie_id) where kind = #{kind}")
    public List<Movie> get_kind_Movies(int kind);

    /***
     * 通过地区获取电影
     * @return
     */
    public List<Movie> get_Area_Movies(int aid);

    /***
     * 通过语言获取电影
     * @return
     */
    public List<Movie> get_Language_Movies(int lid);

    /**
     * 根据id删除电影
     * @param id
     */
    @Delete("delete from movies where id = #{id}")
    public int deleteMovieById(int id);

    /**
     * 根据id删除电影类型
     * @param id
     */
    @Delete("delete from movie_kind where movie_id = #{id}")
    public int deleteMovieKindById(int id);

    /***
     * 电影信息修改
     */
    @Update("update movies set picture_url=#{picture_url},movie_url=#{picture_url}," +
            "jiexi_url=#{jiexi_url},score=#{score},popular=#{popular} where id=#{id}")
    public int updateMovie(Movie movie);

}
