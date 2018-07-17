package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Comment;
import cn.kastner.analyst.domain.Item;

import java.time.LocalDate;
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

    List<Comment> findByItemAndCrawDateAfter(Item item, LocalDate crawDate);

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
