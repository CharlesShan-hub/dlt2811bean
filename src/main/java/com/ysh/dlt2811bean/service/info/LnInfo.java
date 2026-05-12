package com.ysh.dlt2811bean.service.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum LnInfo {
    // 61850 7-3 5.3 L
    LPHD("LPHD", "物理装置信息", "用于为物理装置的公用信息建模", LnClass.L),
    LLN0("LLN0", "逻辑节点零", "用于访问逻辑装置的公用信息", LnClass.L),
    
    // 61850 7-3 5.4 P
    PDIF("PDIF", "差动保护", "用于各种电流差动保护", LnClass.P),
    PDIR("PDIR", "方向比较", "跳闸策略基于对故障点周围故障传感器提供的故障方向确认（如方向继电器）", LnClass.P),
    PDIS("PDIS", "距离保护", "距离保护", LnClass.P),
    PDOP("PDOP", "定向过功率", "定向过功率", LnClass.P),
    PDUC("PDUC", "欠功率方向", "欠功率方向", LnClass.P),
    PFRC("PFRC", "频率变化率", "频率变化率", LnClass.P),
    PHAR("PHAR", "谐波制动", "谐波制动", LnClass.P),
    PHIZ("PHIZ", "接地检测", "仅适用于高阻抗绝缘故障", LnClass.P),
    PIOC("PIOC", "瞬时过电流", "瞬时过电流保护", LnClass.P),
    PMRI("PMRI", "禁止电动机重新启动", "禁止电动机重新启动", LnClass.P),
    PMSS("PMSS", "电动机启动时间监视", "电动机启动时间监视", LnClass.P),
    POPF("POPF", "过功率因素", "过功率因素", LnClass.P),
    PPAM("PPAM", "相角测量", "相角测量", LnClass.P),
    PSCH("PSCH", "保护配置", "保护配置", LnClass.P),
    PSDE("PSDE", "灵敏方向接地故障", "灵敏方向接地故障", LnClass.P),
    PIEF("PIEF", "瞬时接地故障", "瞬时接地故障", LnClass.P),
    PTOC("PTOC", "延时过电流", "延时过电流", LnClass.P),
    PTOF("PTOF", "高周（频率）", "频率过速保护", LnClass.P),
    PTOV("PTOV", "过电压", "过压保护", LnClass.P),
    PTRC("PTRC", "保护跳闸条件", "保护跳闸条件", LnClass.P),
    PTTR("PTTR", "过热", "过热保护", LnClass.P),
    PTUC("PTUC", "欠电流", "欠电流保护", LnClass.P),
    PTUV("PTUV", "低电压", "欠压保护", LnClass.P),
    PUPF("PUPF", "过功率因数", "过功率因数", LnClass.P),
    PTUF("PTUF", "低频", "欠频保护", LnClass.P),
    PVOC("PVOC", "电压闭锁延时过电流", "电压闭锁延时过电流", LnClass.P),
    PVPH("PVPH", "电压频率", "电压频率", LnClass.P),
    PZSU("PZSU", "零速或欠速保护", "零速或欠速保护", LnClass.P),

    // 61850 7-3 5.5 R
    RDRE("RDRE", "扰动记录功能", "扰动记录功能", LnClass.R),
    RADR("RADR", "扰动记录模拟通道", "扰动记录模拟通道", LnClass.R),
    RBDR("RBDR", "扰动记录状态量通道", "扰动记录状态量通道", LnClass.R),
    RDRS("RDRS", "扰动记录处理", "扰动记录处理", LnClass.R),
    RBRF("RBRF", "断路器失灵", "断路器失灵处理", LnClass.R),
    RDIR("RDIR", "方向元件", "方向元件", LnClass.R),
    RFLO("RFLO", "故障定位", "故障定位", LnClass.R),
    RPSB("RPSB", "电网振荡检测或闭锁", "电网振荡检测或闭锁", LnClass.R),
    RREC("RREC", "自动重合闸", "重合闸", LnClass.R),
    RSYN("RSYN", "同步检查或同步", "同步检查或同步", LnClass.R),

    // 61850 7-3 5.6 C
    CALH("CALH", "告警处理", "告警处理", LnClass.C),
    CCGR("CCGR", "冷却组控制", "冷却组控制", LnClass.C),
    CILO("CILO", "联锁", "联锁", LnClass.C),
    CPOW("CPOW", "定点分合", "定点分合", LnClass.C),
    CSWI("CSWI", "开关控制器", "开关控制", LnClass.C),

    // 61850 7-3 5.7 G
    GGIO("GGIO", "通用过程输入输出", "通用输入输出", LnClass.G),
    GSAL("GSAL", "通用安全应用", "通用安全应用", LnClass.G),

    // 61850 7-3 5.8 I
    IARC("IARC", "存档", "存档", LnClass.I),
    IHMI("IHMI", "人机接口", "人机接口", LnClass.I),
    ITCI("ITCI", "远方控制接口", "远方控制接口", LnClass.I),
    ITMI("ITMI", "远方监视接口", "远方监视接口", LnClass.I),

    // 61850 7-3 5.9 A
    ANCR("ANCR", "中性点电流调节", "中性点电流调节", LnClass.A),
    ARCO("ARCO", "无功控制", "无功控制", LnClass.A),
    ATCC("ATCC", "自动调分接开关控制", "自动调分接开关控制", LnClass.A),
    AVCO("AVCO", "电压控制", "电压控制", LnClass.A),

    // 61850 7-3 5.10 M
    MDIF("MDIF", "差动测量", "差动测量", LnClass.M),
    MHAI("MHAI", "谐波和间谐波", "谐波和间谐波", LnClass.M),
    MHAN("MHAN", "相别无关谐波和间谐波", "相别无关谐波和间谐波", LnClass.M),
    MMTR("MMTR", "计量", "计量", LnClass.M),
    MMXN("MMXN", "相别无关值测量", "相别无关值测量", LnClass.M),
    MMXU("MMXU", "测量", "测量", LnClass.M),
    MSQI("MSQI", "相序和不平衡", "相序和不平衡", LnClass.M),
    MSTA("MSTA", "计量统计", "计量统计", LnClass.M),

    // 61850 7-3 5.11 S
    SARC("SARC", "电弧监视和诊断", "电弧监视和诊断", LnClass.S),
    SIMG("SIMG", "绝缘介质监视（气体）", "绝缘介质监视（气体）", LnClass.S),
    SIML("SIML", "绝缘介质监视（液体）", "绝缘介质监视（液体）", LnClass.S),
    SPDC("SPDC", "局部放电监视和诊断", "局部放电监视和诊断", LnClass.S),

    // 61850 7-3 5.12 X
    XCBR("XCBR", "断路器", "断路器", LnClass.X),
    XSWI("XSWI", "隔离开关", "隔离开关", LnClass.X),

    // 61850 7-3 5.13 T
    TCTR("TCTR", "电流互感器", "电流互感器", LnClass.T),
    TVTR("TVTR", "电压互感器", "电压互感器", LnClass.T),

    // 61850 7-3 5.14 Y
    YEFN("YEFN", "接地故障中性点补偿（消弧线圈）", "接地故障中性点补偿（消弧线圈）", LnClass.Y),
    YLTC("YLTC", "分接开关", "分接开关", LnClass.Y),
    YPSH("YPSH", "功率分流", "功率分流", LnClass.Y),
    YPTR("YPTR", "电力变压器", "电力变压器", LnClass.Y),

    // 61850 7-3 5.15 Z
    ZAXN("ZAXN", "辅助电源", "辅助电源", LnClass.Z),
    ZBAT("ZBAT", "电池", "电池", LnClass.Z),
    ZBSH("ZBSH", "断路器", "断路器", LnClass.Z),
    ZCAB("ZCAB", "电力电缆", "电力电缆", LnClass.Z),
    ZCAP("ZCAP", "电容器组", "电容器组", LnClass.Z),
    ZCON("ZCON", "转换器", "转换器", LnClass.Z),
    ZGEN("ZGEN", "发电机", "发电机", LnClass.Z),
    ZGIL("ZGIL", "气体绝缘线", "气体绝缘线", LnClass.Z),
    ZLIN("ZLIN", "电力架空线", "电力架空线", LnClass.Z),
    ZMOT("ZMOT", "电动机", "电动机", LnClass.Z),
    ZREA("ZREA", "电抗器", "电抗器", LnClass.Z),
    ZRRC("ZRRC", "旋转无功元件", "旋转无功元件", LnClass.Z),
    ZSAR("ZSAR", "浪涌抑制器", "浪涌抑制器", LnClass.Z),
    ZTCF("ZTCF", "精闸管控制频率转换器", "精闸管控制频率转换器", LnClass.Z),    
    ZTCR("ZTCR", "精闸管控制无功元件", "精闸管控制无功元件", LnClass.Z);

    public enum LnClass {
        L("系统逻辑节点"),
        P("保护功能逻辑节点"),
        R("保护相关功能逻辑节点"),
        C("控制逻辑节点"),
        G("通用引用逻辑节点"),
        I("接口和存档逻辑节点组"),
        A("自动控制逻辑节点组"),
        M("计量和测量逻辑节点"),
        S("传感器监视逻辑节点"),
        X("开关设备相关逻辑节点"),
        T("仪用互感器逻辑节点"),
        Y("电力变压器逻辑节点"),
        Z("其他电力设备逻辑节点"),
        COMMON("公共"),
        SYSTEM("系统"),
        PROTECTION("保护"),
        CONTROL("控制"),
        MEASUREMENT("测量");
        private final String chineseName;
        LnClass(String cn) { this.chineseName = cn; }
        public String getChineseName() { return chineseName; }
    }

    private static final Map<String, LnInfo> BY_NAME = new HashMap<>();
    private static final Map<LnClass, List<LnInfo>> BY_CLASS = new HashMap<>();

    static {
        for (LnInfo ln : values()) {
            BY_NAME.put(ln.name, ln);
            BY_CLASS.computeIfAbsent(ln.lnClass, k -> new ArrayList<>()).add(ln);
        }
    }

    private final String name;
    private final String chineseName;
    private final String description;
    private final LnClass lnClass;

    LnInfo(String name, String chineseName, String description, LnClass lnClass) {
        this.name = name;
        this.chineseName = chineseName;
        this.description = description;
        this.lnClass = lnClass;
    }

    public String getName() { return name; }
    public String getChineseName() { return chineseName; }
    public String getDescription() { return description; }
    public LnClass getLnClass() { return lnClass; }

    public static LnInfo byName(String name) {
        return BY_NAME.get(name);
    }

    public static List<LnInfo> byClass(LnClass lnClass) {
        return BY_CLASS.getOrDefault(lnClass, List.of());
    }
}
