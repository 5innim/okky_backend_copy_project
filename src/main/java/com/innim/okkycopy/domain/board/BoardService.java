package com.innim.okkycopy.domain.board;

import com.innim.okkycopy.domain.board.dto.response.topics.TopicsResponse;
import com.innim.okkycopy.domain.board.entity.BoardType;
import com.innim.okkycopy.domain.board.entity.Post;
import com.innim.okkycopy.domain.board.repository.BoardTypeRepository;
import com.innim.okkycopy.domain.board.repository.PostRepository;
import com.innim.okkycopy.domain.member.entity.Member;
import com.innim.okkycopy.global.error.ErrorCode;
import com.innim.okkycopy.global.error.exception.FailInitializationException;
import com.innim.okkycopy.global.error.exception.NoSuchPostException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardTypeRepository boardTypeRepository;
    private final PostRepository postRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TopicsResponse findAllBoardTopics() {
        List<BoardType> boardTypes = boardTypeRepository.findAll();

        if (boardTypes.isEmpty())
            throw new FailInitializationException(ErrorCode._500_FAIL_INITIALIZATION);

        return TopicsResponse.toDto(boardTypes);
    }

    @Transactional
    public void scrapPost(Member member, long postId) {
        Member mergedMember = entityManager.merge(member);

        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NoSuchPostException(ErrorCode._400_NO_SUCH_POST));
        mergedMember.getScrappedPosts().add(post);
    }

    @Transactional
    public void cancelScrap(Member member, long postId) {
        Member mergedMember = entityManager.merge(member);

        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NoSuchPostException(ErrorCode._400_NO_SUCH_POST));
        mergedMember.getScrappedPosts().remove(post);
    }

}
