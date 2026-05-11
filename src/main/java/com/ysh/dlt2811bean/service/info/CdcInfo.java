package com.ysh.dlt2811bean.service.info;

import java.util.HashMap;
import java.util.Map;

public enum CdcInfo {

    // ==================== 状态量 (§6.1) ====================
    SPS    ("SPS",    "单点状态",        "Single Point Status — 代表一个布尔状态，如开关位置"),
    DPS    ("DPS",    "双点状态",        "Double Point Status — 代表双点状态（0=中间/1=分/2=合/3=无效）"),
    INS    ("INS",    "整数状态",        "Integer Status — 代表整数状态值，如 Health(1=ok/2=warning/3=alarm)"),
    ACT    ("ACT",    "保护动作",        "Protection Activation — 代表保护启动/动作信号（通用分类）"),
    ACD    ("ACD",    "保护方向动作",     "Directional Protection Activation — 带方向信息的保护动作"),
    SEC    ("SEC",    "安全警告",        "Security Violation — 安全违规告警"),
    BCR    ("BCR",    "计数值",          "Binary Counter Reading — 二进制计数器读数"),

    // ==================== 可控状态量 (§6.2) ====================
    SPC    ("SPC",    "单点控制",        "Single Point Control — 可控的布尔值，如遥控分合"),
    DPC    ("DPC",    "双点控制",        "Double Point Control — 可控的双点值"),
    INC    ("INC",    "整数控制",        "Integer Status Control — 可控的整数状态值，如 Mod(1=on/2=blocked/3=test)"),
    BAC    ("BAC",    "二进制模拟控制",   "Binary Analog Control — 二进制模拟输出控制"),
    ISC    ("ISC",    "整数步进控制",     "Integer Step Control — 整数步进位置控制"),

    // ==================== 测量量 (§6.3) ====================
    MV     ("MV",     "测量值",          "Measured Value — 遥测量，含品质和时间戳"),
    CMV    ("CMV",    "复数测量值",       "Complex Measured Value — 复数测量值（幅值+角度）"),
    WYE    ("WYE",    "三相测量值",       "Wye Connected Measured Value — 三相测量值（A/B/C相）"),
    DEL    ("DEL",    "线电压测量值",     "Delta Connected Measured Value — 线电压/线电流测量值"),
    SEQ    ("SEQ",    "序分量测量值",     "Sequence Measured Value — 正序/负序/零序测量值"),
    HMV    ("HMV",    "谐波测量值",       "Harmonic Measured Value — 谐波测量值"),
    HWYE   ("HWYE",   "三相谐波测量值",   "Harmonic Wye Measured Value — 三相谐波测量值"),
    HDEL   ("HDEL",   "线电压谐波测量值", "Harmonic Delta Measured Value — 线电压谐波测量值"),

    // ==================== 采样值 (§6.4) ====================
    SAV    ("SAV",    "采样值",          "Sampled Value — 采样值数据"),
    ISAV   ("ISAV",   "整数采样值",       "Integer Sampled Value — 整数采样值"),

    // ==================== 定值 (§6.5) ====================
    SPG    ("SPG",    "单点定值",        "Single Point Setting — 布尔型定值"),
    ING    ("ING",    "整数定值",        "Integer Status Setting — 整数型定值"),
    ASG    ("ASG",    "模拟定值",        "Analog Setting — 浮点型定值"),
    CURVE  ("CURVE",  "曲线定值",        "Curve Setting — 曲线特性定值"),
    VSS    ("VSS",    "可见字符串定值",   "Visible String Setting — 字符串型定值"),
    LPL    ("LPL",    "逻辑节点铭牌",     "Logical Node Name Plate — LN铭牌（含厂商/版本/描述等）"),
    CSG    ("CSG",    "定值组",          "Setting Group — 定值组定义"),
    SG     ("SG",     "定值组数据",       "Setting Group Data — 定值组数据"),

    // ==================== 控制块 (§6.6) ====================
    BRCB   ("BRCB",   "缓存报告控制块",   "Buffered Report Control Block"),
    URCB   ("URCB",   "非缓存报告控制块",  "Unbuffered Report Control Block"),
    LCB    ("LCB",    "日志控制块",       "Log Control Block"),
    SGCB   ("SGCB",   "定值组控制块",     "Setting Group Control Block"),
    LOCAL  ("LOCAL",  "本地操作员",       "Local Operation — 本地/远程控制状态"),
    MSVCB  ("MSVCB",  "多播采样值控制块",  "Multicast Sampled Value Control Block"),
    USVCB  ("USVCB",  "单播采样值控制块",  "Unicast Sampled Value Control Block"),
    GOOSE  ("GOOSE",  "GOOSE控制块",     "Generic Object Oriented Substation Event Control Block"),
    GSSE   ("GSSE",   "GSSE控制块",      "Generic Substation State Event Control Block"),
    LLN0   ("LLN0",   "逻辑节点零",       "Logical Node Zero — LN管理信息"),

    // ==================== 日志 (§6.7) ====================
    LOG    ("LOG",    "日志",            "Log — 日志记录");

    private static final Map<String, CdcInfo> BY_NAME = new HashMap<>();

    static {
        for (CdcInfo cdc : values()) {
            BY_NAME.put(cdc.name, cdc);
        }
    }

    private final String name;
    private final String chineseName;
    private final String description;

    CdcInfo(String name, String chineseName, String description) {
        this.name = name;
        this.chineseName = chineseName;
        this.description = description;
    }

    public String getName() { return name; }
    public String getChineseName() { return chineseName; }
    public String getDescription() { return description; }

    public static CdcInfo byName(String name) {
        return BY_NAME.get(name);
    }
}
