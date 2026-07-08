package com.ysh.jcms.utils.scl2.query;

import com.ysh.jcms.utils.scl2.model.ied.*;
import com.ysh.jcms.utils.scl2.model.input.SclDataSet;
import com.ysh.jcms.utils.scl2.ref.SclRef;
import com.ysh.jcms.utils.scl2.ref.SclRefParser;

import java.util.List;

/**
 * IED 作用域下的查询门面。
 */
public class IedQuery {

    private final SclIED ied;

    public IedQuery(SclIED ied) {
        this.ied = ied;
    }

    /** 返回当前 IED */
    public SclIED ied() {
        return ied;
    }

    // ==================== AccessPoint ====================

    /** 按名称查找 AccessPoint */
    public SclAccessPoint accessPoint(String name) {
        return ied.findAccessPointByName(name);
    }

    /** 返回所有 AccessPoint */
    public List<SclAccessPoint> accessPoints() {
        return ied.accessPoints();
    }

    // ==================== LDevice ====================

    /** 在所有 AccessPoint 的 Server 中查找 LDevice */
    public SclLDevice findLd(String inst) {
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer server = ap.server();
            if (server != null) {
                SclLDevice ld = server.findLDeviceByInst(inst);
                if (ld != null) return ld;
            }
        }
        return null;
    }

    // ==================== LN ====================

    /** 按引用字符串查找 LN（格式：{@code LD/LN}） */
    public SclLN findLnByRef(String ref) {
        if (!SclRefParser.isValid(ref)) return null;
        SclRef sclRef = SclRefParser.parse(ref);
        if (!sclRef.isLnLevel()) return null;

        SclLDevice ld = findLd(sclRef.ldName());
        if (ld == null) return null;

        for (SclLN ln : ld.lns()) {
            if (ln.getFullName().equals(sclRef.lnName())) {
                return ln;
            }
        }
        return null;
    }

    /** 按 lnClass + inst 查找 LN */
    public SclLN findLn(String ldInst, String lnClass, String lnInst) {
        SclLDevice ld = findLd(ldInst);
        if (ld == null) return null;
        for (SclLN ln : ld.lns()) {
            if (lnClass.equals(ln.lnClass()) && (lnInst == null || lnInst.equals(ln.inst()))) {
                return ln;
            }
        }
        return null;
    }

    /** 按 lnClass 查找所有 LN */
    public List<SclLN> findLnsByClass(String ldInst, String lnClass) {
        SclLDevice ld = findLd(ldInst);
        if (ld == null) return java.util.Collections.emptyList();
        return ld.findLnsByClass(lnClass);
    }

    // ==================== DataSet ====================

    /** 按名称查找 DataSet */
    public SclDataSet findDataSet(String ldInst, String dataSetName) {
        SclLDevice ld = findLd(ldInst);
        if (ld == null) return null;
        for (SclLN ln : ld.lns()) {
            SclDataSet ds = ln.findDataSetByName(dataSetName);
            if (ds != null) return ds;
        }
        return null;
    }
}
