package com.insider.assignment.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter @Setter
@Entity
@Table(name = "stories")
public class Stories {

    @Id
    @Column(name = "storyid")
    private Long storyId;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "score")
    private Integer score;

    @Column(name = "timeofsubmission")
    private Date timeOfSubmission;

    @Column(name = "storyuser")
    private String user;

}
