package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.Comment;
import cn.kastner.analyst.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>,
        JpaSpecificationExecutor<Comment> {
    List<Comment> findByItemAndCrawDateAfter(Item item, LocalDate crawDate);
    Comment findByCommentId(Long commentId);
}
