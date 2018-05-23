package cn.kastner.analyst.domain.detail;

import cn.kastner.analyst.domain.core.Brand;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class PhoneDetail {

    /**
     * 手机详情 id
     */
    private Long phoneDetailId;

    /**
     * 品牌
     */
    private Brand brand;

    /**
     * 型号
     */
    private String model;

    /**
     * 发布日期
     */
    private LocalDate released;

    /**
     * 附加功能（additional feature）
     */
    private String addlFeatures;

    /**
     * 运营商
     */
    private String operator;

    /**
     * 宽度
     */
    private Long width;

    /**
     * 高度
     */
    private Long height;

    /**
     * 质量
     */
    private Long mass;

    /**
     * 平台
     */
    private String platform;

    /**
     * 其他软件（Software Extra）
     */
    private String swExtras;

    /**
     * cpu 型号
     */
    private String cpu;

    /**
     * ram 类型
     */
    private String ram;

    /**
     * ram 时钟频率
     */
    private Long ramClock;

    /**
     * ram 容量
     */
    private Long ramCapacity;

    /**
     * rom 类型
     */
    private String romType;

    /**
     * rom 容量
     */
    private Long romCapacity;

    /**
     * 可用 rom 容量
     */
    private Long freeRomCapacity;

    /**
     * 宽分辨率（Wide Resolution）
     */
    private int wideRez;




    public Long getPhoneDetailId() {
        return phoneDetailId;
    }

    public void setPhoneDetailId(Long phoneDetailId) {
        this.phoneDetailId = phoneDetailId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    public String getAddlFeatures() {
        return addlFeatures;
    }

    public void setAddlFeatures(String addlFeatures) {
        this.addlFeatures = addlFeatures;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSwExtras() {
        return swExtras;
    }

    public void setSwExtras(String swExtras) {
        this.swExtras = swExtras;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public Long getRamClock() {
        return ramClock;
    }

    public void setRamClock(Long ramClock) {
        this.ramClock = ramClock;
    }

    public Long getRamCapacity() {
        return ramCapacity;
    }

    public void setRamCapacity(Long ramCapacity) {
        this.ramCapacity = ramCapacity;
    }

    public String getRomType() {
        return romType;
    }

    public void setRomType(String romType) {
        this.romType = romType;
    }

    public Long getRomCapacity() {
        return romCapacity;
    }

    public void setRomCapacity(Long romCapacity) {
        this.romCapacity = romCapacity;
    }

    public Long getFreeRomCapacity() {
        return freeRomCapacity;
    }

    public void setFreeRomCapacity(Long freeRomCapacity) {
        this.freeRomCapacity = freeRomCapacity;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getMass() {
        return mass;
    }

    public void setMass(Long mass) {
        this.mass = mass;
    }

    public int getWideRez() {
        return wideRez;
    }

    public void setWideRez(int wideRez) {
        this.wideRez = wideRez;
    }
}
