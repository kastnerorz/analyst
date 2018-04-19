package cn.kastner.analyst.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "commentContent")
public class CommentContent {

    @Id
    private String contentId;

    private String commentId;

    private String itemId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean isGood;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public CommentContent () {
        contentId = UUID.randomUUID().toString();
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

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
