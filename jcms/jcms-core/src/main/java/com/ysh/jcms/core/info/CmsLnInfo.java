package com.ysh.jcms.core.info;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Logical Node (LN) type information — IEC 61850-7-4 logical nodes.
 */
@Getter
@Accessors(fluent = true)
public enum CmsLnInfo {

    // ==================== L — System ====================
    LPHD("LPHD", "Physical Device Information", "物理装置信息", LnClass.L),
    LLN0("LLN0", "Logical Node Zero", "逻辑节点零", LnClass.L),

    // ==================== P — Protection ====================
    PDIF("PDIF", "Differential Protection", "差动保护", LnClass.P),
    PDIR("PDIR", "Direction Comparison", "方向比较", LnClass.P),
    PDIS("PDIS", "Distance Protection", "距离保护", LnClass.P),
    PDOP("PDOP", "Directional Over Power", "定向过功率", LnClass.P),
    PDUC("PDUC", "Under Power Direction", "欠功率方向", LnClass.P),
    PFRC("PFRC", "Frequency Rate of Change", "频率变化率", LnClass.P),
    PHAR("PHAR", "Harmonic Braking", "谐波制动", LnClass.P),
    PHIZ("PHIZ", "Ground Detection", "接地检测", LnClass.P),
    PIOC("PIOC", "Instantaneous Over Current", "瞬时过电流", LnClass.P),
    PMRI("PMRI", "Motor Restart Inhibit", "禁止电动机重新启动", LnClass.P),
    PMSS("PMSS", "Motor Start Time Monitor", "电动机启动时间监视", LnClass.P),
    POPF("POPF", "Over Power Factor", "过功率因素", LnClass.P),
    PPAM("PPAM", "Phase Angle Measuring", "相角测量", LnClass.P),
    PSCH("PSCH", "Protection Configuration", "保护配置", LnClass.P),
    PSDE("PSDE", "Sensitive Directional Earth Fault", "灵敏方向接地故障", LnClass.P),
    PIEF("PIEF", "Instantaneous Earth Fault", "瞬时接地故障", LnClass.P),
    PTOC("PTOC", "Time Over Current", "延时过电流", LnClass.P),
    PTOF("PTOF", "Over Frequency", "高周（频率）", LnClass.P),
    PTOV("PTOV", "Over Voltage", "过电压", LnClass.P),
    PTRC("PTRC", "Protection Trip Conditioning", "保护跳闸条件", LnClass.P),
    PTTR("PTTR", "Thermal Overload", "过热", LnClass.P),
    PTUC("PTUC", "Under Current", "欠电流", LnClass.P),
    PTUV("PTUV", "Under Voltage", "低电压", LnClass.P),
    PUPF("PUPF", "Under Power Factor", "过功率因数", LnClass.P),
    PTUF("PTUF", "Under Frequency", "低频", LnClass.P),
    PVOC("PVOC", "Voltage Controlled Time Over Current", "电压闭锁延时过电流", LnClass.P),
    PVPH("PVPH", "Voltage and Frequency", "电压频率", LnClass.P),
    PZSU("PZSU", "Zero Speed / Under Speed", "零速或欠速保护", LnClass.P),

// ==================== R — Protection Related ====================
    RDRE("RDRE", "Disturbance Recording", "扰动记录功能", LnClass.R),
    RADR("RADR", "Disturbance Record Analog", "扰动记录模拟通道", LnClass.R),
    RBDR("RBDR", "Disturbance Record Binary", "扰动记录状态量通道", LnClass.R),
    RDRS("RDRS", "Disturbance Record Handling", "扰动记录处理", LnClass.R),
    RBRF("RBRF", "Breaker Failure", "断路器失灵", LnClass.R),
    RDIR("RDIR", "Directional Element", "方向元件", LnClass.R),
    RFLO("RFLO", "Fault Locator", "故障定位", LnClass.R),
    RPSB("RPSB", "Power Swing Detection", "电网振荡检测或闭锁", LnClass.R),
    RREC("RREC", "Auto Reclose", "自动重合闸", LnClass.R),
    RSYN("RSYN", "Synchro Check", "同步检查或同步", LnClass.R),

    // ==================== C — Control ====================
    CALH("CALH", "Alarm Handling", "告警处理", LnClass.C),
    CCGR("CCGR", "Cooling Group Control", "冷却组控制", LnClass.C),
    CILO("CILO", "Interlocking", "联锁", LnClass.C),
    CPOW("CPOW", "Point Control", "定点分合", LnClass.C),
    CSWI("CSWI", "Switch Controller", "开关控制器", LnClass.C),

    // ==================== G — General Reference ====================
    GGIO("GGIO", "Generic I/O", "通用过程输入输出", LnClass.G),
    GSAL("GSAL", "Generic Security Application", "通用安全应用", LnClass.G),

    // ==================== I — Interface & Archive ====================
    IARC("IARC", "Archiving", "存档", LnClass.I),
    IHMI("IHMI", "HMI", "人机接口", LnClass.I),
    ITCI("ITCI", "Remote Control Interface", "远方控制接口", LnClass.I),
    ITMI("ITMI", "Remote Monitor Interface", "远方监视接口", LnClass.I),

    // ==================== A — Auto Control ====================
    ANCR("ANCR", "Neutral Current Regulator", "中性点电流调节", LnClass.A),
    ARCO("ARCO", "Reactive Power Control", "无功控制", LnClass.A),
    ATCC("ATCC", "Auto Tap Changer Control", "自动调分接开关控制", LnClass.A),
    AVCO("AVCO", "Voltage Control", "电压控制", LnClass.A),

    // ==================== M — Metering & Measurement ====================
    MDIF("MDIF", "Differential Measurements", "差动测量", LnClass.M),
    MHAI("MHAI", "Harmonics and Interharmonics", "谐波和间谐波", LnClass.M),
    MHAN("MHAN", "Non-Phase Harmonics", "相别无关谐波和间谐波", LnClass.M),
    MMTR("MMTR", "Metering", "计量", LnClass.M),
    MMXN("MMXN", "Non-Phase Measurement", "相别无关值测量", LnClass.M),
    MMXU("MMXU", "Measurement", "测量", LnClass.M),
    MSQI("MSQI", "Sequence Imbalance", "相序和不平衡", LnClass.M),
    MSTA("MSTA", "Metering Statistics", "计量统计", LnClass.M),

    // ==================== S — Sensor Monitoring ====================
    SARC("SARC", "Arc Monitoring", "电弧监视和诊断", LnClass.S),
    SIMG("SIMG", "Insulation Gas Monitoring", "绝缘介质监视（气体）", LnClass.S),
    SIML("SIML", "Insulation Liquid Monitoring", "绝缘介质监视（液体）", LnClass.S),
    SPDC("SPDC", "Partial Discharge Monitoring", "局部放电监视和诊断", LnClass.S),

    // ==================== X — Switchgear ====================
    XCBR("XCBR", "Circuit Breaker", "断路器", LnClass.X),
    XSWI("XSWI", "Disconnector", "隔离开关", LnClass.X),

    // ==================== T — Instrument Transformer ====================
    TCTR("TCTR", "Current Transformer", "电流互感器", LnClass.T),
    TVTR("TVTR", "Voltage Transformer", "电压互感器", LnClass.T),

    // ==================== Y — Power Transformer ====================
    YEFN("YEFN", "Earth Fault Neutraliser", "接地故障中性点补偿（消弧线圈）", LnClass.Y),
    YLTC("YLTC", "Tap Changer", "分接开关", LnClass.Y),
    YPSH("YPSH", "Power Shunt", "功率分流", LnClass.Y),
    YPTR("YPTR", "Power Transformer", "电力变压器", LnClass.Y),

    // ==================== Z — Other Power Equipment ====================
    ZAXN("ZAXN", "Auxiliary Supply", "辅助电源", LnClass.Z),
    ZBAT("ZBAT", "Battery", "电池", LnClass.Z),
    ZBSH("ZBSH", "Bypass Switch", "断路器", LnClass.Z),
    ZCAB("ZCAB", "Power Cable", "电力电缆", LnClass.Z),
    ZCAP("ZCAP", "Capacitor Bank", "电容器组", LnClass.Z),
    ZCON("ZCON", "Converter", "转换器", LnClass.Z),
    ZGEN("ZGEN", "Generator", "发电机", LnClass.Z),
    ZGIL("ZGIL", "Gas Insulated Line", "气体绝缘线", LnClass.Z),
    ZLIN("ZLIN", "Overhead Line", "电力架空线", LnClass.Z),
    ZMOT("ZMOT", "Motor", "电动机", LnClass.Z),
    ZREA("ZREA", "Reactor", "电抗器", LnClass.Z),
    ZRRC("ZRRC", "Rotating Reactive Compensator", "旋转无功元件", LnClass.Z),
    ZSAR("ZSAR", "Surge Arrester", "浪涌抑制器", LnClass.Z),
    ZTCF("ZTCF", "Thyristor Controlled Frequency Conv.", "精闸管控制频率转换器", LnClass.Z),
    ZTCR("ZTCR", "Thyristor Controlled Reactive Comp.", "精闸管控制无功元件", LnClass.Z);

    /**
     * LN class group according to IEC 61850-7-4.
     */
    @Getter
    @Accessors(fluent = true)
    public enum LnClass {
        L("System", "系统逻辑节点"),
        P("Protection", "保护功能逻辑节点"),
        R("Protection Related", "保护相关功能逻辑节点"),
        C("Control", "控制逻辑节点"),
        G("General Reference", "通用引用逻辑节点"),
        I("Interface & Archive", "接口和存档逻辑节点组"),
        A("Auto Control", "自动控制逻辑节点组"),
        M("Metering & Measurement", "计量和测量逻辑节点"),
        S("Sensor Monitoring", "传感器监视逻辑节点"),
        X("Switchgear", "开关设备相关逻辑节点"),
        T("Instrument Transformer", "仪用互感器逻辑节点"),
        Y("Power Transformer", "电力变压器逻辑节点"),
        Z("Other Power Equipment", "其他电力设备逻辑节点");

        private final String enName;
        private final String cnName;

        LnClass(String en, String cn) {
            this.enName = en;
            this.cnName = cn;
        }
    }

    private static final Map<String, CmsLnInfo> BY_NAME = new HashMap<>();
    private static final Map<LnClass, List<CmsLnInfo>> BY_CLASS = new HashMap<>();

    static {
        for (CmsLnInfo ln : values()) {
            BY_NAME.put(ln.lnName, ln);
            BY_CLASS.computeIfAbsent(ln.lnClass, k -> new ArrayList<>()).add(ln);
        }
    }

    private final String lnName;
    private final String enName;
    private final String cnName;
    private final LnClass lnClass;

    CmsLnInfo(String lnName, String enName, String cnName, LnClass lnClass) {
        this.lnName = lnName;
        this.enName = enName;
        this.cnName = cnName;
        this.lnClass = lnClass;
    }

    public static CmsLnInfo byName(String name) {
        return BY_NAME.get(name);
    }

    public static List<CmsLnInfo> byClass(LnClass lnClass) {
        return BY_CLASS.getOrDefault(lnClass, Collections.emptyList());
    }
}
