package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.scl2.model.SclDataDefinitionEntry;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDefinition;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDefinitionEntry;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetDataDefinitionHandler extends AbstractCmsServiceHandler<CmsGetDataDefinition> {

    private static final int MAX_ENTRIES_PER_RESPONSE = 50;

    public GetDataDefinitionHandler() {
        super(ServiceName.GET_DATA_DEFINITION, CmsGetDataDefinition::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        SclDataTypeTemplates templates = sclDocument != null ? sclDocument.getDataTypeTemplates() : null;

        CmsArray<CmsGetDataDefinitionEntry> definitions = new CmsArray<>(CmsGetDataDefinitionEntry::new);
        int processedCount = 0;

        for (int i = 0; i < asdu.data.size() && processedCount < MAX_ENTRIES_PER_RESPONSE; i++) {
            CmsGetDataValuesEntry entry = asdu.data.get(i);
            String ref = entry.reference.get();
            String fc = entry.fc.get();

            if (ref == null || ref.isEmpty()) {
                definitions.add(buildErrorEntry());
                processedCount++;
                continue;
            }

            SclDataDefinitionEntry resolved = server.resolveDataDefinition(ref, fc, templates);
            if (resolved != null) {
                CmsGetDataDefinitionEntry defEntry = new CmsGetDataDefinitionEntry()
                        .cdcType(resolved.cdcType())
                        .definition(resolved.definition());
                definitions.add(defEntry);
            } else {
                definitions.add(buildErrorEntry());
            }
            processedCount++;
        }

        boolean moreFollows = processedCount < asdu.data.size();

        CmsGetDataDefinition response = new CmsGetDataDefinition(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.definition = definitions;
        response.moreFollows.set(moreFollows);

        log.debug("[Server] GetDataDefinition: {} entries, moreFollows={}", definitions.size(), moreFollows);
        return new CmsApdu(response);
    }

    private static CmsGetDataDefinitionEntry buildErrorEntry() {
        CmsDataDefinition def = new CmsDataDefinition();
        def.set(CmsDataDefinition.ERROR);
        return new CmsGetDataDefinitionEntry().definition(def);
    }
}
