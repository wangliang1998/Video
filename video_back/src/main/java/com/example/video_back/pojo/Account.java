package com.example.video_back.pojo;


import lombok.Data;

import java.io.Serializable;

@Data
public class Account implements Serializable{
	private String username;
	private String password;
	private int access;
}
