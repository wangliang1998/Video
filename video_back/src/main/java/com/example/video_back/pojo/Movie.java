package com.example.video_back.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Movie implements Serializable{
	private int id;
	private String name;
	private String actors;
	private String information;
	private String picture_url;
	private String movie_url;
	private String jiexi_url;
	private String area;
	private float score;
	private int popular;
	private String language;
	private String time;
	private String director;

    public Movie(int id, String name, String actors, String information, String picture_url, String movie_url,
                  String jiexi_url, String area, float score, int popular, String language, String time, String director) {
        super();
        this.id = id;
        this.name = name;
        this.actors = actors;
        this.information = information;
        this.picture_url = picture_url;
        this.movie_url = movie_url;
        this.jiexi_url = jiexi_url;
        this.area = area;
        this.score = score;
        this.popular = popular;
        this.language = language;
        this.time = time;
        this.director = director;
    }

    public Movie(){

    }

}
