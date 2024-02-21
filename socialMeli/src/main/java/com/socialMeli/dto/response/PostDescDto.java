package com.socialMeli.dto.response;

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
public class PostDescDto extends PostDto{

    Boolean hasPromo;
    Double discount;

    public PostDescDto(Integer id, Post post, ProductDto productDto, Boolean hasPromo, Double discount) {
        super(id, post.getUserId(), post.getDate(), productDto, post.getCategory(), post.getPrice());
        this.hasPromo = hasPromo;
        this.discount = discount;
    }
}
