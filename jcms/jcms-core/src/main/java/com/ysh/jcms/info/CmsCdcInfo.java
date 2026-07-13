package com.ysh.jcms.info;

import java.util.HashMap;
import java.util.Map;

/**
 * CDC (Common Data Class) information — IEC 61850-7-3 data types.
 */
public enum CmsCdcInfo {

    // ==================== Status (ST) ====================
    SPS("SPS", "Single Point Status", "单点状态", "代表一个布尔状态，如开关位置"),
    DPS("DPS", "Double Point Status", "双点状态", "代表双点状态（0=中间/1=分/2=合/3=无效）"),
    INS("INS", "Integer Status", "整数状态", "代表整数状态值，如 Health(1=ok/2=warning/3=alarm)"),
    ACT("ACT", "Protection Activation", "保护动作", "代表保护启动/动作信号（通用分类）"),
    ACD("ACD", "Directional Protection", "保护方向动作", "带方向信息的保护动作"),
    SEC("SEC", "Security Violation", "安全警告", "安全违规告警"),
    BCR("BCR", "Binary Counter Reading", "计数值", "二进制计数器读数"),

    // ==================== Control (CO) ====================
    SPC("SPC", "Single Point Control", "单点控制", "可控的布尔值，如遥控分合"),
    DPC("DPC", "Double Point Control", "双点控制", "可控的双点值"),
    INC("INC", "Integer Control", "整数控制", "可控的整数状态值，如 Mod(1=on/2=blocked/3=test)"),
    BAC("BAC", "Binary Analog Control", "二进制模拟控制", "二进制模拟输出控制"),
    ISC("ISC", "Integer Step Control", "整数步进控制", "整数步进位置控制"),

    // ==================== Measurands (MX) ====================
    MV("MV", "Measured Value", "测量值", "遥测量，含品质和时间戳"),
    CMV("CMV", "Complex Measured Value", "复数测量值", "复数测量值（幅值+角度）"),
    WYE("WYE", "3-Phase Measured Value", "三相测量值", "三相测量值（A/B/C相）"),
    DEL("DEL", "Delta Measured Value", "线电压测量值", "线电压/线电流测量值"),
    SEQ("SEQ", "Sequence Measured Value", "序分量测量值", "正序/负序/零序测量值"),
    HMV("HMV", "Harmonic Measured Value", "谐波测量值", "谐波测量值"),
    HWYE("HWYE", "3-Phase Harmonic Measured", "三相谐波测量值", "三相谐波测量值"),
    HDEL("HDEL", "Harmonic Delta Measured", "线电压谐波测量值", "线电压谐波测量值"),

    // ==================== Sampled Value (SV) ====================
    SAV("SAV", "Sampled Value", "采样值", "采样值数据"),
    ISAV("ISAV", "Integer Sampled Value", "整数采样值", "整数采样值"),

    // ==================== Setting (SP) ====================
    SPG("SPG", "Single Point Setting", "单点定值", "布尔型定值"),
    ING("ING", "Integer Setting", "整数定值", "整数型定值"),
    ASG("ASG", "Analog Setting", "模拟定值", "浮点型定值"),
    CURVE("CURVE", "Curve Setting", "曲线定值", "曲线特性定值"),
    VSS("VSS", "Visible String Setting", "可见字符串定值", "字符串型定值"),
    LPL("LPL", "LN Name Plate", "逻辑节点铭牌", "LN铭牌（含厂商/版本/描述等）"),
    CSG("CSG", "Setting Group", "定值组", "定值组定义"),

    // ==================== Control blocks ====================
    BRCB("BRCB", "Buffered Report CB", "缓存报告控制块", "Buffered Report Control Block"),
    URCB("URCB", "Unbuffered Report CB", "非缓存报告控制块", "Unbuffered Report Control Block"),
    LCB("LCB", "Log Control Block", "日志控制块", "Log Control Block"),
    SGCB("SGCB", "Setting Group CB", "定值组控制块", "Setting Group Control Block"),
    LOCAL("LOCAL", "Local Operation", "本地操作员", "本地/远程控制状态"),
    MSVCB("MSVCB", "Multicast SV CB", "多播采样值控制块", "Multicast Sampled Value Control Block"),
    USVCB("USVCB", "Unicast SV CB", "单播采样值控制块", "Unicast Sampled Value Control Block"),
    GOOSE("GOOSE", "GOOSE Control Block", "GOOSE控制块",
            "Generic Object Oriented Substation Event Control Block"),
    GSSE("GSSE", "GSSE Control Block", "GSSE控制块",
            "Generic Substation State Event Control Block"),

    // ==================== Other ====================
    LLN0("LLN0", "Logical Node Zero", "逻辑节点零", "LN管理信息"),
    LOG("LOG", "Log", "日志", "日志记录");

    private static final Map<String, CmsCdcInfo> BY_NAME = new HashMap<>();

    static {
        for (CmsCdcInfo cdc : values()) {
            BY_NAME.put(cdc.name, cdc);
        }
    }

    private final String name;
    private final String enName;
    private final String cnName;
    private final String description;

    CmsCdcInfo(String name, String enName, String cnName, String description) {
        this.name = name;
        this.enName = enName;
        this.cnName = cnName;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public String getEnName() {
        return enName;
    }
    public String getCnName() {
        return cnName;
    }
    public String getDescription() {
        return description;
    }

    public static CmsCdcInfo byName(String name) {
        return BY_NAME.get(name);
    }
}
