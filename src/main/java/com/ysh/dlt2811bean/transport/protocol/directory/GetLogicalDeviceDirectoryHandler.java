package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetLogicalDeviceDirectoryHandler extends AbstractCmsServiceHandler<CmsGetLogicalDeviceDirectory> {

    public GetLogicalDeviceDirectoryHandler() {
        super(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, CmsGetLogicalDeviceDirectory::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String ldName = asdu.ldName != null ? asdu.ldName.get() : null;
        List<SclIED.SclLDevice> targetDevices = new ArrayList<>();

        if (ldName != null && !ldName.isEmpty()) {
            SclIED.SclLDevice device = findLDevice(accessPoint, ldName);
            if (device == null) {
                log.warn("[Server] LDevice not found: {}", ldName);
                return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            targetDevices.add(device);
        } else {
            targetDevices.addAll(server.getLDevices());
        }

        List<String> lnRefs = new ArrayList<>();
        for (SclIED.SclLDevice device : targetDevices) {
            SclIED.SclLN0 ln0 = device.getLn0();
            if (ln0 != null) {
                lnRefs.add(buildLnReference("", ln0.getLnClass(), ln0.getInst()));
            }
            for (SclIED.SclLN ln : device.getLns()) {
                lnRefs.add(buildLnReference(ln.getPrefix(), ln.getLnClass(), ln.getInst()));
            }
        }

        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        int startIndex = 0;
        if (after != null && !after.isEmpty()) {
            boolean found = false;
            for (int i = 0; i < lnRefs.size(); i++) {
                if (lnRefs.get(i).equals(after)) {
                    startIndex = i + 1;
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.warn("[Server] referenceAfter not found: {}", after);
                return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        CmsArray<CmsSubReference> refs = new CmsArray<>(CmsSubReference::new);
        for (int i = startIndex; i < lnRefs.size(); i++) {
            refs.add(new CmsSubReference(lnRefs.get(i)));
        }

        CmsGetLogicalDeviceDirectory response = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .lnReference(refs);
        response.moreFollows.set(false);

        log.debug("[Server] GetLogicalDeviceDirectory: {} entries", refs.size());
        return new CmsApdu(response);
    }

    private String buildLnReference(String prefix, String lnClass, String inst) {
        return (prefix == null || prefix.isEmpty()) ? lnClass + inst : prefix + lnClass + inst;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclAccessPoint accessPoint, String ldName) {
        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getInst().equals(ldName)) {
                return ld;
            }
        }
        return null;
    }
}