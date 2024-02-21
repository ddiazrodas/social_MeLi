package com.socialMeli.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialMeli.entity.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    Integer id;
    @JsonProperty("user_id")
    Integer userId;
    LocalDate date;
    ProductDto product;
    Integer category;
    Double price;

    public PostDto(Integer id, Post post, ProductDto postProductDto) {
        this.id = id;
        this.userId = post.getUserId();
        this.date = post.getDate();
        this.product = postProductDto;
        this.category = post.getCategory();
        this.price = post.getPrice();
    }
}
