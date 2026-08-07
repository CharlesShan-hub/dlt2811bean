package com.ysh.jcms.app.console;

/**
 * Central registry of all CLI command metadata.
 *
 * <p>
 * Each constant defines the command {@link #name} (used as the lookup key in
 * {@link CmsConsole}) and its {@link #description} (shown in help output).
 *
 * <p>
 * Console handler classes reference a constant via
 * {@link BaseConsoleHandler#BaseConsoleHandler(CommandInfo)} — no need to
 * override {@code name()} / {@code description()} individually.
 */
public enum CommandInfo {

    // ── 连接管理 ──
    CONNECT("connect", "连接到 CMS 服务器"), CONNECT_TLS("connect-tls", "TLS 连接 CMS 服务器"), DISCONNECT("disconnect",
            "断开当前连接"), ASSOCIATE("associate", "建立关联"), RELEASE("release", "释放关联"), ABORT("abort", "异常中止关联"), NEGOTIATE("negotiate", "协商参数"),

    // ── 本地配置 ──
    AP_DIR("ap-dir", "列出 SCD 中所有可用的 AccessPoint"), AP_CFG("ap-cfg", "查看/修改 AP 来源配置"), NEG_CFG("neg-cfg", "查看/修改协商参数"),

    // ── 目录服务 ──
    SERVER_DIR("server-dir", "获取逻辑设备目录"), LD_DIR("ld-dir", "获取逻辑节点目录"), LN_DIR("ln-dir", "获取逻辑节点子目录"), ALL_DATA("all-data",
            "获取所有数据值"), ALL_DEF("all-def", "获取所有数据定义"), ALL_CB("all-cb", "获取所有控制块值"),

    // ── 数据服务 ──
    GET_DATA_VALUES("get-data-values", "获取数据值"), SET_DATA_VALUES("set-data-values", "设置数据值"), DATA_DIR("data-dir",
            "获取数据目录"), GET_DATA_DEF("get-data-def", "获取数据定义"),

    // ── 数据集服务 ──
    GET_DATASET_VALUES("get-dataset-values", "获取数据集值"), SET_DATASET_VALUES("set-dataset-values", "设置数据集值"), CREATE_DATASET("create-dataset",
            "创建数据集"), DELETE_DATASET("delete-dataset", "删除数据集"), GET_DATASET_DIR("get-dataset-dir", "获取数据集目录"),

    // ── 定值组服务 ──
    SELECT_ACTIVE_SG("select-active-sg", "选择激活定值组"), SELECT_EDIT_SG("select-edit-sg", "选择编辑定值组"), SET_EDIT_SG("set-edit-sg",
            "设置编辑定值组值"), GET_EDIT_SG("get-edit-sg",
                    "获取编辑定值组值"), CONFIRM_EDIT_SG("confirm-edit-sg", "确认编辑定值组值生效"), SGCB_VALS("sgcb-vals", "获取定值组控制块值"),

    // ── 报告服务 ──
    GET_BRCB_VALS("get-brcb-vals", "获取缓存报告控制块值"), SET_BRCB_VALS("set-brcb-vals", "设置缓存报告控制块值"), GET_URCB_VALS("get-urcb-vals",
            "获取非缓存报告控制块值"), SET_URCB_VALS("set-urcb-vals", "设置非缓存报告控制块值"),

    // ── 日志服务 ──
    GET_LCB_VALS("get-lcb-vals", "获取日志控制块值"), SET_LCB_VALS("set-lcb-vals", "设置日志控制块值"), QUERY_LOG_BY_TIME("query-log-by-time",
            "按时间查询日志"), QUERY_LOG_AFTER("query-log-after", "查询指定条目之后的日志"), GET_LOG_STATUS("get-log-status", "获取日志状态值"),

    // ── GOOSE 服务 ──
    GET_GOCB_VALS("get-gocb-vals", "获取 GOOSE 控制块值"), SET_GOCB_VALS("set-gocb-vals", "设置 GOOSE 控制块值"), GET_GO_REF("get-go-ref",
            "读 GOOSE 引用"), GET_GOOSE_ELEM_NUM("get-goose-elem-num", "读 GOOSE 元素序号"),

    // ── 多播采样值服务 ──
    GET_MSVCB_VALS("get-msvcb-vals", "读多播采样值控制块值"), SET_MSVCB_VALS("set-msvcb-vals", "设置多播采样值控制块值"),

    // ── 控制服务 ──
    SELECT("select", "选择控制对象"), SELECT_WITH_VALUE("select-with-value", "带值选择控制对象"), OPERATE("operate", "执行控制操作"), CANCEL("cancel",
            "取消选择控制对象"), TIME_ACT_OPE("time-act-ope", "定时执行控制"),

    // ── 文件服务 ──
    GET_FILE_DIR("get-file-dir", "列文件目录"), GET_FILE_ATTRS("get-file-attrs", "读文件属性值"), GET_FILE("get-file", "读文件"), SET_FILE("set-file",
            "写文件"), DELETE_FILE("delete-file", "删除文件"),

    // ── RPC 服务 ──
    RPC_IFACE_DIR("rpc-iface-dir", "读 RPC 接口目录"), RPC_METHOD_DIR("rpc-method-dir", "读 RPC 方法目录"), RPC_IFACE_DEF("rpc-iface-def",
            "读 RPC 接口定义"), RPC_METHOD_DEF("rpc-method-def", "读 RPC 方法定义"), RPC_CALL("rpc-call", "远程过程调用"),

    // ── 其他 ──
    TEST("test", "测试连接"), TRACE_PDU("trace-pdu", "开启/关闭 PDU 跟踪"), MAX_ENTRIES("max-entries", "设置服务端最大返回条数（用于测试分页）"), CLEAR("clear",
            "清空屏幕"), LIST_AP("list-ap", "列出全部访问点"), HELP("help", "显示帮助信息");

    private final String name;
    private final String description;

    CommandInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /** Command name used as the lookup key in {@link CmsConsole}. */
    public String commandName() {
        return name;
    }

    /** Short description shown in help output. */
    public String description() {
        return description;
    }
}
