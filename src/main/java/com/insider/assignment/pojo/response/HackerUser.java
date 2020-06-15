package com.insider.assignment.pojo.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class HackerUser {

    private Long created;
    private String id;
    private Integer karma;
    private List<Long> submitted;

}
