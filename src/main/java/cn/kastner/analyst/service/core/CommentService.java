package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.core.Comment;

import java.util.List;

public interface CommentService {

    /**
     * create a comment
     *
     * @param comment
     * @return comment inserted
     */
    Comment insertByComment(Comment comment);

    /**
     * retrieve a comment by id
     *
     * @param commentId
     * @return comment retrieved
     */
    Comment findById(Long commentId);

    /**
     * retrieve all categories
     *
     * @return
     */
    List<Comment> findAll();

    /**
     * update a comment by id
     *
     * @param comment
     * @return
     */
    Comment update(Comment comment);

    /**
     * delete a comment by id
     *
     * @param commentId
     * @return comment deleted
     */
    Comment delete(Long commentId);
}
