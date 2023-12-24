package ru.ac.secondhand.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Comment;
import ru.ac.secondhand.exception.CommentNotFoundException;
import ru.ac.secondhand.mapper.CommentMapper;
import ru.ac.secondhand.repository.CommentRepository;
import ru.ac.secondhand.repository.UserRepository;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.utils.TestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;
    @Mock
    AdService adService;
    @Mock
    CommentMapper mapper;
    @Mock
    Authentication authentication;
    @Mock
    SecurityContext securityContext;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    CommentServiceImpl commentService;

    private Integer adId;
    private Integer commentId;
    private Comment deletedComment;
    private CreateOrUpdateComment createOrUpdateComment;
    private Ad ad;


    @BeforeEach
    void setUp() {
        adId = TestUtils.AD_ID;
        commentId = TestUtils.COMMENT_ID;
        deletedComment = TestUtils.getCommentEntity();
        createOrUpdateComment = TestUtils.getCreateOrUpdateComment();
        ad = TestUtils.getAdEntity();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void setUpAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(true);
        UserDetails userDetails = new User(
                "USER",
                "666999666",
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }


    @Test
    void getComments_Success() {
        List<Comment> commentsList = TestUtils.getCommentList();
        Comments expectedComments = new Comments();
        expectedComments.setCount(commentsList.size());
        expectedComments.setResults(TestUtils.getCommentDTOList());

        when(adService.getAdById(adId)).thenReturn(TestUtils.getAdEntity());
        when(commentRepository.findByAdId(adId)).thenReturn(commentsList);
        when(mapper.toComments(commentsList)).thenReturn(expectedComments);

        Comments actualComments = commentService.getComments(adId);

        assertEquals(expectedComments, actualComments);
        verify(adService).getAdById(adId);
        verify(commentRepository).findByAdId(adId);
        verify(mapper).toComments(commentsList);
    }

    @Test
    void createdComment_Success() {
       setUpAuthentication();

        Comment newComment = TestUtils.getCommentEntity();
        CommentDTO expectedCommentDTO = TestUtils.getCommentDTO();

        when(adService.getAdById(adId)).thenReturn(ad);
        when(mapper.toComment(createOrUpdateComment)).thenReturn(newComment);

        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);
        when(mapper.toCommentDTO(any(Comment.class))).thenReturn(expectedCommentDTO);

        CommentDTO actualCommentDTO = commentService.createComment(createOrUpdateComment, adId);

        assertEquals(expectedCommentDTO, actualCommentDTO);
        verify(adService).getAdById(adId);
        verify(mapper).toComment(createOrUpdateComment);
        verify(commentRepository).save(any(Comment.class));
        verify(mapper).toCommentDTO(any(Comment.class));
    }

    @Test
    void delete_CommentFoundAndDeletedSuccessfully() {
        when(adService.getAdById(adId)).thenReturn(TestUtils.getAdEntity());
        when(commentRepository.findByAdIdAndId(adId, commentId)).thenReturn(Optional.of(deletedComment));

        commentService.delete(adId, commentId);

        verify(adService).getAdById(adId);
        verify(commentRepository).findByAdIdAndId(adId, commentId);
        verify(commentRepository).delete(deletedComment);
    }

    @Test
    void delete_CommentNotFound_ThrowsException() {

        when(adService.getAdById(adId)).thenReturn(TestUtils.getAdEntity());
        when(commentRepository.findByAdIdAndId(adId, commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.delete(adId, commentId));
        verify(adService).getAdById(adId);
        verify(commentRepository).findByAdIdAndId(adId, commentId);
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    void updateComment_Success() {
        Comment existingComment = TestUtils.getCommentEntity();
        CommentDTO expectedCommentDTO = TestUtils.getCommentDTO();

        when(adService.getAdById(adId)).thenReturn(TestUtils.getAdEntity());
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(mapper.toCommentDTO(any(Comment.class))).thenReturn(expectedCommentDTO);

        CommentDTO actualCommentDTO = commentService.updateComment(adId, commentId, createOrUpdateComment);

        assertEquals(expectedCommentDTO, actualCommentDTO);
        verify(commentRepository).save(existingComment);
        verify(mapper).toCommentDTO(existingComment);
    }

    @Test
    void updateComment_NotFound_ThrowsException() {
        when(adService.getAdById(adId)).thenReturn(TestUtils.getAdEntity());
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(adId, commentId, createOrUpdateComment));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    public void testIsOwnerSuccess() {
        Comment comment = TestUtils.createStandardComment();
        comment.setUser(TestUtils.createStandardUser());

        when(commentRepository.findById(TestUtils.COMMENT_ID)).thenReturn(Optional.of(comment));
        assertTrue(commentService.isOwner(TestUtils.STANDARD_USERNAME, TestUtils.COMMENT_ID));
    }

    @Test
    public void testIsOwnerCommentNotFound() {
        when(commentRepository.findById(TestUtils.COMMENT_ID)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> {
            commentService.isOwner(TestUtils.STANDARD_USERNAME, TestUtils.COMMENT_ID);
        });
    }

    @Test
    public void testIsOwnerNotOwner() {
        Comment comment = TestUtils.createStandardComment();
        comment.setUser(TestUtils.createStandardUser());

        when(commentRepository.findById(TestUtils.COMMENT_ID)).thenReturn(Optional.of(comment));
        assertFalse(commentService.isOwner(TestUtils.OTHER_USERNAME, TestUtils.COMMENT_ID));
    }
}
