package com.example.video_back.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class runPage implements Serializable{
	
	private String run_name;
	private Integer page;

}
