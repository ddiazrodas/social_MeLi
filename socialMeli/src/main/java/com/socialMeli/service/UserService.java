package com.socialMeli.service;

import com.socialMeli.dto.response.UserVendorDTO;
import com.socialMeli.dto.response.VendorFollowerListDTO;
import com.socialMeli.entity.User;
import com.socialMeli.exception.NotFoundException;
import com.socialMeli.exception.UserIsNotVendorException;
import com.socialMeli.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.socialMeli.entity.UserType.VENDOR;

@Service
@AllArgsConstructor
public class UserService implements IUserService{
    private final IUserRepository userRepository;

    @Override
    public VendorFollowerListDTO getVendorFollowers(Integer userId) {
        Optional<User> userFound = userRepository.findUserByUserId(userId);

        //Excepciones
        if(userFound.isEmpty()) throw new NotFoundException("Vendedor no encontrado");
        if (!VENDOR.equals(userFound.get().getType())) throw new UserIsNotVendorException("El usuario no es un vendedor");

        //User es válido
        List<User> followers= userRepository.getAllFollowers(userFound.get().getFollowersId());
        List<UserVendorDTO> followersListDTO = followers
                .stream()
                .map(user -> new UserVendorDTO(user.getId(),user.getName()))
                .toList();
        return new VendorFollowerListDTO(userId,userFound.get().getName(),followersListDTO);
    }
}
