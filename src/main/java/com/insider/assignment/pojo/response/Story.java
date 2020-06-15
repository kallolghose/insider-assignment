package com.insider.assignment.pojo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class Story {

    private String title;

    private String url;

    private Integer score;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Date timeOfSubmission;

    private String user;

}
