package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclCBEntry;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.util.SclFilters;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllCBValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValueEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetAllCBValuesHandler extends AbstractCmsServiceHandler<CmsGetAllCBValues> {

    public GetAllCBValuesHandler() {
        super(ServiceName.GET_ALL_CB_VALUES, CmsGetAllCBValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        // resolve reference param
        boolean useLdName = asdu.reference.getSelectedIndex() == 0;
        String ldName = useLdName ? asdu.reference.ldName.get() : null;
        String lnRef = useLdName ? null : asdu.reference.lnReference.get();
        log.debug("[Server] reference: ldName='{}', lnReference='{}'", ldName, lnRef);

        // resolve acsiClass param
        int acsiClass = asdu.acsiClass.get();

        // resolve logic node list (CBs are only under LLN0)
        List<SclLN> targets = server.resolveLns(ldName, lnRef);
        if (targets == null) {
            log.warn("[Server] LN not found: ldName={}, lnReference={}", ldName, lnRef);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // collect CB values from each target LN
        List<SclCBEntry> entries = new ArrayList<>();
        for (SclLN ln : targets) {
            entries.addAll(ln.collectCBValues(acsiClass));
        }

        // referenceAfter pagination
        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        List<SclCBEntry> filtered = SclFilters.filterAfter(entries, after, SclCBEntry::ref);
        if (filtered == null && after != null && !after.isEmpty()) {
            log.warn("[Server] referenceAfter not found: {}", after);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        if (filtered != null) entries = filtered;

        // build positive response
        CmsArray<CmsCBValueEntry> cbValue = new CmsArray<>(CmsCBValueEntry::new)
                .capacity(Math.max(1, entries.size()));
        for (SclCBEntry e : entries) {
            cbValue.add(new CmsCBValueEntry().reference(e.ref()).value(e.value()));
        }

        CmsGetAllCBValues response = new CmsGetAllCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .cbValue(cbValue);
        response.moreFollows.set(false);

        log.debug("[Server] GetAllCBValues: {} entries (acsiClass={})", cbValue.size(), acsiClass);
        return new CmsApdu(response);
    }
}
