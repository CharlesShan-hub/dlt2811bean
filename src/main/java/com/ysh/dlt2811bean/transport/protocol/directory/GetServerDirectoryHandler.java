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
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.List;

public class GetServerDirectoryHandler extends AbstractCmsServiceHandler<CmsGetServerDirectory> {

    public GetServerDirectoryHandler() {
        super(ServiceName.GET_SERVER_DIRECTORY, CmsGetServerDirectory::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        if (asdu.objectClass.get() != CmsObjectClass.LOGICAL_DEVICE) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        List<String> ldNames = server.getLDevices().stream()
            .map(SclIED.SclLDevice::getInst)
            .toList();

        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        int startIndex = 0;
        if (after != null && !after.isEmpty()) {
            startIndex = ldNames.indexOf(after);
            if (startIndex < 0) {
                log.warn("[Server] referenceAfter not found: {}", after);
                return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            startIndex++;
        }

        CmsArray<CmsObjectReference> refs = new CmsArray<>(CmsObjectReference::new);
        for (int i = startIndex; i < ldNames.size(); i++) {
            refs.add(new CmsObjectReference(ldNames.get(i)));
        }

        CmsGetServerDirectory response = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(refs);
        response.moreFollows.set(false);

        log.debug("[Server] GetServerDirectory: {} entries", refs.size());
        return new CmsApdu(response);
    }
}