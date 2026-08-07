package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.pdu.data.CmsGetDataDirectoryError;
import com.ysh.jcms.pdu.data.CmsGetDataDirectoryRequest;
import com.ysh.jcms.pdu.data.CmsGetDataDirectoryResponse;
import com.ysh.jcms.data.sequence.data.CmsSubRefEntry;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetDataDirectoryServer extends BaseServerHandler<CmsGetDataDirectoryRequest, CmsGetDataDirectoryError> {

    /** Simple directory entry with ref name and optional fc. */
    private static final class DirEntry {
        final String ref;
        final String fc;
        DirEntry(String ref, String fc) {
            this.ref = ref;
            this.fc = fc;
        }
    }

    public GetDataDirectoryServer() {
        super(ServiceName.GET_DATA_DIRECTORY, CmsGetDataDirectoryRequest.class, CmsGetDataDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetDataDirectoryRequest req, int reqId) {
        log.info("GetDataDirectory from {}: reqId={}", session.getSessionId(), reqId);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        String ref = str(req.dataReference);
        log.info("GetDataDirectory ref='{}'", ref);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        // Parse reference
        SclRef parsed = SclRefParser.parse(ref);
        Navigator nav = Navigator.go(ied, parsed);
        if (!nav.isValid())
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclLN ln = nav.ln();
        List<DirEntry> allEntries;

        if (parsed.doName() != null) {
            // DO level or deeper
            String sdoName = parsed.daName();
            if (sdoName != null) {
                // SDO level: ref = LD/LN.DO.SDO
                allEntries = collectSdoDirectory(doc, ln, parsed.doName(), sdoName);
                if (allEntries == null) {
                    log.debug("GetDataDirectory: '{}' is not an SDO, returning empty", sdoName);
                    allEntries = new ArrayList<>();
                }
            } else {
                // DO level: list DA/SDI
                SclDOI doi = nav.doi();
                if (doi != null) {
                    allEntries = collectDoDirectory(doc, doi, ln);
                } else {
                    log.debug("GetDataDirectory: doi not found for ref='{}', fallback to template only", ref);
                    allEntries = collectDoDirectoryFromTemplate(doc, ln, parsed.doName());
                }
            }
        } else {
            // LN level: collect DO directory
            allEntries = collectLnDirectory(doc, ln);
        }

        // referenceAfter pagination
        int startIdx = afterIndex(allEntries, refAfter);
        if (startIdx < 0)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Build paged response
        CmsGetDataDirectoryResponse resp = new CmsGetDataDirectoryResponse();
        int ps = pageSize();
        int count = 0;
        for (int i = startIdx; i < allEntries.size() && count < ps; i++) {
            DirEntry e = allEntries.get(i);
            CmsSubRefEntry entry = new CmsSubRefEntry().reference(e.ref);
            if (e.fc != null && !e.fc.isEmpty())
                entry.fc(CmsFC.fromCodeOr(e.fc, CmsFC.XX));
            resp.dataAttribute.add(entry);
            count++;
        }
        resp.moreFollows(allEntries.size() > startIdx + ps);
        log.info("GetDataDirectory: '{}' -> {} entries (pageSize={})", ref, count, ps);
        return ok(resp, reqId);
    }

    /** Collect DO names at LN level: merge instance DOIs with type template DOs. */
    private static List<DirEntry> collectLnDirectory(SclDocument doc, SclLN ln) {
        Set<String> seen = new HashSet<>();
        List<DirEntry> entries = new ArrayList<>();

        for (SclDOI doi : ln.dois()) {
            String name = doi.name();
            seen.add(name);
            entries.add(new DirEntry(name, null));
        }

        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates != null && ln.lnType() != null && !ln.lnType().isEmpty()) {
            SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
            if (lnt != null) {
                for (SclDO doDef : lnt.dos()) {
                    if (!seen.contains(doDef.name())) {
                        entries.add(new DirEntry(doDef.name(), null));
                        seen.add(doDef.name());
                    }
                }
            }
        }

        return entries;
    }

    /**
     * Collect DA/SDI names at DO level: merge instance DAIs/SDIs with type template
     * DAs/SDOs.
     */
    private static List<DirEntry> collectDoDirectory(SclDocument doc, SclDOI doi, SclLN ln) {
        Set<String> seen = new HashSet<>();
        List<DirEntry> entries = new ArrayList<>();

        String doName = doi.name();

        // Instance DAIs (如有实例值，优先列出）
        for (SclDAI dai : doi.dais()) {
            String daName = dai.name();
            seen.add(daName);
            String fc = resolveDaFc(doc, ln, doName, daName);
            entries.add(new DirEntry(daName, fc));
        }

        // Instance SDIs
        for (SclSDI sdi : doi.sdis()) {
            String sdiName = sdi.name();
            seen.add(sdiName);
            entries.add(new DirEntry(sdiName, null));
        }

        // Type template DAs/SDOs not present in instance
        addTemplateDirs(doc, ln, doName, seen, entries);

        return entries;
    }

    /** DO 级别目录：仅从模板收集（当 DOI 为 null 时兜底）。 */
    private static List<DirEntry> collectDoDirectoryFromTemplate(SclDocument doc, SclLN ln, String doName) {
        List<DirEntry> entries = new ArrayList<>();
        addTemplateDirs(doc, ln, doName, new HashSet<>(), entries);
        return entries;
    }

    /** 从 DOType 模板追加 DA/SDO 目录条目（跳过已存在的）。 */
    private static void addTemplateDirs(SclDocument doc, SclLN ln, String doName, Set<String> seen, List<DirEntry> entries) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return;
        SclDOType doType = TypeChain.of(templates).from(ln.lnType()).doDef(doName).doType();
        if (doType == null)
            return;
        for (SclDA da : doType.das()) {
            if (!seen.contains(da.name())) {
                seen.add(da.name());
                entries.add(new DirEntry(da.name(), da.fc()));
            }
        }
        for (SclSDO sdo : doType.sdos()) {
            if (!seen.contains(sdo.name())) {
                seen.add(sdo.name());
                entries.add(new DirEntry(sdo.name(), null));
            }
        }
    }

    /**
     * 收集 SDO 级别的目录：穿透到 SDO 的 DOType 中返回 DA 列表。 如 ref=LD/LN.DO.SDO，则返回 SDO 对应的
     * DOType 中的 DA。
     *
     * @return 条目列表，若 sdoName 不是有效的 SDO 则返回 null
     */
    private static List<DirEntry> collectSdoDirectory(SclDocument doc, SclLN ln, String doName, String sdoName) {
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
        List<DirEntry> entries = new ArrayList<>();
        for (SclDA da : sdoType.das()) {
            entries.add(new DirEntry(da.name(), da.fc()));
        }
        return entries;
    }

    /** Resolve FC for a DA name from the DOType. */
    private static String resolveDaFc(SclDocument doc, SclLN ln, String doName, String daName) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates == null || ln.lnType() == null)
            return null;
        TypeChain.DaStep daStep = TypeChain.of(templates).from(ln.lnType()).doDef(doName).daDef(daName);
        return daStep != null ? daStep.fc() : null;
    }

    /** Compute starting index for referenceAfter pagination. */
    private static int afterIndex(List<DirEntry> entries, String after) {
        if (after == null || after.isEmpty())
            return 0;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).ref.equals(after))
                return i + 1;
        }
        return -1;
    }
}
