package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.scl2.util.SclFilters;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetLogicalNodeDirectoryHandler extends AbstractCmsServiceHandler<CmsGetLogicalNodeDirectory> {

    public GetLogicalNodeDirectoryHandler() {
        super(ServiceName.GET_LOGIC_NODE_DIRECTORY, CmsGetLogicalNodeDirectory::new);
    }

    @Override
    protected CmsApdu doServerHandle() {
        
        // resolve logic node
        List<SclLN> lns = resolveLns();
        if (lns == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // resolve data object under each logic node
        List<String> entries = new ArrayList<>();
        for (SclLN ln : lns) {
            List<String> collected = collectEntries(ln, asdu.acsiClass.get());
            if (collected == null) {
                return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
            entries.addAll(collected);
        }

        // resolve after
        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        List<String> filtered = SclFilters.filterAfter(entries, after);
        if (filtered == null) {
            log.warn("[Server] referenceAfter not found: {}", after);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // build positive response
        CmsArray<CmsSubReference> refs = new CmsArray<>(CmsSubReference::new);
        for (String name : filtered) {
            refs.add(new CmsSubReference(name));
        }

        CmsGetLogicalNodeDirectory response = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .referenceResponse(refs);
        response.moreFollows.set(false);

        log.debug("[Server] GetLogicalNodeDirectory: {} entries (acsiClass={})", refs.size(), asdu.acsiClass.get());
        return new CmsApdu(response);
    }

    private List<SclLN> resolveLns() {
        boolean useLdName = asdu.referenceRequest.getSelectedIndex() == 0;
        String ldName = useLdName ? asdu.referenceRequest.ldName.get() : null;
        String lnRef = useLdName ? null : asdu.referenceRequest.lnReference.get();
        List<SclLN> lns = server.resolveLns(ldName, lnRef);
        if (lns == null) {
            log.warn("[Server] LN not found: ldName={}, lnReference={}", ldName, lnRef);
        }
        return lns;
    }

    private List<String> collectEntries(SclLN ln, int acsiClass) {
        return switch (acsiClass) {
            case CmsACSIClass.DATA_OBJECT -> ln.getDataObjectNames(sclDocument.getDataTypeTemplates());
            case CmsACSIClass.DATA_SET -> ln.getDataSetNames();
            case CmsACSIClass.BRCB -> ln.getReportControlNames(true);
            case CmsACSIClass.URCB -> ln.getReportControlNames(false);
            case CmsACSIClass.LCB -> ln.getLogControlNames();
            case CmsACSIClass.LOG -> ln.getLogNames();
            case CmsACSIClass.GO_CB -> ln.getGseControlNames();
            case CmsACSIClass.MSV_CB -> ln.getSvControlNames();
            case CmsACSIClass.SGCB -> List.of("SG1");
            default -> null;
        };
    }
}