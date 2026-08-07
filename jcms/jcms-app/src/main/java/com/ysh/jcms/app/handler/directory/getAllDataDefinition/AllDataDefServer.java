package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.common.CmsDataDefinitionStructElem;
import com.ysh.jcms.data.sequence.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.scl.convert.DataDefinitionResolver;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class AllDataDefServer extends BaseServerHandler<CmsGetAllDataDefinitionRequest, CmsGetAllDataDefinitionError> {

    public AllDataDefServer() {
        super(ServiceName.GET_ALL_DATA_DEFINITION, CmsGetAllDataDefinitionRequest.class, CmsGetAllDataDefinitionError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetAllDataDefinitionRequest req, int reqId) {
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        log.info("GetAllDataDefinition from {}: reqId={}", session.getSessionId(), reqId);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);
        SclDataTypeTemplates templates = doc.dataTypeTemplates();

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.value();
        } else if (req.reference.choice() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = str(req.reference.altLnReference);
        }

        List<SclLN> lns = Navigator.resolveLns(ied, ldName, lnReference);
        if (lns == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Resolve fc filter from request
        String fcFilter = fcCode(req.isPresent("fc") ? req.fc.value() : -1);

        // Collect DO definitions
        List<CmsDataDefinitionEntry> entries = new ArrayList<>();
        int pageSize = pageSize();
        outer : for (SclLN ln : lns) {
            if (templates == null || ln.lnType() == null)
                continue;
            SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
            if (lnt == null)
                continue;

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
                    if (doType == null)
                        continue;
                    boolean hasFc = false;
                    for (SclDA da : doType.das()) {
                        if (fcFilter.equalsIgnoreCase(da.fc())) {
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

                // Resolve CDC from DOType
                SclDOType doType2 = doDef.type() != null ? templates.findDoTypeById(doDef.type()) : null;
                String cdc = doType2 != null ? doType2.cdc() : null;

                CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry().reference(doName);
                if (cdc != null)
                    entry.cdcType(cdc);
                entry.definition = def;
                entries.add(entry);

                if (entries.size() >= pageSize)
                    break outer;
            }
        }

        CmsGetAllDataDefinitionResponse resp = new CmsGetAllDataDefinitionResponse();
        for (CmsDataDefinitionEntry e : entries) {
            resp.data.add(e);
        }
        resp.moreFollows(entries.size() >= pageSize());

        log.info("GetAllDataDefinition: returning {} entries", entries.size());
        return ok(resp, reqId);
    }

    private CmsDataDefinition buildDoDefinition(SclDataTypeTemplates templates, SclDO doDef) {
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

}
