package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.choice.CmsDataDefinitionStructElem;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.svc.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.svc.directory.CmsGetAllDataDefinitionError;
import com.ysh.jcms.svc.directory.CmsGetAllDataDefinitionRequest;
import com.ysh.jcms.svc.directory.CmsGetAllDataDefinitionResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final Map<String, Integer> FC_MAP = new HashMap<>();
    static {
        FC_MAP.put("ST", CmsFC.ST);
        FC_MAP.put("MX", CmsFC.MX);
        FC_MAP.put("SP", CmsFC.SP);
        FC_MAP.put("SV", CmsFC.SV);
        FC_MAP.put("CF", CmsFC.CF);
        FC_MAP.put("DC", CmsFC.DC);
        FC_MAP.put("SG", CmsFC.SG);
        FC_MAP.put("SE", CmsFC.SE);
        FC_MAP.put("SR", CmsFC.SR);
        FC_MAP.put("OR", CmsFC.OR);
        FC_MAP.put("BL", CmsFC.BL);
        FC_MAP.put("EX", CmsFC.EX);
        FC_MAP.put("XX", CmsFC.XX);
    }

    public AllDataDefServer() {
        super(ServiceName.GET_ALL_DATA_DEFINITION, CmsGetAllDataDefinitionRequest.class, CmsGetAllDataDefinitionError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetAllDataDefinitionRequest req = (CmsGetAllDataDefinitionRequest) rawReq;
        int reqId = req.reqId.value();
        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        log.info("GetAllDataDefinition from {}: reqId={}", session.getSessionId(), reqId);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice.value() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.len > 0
                ? new String(req.reference.altLdName.value(), StandardCharsets.UTF_8) : null;
        } else if (req.reference.choice.value() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = req.reference.altLnReference.len > 0
                ? new String(req.reference.altLnReference.value(), StandardCharsets.UTF_8) : null;
        }

        List<SclLN> lns = server.resolveLns(ldName, lnReference);
        if (lns == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Collect DO definitions
        List<CmsDataDefinitionEntry> entries = new ArrayList<>();
        int pageSize = pageSize();
        outer:
        for (SclLN ln : lns) {
            if (templates == null || ln.getLnType() == null) continue;
            SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
            if (lnt == null) continue;

            for (SclDO doDef : lnt.getDos()) {
                String doName = doDef.getName();

                // referenceAfter pagination
                if (refAfter != null) {
                    if (doName.equals(refAfter)) {
                        refAfter = null;
                    }
                    continue;
                }

                CmsDataDefinition def = buildDoDefinition(templates, doDef, ln);
                if (def == null) continue;

                // Resolve CDC from DOType
                SclDOType doType = doDef.getType() != null ? templates.findDoTypeById(doDef.getType()) : null;
                String cdc = doType != null ? doType.getCdc() : null;

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
        resp.data.allocSize = pageSize;
        for (CmsDataDefinitionEntry e : entries) {
            resp.data.add(e);
        }
        resp.moreFollows(entries.size() >= pageSize());

        log.info("GetAllDataDefinition: returning {} entries", entries.size());

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetAllDataDefinitionResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsDataDefinition buildDoDefinition(SclDataTypeTemplates templates, SclDO doDef, SclLN ln) {
        if (doDef.getType() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.getType());
        if (doType == null) return null;

        CmsArray<CmsDataDefinitionStructElem> arr = new CmsArray<>();
        arr.allocSize = Math.max(doType.getDas().size() + doType.getSdos().size(), 1);
        for (SclDA da : doType.getDas()) {
            String bType = da.getBType();
            if (bType == null) bType = "BOOLEAN";
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem()
                .name(da.getName())
                .fc(da.getFc() != null ? FC_MAP.getOrDefault(da.getFc(), 0) : 0)
                .type(bTypeToDataDefinition(bType));
            arr.add(elem);
        }
        for (SclSDO sdo : doType.getSdos()) {
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem()
                .name(sdo.getName());
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

    private static int pageSize() {
        return CmsConfigLoader.load().getProtocol().getMaxArraySize();
    }
}
