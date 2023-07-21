package com.example.video_back.pojo;

import lombok.Data;
import org.apache.ibatis.annotations.ConstructorArgs;

import java.io.Serializable;

@Data
public class movieKind implements Serializable{
	private int movie_id;
	private int kind;

    public movieKind(int movie_id, int kind) {
        this.movie_id = movie_id;
        this.kind = kind;
    }
}
