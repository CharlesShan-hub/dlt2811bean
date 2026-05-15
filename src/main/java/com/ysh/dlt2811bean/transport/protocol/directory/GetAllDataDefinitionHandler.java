package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclDataDefinitionEntry;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.util.SclFilters;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetAllDataDefinitionHandler extends AbstractCmsServiceHandler<CmsGetAllDataDefinition> {

    public GetAllDataDefinitionHandler() {
        super(ServiceName.GET_ALL_DATA_DEFINITION, CmsGetAllDataDefinition::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        // resolve reference param
        boolean useLdName = asdu.reference.getSelectedIndex() == 0;
        String ldName = useLdName ? asdu.reference.ldName.get() : null;
        String lnRef = useLdName ? null : asdu.reference.lnReference.get();
        log.debug("[Server] reference: ldName='{}', lnReference='{}'", ldName, lnRef);

        // resolve fc param
        String fcFilter = asdu.fc != null ? asdu.fc.get() : null;
        if (fcFilter != null && (fcFilter.isEmpty() || "XX".equals(fcFilter))) {
            fcFilter = null;
        }

        // resolve logic node list
        List<SclLN> targets = server.resolveLns(ldName, lnRef);
        if (targets == null) {
            log.warn("[Server] LN not found: ldName={}, lnReference={}", ldName, lnRef);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // get data definitions under each logic node
        SclDataTypeTemplates templates = sclDocument != null ? sclDocument.getDataTypeTemplates() : null;
        List<SclDataDefinitionEntry> entries = new ArrayList<>();
        for (SclLN ln : targets) {
            entries.addAll(ln.collectDataDefinitions(templates, fcFilter, !useLdName));
        }

        // referenceAfter pagination
        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        List<SclDataDefinitionEntry> filtered = SclFilters.filterAfter(entries, after, SclDataDefinitionEntry::ref);
        if (filtered == null && after != null && !after.isEmpty()) {
            log.warn("[Server] referenceAfter not found: {}", after);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        if (filtered != null) entries = filtered;

        // build positive response
        CmsArray<CmsDataDefinitionEntry> data = new CmsArray<>(CmsDataDefinitionEntry::new)
                .capacity(Math.max(1, entries.size()));
        for (SclDataDefinitionEntry de : entries) {
            CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry()
                    .reference(de.ref())
                    .cdcType(de.cdcType())
                    .definition(de.definition());
            data.add(entry);
        }

        CmsGetAllDataDefinition response = new CmsGetAllDataDefinition(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .data(data);
        response.moreFollows.set(false);

        log.debug("[Server] GetAllDataDefinition: {} entries{}", data.size(),
                fcFilter != null ? " (fc=" + fcFilter + ")" : "");
        return new CmsApdu(response);
    }
}
