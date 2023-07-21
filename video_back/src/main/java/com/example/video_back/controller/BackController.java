package com.example.video_back.controller;


import com.example.video_back.pojo.*;
import com.example.video_back.service.JieXiService;
import com.example.video_back.service.KindService;
import com.example.video_back.service.MovieService;
import com.example.video_back.service.UserService;
import com.example.video_back.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class BackController {

    @Autowired
    private UserService userService;

    @Autowired
    private KindService kindService;


    @Autowired
    private MovieService movieService;

    @Autowired
    private JieXiService jieXiService;

    //获取所有用户
    @GetMapping("/user/back/all/{page}")
    public Result getAllUsers(@PathVariable("page") int page){
        Map<String,Object> map = new HashMap<>();
        map = userService.getAllUsers(page);
        return Result.succ(map);
    }


    //用户搜索
    @RequestMapping("/user/back/search")
    public Result search(@RequestParam("username") String username){
        List<Account> list = new ArrayList<>();
        Account aa = userService.getAccount(username);
        list.add(aa);
        if(aa==null){
            return Result.fail("用户不存在");
        }
        return Result.succ(list);
    }

    //用户删除
    @RequestMapping("/user/back/delete/{username}")
    public Result delete(@PathVariable("username") String username){
        int count = userService.delete(username);
        if(count>0)
            return Result.succ(null);
        else
            return Result.fail("用户删除失败");
    }

    //用户信息修改
    @RequestMapping("/user/back/update")
    public Result update(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("access") int access){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setAccess(access);
        int count = userService.update(account);
        if(count>0)
            return Result.succ(null);
        else
            return Result.fail("用户信息修改失败");
    }
    //增加用户
    @RequestMapping("/user/back/add")
    public Result adduser(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("access") int access){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setAccess(access);
        int count = userService.adduser(account);
        if(count>0)
            return Result.succ(null);
        else
            return Result.fail("增加用户失败");
    }

    //电影类别信息
    @GetMapping("/movie/back/kind/select")
    public Result getKinds() throws JsonProcessingException {
        Map<String,Object> map = new HashMap<>();
        List<Kinds> list = kindService.getAllKinds();
        map.put("kinds",list);
        map.put("count",list.size());
        return Result.succ(map);
    }
    //电影类别增加
    @RequestMapping("/movie/back/kind/add")
    public Result addKind(@RequestParam("id") int id,@RequestParam("name") String name
                          ){
        kindService.addKind(id,name);
        return Result.succ(null);
    }

    //电影类别删除
    @GetMapping("/movie/back/kind/delete/{id}")
    public Result deleteKind(@PathVariable("id") int id){
        kindService.deleteKind(id);
        return Result.succ(null);
    }

    //电影类别信息修改
    @GetMapping("/movie/back/kind/update")
    public Result updateKind(@RequestParam("id") int id,@RequestParam("name") String name){
        kindService.updateKind(id,name);
        return Result.succ(null);
    }

    //电影查重
    @RequestMapping("/movie/back/upload/find/")
    public Result movieExists(@RequestParam("name") String name){
        boolean result = movieService.isNameExist(name);
        return Result.succ(result);
    }

    //电影上传
    @RequestMapping("/movie/back/upload/do")
    @Transactional
    public Result upload(
                         @RequestParam("name") String name,@RequestParam(value = "kind",required = false) String kind,
                         @RequestParam(value = "actors",required = false,defaultValue = "") String actors,@RequestParam(value = "director",required = false) String director,
                         @RequestParam("picture_url") String picture_url,@RequestParam(value = "movie_url",required = false) String movie_url,
                         @RequestParam(value = "jinx_url",required = false) String jinx_url,@RequestParam(value = "area",required = false) String area,
                         @RequestParam(value = "language",required = false) String language,@RequestParam(value = "score",required = false,defaultValue = "0") String score,
                         @RequestParam(value = "time",required = false) String time,@RequestParam(value = "information",required = false) String information
    )  {
        int id = movieService.get_auto_increase();
        Movie movie =  new Movie(id, name, actors, information, picture_url, movie_url,
                jinx_url, area, Float.parseFloat(score), (int) Float.parseFloat(score) * 100, language, time, director);

        //同时添加到最新电影和原始电影
        movieService.addMovieSource(movie);
        //添加电影分类
        List<movieKind> movieKinds = new ArrayList<>();
        for (String ss:kind.split(",")) {
            movieKinds.add(new movieKind(id, Integer.parseInt(ss)));
        }
        kindService.batchAddKinds(movieKinds);
        return Result.succ(null);
    }

    /**
     * 电影模糊查询
     */
    @RequestMapping("/movie/back/search")
    public Result searchMovie(@RequestParam("name") String name,@RequestParam("page") int page){
        Map<String,Object> map = new HashMap<>();
        map =  movieService.search_Movies(name,page);
        return Result.succ(map);
    }

    /**
     * 根据电影id删除电影
     * 流程：
     *   设置了触发器，只要删除movies表即可，其他表会一并删除
     *   1.删除movies表
     *   2.删除movie_kind表
     *
     */
    @GetMapping("/movie/back/delete")
    @Transactional
   public Result deleteMovie(@RequestParam("id") int id){
        //删除电影表
        movieService.deleteMovieById(id);
        //批量删除删除movie_kind表
        movieService.deleteMovieKindById(id);
        return Result.succ(null);
   }

    /**
     * 电影信息更改
     */
    @RequestMapping("/movie/back/update")
    public Result updateMovie(@RequestParam("id") int id,
                              @RequestParam("name") String name,
                              @RequestParam("picture_url") String picture_url,
                              @RequestParam("movie_url") String movie_url,
                              @RequestParam("jiexi_url") String jinx_url,
                              @RequestParam("score") String score,
                              @RequestParam("popular") String popular) throws JsonProcessingException {
        Movie movie = movieService.getMoviesById(id);
        movie.setName(name);
        movie.setPicture_url(picture_url);
        movie.setMovie_url(movie_url);
        movie.setJiexi_url(jinx_url);
        movie.setScore(Float.parseFloat(score));
        movie.setPopular(Integer.parseInt(popular));
        movieService.updateMovie(movie);
        return Result.succ(null);
    }


    //接口类别信息
    @GetMapping("/jx/back/select")
    public Result getJieKous(){
        List<Jiexi> list = jieXiService.getAllJieXi();
        return Result.succ(list);
    }

    //接口增加
    @RequestMapping("/jx/back/add")
    public Result addJieKou(@RequestParam("name") String name,@RequestParam("url") String url,@RequestParam("rank") int rank){
        Jiexi jiexi = new Jiexi();
        jiexi.setName(name);
        jiexi.setRank(rank);
        jiexi.setUrl(url);
        jiexi.setId(-1);
        jieXiService.addJieXi(jiexi);
        return Result.succ(null);
    }

    //接口删除
    @GetMapping("/jx/back/delete/{id}")
    public Result deleteJieKou(@PathVariable("id") int id){
        jieXiService.deleteJieXi(id);
        return Result.succ(null);
    }

    //接口信息修改
    @RequestMapping("/jx/back/update")
    public Result updateJieKou(@RequestParam("id") int id,@RequestParam("name") String name,
                               @RequestParam("url") String url,@RequestParam("rank") int rank
                               ){
        Jiexi jiexi = new Jiexi();
        jiexi.setName(name);
        jiexi.setRank(rank);
        jiexi.setUrl(url);
        jiexi.setId(id);
        jieXiService.updateJieXi(jiexi);
        return Result.succ(null);
    }


}
