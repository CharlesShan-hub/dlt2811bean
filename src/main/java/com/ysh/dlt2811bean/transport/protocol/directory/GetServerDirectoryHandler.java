package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GetServerDirectoryHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_SERVER_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetServerDirectory asdu = (CmsGetServerDirectory) request.getAsdu();

        // §8.3.1.2 c: objectClass must be logical-device
        if (asdu.objectClass.get() != CmsObjectClass.LOGICAL_DEVICE) {
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<SclIED.SclLDevice> lDevices = accessPoint.getServer().getLDevices();

        // §8.3.1.2 a/b: handle referenceAfter
        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        int startIndex = 0;
        if (after != null && !after.isEmpty()) {
            boolean found = false;
            for (int i = 0; i < lDevices.size(); i++) {
                if (lDevices.get(i).getInst().equals(after)) {
                    startIndex = i + 1;
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.warn("[Server] referenceAfter not found: {}", after);
                return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        CmsArray<CmsObjectReference> refs = new CmsArray<>(CmsObjectReference::new).capacity(100);
        for (int i = startIndex; i < lDevices.size(); i++) {
            refs.add(new CmsObjectReference(lDevices.get(i).getInst()));
        }

        CmsGetServerDirectory response = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(refs);
        response.moreFollows.set(false);

        log.info("[Server] GetServerDirectory: {} entries", refs.size());
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsGetServerDirectory request, int errorCode) {
        CmsGetServerDirectory response = new CmsGetServerDirectory(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get());
        response.serviceError.set(errorCode);
        return new CmsApdu(response);
    }
}