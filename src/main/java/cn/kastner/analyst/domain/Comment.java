package cn.kastner.analyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnore
    private Feature feature;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    @JsonIgnore
    private Item item;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition="BOOLEAN DEFAULT true")
    private Boolean isGood;

    private LocalDate crawDate;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    public LocalDate getCrawDate() {
        return crawDate;
    }

    public void setCrawDate(LocalDate crawDate) {
        this.crawDate = crawDate;
    }
}
