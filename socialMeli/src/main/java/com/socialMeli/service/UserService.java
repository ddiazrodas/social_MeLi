package com.socialMeli.service;

import com.socialMeli.dto.response.FollowedListDto;
import com.socialMeli.dto.response.VendorDto;
import com.socialMeli.dto.response.VendorFollowCountDto;
import com.socialMeli.dto.response.*;
import com.socialMeli.entity.User;
import com.socialMeli.entity.UserType;
import com.socialMeli.exception.NotFoundException;
import com.socialMeli.exception.UserFollowException;
import com.socialMeli.exception.UserIsNotVendorException;
import com.socialMeli.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.List;
import java.util.stream.Collectors;

import static com.socialMeli.entity.UserType.VENDOR;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Override
    public MessageDTO newFollow(Integer userId, Integer userIdToFollow) {

        User user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NotFoundException("No se encontro al usuario"));
        User userFollow = userRepository.findUserByUserId(userIdToFollow).orElseThrow(() -> new NotFoundException("No se encontro el usuario a seguir"));

        if (!VENDOR.equals(userFollow.getType()))
            throw new UserIsNotVendorException("El usuario " + userFollow.getName() + " a seguir no es un vendedor");

        boolean userIsMatch = user.getFollowedId().contains(userIdToFollow);  //stream().noneMatch(id -> id.equals(userIdToFollow));

        if (!userIsMatch) {
            userRepository.followUser(user, userFollow);
            return new MessageDTO("Comenzaste a seguir al usuario " + userFollow.getName());
        }

        throw new UserFollowException("Ya sigues a este usuario");
    }

    @Override
    public VendorFollowerListDTO getVendorFollowers(Integer userId) {
        User userFound = getUserByIdOrThrow(userId, "Vendedor no encontrado");
        if (!VENDOR.equals(userFound.getType())) throw new UserIsNotVendorException("El usuario no es un vendedor");
        List<UserVendorDTO> followersListDTO =
                userRepository.getAllFollowers(userFound.getFollowersId())
                        .stream()
                        .map(UserVendorDTO::new)
                        .toList();
        return new VendorFollowerListDTO(userId, userFound.getName(), followersListDTO);
    }


    public VendorFollowCountDto getFollowerCount(Integer userId) {
        User user = getUserByIdOrThrow(userId, "El usuario no fue encontrado");
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
            userRepository.unfollowUser(user, userToUnfollow);
            return new UserUnfollowedDto(userId, userIdToUnfollow);
        }
        throw new UserFollowException("El usuario no esta en tu lista de followed");
    }

    private User getUserByIdOrThrow(Integer userId, String errorMessage) {
        return userRepository
                .findUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException(errorMessage));
    }

    @Override
    public FollowedListDto getFollowedList(Integer userId) {
        User user = getUserByIdOrThrow(userId, "No se encontró al usuario");



        List<VendorDto> followedVendors = user.getFollowedId().stream()
                .map(id -> userRepository.findUserByUserId(id).get())
                .filter(user1 -> VENDOR.equals(user1.getType()))
                .map(u -> new VendorDto(u.getId(), u.getName()))
                .toList();


        return new FollowedListDto(userId, user.getName(), followedVendors);
    }
}
