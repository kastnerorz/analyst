package cn.kastner.analyst.domain.detail;

import cn.kastner.analyst.domain.core.Brand;
import cn.kastner.analyst.domain.core.Item;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "phoneDetail")
public class PhoneDetail {

    /**
     * 手机详情 id
     */
    @Id
    private Long phoneDetailId;

    /**
     * 所属商品
     */
    @OneToOne
    private Item item;

    /**
     * 获取时间
     */
    private LocalDate crawDate;

    /**
     * 品牌
     */
    @ManyToOne
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
     * 单位 mm
     */
    private Long width;

    /**
     * 高度
     * 单位 mm
     */
    private Long height;

    /**
     * 质量
     * 单位 g
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
     * cpu 时钟频率
     * 单位 MHZ
     */
    private Long cpuClock;

    /**
     * ram 类型
     */
    private String ram;

    /**
     * ram 时钟频率
     * 单位 MHZ
     */
    private Long ramClock;

    /**
     * ram 容量
     * 单位 MB
     */
    private Long ramCapacity;

    /**
     * rom 类型
     */
    private String romType;

    /**
     * rom 容量
     * 单位 MB
     */
    private Long romCapacity;

    /**
     * 可用 rom 容量
     * 单位 MB
     */
    private Long freeRomCapacity;

    /**
     * 宽分辨率（Wide Resolution）
     */
    private int wideRez;

    /**
     * 长分辨率（Long Resolution）
     */
    private int longRez;

    /**
     * 屏幕尺寸（Display Diagonal）
     * 单位英寸（inch）
     */
    private int dispDiagonal;

    /**
     * 屏占比（Display Area Utilization）
     * 单位 %
     */
    private int dispAreaUtilization;

    /**
     * 像素密度（Pixel Density）
     * 单位 ppi
     */
    private int pxDensity;

    /**
     * 屏幕类型（Display Type）
     */
    private String dispType;

    /**
     * 图形控制器（Graphical Controller）
     */
    private String gController;

    /**
     * USB 版本
     */
    private String usb;

    /**
     * USB 接口类型
     */
    private String usbConnector;

    /**
     * 蓝牙版本
     */
    private String bluetooth;

    /**
     * NFC
     * 注意：分 NFC (A) 和 NFC (B)
     */
    private String nfc;

    /**
     * 有效像素数（Number of Effective Pixels）
     */
    private String numOfEffPixels;

    /**
     * 光圈
     * 单位 W
     */
    private String aperture;

    /**
     * 对焦方式
     * 类型 数组
     */
    private String focus;

    /**
     * 闪光灯
     */
    private String flash;

    /**
     * 相机其他功能（Camera Extra Function）
     * 数组
     */
    private String camExFunctions;

    /**
     * 第二相机有效像素数
     */
    private String numOfPixels2;

    /**
     * 第二相机光圈
     */
    private String aperture2;

    /**
     * 第二相机其他功能
     * 数组
     */
    private String camExFunctions2;

    /**
     * 电池容量
     * 单位 mAh
     */
    private int batteryCap;

    /**
     * 通话时间
     * 单位 hours
     */
    private int talkTime;

    /**
     * 无线充电支持
     * 数组
     */
    private String wrlssCharging;

    /**
     * 防护
     * 防尘，防水
     */
    private String protection;

    /**
     * 附加传感器
     * 数组
     */
    private String addSensors;
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

    public int getLongRez() {
        return longRez;
    }

    public void setLongRez(int longRez) {
        this.longRez = longRez;
    }

    public int getDispDiagonal() {
        return dispDiagonal;
    }

    public void setDispDiagonal(int dispDiagonal) {
        this.dispDiagonal = dispDiagonal;
    }

    public Long getCpuClock() {
        return cpuClock;
    }

    public void setCpuClock(Long cpuClock) {
        this.cpuClock = cpuClock;
    }

    public int getDispAreaUtilization() {
        return dispAreaUtilization;
    }

    public void setDispAreaUtilization(int dispAreaUtilization) {
        this.dispAreaUtilization = dispAreaUtilization;
    }

    public int getPxDensity() {
        return pxDensity;
    }

    public void setPxDensity(int pxDensity) {
        this.pxDensity = pxDensity;
    }

    public String getDispType() {
        return dispType;
    }

    public void setDispType(String dispType) {
        this.dispType = dispType;
    }

    public String getgController() {
        return gController;
    }

    public void setgController(String gController) {
        this.gController = gController;
    }

    public String getUsb() {
        return usb;
    }

    public void setUsb(String usb) {
        this.usb = usb;
    }

    public String getUsbConnector() {
        return usbConnector;
    }

    public void setUsbConnector(String usbConnector) {
        this.usbConnector = usbConnector;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getNfc() {
        return nfc;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }

    public String getNumOfEffPixels() {
        return numOfEffPixels;
    }

    public void setNumOfEffPixels(String numOfEffPixels) {
        this.numOfEffPixels = numOfEffPixels;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getCamExFunctions() {
        return camExFunctions;
    }

    public void setCamExFunctions(String camExFunctions) {
        this.camExFunctions = camExFunctions;
    }

    public String getNumOfPixels2() {
        return numOfPixels2;
    }

    public void setNumOfPixels2(String numOfPixels2) {
        this.numOfPixels2 = numOfPixels2;
    }

    public String getAperture2() {
        return aperture2;
    }

    public void setAperture2(String aperture2) {
        this.aperture2 = aperture2;
    }

    public String getCamExFunctions2() {
        return camExFunctions2;
    }

    public void setCamExFunctions2(String camExFunctions2) {
        this.camExFunctions2 = camExFunctions2;
    }

    public int getBatteryCap() {
        return batteryCap;
    }

    public void setBatteryCap(int batteryCap) {
        this.batteryCap = batteryCap;
    }

    public int getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(int talkTime) {
        this.talkTime = talkTime;
    }

    public String getWrlssCharging() {
        return wrlssCharging;
    }

    public void setWrlssCharging(String wrlssCharging) {
        this.wrlssCharging = wrlssCharging;
    }

    public String getProtection() {
        return protection;
    }

    public void setProtection(String protection) {
        this.protection = protection;
    }

    public String getAddSensors() {
        return addSensors;
    }

    public void setAddSensors(String addSensors) {
        this.addSensors = addSensors;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDate getCrawDate() {
        return crawDate;
    }

    public void setCrawDate(LocalDate crawDate) {
        this.crawDate = crawDate;
    }
}