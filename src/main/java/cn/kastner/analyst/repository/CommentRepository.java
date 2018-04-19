package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
