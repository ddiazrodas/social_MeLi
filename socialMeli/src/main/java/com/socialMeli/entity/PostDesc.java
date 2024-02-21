package com.socialMeli.entity;

import com.socialMeli.dto.request.PostDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PostDesc extends Post{
    Boolean hasPromo;
    Double discount;

    public PostDesc(Integer id, PostDTO postDTO) {
        super(id ,postDTO.getDate(), postDTO.getProduct().getId(), postDTO.getCategory(), postDTO.getPrice(),postDTO.getUserId());
        this.hasPromo = postDTO.getHasPromo();
        this.discount = postDTO.getDiscount();
    }
}
