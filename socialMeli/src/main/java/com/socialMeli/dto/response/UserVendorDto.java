package com.socialMeli.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialMeli.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserVendorDto {
    @NotNull(message = "El id de usuario no puede estar vacio")
    @Positive(message = "El id debe ser un valor positivo")
    @JsonProperty("user_id")
    Integer userId;
    @JsonProperty("user_name")
    String userName;
    public UserVendorDto(User user){
        this.userId = user.getId();
        this.userName = user.getName();
    }
}
