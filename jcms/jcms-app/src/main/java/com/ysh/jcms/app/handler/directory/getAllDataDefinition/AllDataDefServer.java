package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.choice.CmsDataDefinitionStructElem;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.svc.directory.CmsGetAllDataDefinitionError;
import com.ysh.jcms.svc.directory.CmsGetAllDataDefinitionRequest;
import com.ysh.jcms.svc.directory.CmsGetAllDataDefinitionResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AllDataDefServer extends BaseServerHandler {

    private static final int SEL_STRUCTURE      = 2;
    private static final int SEL_BOOLEAN        = 3;
    private static final int SEL_INT8           = 4;
    private static final int SEL_INT16          = 5;
    private static final int SEL_INT32          = 6;
    private static final int SEL_INT64          = 7;
    private static final int SEL_INT8U          = 8;
    private static final int SEL_INT16U         = 9;
    private static final int SEL_INT32U         = 10;
    private static final int SEL_INT64U         = 11;
    private static final int SEL_FLOAT32        = 12;
    private static final int SEL_FLOAT64        = 13;
    private static final int SEL_BIT_STRING     = 14;
    private static final int SEL_VISIBLE_STRING = 16;
    private static final int SEL_UNICODE_STRING = 17;
    private static final int SEL_QUALITY        = 18;
    private static final int SEL_UTC_TIME       = 19;
    private static final int SEL_BINARY_TIME    = 20;
    private static final int SEL_DBPOS          = 21;
    private static final int SEL_TCMD           = 22;
    private static final int SEL_CHECK          = 23;

    public AllDataDefServer() {
        super(ServiceName.GET_ALL_DATA_DEFINITION, CmsGetAllDataDefinitionRequest.class, CmsGetAllDataDefinitionError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetAllDataDefinitionRequest req = (CmsGetAllDataDefinitionRequest) rawReq;
        int reqId = req.reqId.value();
        String refAfter = opt(req.refAfterPresent, req.refAfter);

        log.info("GetAllDataDefinition from {}: reqId={}", session.getSessionId(), reqId);

        SclDocument doc = getScl2Document(session);
        if (doc == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = doc.dataTypeTemplates();

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice.value() == CmsReferenceChoice.LD_NAME) {
            ldName = str(req.reference.altLdName);
        } else if (req.reference.choice.value() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = str(req.reference.altLnReference);
        }

        List<SclLN> lns = resolveLns(doc, ldName, lnReference);
        if (lns == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Resolve fc filter from request
        String fcFilter = null;
        if (req.fcPresent.value()) {
            int fcVal = req.fc.value();
            if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                fcFilter = FunctionalConstraint.values()[fcVal].name();
                if ("XX".equals(fcFilter)) fcFilter = null;
            }
        }

        // Collect DO definitions
        List<CmsDataDefinitionEntry> entries = new ArrayList<>();
        int pageSize = pageSize();
        outer:
        for (SclLN ln : lns) {
            if (templates == null || ln.lnType() == null) continue;
            SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
            if (lnt == null) continue;

            for (SclDO doDef : lnt.dos()) {
                String doName = doDef.name();

                // referenceAfter pagination
                if (refAfter != null) {
                    if (doName.equals(refAfter)) {
                        refAfter = null;
                    }
                    continue;
                }

                // fc filter: skip DO if no DA in its DOType matches the requested fc
                if (fcFilter != null) {
                    SclDOType doType = doDef.type() != null ? templates.findDoTypeById(doDef.type()) : null;
                    if (doType == null) continue;
                    boolean hasFc = false;
                    for (SclDA da : doType.das()) {
                        if (fcFilter.equalsIgnoreCase(da.fc())) {
                            hasFc = true;
                            break;
                        }
                    }
                    if (!hasFc) continue;
                }

                CmsDataDefinition def = buildDoDefinition(templates, doDef);
                if (def == null) continue;

                // Resolve CDC from DOType
                SclDOType doType2 = doDef.type() != null ? templates.findDoTypeById(doDef.type()) : null;
                String cdc = doType2 != null ? doType2.cdc() : null;

                CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry()
                    .reference(doName);
                if (cdc != null) entry.cdcType(cdc);
                entry.definition = def;
                entries.add(entry);

                if (entries.size() >= pageSize) break outer;
            }
        }

        CmsGetAllDataDefinitionResponse resp = new CmsGetAllDataDefinitionResponse()
            .reqId(reqId);
        for (CmsDataDefinitionEntry e : entries) {
            resp.data.add(e);
        }
        resp.moreFollows(entries.size() >= pageSize());

        log.info("GetAllDataDefinition: returning {} entries", entries.size());
        return ok(resp, reqId);
    }

    private static List<SclLN> resolveLns(SclDocument doc, String ldName, String lnReference) {
        List<SclLN> result = new ArrayList<>();
        if (ldName != null && !ldName.isEmpty()) {
            for (SclIED ied : doc.ieds()) {
                for (SclAccessPoint ap : ied.accessPoints()) {
                    SclServer srv = ap.server();
                    if (srv != null) {
                        SclLDevice device = srv.findLDeviceByInst(ldName);
                        if (device != null) {
                            result.addAll(device.lns());
                            return result;
                        }
                    }
                }
            }
            return null;
        }
        if (lnReference == null || lnReference.isEmpty()) return null;
        int slashIdx = lnReference.indexOf('/');
        if (slashIdx < 0) return null;
        String refLd = lnReference.substring(0, slashIdx);
        String refLn = lnReference.substring(slashIdx + 1);
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    SclLDevice device = srv.findLDeviceByInst(refLd);
                    if (device != null) {
                        SclLN ln = device.findLnByFullName(refLn);
                        if (ln != null) {
                            result.add(ln);
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    private CmsDataDefinition buildDoDefinition(SclDataTypeTemplates templates, SclDO doDef) {
        if (doDef.type() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.type());
        if (doType == null) return null;

        CmsArray<CmsDataDefinitionStructElem> arr = new CmsArray<>();
        for (SclDA da : doType.das()) {
            String bType = da.bType();
            if (bType == null) bType = "BOOLEAN";
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem()
                .name(da.name())
                .fc(da.fc() != null ? CmsFC.fromCode(da.fc()) : 0)
                .type(bTypeToDataDefinition(bType));
            arr.add(elem);
        }
        for (SclSDO sdo : doType.sdos()) {
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem()
                .name(sdo.name());
            elem.type(nullDataDefinition());
            arr.add(elem);
        }

        CmsDataDefinition def = new CmsDataDefinition();
        def.choice(SEL_STRUCTURE);
        def.alt_structure = arr;
        return def;
    }

    private static CmsDataDefinition nullDataDefinition() {
        return new CmsDataDefinition().choice(SEL_BOOLEAN);
    }

    private static CmsDataDefinition bTypeToDataDefinition(String bType) {
        if (bType == null) return nullDataDefinition();
        switch (bType.toUpperCase()) {
            case "BOOLEAN":      return new CmsDataDefinition().choice(SEL_BOOLEAN);
            case "INT8":         return new CmsDataDefinition().choice(SEL_INT8);
            case "INT16":        return new CmsDataDefinition().choice(SEL_INT16);
            case "INT32":        return new CmsDataDefinition().choice(SEL_INT32);
            case "INT64":        return new CmsDataDefinition().choice(SEL_INT64);
            case "INT8U":        return new CmsDataDefinition().choice(SEL_INT8U);
            case "INT16U":       return new CmsDataDefinition().choice(SEL_INT16U);
            case "INT32U":       return new CmsDataDefinition().choice(SEL_INT32U);
            case "INT64U":       return new CmsDataDefinition().choice(SEL_INT64U);
            case "FLOAT32":      return new CmsDataDefinition().choice(SEL_FLOAT32);
            case "FLOAT64":      return new CmsDataDefinition().choice(SEL_FLOAT64);
            case "BIT_STRING":
            case "BITSTRING": {
                CmsDataDefinition def = new CmsDataDefinition().choice(SEL_BIT_STRING);
                def.alt_bit_string_len.value(0);
                return def;
            }
            case "OCTET_STRING":
            case "OCTETSTRING":
            case "VISSTRING255":
            case "VISIBLE_STRING": {
                CmsDataDefinition def = new CmsDataDefinition().choice(SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-255);
                return def;
            }
            case "UNICODE_STRING":
            case "UNICODESTRING":
            case "UNICODE255": {
                CmsDataDefinition def = new CmsDataDefinition().choice(SEL_UNICODE_STRING);
                def.alt_unicode_string_len.value(-255);
                return def;
            }
            case "UTC_TIME":
            case "UTCTIME":
            case "TIMESTAMP":    return new CmsDataDefinition().choice(SEL_UTC_TIME);
            case "BINARY_TIME":
            case "BINARYTIME":
            case "ENTRYTIME":    return new CmsDataDefinition().choice(SEL_BINARY_TIME);
            case "QUALITY":      return new CmsDataDefinition().choice(SEL_QUALITY);
            case "DBPOS":        return new CmsDataDefinition().choice(SEL_DBPOS);
            case "TCMD":         return new CmsDataDefinition().choice(SEL_TCMD);
            case "CHECK":        return new CmsDataDefinition().choice(SEL_CHECK);
            case "VISSTRING64": {
                CmsDataDefinition def = new CmsDataDefinition().choice(SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-64);
                return def;
            }
            case "STRUCT":       return new CmsDataDefinition().choice(SEL_BOOLEAN);
            default:             return nullDataDefinition();
        }
    }
}
