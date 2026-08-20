package com.ysh.jcms.app.handler.console.ap;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.node.SclManager;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.utils.scl.model.ied.SclIED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Server-side {@code ap} command: list the access points this server serves.
 *
 * <p>
 * The server always reads from the SCD source — the SCD is parsed into
 * {@link SclManager} at startup, so the model IS the SCD content; no re-scan of
 * the file is needed. Unlike {@link ApClientHandler}, there is no {@code --cfg}
 * (the server's access points are not configurable) and no {@code --source}
 * override.
 *
 * <p>
 * Examples:
 * <pre>
 *   ap                          # list all served access points
 *   ap --list --limit 10 --offset 0     # first page of 10
 * </pre>
 */
public class ApServerHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ApServerHandler() {
        super(CommandInfo.AP);
        Param p1 = Param.of("list", null, null, String.class, false);
        param(p1, "列出访问点（默认行为）");
        Param p2 = Param.of("limit", null, null, String.class, false);
        param(p2, "数量（不传则列出全部）");
        Param p3 = Param.of("offset", "0", null, String.class, false);
        param(p3, "起始索引");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        SclManager scl = console.sclManager();
        if (!scl.loaded()) {
            CmsPrinter.error("SCL not loaded.");
            return;
        }

        List<String> refs = new ArrayList<>();
        for (SclIED ied : scl.ieds()) {
            ied.accessPoints().forEach(ap -> refs.add(ied.name() + "/" + ap.name()));
        }

        String offsetStr = args.get("offset");
        String limitStr = args.get("limit");
        int offset = Integer.parseInt(offsetStr);
        int limit = (limitStr != null && !limitStr.isEmpty()) ? Integer.parseInt(limitStr) : refs.size();

        int to = Math.min(offset + limit, refs.size());
        List<String> page = offset >= refs.size() ? Collections.emptyList() : refs.subList(offset, to);

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("source", "scd");
        data.put("scd", scl.source());
        data.put("total", refs.size());
        data.put("accessPoints", page);
        CmsPrinter.result(data);
    }
}
