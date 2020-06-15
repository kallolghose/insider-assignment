package com.insider.assignment.service;

import com.insider.assignment.entity.Stories;
import com.insider.assignment.pojo.response.Comment;
import com.insider.assignment.pojo.response.HackerItem;
import com.insider.assignment.pojo.response.HackerUser;
import com.insider.assignment.pojo.response.Story;
import com.insider.assignment.repository.StoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class HackerRankServiceImpl implements HackerRankService {

    RestTemplate restTemplate = new RestTemplate();
    String URL = "https://hacker-news.firebaseio.com/v0";

    @Autowired
    private StoriesRepository storiesRepository;

    @Cacheable(value = "topStories", key = "topStories")
    @Override
    public List<Story> getTopStories() {
        String topStories = URL + "/topstories.json?print=pretty";
        ResponseEntity<Long[]> responseEntity =  restTemplate.getForEntity(topStories, Long[].class);
        if(responseEntity.getStatusCodeValue() == 200){
            Long [] ids = responseEntity.getBody();
            List<Stories> storiesList = new ArrayList<>();
            for(int i=0; i< ids.length; i++){
                Long storyId = ids[i];
                String storyURL = URL + "/item/" + storyId + ".json?print=pretty";
                ResponseEntity<HackerItem> storyItem = restTemplate.getForEntity(storyURL, HackerItem.class);
                if(storyItem.getStatusCodeValue() == 200){
                    HackerItem _hackerItem = storyItem.getBody();

                    Stories stories = new Stories();
                    stories.setStoryId(_hackerItem.getId());
                    stories.setTitle(_hackerItem.getTitle());
                    stories.setUrl(_hackerItem.getUrl());
                    stories.setScore(_hackerItem.getScore());
                    stories.setUser(_hackerItem.getBy());
                    stories.setTimeOfSubmission(new Date(_hackerItem.getTime()));
                    storiesList.add(stories);
                }
            }
            storiesList.sort((s1, s2) -> s2.getScore() - s1.getScore());
            List<Story> _myStories = new ArrayList<>();
            for(int i=0; i<10; i++){
                Stories stories = storiesList.get(i);
                if(storiesRepository.findById(stories.getStoryId()) == null)
                    storiesRepository.save(stories);
                Story story = new Story();
                story.setTitle(stories.getTitle());
                story.setScore(stories.getScore());
                story.setTimeOfSubmission(stories.getTimeOfSubmission());
                story.setUser(stories.getUser());
                story.setUrl(stories.getUrl());
                _myStories.add(story);
            }
            return _myStories;
        }
        return new ArrayList<>();
    }

    @Cacheable(value = "comments", key = "#storyId")
    @Override
    public List<Comment> getCommentsForAStory(Long storyId) {

        String storyURL = URL + "/item/" + storyId + ".json?print=pretty";
        ResponseEntity<HackerItem> storyItem = restTemplate.getForEntity(storyURL, HackerItem.class);
        HackerItem hackerItem = storyItem.getBody();
        List<Long> commentIds = hackerItem.getKids();
        List<Comment> comments = new ArrayList<>();

        for(Long commentId : commentIds) {
            String commentURL = URL + "/item/" + commentId + ".json?print=pretty";
            ResponseEntity<HackerItem> commentRE = restTemplate.getForEntity(commentURL, HackerItem.class);

            if(commentRE.getStatusCodeValue() == 200) {
                HackerItem commentItem = commentRE.getBody();
                String userURL = URL + "/user/" + commentItem.getBy() + ".json?print=pretty";
                ResponseEntity<HackerUser> userItemRE = restTemplate.getForEntity(userURL, HackerUser.class);
                if(userItemRE.getStatusCodeValue() == 200) {
                    HackerUser hackerUser = userItemRE.getBody();
                    Comment _comment = new Comment();
                    _comment.setText(commentItem.getText());
                    _comment.setUserHandle(hackerUser.getId());
                    _comment.setAge(getDiffYears(new Date(), new Date(hackerUser.getCreated())));
                    if(commentItem.getKids()!=null)
                        _comment.setReplies(commentItem.getKids().size());
                    else
                        _comment.setReplies(0);
                    comments.add(_comment);
                }
            }
        }
        comments.sort((c1, c2) -> { return c2.getReplies() - c1.getReplies();});
        return comments;
    }

    @Override
    public List<Story> getPastTopStories() {
        List<Stories> storiesList = storiesRepository.findAll();
        List<Story> _myStories = new ArrayList<>();
        for(int i=0; i<storiesList.size(); i++){
            Stories stories = storiesList.get(i);
            Story story = new Story();
            story.setTitle(stories.getTitle());
            story.setScore(stories.getScore());
            story.setTimeOfSubmission(stories.getTimeOfSubmission());
            story.setUser(stories.getUser());
            story.setUrl(stories.getUrl());
            _myStories.add(story);
        }
        return _myStories;
    }

    private int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return Math.abs(diff);
    }

    private Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}
