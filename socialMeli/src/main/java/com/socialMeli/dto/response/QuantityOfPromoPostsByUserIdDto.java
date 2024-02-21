package com.socialMeli.dto.response;

import com.socialMeli.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class QuantityOfPromoPostsByUserIdDto {
    Integer userId;
    String userName;
    Integer promoProductsCount;

    public QuantityOfPromoPostsByUserIdDto(Integer userId, User user, Integer posts) {
        this.userId = userId;
        this.userName = user.getName();
        this.promoProductsCount = posts;

    }
}
