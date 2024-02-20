package com.socialMeli.controller;

import com.socialMeli.dto.request.PostDTO;
import com.socialMeli.dto.response.PublicationDto;
import com.socialMeli.service.IPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostController {

    private final IPostService postService;


    @GetMapping("/products/followed/{userId}/list")
    public ResponseEntity<PublicationDto> obtainLastPublicationsByTheFollowedVendors(
                                                            @PathVariable Integer userId,
                                                            @RequestParam String order) {
        return ResponseEntity.ok().body(postService.obtainLastPublicationsByTheFollowedVendors(userId, order));
    }

    @PostMapping("/products/post")
    public ResponseEntity<?> addNewPost(@RequestBody PostDTO postDto){
        postService.addPost(postDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
