package com.socialMeli.service;

import com.socialMeli.dto.response.UserUnfollowedDto;
import com.socialMeli.dto.response.UserVendorDTO;
import com.socialMeli.dto.response.VendorFollowerListDTO;

import com.socialMeli.dto.response.VendorFollowCountDto;
import com.socialMeli.entity.User;
import com.socialMeli.exception.NotFoundException;
import com.socialMeli.exception.UserIsNotVendorException;
import com.socialMeli.exception.UserFollowException;
import com.socialMeli.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

import static com.socialMeli.entity.UserType.VENDOR;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    @Override
    public VendorFollowerListDTO getVendorFollowers(Integer userId) {
        Optional<User> userFound = userRepository.findUserByUserId(userId);

        //Excepciones
        if (userFound.isEmpty()) throw new NotFoundException("Vendedor no encontrado");
        if (!VENDOR.equals(userFound.get().getType()))
            throw new UserIsNotVendorException("El usuario no es un vendedor");

        //User es válido
        List<User> followers = userRepository.getAllFollowers(userFound.get().getFollowersId());
        List<UserVendorDTO> followersListDTO = followers
                .stream()
                .map(user -> new UserVendorDTO(user.getId(), user.getName()))
                .toList();
        return new VendorFollowerListDTO(userId, userFound.get().getName(), followersListDTO);
    }


    public VendorFollowCountDto getFollowerCount(Integer userId) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No se encontro al usuario"));
        if (!VENDOR.equals(user.getType())) {
            throw new UserIsNotVendorException("El usuario no es un vendedor");
        }

        return new VendorFollowCountDto(user);
    }

    @Override
    public UserUnfollowedDto unfollowUser(Integer userId, Integer userIdToUnfollow) {
        User user = getUserByIdOrThrow(userId, "No se encontro al usuario");
        User userToUnfollow = getUserByIdOrThrow(userIdToUnfollow, "No existe el usuario que se quieres dejar de seguir ");
        if (user.getFollowedId().contains(userIdToUnfollow)) {
            userRepository.unfollowUser(user,userToUnfollow );
            return new UserUnfollowedDto(userId,userIdToUnfollow);
        }
        throw new UserFollowException("El usuario no esta en tu lista de followed");
    }

    private User getUserByIdOrThrow(Integer userId, String errorMessage) {
        return userRepository
                .findUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException(errorMessage));
    }

}
