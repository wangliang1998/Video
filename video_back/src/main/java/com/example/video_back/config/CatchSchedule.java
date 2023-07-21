package com.example.video_back.config;

import com.example.video_back.collect.catch_aqiyi;
import com.example.video_back.pojo.Aqiyi;
import com.example.video_back.pojo.Movie;
import com.example.video_back.pojo.movieKind;
import com.example.video_back.service.KindService;
import com.example.video_back.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CatchSchedule {
    @Autowired
    private MovieService dm;
    @Autowired
    private KindService dk;

    @Autowired
    private catch_aqiyi ca;

    @Autowired
    private StringRedisTemplate redisTemplate;


    //cron 表达式：秒，分，时，日，月，周几
    /**
     * 30 15 10 * * ? 每天10点15分30秒执行一次
     * 30 0/5 10,18 * * ？ 每天10点，18点，每隔5分钟执行一次
     */
    //爱奇艺爬取:每天2点执行一次
    //0 0 2 * * ?
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void runAqiyi() throws SQLException, InterruptedException {

        //创建固定核心线程为10的线程池
        ExecutorService executorService = new ThreadPoolExecutor(
                10,
                30,
                1,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        CountDownLatch endLatch = new CountDownLatch(30);

        //Aqiyi保存一个电影和其类别的关系
        List<Aqiyi> result = new ArrayList<>();

        for(int i=0;i<30;i++){
            int page = i;
            executorService.execute(()->{
                try {
                    // 处理任务并保存结果
                    String url = "https://pcw-api.iqiyi.com/search/recommend/list?channel_id=1&data_type=1&mode=4&page_id="+page+"&ret_num=60";
                    List<Aqiyi> re = ca.getCatch_aqiyis(url);
                    if(re!=null){
                        result.addAll(re);
                    }
                    // 释放结束信号
                    endLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        // 等待所有任务完成
        endLatch.await();

        // 所有任务处理完毕，进行最后处理：统一进行主键赋值

        //获取电影id自动增长值
        int next_id = dm.get_auto_increase()+1;

        List<Movie> movies = new ArrayList<>();
        List<movieKind> movieKinds = new ArrayList<>();

        //记录电影名称
        List<String> movieNames = new ArrayList<>();

        for(int i=0;i<result.size();i++){
            Aqiyi m = result.get(i);
            Movie movie = m.getMovies();

            //判断电影是否重复
            if(movieNames.contains(movie.getName())){
                continue;
            }
            movieNames.add(movie.getName());

            movie.setId(next_id);
            movies.add(movie);

            List<movieKind> k = m.getMovie_kinds();
            for(int j=0;j<k.size();j++){
                movieKind mK = k.get(j);
                mK.setMovie_id(next_id);
                movieKinds.add(mK);
            }
            next_id++;
        }

        executorService.shutdown();

        dm.batchAddMovies(movies);
        dk.batchAddKinds(movieKinds);

//        保存新的电影名称到redis
        redisTemplate.opsForSet().add("movieNames",  movieNames.toArray(new String[0]));

    }
}
