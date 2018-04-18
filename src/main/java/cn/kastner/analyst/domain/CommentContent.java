package cn.kastner.analyst.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "commentContent")
public class CommentContent {

    @Id
    private String id;

    private String commentId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean isGood;

    public CommentContent () {
        id = UUID.randomUUID().toString();
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getGood() {
        return isGood;
    }

    public void setGood(Boolean good) {
        isGood = good;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}