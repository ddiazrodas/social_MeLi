package com.socialMeli.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserVendorDescPostsDto extends UserVendorDto {
    List<PostDescDto> descPosts;

    public UserVendorDescPostsDto(Integer id, String name, List<PostDescDto> posts) {
        super(id, name);
        this.descPosts = posts;
    }
}
