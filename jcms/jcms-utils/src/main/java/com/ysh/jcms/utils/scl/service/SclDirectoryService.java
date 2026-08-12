package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.core.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 目录服务 —— 服务器目录（8.3.1）/ 逻辑设备目录（8.3.2）/ 逻辑节点目录（8.3.3）。
 * <p>
 * 各方法返回完整结果列表，分页（referenceAfter / pageSize）由 handler 层处理。 全部数据值/定义/控制块值见
 * {@link SclAllValuesService}，数据目录见 {@link SclDataDirectoryService}。
 */
public final class SclDirectoryService {

    private SclDirectoryService() {
    }

    // ==================== 服务器目录（8.3.1） ====================

    /**
     * 获取指定 IED 下、指定 AP 作用域内的所有逻辑设备实例名。
     *
     * @param ap
     *            当前关联的访问点（限定在该 AP 下查找）
     * @return 逻辑设备实例名列表
     */
    public static List<String> getServerDirectory(SclAccessPoint ap) {
        List<String> names = new ArrayList<>();
        SclServer srv = ap.server();
        if (srv != null) {
            for (SclLDevice ld : srv.lDevices()) {
                names.add(ld.inst());
            }
        }
        return names;
    }

    // ==================== 逻辑设备目录（8.3.2） ====================

    /**
     * 获取逻辑设备目录（LN 名称列表）。
     * <p>
     * 当 {@code ldName} 非空时，返回该 LD 下所有 LN 的短名称（如 {@code LLN0}、{@code PIOC1}）； 当
     * {@code ldName} 为空时，返回指定 AP 下所有 LD 的所有 LN 的完整引用（如
     * {@code LD0/LLN0}、{@code LD0/PIOC1}）。
     *
     * @param ap
     *            当前关联的访问点（限定在该 AP 下查找）
     * @param ldName
     *            逻辑设备实例名，为空时返回全站 LN
     * @return LN 名称列表，LD 不存在时返回 null
     */
    public static List<String> getLogicalDeviceDirectory(SclAccessPoint ap, String ldName) {
        if (ldName != null) {
            SclLDevice device = findLdInAp(ap, ldName);
            if (device == null)
                return null;
            return getLnNames(device);
        }
        return getAllLnNames(ap);
    }

    private static SclLDevice findLdInAp(SclAccessPoint ap, String ldName) {
        SclServer srv = ap.server();
        if (srv != null) {
            return srv.findLDeviceByInst(ldName);
        }
        return null;
    }

    private static List<String> getLnNames(SclLDevice device) {
        List<String> names = new ArrayList<>();
        SclLN ln0 = null;
        for (SclLN ln : device.lns()) {
            if ("LLN0".equals(ln.lnClass())) {
                ln0 = ln;
            } else {
                names.add(ln.getFullName());
            }
        }
        if (ln0 != null)
            names.add(0, ln0.getFullName());
        return names;
    }

    private static List<String> getAllLnNames(SclAccessPoint ap) {
        List<String> names = new ArrayList<>();
        SclServer srv = ap.server();
        if (srv != null) {
            for (SclLDevice ld : srv.lDevices()) {
                for (String n : getLnNames(ld)) {
                    names.add(ld.inst() + "/" + n);
                }
            }
        }
        return names;
    }

    // ==================== 逻辑节点目录（8.3.3） ====================

    /**
     * 获取逻辑节点目录（按 ACSI 类收集名称）。
     * <p>
     * 各 ACSI 类返回内容：
     * <ul>
     * <li>{@code DATA_OBJECT} — 完整 DO 引用（{@code ldName/lnFullName.doName}），含 SDO
     * 递归</li>
     * <li>{@code DATA_SET} — 数据集名称</li>
     * <li>{@code BRCB} / {@code URCB} / {@code LCB} / {@code GOCB} / {@code MSVCB}
     * — 控制块名称</li>
     * <li>{@code LOG} — 日志引用名</li>
     * </ul>
     *
     * @param doc
     *            SCL 文档（可为 null）
     * @param lns
     *            已解析的 LN 列表
     * @param ldName
     *            逻辑设备名（DO 类型需要以此作为前缀）
     * @param acsiClass
     *            ACSI 类（见 {@link CmsAcsiClass}）
     * @return 名称列表（未找到时返回空列表而非 null）
     */
    public static List<String> getLogicalNodeDirectory(SclDocument doc, List<SclLN> lns, String ldName, int acsiClass) {
        SclDataTypeTemplates templates = doc != null ? doc.dataTypeTemplates() : null;
        List<String> all = new ArrayList<>();
        for (SclLN ln : lns) {
            switch (acsiClass) {
                case CmsAcsiClass.DATA_OBJECT :
                    if (templates != null) {
                        all.addAll(getDataObjectNames(ldName, ln, templates));
                    } else {
                        for (SclDOI doi : ln.dois()) {
                            all.add(ldName + "/" + ln.getFullName() + "." + doi.name());
                        }
                    }
                    break;
                case CmsAcsiClass.DATA_SET :
                    for (SclDataSet ds : ln.dataSets()) {
                        all.add(ds.name());
                    }
                    break;
                case CmsAcsiClass.BRCB :
                    for (SclReportControl rc : ln.reportControls()) {
                        if ("true".equals(rc.buffered()))
                            all.add(rc.name());
                    }
                    break;
                case CmsAcsiClass.URCB :
                    for (SclReportControl rc : ln.reportControls()) {
                        if (!"true".equals(rc.buffered()))
                            all.add(rc.name());
                    }
                    break;
                case CmsAcsiClass.LCB :
                    for (SclLogControl lc : ln.logControls()) {
                        all.add(lc.name());
                    }
                    break;
                case CmsAcsiClass.LOG :
                    for (SclLogControl lc : ln.logControls()) {
                        String logName = lc.logName();
                        if (logName != null && !logName.isEmpty()) {
                            all.add(logName);
                        }
                    }
                    break;
                case CmsAcsiClass.GOCB :
                    for (SclGSEControl gc : ln.gseControls()) {
                        all.add(gc.name());
                    }
                    break;
                case CmsAcsiClass.MSVCB :
                    for (SclSampledValueControl sv : ln.svControls()) {
                        all.add(sv.name());
                    }
                    break;
                default :
                    break;
            }
        }
        return all;
    }

    private static List<String> getDataObjectNames(String ldName, SclLN ln, SclDataTypeTemplates templates) {
        List<String> names = new ArrayList<>();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return names;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
        if (lnt == null)
            return names;
        String lnPrefix = ldName + "/" + ln.getFullName() + ".";
        for (SclDO doDef : lnt.dos()) {
            names.add(lnPrefix + doDef.name());
            collectSdoNames(lnPrefix + doDef.name(), doDef.type(), templates, names, new HashSet<>());
        }
        return names;
    }

    private static void collectSdoNames(String parentRef, String doTypeId, SclDataTypeTemplates templates, List<String> names,
            HashSet<String> visited) {
        if (doTypeId == null || doTypeId.isEmpty() || !visited.add(doTypeId))
            return;
        SclDOType doType = templates.findDoTypeById(doTypeId);
        if (doType == null)
            return;
        for (SclSDO sdo : doType.sdos()) {
            String ref = parentRef + "." + sdo.name();
            names.add(ref);
            collectSdoNames(ref, sdo.type(), templates, names, visited);
        }
    }
}
