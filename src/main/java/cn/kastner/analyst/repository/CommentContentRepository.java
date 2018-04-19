package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.CommentContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentContentRepository extends JpaRepository<CommentContent, String> {
}
