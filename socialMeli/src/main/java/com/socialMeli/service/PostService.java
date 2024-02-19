package com.socialMeli.service;

import com.socialMeli.dto.request.PostDTO;
import com.socialMeli.dto.response.PostDto;
import com.socialMeli.dto.response.PublicationDto;
import com.socialMeli.entity.Post;
import com.socialMeli.exception.InvalidDataException;
import com.socialMeli.exception.NotFoundException;
import com.socialMeli.repository.IPostRepository;
import com.socialMeli.repository.IProductRepository;
import com.socialMeli.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService implements IPostService {
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    @Override
    public PublicationDto obtainLastPublicationsByTheFollowedVendors(Integer userId) {
        List<Integer> followedVendors = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No se encontro al usuario"))
                .getFollowedId();

        List<Post> latestPost = new ArrayList<>();
        for (Integer vendorId : followedVendors) {
            Optional<List<Post>> filteredPostOfOneUser = postRepository.getPostFromTheLastTwoWeeksByUserId(vendorId);
            filteredPostOfOneUser.ifPresent(latestPost::addAll);
        }
        List<PostDto> sortedPost = addRecoverProductsOnPosts(latestPost);
        return new PublicationDto(userId, sortPostsByDate(sortedPost));
    }

    public List<PostDto> addRecoverProductsOnPosts(List<Post> postList) {
        List<PostDto> postDtoList = new ArrayList<>();
        for (Post post : postList) {
            PostDto postDto = convertPostToDto(post);
            postDto.setProduct(productRepository.getProductById(post.getProductId()).orElseThrow(() -> new NotFoundException("No se encontro el producto")));
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

    @Override
    public void addPost(PostDTO postDto) {
        validatePost(postDto);
        //TODO add constructor with object instead of data passing
        Post finalPost = new Post(idCounter.incrementAndGet(), postDto.getDate(), postDto.getProduct().getId(), postDto.getCategory(), postDto.getPrice(), postDto.getUser_id());
        productRepository.add(postDto.getProduct());
        postRepository.add(finalPost);
    }

    private void validatePost(PostDTO post) {
        if (post.getUser_id() <= 0) {
            throw new InvalidDataException("Error al enviar los datos: Usuario no valido");
        }
        if (post.getDate() == null) {//Preguntar
            throw new InvalidDataException("Error al enviar los datos: La fecha no puede ser nula");
        }
        if (post.getProduct() == null
                || post.getProduct().getId() <= 0
                || post.getProduct().getName() == null
                || post.getProduct().getName().isEmpty()) {
            throw new InvalidDataException("Error al enviar los datos: Producto no válido");
        }
        if (post.getCategory() <= 0) {
            throw new InvalidDataException("Error al enviar los datos: Categoría no válida");
        }
        if (post.getPrice() <= 0) {
            throw new InvalidDataException("Error al enviar los datos: Precio no válido");
        }
    }

    public List<PostDto> sortPostsByDate(List<PostDto> posts) {
        return posts.stream()
                .sorted(Comparator.comparing(PostDto::getDate).reversed())
                .toList();
    }

    public PostDto convertPostToDto(Post post) {
        PostDto convertedPost = new PostDto();
        convertedPost.setUserId(post.getUserId());
        convertedPost.setId(post.getId());
        convertedPost.setProduct(productRepository
                .getProductById(post.getProductId())
                .orElseThrow(() -> new NotFoundException("El producto buscado no existe.")));
        convertedPost.setDate(post.getDate());
        convertedPost.setPrice(post.getPrice());
        convertedPost.setCategory(post.getCategory());
        return convertedPost;
    }
}
