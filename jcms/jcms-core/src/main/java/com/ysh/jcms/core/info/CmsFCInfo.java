package com.ysh.jcms.core.info;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Functional Constraint (FC) reference table — IEC 61850-7-4 §5.4
 *
 * DL/T 2811 adopts IEC 61850; FC values are fixed and not extensible.
 *
 * Each FC defines the semantic category, permitted operations, and initial-value
 * behavior of a data attribute. The wire-level value type is {@code CmsFC}
 * (jcms-core data.scalar); this enum is the semantic reference table.
 */
@Getter
@Accessors(fluent = true)
public enum CmsFCInfo {

    ST("Status information", "状态信息",
            "DataAttribute shall represent status information whose value may be read, substituted, reported, and logged but shall not be writeable.",
            "DataAttribute 表示状态信息，其值可读、替代、报告和日志，但不可写。", "Initial value of the DataAttribute shall be taken from the process.",
            "DataAttribute 的初始值应从过程获取。"),

    MX("Measurands (analogue values)", "测量值（模拟量）",
            "DataAttribute shall represent measurand information whose value may be read, substituted, reported, and logged but shall not be writeable.",
            "DataAttribute 表示测量值信息，其值可读、替代、报告和日志，但不可写。", "Initial value of the DataAttribute shall be taken from the process.",
            "DataAttribute 的初始值应从过程获取。"),

    SP("Setting (outside setting group)", "设置值（设置组外）",
            "DataAttribute shall represent setting parameter information whose value is read and may be written. Changes of values shall become effective immediately, and may be reported.",
            "DataAttribute 表示设置参数信息，其值可读写。值变更应立即生效，并可报告。",
            "Initial value of the DataAttribute shall be as configured; value shall be non-volatile.", "DataAttribute 的初始值应为配置值；值应为非易失性。"),

    SV("Substitution", "替代值",
            "DataAttribute shall represent substitution information whose value may be written to substitute the value attribute and read.",
            "DataAttribute 表示替代信息，其值可写入以替代 value 属性，并可读取。",
            "If the value of the DataAttribute is volatile then the initial value shall be FALSE, else the value should be as set or configured.",
            "若 DataAttribute 值为易失性，则初始值应为 FALSE，否则应为已设置或配置的值。"),

    CF("Configuration", "配置",
            "DataAttribute shall represent configuration information whose value may be written and read. Values written may become effective immediately or deferred for reasons outside the scope of this standard. Value changes may be reported.",
            "DataAttribute 表示配置信息，其值可读写。写入的值可立即生效或因本标准范围外的原因延迟生效。值变更可报告。",
            "Initial value of the DataAttribute shall be as configured; value shall be non-volatile.", "DataAttribute 的初始值应为配置值；值应为非易失性。"),

    DC("Description", "描述", "DataAttribute shall represent description information whose value may be written and read.",
            "DataAttribute 表示描述信息，其值可读写。", "Initial value of the DataAttribute shall be as configured; value shall be non-volatile.",
            "DataAttribute 的初始值应为配置值；值应为非易失性。"),

    SG("Setting group", "设置组",
            "Logical devices that implement the SGCB class maintain multiple grouped values of all instances of DataAttributes with functional constraint SG. Each group contains one value for each DataAttribute. DataAttributes with functional constraint SG shall be the current active value (for details see Clause 16). DataAttributes with FC=SG shall not be writeable.",
            "实现 SGCB 类的逻辑设备维护所有 FC=SG 的 DataAttribute 实例的多个分组值。每个分组包含每个 DataAttribute 的一个值。FC=SG 的 DataAttribute 应为当前活动值（详见第 16 章）。FC=SG 的 DataAttribute 不可写。",
            "Initial value of the DataAttribute shall be as configured; value shall be non-volatile.", "DataAttribute 的初始值应为配置值；值应为非易失性。"),

    SE("Setting group editable", "设置组可编辑",
            "DataAttribute that can be edited by SGCB services. Defines the edit buffer for the value sets belonging to attributes with fc=SG.",
            "可由 SGCB 服务编辑的 DataAttribute。定义属于 FC=SG 属性的值集的编辑缓冲区。",
            "Value of the DataAttribute shall be available after SelectEditSG service has been processed.",
            "DataAttribute 的值应在 SelectEditSG 服务处理后可用。"),

    SR("Service response", "服务响应",
            "DataAttribute shall represent data from different process objects with the same tracking object whose values can be used to be reported and logged; the values shall not be writeable. These attributes are used for service tracking (see 15.3.2).",
            "DataAttribute 表示来自不同过程对象但具有相同跟踪对象的数据，其值可用于报告和日志；值不可写。这些属性用于服务跟踪（详见 15.3.2）。",
            "Initial values of the DataAttribute are a private issue, for example, all zero (except for time stamp).",
            "DataAttribute 的初始值是私有问题，例如全零（时间戳除外）。"),

    OR("Operate received", "操作接收",
            "DataAttribute shall represent the result of an Operate request at the data object receiving the Operate request, even if the execution of the Operate is blocked.",
            "DataAttribute 表示接收 Operate 请求的数据对象上的 Operate 请求结果，即使 Operate 的执行被阻塞。", "Initial value is irrelevant / arbitrary", "初始值无关/任意"),

    BL("Blocking", "阻塞", "DataAttribute is used for blocking value updates.", "DataAttribute 用于阻塞值更新。",
            "If the value of the DataAttribute is volatile then the initial value shall be FALSE, else the value should be as set or configured.",
            "若 DataAttribute 值为易失性，则初始值应为 FALSE，否则应为已设置或配置的值。"),

    EX("Extended definition (application name space)", "扩展定义（应用命名空间）",
            "DataAttribute shall represent an application name space. Application name spaces are used to define the semantic definitions of LNs, data object class, and DataAttributes as specified in 61850-7-3 and IEC 61850-7-4. DataAttributes with FC=EX shall not be writeable. Note that private extensions of Control Blocks may use the FC EX at SCSM level.",
            "DataAttribute 表示应用命名空间。应用命名空间用于定义 LN、数据对象类和 DataAttribute 的语义定义，如 61850-7-3 和 IEC 61850-7-4 所述。FC=EX 的 DataAttribute 不可写。注意 Control Block 的私有扩展可在 SCSM 层使用 FC EX。",
            "Value of the DataAttribute shall be as configured; value shall be non-volatile.", "DataAttribute 的值应为配置值；值应为非易失性。"),

    XX("Representing all DataAttributes as a service parameter", "表示所有 DataAttribute 作为服务参数",
            "Shall represent all DataAttributes of a data object (of any FC) to be accessed, for example, to be written and read. The FC value \"XX\" shall only be used in the functional constrained data (FCD); \"XX\" shall not be used as FC value in a DataAttribute.",
            "表示数据对象的所有 DataAttribute（任何 FC）可被访问，例如读写。FC 值 \"XX\" 仅应用于功能约束数据（FCD）；\"XX\" 不得用作 DataAttribute 中的 FC 值。", "", "");

    private final String semantic;
    private final String semanticZh;
    private final String servicesAllowed;
    private final String servicesAllowedZh;
    private final String initialValue;
    private final String initialValueZh;

    CmsFCInfo(String semantic, String semanticZh, String servicesAllowed, String servicesAllowedZh, String initialValue,
            String initialValueZh) {
        this.semantic = semantic;
        this.semanticZh = semanticZh;
        this.servicesAllowed = servicesAllowed;
        this.servicesAllowedZh = servicesAllowedZh;
        this.initialValue = initialValue;
        this.initialValueZh = initialValueZh;
    }

    /** Look up by 2-char FC code, e.g. "ST", "MX". */
    public static CmsFCInfo fromCode(String code) {
        return valueOf(code);
    }

    /** Return the 2-char code, e.g. "ST" / "MX". */
    public String code() {
        return name();
    }
}
