package cn.kastner.analyst.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long contentId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "feature_id")
    private Feature feature;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition="BOOLEAN DEFAULT true")
    private Boolean isGood;

    @Temporal(TemporalType.DATE)
    private LocalDate crawDate;



    public Comment() {
        isGood = true;
    }


    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
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
