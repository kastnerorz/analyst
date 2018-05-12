package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Comment;
import cn.kastner.analyst.repository.CommentRepository;
import cn.kastner.analyst.service.core.CategoryService;
import cn.kastner.analyst.service.core.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    final private CommentRepository commentRepository;

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
