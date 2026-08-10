package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.data.choice.CmsData;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetEditSgValueConsole extends CommandHandler {

    private static final Map<String, Integer> TYPE_MAP = buildTypeMap();
    private static Map<String, Integer> buildTypeMap() {
        Map<String, Integer> m = new HashMap<>();
        m.put("boolean", CmsData.CHOICE_BOOLEAN);
        m.put("int8", CmsData.CHOICE_INT8);
        m.put("int16", CmsData.CHOICE_INT16);
        m.put("int32", CmsData.CHOICE_INT32);
        m.put("int64", CmsData.CHOICE_INT64);
        m.put("int8u", CmsData.CHOICE_INT8U);
        m.put("int16u", CmsData.CHOICE_INT16U);
        m.put("int32u", CmsData.CHOICE_INT32U);
        m.put("int64u", CmsData.CHOICE_INT64U);
        m.put("float32", CmsData.CHOICE_FLOAT32);
        m.put("float64", CmsData.CHOICE_FLOAT64);
        m.put("visible-string", CmsData.CHOICE_VISIBLE_STRING);
        m.put("octet-string", CmsData.CHOICE_OCTET_STRING);
        return Collections.unmodifiableMap(m);
    }

    public SetEditSgValueConsole() {
        super(CommandInfo.SET_EDIT_SG);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "数据引用列表（空格分隔），如 \"PROT/OCPTOC2.StrVal PROT/OCPTOC2.OpDlTmms\"", null),
                new Param("values", "定值列表（空格分隔），与 refs 一一对应", null), new Param("type", "数据类型，默认 visible-string", "visible-string"));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        String refsStr = args.get("refs");
        String valuesStr = args.get("values");
        String typeStr = args.get("type");
        if (typeStr == null)
            typeStr = "visible-string";

        if (refsStr == null || refsStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing --refs");
            return;
        }
        if (valuesStr == null || valuesStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing --values (must match --refs count)");
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        String[] vals = valuesStr.trim().split("\\s+");
        if (refs.length != vals.length) {
            ConsolePrinter.error("--refs count (" + refs.length + ") != --values count (" + vals.length + ")");
            return;
        }

        Integer choiceType = TYPE_MAP.get(typeStr);
        if (choiceType == null) {
            ConsolePrinter.error("Unknown type: " + typeStr
                    + ". Supported: visible-string, int32, float32, boolean, int8, int16, int8u, int16u, int32u, int64, int64u, float64, octet-string");
            return;
        }

        SetEditSgValueDao dao = new SetEditSgValueDao();
        for (int i = 0; i < refs.length; i++) {
            byte[] valueBytes = vals[i].getBytes(StandardCharsets.UTF_8);
            dao.addEntry(refs[i].trim(), valueBytes, choiceType);
        }

        ConsolePrinter.info("Setting edit SG values (" + typeStr + ") for " + refs.length + " ref(s)");

        console.getClient(SetEditSgValueClient.class).execute(dao);

        ConsolePrinter.success("Edit SG values set successfully");
    }
}
