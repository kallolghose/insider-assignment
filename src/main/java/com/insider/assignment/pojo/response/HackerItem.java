package com.insider.assignment.pojo.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class HackerItem {

    private String by;
    private Integer descendants;
    private Long id;
    private List<Long> kids;
    private Long time;
    private String title;
    private String type;
    private String url;
    private Integer score;
    private Long parent;
    private String text;

}
