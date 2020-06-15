package com.insider.assignment.service;

import com.insider.assignment.pojo.response.Comment;
import com.insider.assignment.pojo.response.Story;

import java.util.List;

public interface HackerRankService {

    public List<Story> getTopStories();
    public List<Comment> getCommentsForAStory(Long id);
    public List<Story> getPastTopStories();

}
