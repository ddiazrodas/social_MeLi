package com.socialMeli.controller;

import com.socialMeli.dto.request.PostDTO;
import com.socialMeli.dto.request.PostDescDTO;
import com.socialMeli.dto.response.MessageDto;
import com.socialMeli.dto.response.PostDto;
import com.socialMeli.dto.response.PublicationDto;
import com.socialMeli.dto.response.QuantityOfPromoPostsByUserIdDto;
import com.socialMeli.service.IPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostController {

    private final IPostService postService;

    @GetMapping("/products/followed/{userId}/list")
    public ResponseEntity<PublicationDto> obtainLastPublicationsByTheFollowedVendors(
                                                            @PathVariable Integer userId,
                                                            @RequestParam(required = false) String order) {
        return ResponseEntity.ok().body(postService
                .obtainLastPublicationsByTheFollowedVendors(userId, order));
    }

    @PostMapping("/products/post")
    public ResponseEntity<MessageDto> addNewPost(@RequestBody PostDTO postDto){
        postService.addPost(postDto);
        return new ResponseEntity<>(new MessageDto("Post creado con éxito"), HttpStatus.OK);
    }
    // US 0010: Da de alta una nueva publicacion/producto con descuento ddiazrodas
    @PostMapping("/products/promo-post")
    public ResponseEntity<?> addNewPromotionPost(@RequestBody PostDescDTO postDescDTO) {
        postService.addPost(postDescDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //US 0011: trae el listado de los productos en promo de un determinado vendedor
    @GetMapping("/products/promo-post/count")
    public ResponseEntity<QuantityOfPromoPostsByUserIdDto> getPromoPostsByVendor(@RequestParam Integer userId) {
        return ResponseEntity.ok().body(postService.getPromotionPostsById(userId));
    }
}
