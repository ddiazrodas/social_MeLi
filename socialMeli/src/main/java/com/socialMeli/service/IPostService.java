package com.socialMeli.service;

import com.socialMeli.dto.response.PublicationDto;

import com.socialMeli.dto.request.PostDTO;
import com.socialMeli.dto.response.QuantityOfPromoPostsByUserIdDto;
import com.socialMeli.dto.response.UserVendorDescPostsDto;


public interface IPostService {

    PublicationDto obtainLastPublicationsByTheFollowedVendors(Integer userId, String order);
    void addPost(PostDTO post);
    QuantityOfPromoPostsByUserIdDto getPromotionPostsById(Integer userId);

    UserVendorDescPostsDto getAllPromotionPostsByVender(Integer userId);
}
