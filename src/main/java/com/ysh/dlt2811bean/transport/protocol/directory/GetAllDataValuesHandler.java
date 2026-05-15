package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl2.model.SclDataValue;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.util.SclFilters;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetAllDataValuesHandler extends AbstractCmsServiceHandler<CmsGetAllDataValues> {

    public GetAllDataValuesHandler() {
        super(ServiceName.GET_ALL_DATA_VALUES, CmsGetAllDataValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        // resolve reference param(logic device or logic node under logic node)
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

        // get data values under each logic node
        SclDataTypeTemplates templates = sclDocument != null ? sclDocument.getDataTypeTemplates() : null;
        List<SclDataValue> values = new ArrayList<>();
        for (SclLN ln : targets) {
            values.addAll(ln.collectDataValues(templates, fcFilter, !useLdName));
        }

        // referenceAfter pagination
        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        List<SclDataValue> filtered = SclFilters.filterAfter(values, after, SclDataValue::ref);
        if (filtered == null && after != null && !after.isEmpty()) {
            log.warn("[Server] referenceAfter not found: {}", after);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        if (filtered != null) values = filtered;

        // build positive response
        CmsArray<CmsDataEntry> data = new CmsArray<>(CmsDataEntry::new).capacity(Math.max(1, values.size()));
        for (SclDataValue dv : values) {
            CmsType<?> typedValue = SclTypeResolver.createTypedValue(dv.bType(), dv.val());
            CmsDataEntry entry = new CmsDataEntry()
                    .reference(dv.ref())
                    .value(typedValue);
            data.add(entry);
        }

        CmsGetAllDataValues response = new CmsGetAllDataValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .data(data);
        response.moreFollows.set(false);

        log.debug("[Server] GetAllDataValues: {} entries{}", data.size(),
                fcFilter != null ? " (fc=" + fcFilter + ")" : "");
        return new CmsApdu(response);
    }
}
