package com.ysh.dlt2811bean.service.info;

import java.util.HashMap;
import java.util.Map;

public enum FcInfo {

    ST  ("ST",  "状态值（Status）",     "状态量数据，如开关位置、告警信号等"),
    MX  ("MX",  "测量值（Measurand）",  "遥测量数据，如电压、电流、功率等"),
    SP  ("SP",  "定值（Setting）",      "保护/控制定值参数"),
    SV  ("SV",  "替代值（Substitution）","用于替代真实测量值的人工输入值"),
    CF  ("CF",  "配置（Configuration）","数据对象/数据属性的配置描述"),
    DC  ("DC",  "描述（Description）",  "数据对象的「纯文本」描述内容"),
    SG  ("SG",  "定值组（Setting Group）","定值组数据"),
    SE  ("SE",  "编辑定值（Setting Edited）","编辑中的定值数据"),
    SR  ("SR",  "服务响应（Service Response）","控制操作的响应信息"),
    OR  ("OR",  "操作接收（Operate Received）","控制操作的操作值"),
    BL  ("BL",  "联锁（Blocking）",     "控制操作的联锁条件"),
    EX  ("EX",  "扩展定义（Extension）", "扩展的功能约束"),
    XX  ("XX",  "全部（All）",          "表示所有功能约束，用于查询和过滤");

    private static final Map<String, FcInfo> BY_NAME = new HashMap<>();

    static {
        for (FcInfo fc : values()) {
            BY_NAME.put(fc.name, fc);
        }
    }

    private final String name;
    private final String chineseName;
    private final String description;

    FcInfo(String name, String chineseName, String description) {
        this.name = name;
        this.chineseName = chineseName;
        this.description = description;
    }

    public String getName() { return name; }
    public String getChineseName() { return chineseName; }
    public String getDescription() { return description; }

    public static FcInfo byName(String name) {
        return BY_NAME.get(name);
    }

    public static java.util.List<com.ysh.dlt2811bean.cli.Param.EnumChoice> enumChoices() {
        java.util.List<com.ysh.dlt2811bean.cli.Param.EnumChoice> choices = new java.util.ArrayList<>();
        for (FcInfo fc : values()) {
            choices.add(new com.ysh.dlt2811bean.cli.Param.EnumChoice(fc.getName(), fc.getChineseName()));
        }
        return choices;
    }
}
