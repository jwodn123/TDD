package com.example.tdd.web.entity;

import lombok.Data;

@Data
public class Content {

    private Long id;
    private String title;
    private String texts;

    private String writer;
    private String password;

    private String updateDate;

}
