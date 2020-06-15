package com.insider.assignment.controller;

import com.insider.assignment.service.HackerRankService;
import com.insider.assignment.pojo.response.APIResponse;
import com.insider.assignment.pojo.response.Comment;
import com.insider.assignment.pojo.response.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v0")
public class APIController {


    @Autowired
    private HackerRankService hackerRankAPIs;

    @GetMapping("/top-stories")
    public ResponseEntity<APIResponse> getTopStories(){
        try {
            List<Story> stories = hackerRankAPIs.getTopStories();
            APIResponse apiResponse = new APIResponse();
            apiResponse.setStatus(true);
            apiResponse.setMessage("Top Stories");
            apiResponse.setData(stories);
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            APIResponse apiResponse = new APIResponse();
            apiResponse.setStatus(false);
            apiResponse.setMessage("Error on Top Stories");
            apiResponse.setErrors(new ArrayList<String>(){{add(e.getMessage());}});
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/comment/{storyid}")
    public ResponseEntity<APIResponse> getComments(@PathVariable(name = "storyid") Long storyId){
        try {
            List<Comment> comments = hackerRankAPIs.getCommentsForAStory(storyId);
            APIResponse apiResponse = new APIResponse();
            apiResponse.setStatus(true);
            apiResponse.setMessage("Comments for Story ID : " + storyId);
            apiResponse.setData(comments);
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            APIResponse apiResponse = new APIResponse();
            apiResponse.setStatus(false);
            apiResponse.setMessage("Error on Comments for Story ID : " + storyId);
            apiResponse.setErrors(new ArrayList<String>(){{add(e.getMessage());}});
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/past-stories")
    public ResponseEntity<APIResponse> getPastStories(){
        try {
            List<Story> stories = hackerRankAPIs.getPastTopStories();
            APIResponse apiResponse = new APIResponse();
            apiResponse.setStatus(true);
            apiResponse.setMessage("Past Stories");
            apiResponse.setData(stories);
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            APIResponse apiResponse = new APIResponse();
            apiResponse.setStatus(false);
            apiResponse.setMessage("Error on Past Stories");
            apiResponse.setErrors(new ArrayList<String>(){{add(e.getMessage());}});
            return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
