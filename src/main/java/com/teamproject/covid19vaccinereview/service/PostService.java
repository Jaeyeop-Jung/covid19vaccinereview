package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.IncorrcetBaordException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.PostContentBlankException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.PostTitleBlankException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.UserNotFoundException;
import com.teamproject.covid19vaccinereview.domain.Board;
import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.ImageDto;
import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import com.teamproject.covid19vaccinereview.repository.BoardRepository;
import com.teamproject.covid19vaccinereview.repository.PostImageRepository;
import com.teamproject.covid19vaccinereview.repository.PostRepository;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public long write(String accessToken, PostWriteRequest postWriteRequest) {

        if(!jwtTokenProvider.validateToken(accessToken)){
            throw new MalformedJwtException("");
        }

        Optional<User> findUserOptional = userRepository.findById(jwtTokenProvider.findUserIdByJwt(accessToken));
        if(findUserOptional.isEmpty()){
            throw new UserNotFoundException("");
        }

        List<Board> findBoard = boardRepository.findByVaccineTypeAndOrdinalNumber(postWriteRequest.getVaccineType(), postWriteRequest.getOrdinalNumber());
        if(findBoard.isEmpty()){
            throw new IncorrcetBaordException("");
        }

        if(postWriteRequest.getTitle().isBlank()){
            throw new PostTitleBlankException("");
        } else if(postWriteRequest.getContent().isBlank()){
            throw new PostContentBlankException("");
        }

        Post post = Post.of(
                findUserOptional.get(),
                findBoard.get(0),
                postWriteRequest.getTitle(),
                postWriteRequest.getContent()
        );
        postRepository.save(post);

        for (ImageDto imageDto : postWriteRequest.getAttachedImage()) {
            postImageRepository.save(
                    PostImage.of(
                            post,
                            imageDto.getFileName(),
                            imageDto.getFileSize(),
                            imageDto.getFileExtension()
                    )
            );
        }

        return post.getId();
    }
}
