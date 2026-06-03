package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
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

        // only support logical device (the document rule)
        if (asdu.objectClass.get() != CmsObjectClass.LOGICAL_DEVICE) {
            log.warn("[Server] objectClass not supported: {}", asdu.objectClass.get());
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        // get logical device names after referenceAfter
        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        List<String> ldNames = server.getLDeviceNames(after);
        if (ldNames == null) {
            log.warn("[Server] referenceAfter not found: {}", after);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        
        // build response
        CmsArray<CmsObjectReference> refs = new CmsArray<>(CmsObjectReference::new);
        for (String name : ldNames) {
            refs.add(new CmsObjectReference(name));
        }

        CmsGetServerDirectory response = new CmsGetServerDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(refs);
        response.moreFollows.set(false);

        log.debug("[Server] GetServerDirectory: {} entries", refs.size());
        return new CmsApdu(response);
    }
}