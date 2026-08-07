package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.data.choice.CmsCbValueChoice;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.common.CmsDataDefinitionStructElem;
import com.ysh.jcms.data.sequence.directory.CmsCbValueEntry;
import com.ysh.jcms.data.sequence.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.data.sequence.data.CmsSubRefEntry;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.CbConverter;
import com.ysh.jcms.utils.scl.convert.DataConverter;
import com.ysh.jcms.utils.scl.convert.DataDefinitionResolver;
import com.ysh.jcms.utils.scl.convert.DataValueResolver;
import com.ysh.jcms.utils.scl.convert.DataValueEntry;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 目录服务 —— 封装所有目录相关 SCL 操作，向 handler 层提供统一的调用接口。
 * <p>
 * 各方法返回完整结果列表，分页（referenceAfter / pageSize）由 handler 层处理。
 */
public final class SclDirectoryService {

    private static final Logger log = LoggerFactory.getLogger(SclDirectoryService.class);

    private SclDirectoryService() {
    }

    // ==================== 服务器目录（8.2.5） ====================

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

    // ==================== 逻辑节点目录（8.4.3） ====================

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
                        for (com.ysh.jcms.utils.scl.model.instance.SclDOI doi : ln.dois()) {
                            all.add(ldName + "/" + ln.getFullName() + "." + doi.name());
                        }
                    }
                    break;
                case CmsAcsiClass.DATA_SET :
                    for (com.ysh.jcms.utils.scl.model.input.SclDataSet ds : ln.dataSets()) {
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

    // ==================== 全部数据值（8.5.2） ====================

    /**
     * 获取全部数据值。
     *
     * @param doc
     *            SCL 文档
     * @param ied
     *            当前关联的 IED
     * @param lns
     *            已解析的 LN 列表
     * @param fc
     *            FC 过滤码（null 或空表示不过滤）
     * @return 数据值条目列表
     */
    public static List<CmsDataValueEntry> getAllDataValues(SclDocument doc, SclIED ied, List<SclLN> lns, String fc) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        List<CmsDataValueEntry> entries = new ArrayList<>();
        for (SclLN ln : lns) {
            String ldInst = Navigator.findLdInst(ied, ln);
            if (ldInst == null)
                continue;

            List<String> doNames = getDoNames(ln, templates);
            for (String doName : doNames) {
                String fullRef = ied.name() + "/" + ldInst + "/" + ln.getFullName() + "." + doName;
                DataValueEntry dv = DataValueResolver.resolve(doc, fullRef, fc);
                if (dv != null && dv.val() != null && !dv.val().isEmpty() && dv.bType() != null && !dv.bType().isEmpty()) {
                    entries.add(new CmsDataValueEntry().reference(fullRef).value(DataConverter.toCmsData(dv)));
                }
            }
        }
        return entries;
    }

    private static List<String> getDoNames(SclLN ln, SclDataTypeTemplates templates) {
        List<String> names = new ArrayList<>();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return names;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
        if (lnt == null)
            return names;
        for (SclDO doDef : lnt.dos()) {
            names.add(doDef.name());
        }
        return names;
    }

    // ==================== 全部数据定义（8.5.4） ====================

    /**
     * 获取全部数据定义。
     *
     * @param doc
     *            SCL 文档
     * @param lns
     *            已解析的 LN 列表
     * @param fc
     *            FC 过滤码（null 或空表示不过滤）
     * @return 数据定义条目列表
     */
    public static List<CmsDataDefinitionEntry> getAllDataDefinition(SclDocument doc, List<SclLN> lns, String fc) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        List<CmsDataDefinitionEntry> entries = new ArrayList<>();
        for (SclLN ln : lns) {
            if (templates == null || ln.lnType() == null)
                continue;
            SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
            if (lnt == null)
                continue;

            for (SclDO doDef : lnt.dos()) {
                // FC 过滤
                if (fc != null) {
                    SclDOType doType = doDef.type() != null ? templates.findDoTypeById(doDef.type()) : null;
                    if (doType == null)
                        continue;
                    boolean hasFc = false;
                    for (SclDA da : doType.das()) {
                        if (fc.equalsIgnoreCase(da.fc())) {
                            hasFc = true;
                            break;
                        }
                    }
                    if (!hasFc)
                        continue;
                }

                CmsDataDefinition def = buildDoDefinition(templates, doDef);
                if (def == null)
                    continue;

                SclDOType doType2 = doDef.type() != null ? templates.findDoTypeById(doDef.type()) : null;
                String cdc = doType2 != null ? doType2.cdc() : null;

                CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry().reference(doDef.name());
                if (cdc != null)
                    entry.cdcType(cdc);
                entry.definition = def;
                entries.add(entry);
            }
        }
        return entries;
    }

    private static CmsDataDefinition buildDoDefinition(SclDataTypeTemplates templates, SclDO doDef) {
        if (doDef.type() == null)
            return null;
        SclDOType doType = templates.findDoTypeById(doDef.type());
        if (doType == null)
            return null;

        List<CmsDataDefinitionStructElem> arr = new ArrayList<>();
        for (SclDA da : doType.das()) {
            String bType = da.bType();
            if (bType == null)
                bType = "BOOLEAN";
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem().name(da.name())
                    .fc(da.fc() != null ? CmsFC.fromCodeOr(da.fc(), CmsFC.XX) : 0).type(DataDefinitionResolver.toDataDefinition(bType));
            arr.add(elem);
        }
        for (SclSDO sdo : doType.sdos()) {
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem().name(sdo.name());
            elem.type(DataDefinitionResolver.toDataDefinition(null));
            arr.add(elem);
        }

        CmsDataDefinition def = new CmsDataDefinition();
        def.choice(CmsDataDefinition.CHOICE_STRUCTURE);
        def.alt_structure = arr;
        return def;
    }

    // ==================== 全部控制块值（8.5.6） ====================

    /**
     * 获取全部控制块值。
     *
     * @param lns
     *            已解析的 LN 列表
     * @param acsiClass
     *            ACSI 类（BRCB、URCB、LCB、GOCB、MSVCB）
     * @return 控制块值条目列表
     */
    public static List<CmsCbValueEntry> getAllCbValues(List<SclLN> lns, int acsiClass) {
        List<CmsCbValueEntry> entries = new ArrayList<>();
        for (SclLN ln : lns) {
            List<CbPair> cbPairs = collectCbValues(ln, acsiClass);
            for (CbPair cb : cbPairs) {
                entries.add(new CmsCbValueEntry().reference(ln.getFullName() + "." + cb.ref).value(cb.value));
            }
        }
        return entries;
    }

    private static List<CbPair> collectCbValues(SclLN ln, int acsiClass) {
        List<CbPair> result = new ArrayList<>();
        switch (acsiClass) {
            case CmsAcsiClass.BRCB :
                for (SclReportControl rc : ln.reportControls()) {
                    if ("true".equals(rc.buffered())) {
                        result.add(new CbPair(rc.name(), CbConverter.brcbFrom(rc)));
                    }
                }
                break;
            case CmsAcsiClass.URCB :
                for (SclReportControl rc : ln.reportControls()) {
                    if (!"true".equals(rc.buffered())) {
                        result.add(new CbPair(rc.name(), CbConverter.urcbFrom(rc)));
                    }
                }
                break;
            case CmsAcsiClass.LCB :
                for (SclLogControl lc : ln.logControls()) {
                    result.add(new CbPair(lc.name(), CbConverter.lcbFrom(lc)));
                }
                break;
            case CmsAcsiClass.GOCB :
                for (SclGSEControl gc : ln.gseControls()) {
                    result.add(new CbPair(gc.name(), CbConverter.gocbFrom(gc)));
                }
                break;
            case CmsAcsiClass.MSVCB :
                for (SclSampledValueControl sv : ln.svControls()) {
                    result.add(new CbPair(sv.name(), CbConverter.msvcbFrom(sv)));
                }
                break;
            default :
                break;
        }
        return result;
    }

    // ==================== 数据目录（8.5.5） ====================

    /**
     * 获取数据目录（LN 级列出 DO，DO 级列出 DA/含 fc，SDO 级列出 DA）。
     * <p>
     * 在 DO 级别会合并实例（{@code doi}）与模板中的条目，避免重复。
     *
     * @param doc
     *            SCL 文档
     * @param ln
     *            当前 LN
     * @param doName
     *            DO 名（null = LN 级）
     * @param sdoName
     *            SDO 名（null = DO 级，非 null = SDO 级）
     * @param doi
     *            DO 实例（null = 仅模板，DO 级别时使用）
     * @return 目录条目列表
     */
    public static List<CmsSubRefEntry> getDataDirectory(SclDocument doc, SclLN ln, String doName, String sdoName, SclDOI doi) {
        if (doName == null) {
            return collectLnDirectory(doc, ln);
        } else if (sdoName != null) {
            return collectSdoDirectory(doc, ln, doName, sdoName);
        } else if (doi != null) {
            return collectDoDirectory(doc, doi, ln);
        } else {
            return collectDoDirectoryFromTemplate(doc, ln, doName);
        }
    }

    /** LN 级别：列出 DO 名（合并实例 + 模板）。 */
    private static List<CmsSubRefEntry> collectLnDirectory(SclDocument doc, SclLN ln) {
        Set<String> seen = new HashSet<>();
        List<CmsSubRefEntry> entries = new ArrayList<>();

        for (SclDOI doi : ln.dois()) {
            String name = doi.name();
            seen.add(name);
            entries.add(new CmsSubRefEntry().reference(name));
        }

        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates != null && ln.lnType() != null && !ln.lnType().isEmpty()) {
            SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
            if (lnt != null) {
                for (SclDO doDef : lnt.dos()) {
                    if (!seen.contains(doDef.name())) {
                        entries.add(new CmsSubRefEntry().reference(doDef.name()));
                        seen.add(doDef.name());
                    }
                }
            }
        }

        return entries;
    }

    /** DO 级别：列出 DA/SDI 名（合并实例 + 模板），含 FC。 */
    private static List<CmsSubRefEntry> collectDoDirectory(SclDocument doc, SclDOI doi, SclLN ln) {
        Set<String> seen = new HashSet<>();
        List<CmsSubRefEntry> entries = new ArrayList<>();

        String doName = doi.name();

        // Instance DAIs
        for (SclDAI dai : doi.dais()) {
            String daName = dai.name();
            seen.add(daName);
            String fc = resolveDaFc(doc, ln, doName, daName);
            CmsSubRefEntry entry = new CmsSubRefEntry().reference(daName);
            if (fc != null && !fc.isEmpty())
                entry.fc(CmsFC.fromCodeOr(fc, CmsFC.XX));
            entries.add(entry);
        }

        // Instance SDIs
        for (SclSDI sdi : doi.sdis()) {
            String sdiName = sdi.name();
            seen.add(sdiName);
            entries.add(new CmsSubRefEntry().reference(sdiName));
        }

        // Template DAs/SDOs not in instance
        addTemplateDirs(doc, ln, doName, seen, entries);

        return entries;
    }

    /** DO 级别（仅模板兜底）。 */
    private static List<CmsSubRefEntry> collectDoDirectoryFromTemplate(SclDocument doc, SclLN ln, String doName) {
        List<CmsSubRefEntry> entries = new ArrayList<>();
        addTemplateDirs(doc, ln, doName, new HashSet<>(), entries);
        return entries;
    }

    /** 从 DOType 模板追加 DA/SDO 目录条目（跳过已存在的）。 */
    private static void addTemplateDirs(SclDocument doc, SclLN ln, String doName, Set<String> seen, List<CmsSubRefEntry> entries) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return;
        SclDOType doType = TypeChain.of(templates).from(ln.lnType()).doDef(doName).doType();
        if (doType == null)
            return;
        for (SclDA da : doType.das()) {
            if (!seen.contains(da.name())) {
                seen.add(da.name());
                CmsSubRefEntry entry = new CmsSubRefEntry().reference(da.name());
                if (da.fc() != null && !da.fc().isEmpty())
                    entry.fc(CmsFC.fromCodeOr(da.fc(), CmsFC.XX));
                entries.add(entry);
            }
        }
        for (SclSDO sdo : doType.sdos()) {
            if (!seen.contains(sdo.name())) {
                seen.add(sdo.name());
                entries.add(new CmsSubRefEntry().reference(sdo.name()));
            }
        }
    }

    /** SDO 级别：列出 SDO 的 DOType 中的 DA。 */
    private static List<CmsSubRefEntry> collectSdoDirectory(SclDocument doc, SclLN ln, String doName, String sdoName) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return null;
        SclDOType doType = TypeChain.of(templates).from(ln.lnType()).doDef(doName).doType();
        if (doType == null)
            return null;
        SclSDO sdo = doType.findSdoByName(sdoName);
        if (sdo == null || sdo.type() == null)
            return null;
        SclDOType sdoType = templates.findDoTypeById(sdo.type());
        if (sdoType == null)
            return null;
        List<CmsSubRefEntry> entries = new ArrayList<>();
        for (SclDA da : sdoType.das()) {
            CmsSubRefEntry entry = new CmsSubRefEntry().reference(da.name());
            if (da.fc() != null && !da.fc().isEmpty())
                entry.fc(CmsFC.fromCodeOr(da.fc(), CmsFC.XX));
            entries.add(entry);
        }
        return entries;
    }

    /** 从 DOType 中解析 DA 的 FC。 */
    private static String resolveDaFc(SclDocument doc, SclLN ln, String doName, String daName) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates == null || ln.lnType() == null)
            return null;
        TypeChain.DaStep daStep = TypeChain.of(templates).from(ln.lnType()).doDef(doName).daDef(daName);
        return daStep != null ? daStep.fc() : null;
    }

    // ==================== 内部数据结构 ====================

    private static final class CbPair {
        final String ref;
        final CmsCbValueChoice value;

        CbPair(String ref, CmsCbValueChoice value) {
            this.ref = ref;
            this.value = value;
        }
    }
}
