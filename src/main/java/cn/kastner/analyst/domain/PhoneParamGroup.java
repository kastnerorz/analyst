package cn.kastner.analyst.domain;

import javax.persistence.*;

@Entity
public class PhoneParamGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Demand demand;

    private String os;

    private String cpu;

    private Double cpuClock;

    private String ramType;

    private Double ramClock;

    private Double ramCapacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public Double getCpuClock() {
        return cpuClock;
    }

    public void setCpuClock(Double cpuClock) {
        this.cpuClock = cpuClock;
    }

    public String getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        this.ramType = ramType;
    }

    public Double getRamClock() {
        return ramClock;
    }

    public void setRamClock(Double ramClock) {
        this.ramClock = ramClock;
    }

    public Double getRamCapacity() {
        return ramCapacity;
    }

    public void setRamCapacity(Double ramCapacity) {
        this.ramCapacity = ramCapacity;
    }

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }
}
