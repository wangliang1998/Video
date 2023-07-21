package com.example.video_back.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Aqiyi {
	private Movie movies;
	private List<movieKind> movie_kinds;

    public Aqiyi(Movie movies, List<movieKind> movie_kinds) {
        this.movies = movies;
        this.movie_kinds = movie_kinds;
    }
}
