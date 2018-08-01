package cn.kastner.analyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "demand")
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 所属品类
     */
    @ManyToOne
    @JsonIgnore
    private Category category;

    /**
     * 需求内容
     */
    private String content;

    /**
     * 参数列表
     */
    @JsonIgnore
    @OneToMany(mappedBy = "demand")
    private List<PhoneParamGroup> phoneParamGroups;

    /**
     * 计数器
     */
    private Long flag;

    public Demand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<PhoneParamGroup> getPhoneParamGroups() {
        return phoneParamGroups;
    }

    public void setPhoneParamGroups(List<PhoneParamGroup> phoneParamGroups) {
        this.phoneParamGroups = phoneParamGroups;
    }
}
