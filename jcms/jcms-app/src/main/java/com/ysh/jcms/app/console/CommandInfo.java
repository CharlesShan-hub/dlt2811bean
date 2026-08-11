package com.ysh.jcms.app.console;

/**
 * Central registry of all CLI command metadata.
 *
 * <p>
 * Each constant defines the command {@link #name} (used as the lookup key in
 * {@link CmsConsole}), its {@link #description} (shown in help output), and its
 * {@link Requirement} precondition.
 */
public enum CommandInfo {

    // @formatter:off
    // ── 连接管理 ──
    CONNECT("connect", "连接到 CMS 服务器", Requirement.NONE),
    CONNECT_TLS("connect-tls", "TLS 连接 CMS 服务器", Requirement.NONE),
    DISCONNECT("disconnect", "断开当前连接", Requirement.CONNECTED),
    ASSOCIATE("associate", "建立关联", Requirement.CONNECTED),
    RELEASE("release", "释放关联", Requirement.CONNECTED),
    ABORT("abort", "异常中止关联", Requirement.CONNECTED),
    NEGOTIATE("negotiate", "协商参数", Requirement.CONNECTED),

    // ── 本地配置 ──
    AP_DIR("ap-dir", "列出 SCD 中所有可用的 AccessPoint", Requirement.NONE),
    AP_CFG("ap-cfg", "查看/修改 AP 来源配置", Requirement.NONE),
    NEG_CFG("neg-cfg", "查看/修改协商参数", Requirement.NONE),

    // ── 目录服务 ──
    SERVER_DIR("server-dir", "获取逻辑设备目录", Requirement.ASSOCIATED),
    LD_DIR("ld-dir", "获取逻辑节点目录", Requirement.ASSOCIATED),
    LN_DIR("ln-dir", "获取逻辑节点子目录", Requirement.ASSOCIATED),
    ALL_DATA("all-data", "获取所有数据值", Requirement.ASSOCIATED),
    ALL_DEF("all-def", "获取所有数据定义", Requirement.ASSOCIATED),
    ALL_CB("all-cb", "获取所有控制块值", Requirement.ASSOCIATED),

    // ── 数据服务 ──
    GET_DATA_VALUES("get-data-values", "获取数据值", Requirement.ASSOCIATED),
    SET_DATA_VALUES("set-data-values", "设置数据值", Requirement.ASSOCIATED),
    DATA_DIR("data-dir", "获取数据目录", Requirement.ASSOCIATED),
    GET_DATA_DEF("get-data-def", "获取数据定义", Requirement.ASSOCIATED),

    // ── 数据集服务 ──
    GET_DATASET_VALUES("get-dataset-values", "获取数据集值", Requirement.ASSOCIATED),
    SET_DATASET_VALUES("set-dataset-values", "设置数据集值", Requirement.ASSOCIATED),
    CREATE_DATASET("create-dataset", "创建数据集", Requirement.ASSOCIATED),
    DELETE_DATASET("delete-dataset", "删除数据集", Requirement.ASSOCIATED),
    GET_DATASET_DIR("get-dataset-dir", "获取数据集目录", Requirement.ASSOCIATED),

    // ── 定值组服务 ──
    SELECT_ACTIVE_SG("select-active-sg", "选择激活定值组", Requirement.ASSOCIATED),
    SELECT_EDIT_SG("select-edit-sg", "选择编辑定值组", Requirement.ASSOCIATED),
    SET_EDIT_SG("set-edit-sg", "设置编辑定值组值", Requirement.ASSOCIATED),
    GET_EDIT_SG("get-edit-sg", "获取编辑定值组值", Requirement.ASSOCIATED),
    CONFIRM_EDIT_SG("confirm-edit-sg", "确认编辑定值组值生效", Requirement.ASSOCIATED),
    SGCB_VALS("sgcb-vals", "获取定值组控制块值", Requirement.ASSOCIATED),

    // ── 报告服务 ──
    GET_BRCB_VALS("get-brcb-vals", "获取缓存报告控制块值", Requirement.ASSOCIATED),
    SET_BRCB_VALS("set-brcb-vals", "设置缓存报告控制块值", Requirement.ASSOCIATED),
    GET_URCB_VALS("get-urcb-vals", "获取非缓存报告控制块值", Requirement.ASSOCIATED),
    SET_URCB_VALS("set-urcb-vals", "设置非缓存报告控制块值", Requirement.ASSOCIATED),

    // ── 日志服务 ──
    GET_LCB_VALS("get-lcb-vals", "获取日志控制块值", Requirement.ASSOCIATED),
    SET_LCB_VALS("set-lcb-vals", "设置日志控制块值", Requirement.ASSOCIATED),
    QUERY_LOG_BY_TIME("query-log-by-time", "按时间查询日志", Requirement.ASSOCIATED),
    QUERY_LOG_AFTER("query-log-after", "查询指定条目之后的日志", Requirement.ASSOCIATED),
    GET_LOG_STATUS("get-log-status", "获取日志状态值", Requirement.ASSOCIATED),

    // ── GOOSE 服务 ──
    GET_GOCB_VALS("get-gocb-vals", "获取 GOOSE 控制块值", Requirement.ASSOCIATED),
    SET_GOCB_VALS("set-gocb-vals", "设置 GOOSE 控制块值", Requirement.ASSOCIATED),
    GET_GO_REF("get-go-ref", "读 GOOSE 引用", Requirement.ASSOCIATED),
    GET_GOOSE_ELEM_NUM("get-goose-elem-num", "读 GOOSE 元素序号", Requirement.ASSOCIATED),

    // ── 多播采样值服务 ──
    GET_MSVCB_VALS("get-msvcb-vals", "读多播采样值控制块值", Requirement.ASSOCIATED),
    SET_MSVCB_VALS("set-msvcb-vals", "设置多播采样值控制块值", Requirement.ASSOCIATED),

    // ── 控制服务 ──
    SELECT("select", "选择控制对象", Requirement.ASSOCIATED),
    SELECT_WITH_VALUE("select-with-value", "带值选择控制对象", Requirement.ASSOCIATED),
    OPERATE("operate", "执行控制操作", Requirement.ASSOCIATED),
    CANCEL("cancel", "取消选择控制对象", Requirement.ASSOCIATED),
    TIME_ACT_OPE("time-act-ope", "定时执行控制", Requirement.ASSOCIATED),

    // ── 文件服务 ──
    GET_FILE_DIR("get-file-dir", "列文件目录", Requirement.ASSOCIATED),
    GET_FILE_ATTRS("get-file-attrs", "读文件属性值", Requirement.ASSOCIATED),
    GET_FILE("get-file", "读文件", Requirement.ASSOCIATED),
    SET_FILE("set-file", "写文件", Requirement.ASSOCIATED),
    DELETE_FILE("delete-file", "删除文件", Requirement.ASSOCIATED),

    // ── RPC 服务 ──
    RPC_IFACE_DIR("rpc-iface-dir", "读 RPC 接口目录", Requirement.ASSOCIATED),
    RPC_METHOD_DIR("rpc-method-dir", "读 RPC 方法目录", Requirement.ASSOCIATED),
    RPC_IFACE_DEF("rpc-iface-def", "读 RPC 接口定义", Requirement.ASSOCIATED),
    RPC_METHOD_DEF("rpc-method-def", "读 RPC 方法定义", Requirement.ASSOCIATED),
    RPC_CALL("rpc-call", "远程过程调用", Requirement.ASSOCIATED),

    // ── 其他 ──
    TEST("test", "测试连接", Requirement.CONNECTED),
    LOG("log", "日志设置（PDU 跟踪等）", Requirement.NONE),
    TRACE_PDU("trace-pdu", "开启/关闭 PDU 跟踪", Requirement.NONE),
    MAX_ENTRIES("max-entries", "设置服务端最大返回条数（用于测试分页）", Requirement.NONE),
    CLEAR("clear", "清空屏幕", Requirement.NONE),
    LIST_AP("list-ap", "列出全部访问点", Requirement.NONE),
    HELP("help", "显示帮助信息", Requirement.NONE);
    // @formatter:on

    /** Precondition requirements for executing this command. */
    public enum Requirement {
        NONE, CONNECTED, ASSOCIATED
    }

    private final String name;
    private final String description;
    private final Requirement requirement;

    CommandInfo(String name, String description, Requirement requirement) {
        this.name = name;
        this.description = description;
        this.requirement = requirement;
    }

    /** Command name used as the lookup key in {@link CmsConsole}. */
    public String commandName() {
        return name;
    }

    /** Short description shown in help output. */
    public String description() {
        return description;
    }

    /** Precondition requirement for executing this command. */
    public Requirement requirement() {
        return requirement;
    }
}
