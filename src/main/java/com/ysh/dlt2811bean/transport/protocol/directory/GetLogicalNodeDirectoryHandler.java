package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
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

    private final SclDataTypeTemplates templates;

    public GetLogicalNodeDirectoryHandler(SclDataTypeTemplates templates) {
        super(ServiceName.GET_LOGIC_NODE_DIRECTORY, CmsGetLogicalNodeDirectory::new);
        this.templates = templates;
    }

    @Override
    protected CmsApdu doServerHandle() {
        
        // resolve logic node
        List<SclLN> lns = resolveLns();
        if (lns == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<String> entries = new ArrayList<>();
        for (SclLN ln : lns) {
            List<String> collected = collectEntries(ln, asdu.acsiClass.get());
            if (collected == null) {
                return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
            entries.addAll(collected);
        }

        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        List<String> filtered = filterAfter(entries, after);
        if (filtered == null) {
            log.warn("[Server] referenceAfter not found: {}", after);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

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

        String ldName = null;
        String lnRef = null;
        boolean useLdName = asdu.referenceRequest.getSelectedIndex() == 0;
        if (useLdName) {
            ldName = asdu.referenceRequest.ldName.get();
        } else {
            lnRef = asdu.referenceRequest.lnReference.get();
        }

        if (ldName != null && !ldName.isEmpty()) {
            SclLDevice device = server.findLDeviceByInst(ldName);
            if (device == null) {
                log.warn("[Server] LDevice not found: {}", ldName);
                return null;
            }
            return device.getLns();
        }
        if (lnRef == null || lnRef.isEmpty()) {
            log.warn("[Server] No ldName or lnReference provided");
            return null;
        }
        int slashIdx = lnRef.indexOf('/');
        if (slashIdx < 0) {
            log.warn("[Server] Invalid lnReference (no '/'): {}", lnRef);
            return null;
        }
        SclLDevice device = server.findLDeviceByInst(lnRef.substring(0, slashIdx));
        if (device == null) {
            log.warn("[Server] LDevice not found: {}", lnRef.substring(0, slashIdx));
            return null;
        }
        SclLN ln = device.findLnByFullName(lnRef.substring(slashIdx + 1));
        if (ln == null) {
            log.warn("[Server] LN not found: {}", lnRef);
            return null;
        }
        return List.of(ln);
    }

    private List<String> collectEntries(SclLN ln, int acsiClass) {
        return switch (acsiClass) {
            case CmsACSIClass.DATA_OBJECT -> ln.getDataObjectNames(templates);
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

    private List<String> filterAfter(List<String> entries, String after) {
        if (after == null || after.isEmpty()) return entries;
        int idx = entries.indexOf(after);
        if (idx < 0) return null;
        return entries.subList(idx + 1, entries.size());
    }
}
