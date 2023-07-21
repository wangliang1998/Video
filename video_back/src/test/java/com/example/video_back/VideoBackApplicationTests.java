package com.example.video_back;

import com.example.video_back.config.CatchSchedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
class VideoBackApplicationTests {

    @Autowired
    private CatchSchedule schedule;

    @Test
    void contextLoads() throws SQLException, InterruptedException {
        schedule.runAqiyi();
    }

}
