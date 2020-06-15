package com.insider.assignment.dao;

import com.insider.assignment.pojo.response.Comment;
import com.insider.assignment.pojo.response.Story;

import java.util.List;

public interface HackerRankDAO {

    public List<Story> getTopStories();
    public List<Comment> getCommentsForAStory(Long id);
    public List<Story> getPastTopStories();

}
