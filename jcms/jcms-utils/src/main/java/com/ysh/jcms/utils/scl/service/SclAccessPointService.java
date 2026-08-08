package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 访问点解析服务 —— 从 sapRef 解析 IED + AccessPoint。
 * <p>
 * 供 Associate 服务在建立连接时定位客户端请求的访问点。
 */
public final class SclAccessPointService {

    private static final Logger log = LoggerFactory.getLogger(SclAccessPointService.class);

    /** 解析结果：IED + AccessPoint。 */
    public static final class ResolvedAp {
        public final SclIED ied;
        public final SclAccessPoint ap;

        ResolvedAp(SclIED ied, SclAccessPoint ap) {
            this.ied = ied;
            this.ap = ap;
        }
    }

    private SclAccessPointService() {
    }

    /**
     * 按 sapRef 解析访问点。
     *
     * @param scl
     *            SCL 文档
     * @param sapRef
     *            访问点引用，格式 {@code IEDName[/AccessPointName]}，缺省 AP 名为
     *            {@code S1}
     * @return 解析结果，IED 或 AP 不存在时返回 {@code null}
     */
    public static ResolvedAp resolve(SclDocument scl, String sapRef) {
        if (scl == null || sapRef == null)
            return null;
        int slashIdx = sapRef.indexOf('/');
        String iedName = slashIdx >= 0 ? sapRef.substring(0, slashIdx) : sapRef;
        String apName = slashIdx >= 0 ? sapRef.substring(slashIdx + 1) : "S1";

        SclIED ied = scl.ied(iedName);
        if (ied == null) {
            log.warn("SclAccessPointService: IED '{}' not found", iedName);
            return null;
        }
        SclAccessPoint ap = ied.findAccessPointByName(apName);
        if (ap == null) {
            log.warn("SclAccessPointService: access point '{}' not found on IED '{}'", apName, iedName);
            return null;
        }
        return new ResolvedAp(ied, ap);
    }

    /** 取第一个带访问点的 IED 及其首个 AP；无可用访问点时返回 {@code null}。 */
    public static ResolvedAp resolveDefault(SclDocument scl) {
        if (scl == null)
            return null;
        for (SclIED ied : scl.ieds()) {
            if (!ied.accessPoints().isEmpty()) {
                return new ResolvedAp(ied, ied.accessPoints().get(0));
            }
        }
        return null;
    }
}
