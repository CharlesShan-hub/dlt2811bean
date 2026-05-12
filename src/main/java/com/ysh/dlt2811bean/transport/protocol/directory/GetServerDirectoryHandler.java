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
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import lombok.extern.slf4j.Slf4j;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.List;

public class GetServerDirectoryHandler extends AbstractCmsServiceHandler<CmsGetServerDirectory> {

    public GetServerDirectoryHandler() {
        super(ServiceName.GET_SERVER_DIRECTORY, CmsGetServerDirectory::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetServerDirectory asdu = (CmsGetServerDirectory) request.getAsdu();

        if (asdu.objectClass.get() != CmsObjectClass.LOGICAL_DEVICE) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<SclIED.SclLDevice> lDevices = accessPoint.getServer().getLDevices();

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
                return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        CmsArray<CmsObjectReference> refs = new CmsArray<>(CmsObjectReference::new);
        for (int i = startIndex; i < lDevices.size(); i++) {
            refs.add(new CmsObjectReference(lDevices.get(i).getInst()));
        }

        CmsGetServerDirectory response = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(refs);
        response.moreFollows.set(false);

        log.debug("[Server] GetServerDirectory: {} entries", refs.size());
        return new CmsApdu(response);
    }
}