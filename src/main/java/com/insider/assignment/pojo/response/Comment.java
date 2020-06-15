package com.insider.assignment.pojo.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Comment {

    private String text;

    private String userHandle;

    private Integer age;

    private Integer replies;
}
