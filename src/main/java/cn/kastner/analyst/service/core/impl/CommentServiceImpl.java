package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Comment;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.core.CommentRepository;
import cn.kastner.analyst.service.core.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment insertByComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findByCommentId(commentId);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findByItemAndCrawDateAfter(Item item, LocalDate crawDate) {
        return commentRepository.findByItemAndCrawDateAfter(item, crawDate);
    }

    @Override
    public Comment update(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment delete(Long commentId) {
        Comment comment = commentRepository.findByCommentId(commentId);
        commentRepository.delete(comment);
        return comment;
    }
}
