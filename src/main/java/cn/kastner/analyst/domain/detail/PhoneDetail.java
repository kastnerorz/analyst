package cn.kastner.analyst.domain.detail;

import cn.kastner.analyst.domain.core.Brand;
import cn.kastner.analyst.domain.core.Item;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "phoneDetail")
@Data
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
}
