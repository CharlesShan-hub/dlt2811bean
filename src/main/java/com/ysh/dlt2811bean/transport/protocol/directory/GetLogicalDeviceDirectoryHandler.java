package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GetLogicalDeviceDirectoryHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_LOGIC_DEVICE_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Resolve ldName: if not specified, read all LDevices
        String ldName = asdu.ldName != null ? asdu.ldName.get() : null;
        List<SclIED.SclLDevice> targetDevices = new ArrayList<>();

        if (ldName != null && !ldName.isEmpty()) {
            // §8.3.2.2 a: specified ldName — find that specific LDevice
            SclIED.SclLDevice device = findLDevice(accessPoint, ldName);
            if (device == null) {
                log.warn("[Server] LDevice not found: {}", ldName);
                return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            targetDevices.add(device);
        } else {
            // §8.3.2.2 a: no ldName — read all logical devices
            targetDevices.addAll(accessPoint.getServer().getLDevices());
        }

        // Collect logical node references
        List<String> lnRefs = new ArrayList<>();
        for (SclIED.SclLDevice device : targetDevices) {
            SclIED.SclLN0 ln0 = device.getLn0();
            if (ln0 != null) {
                lnRefs.add(buildLnReference(ln0.getLnClass(), ln0.getInst()));
            }
            for (SclIED.SclLN ln : device.getLns()) {
                lnRefs.add(buildLnReference(ln.getLnClass(), ln.getInst()));
            }
        }

        // Handle referenceAfter pagination (§8.3.2.2 b/c)
        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        int startIndex = 0;
        if (after != null && !after.isEmpty()) {
            for (int i = 0; i < lnRefs.size(); i++) {
                if (lnRefs.get(i).equals(after)) {
                    startIndex = i + 1;
                    break;
                }
            }
        }

        CmsArray<CmsSubReference> refs = new CmsArray<>(CmsSubReference::new).capacity(100);
        for (int i = startIndex; i < lnRefs.size(); i++) {
            refs.add(new CmsSubReference(lnRefs.get(i)));
        }

        CmsGetLogicalDeviceDirectory response = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .lnReference(refs);
        response.moreFollows.set(false);

        log.info("[Server] GetLogicalDeviceDirectory: {} entries", refs.size());
        return new CmsApdu(response);
    }

    private String buildLnReference(String lnClass, String inst) {
        return lnClass + inst;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclAccessPoint accessPoint, String ldName) {
        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getInst().equals(ldName)) {
                return ld;
            }
        }
        return null;
    }

    private CmsApdu buildNegativeResponse(CmsGetLogicalDeviceDirectory request, int errorCode) {
        CmsGetLogicalDeviceDirectory response = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get());
        response.serviceError.set(errorCode);
        return new CmsApdu(response);
    }
}