package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.List;

public class GetLogicalDeviceDirectoryHandler extends AbstractCmsServiceHandler<CmsGetLogicalDeviceDirectory> {

    public GetLogicalDeviceDirectoryHandler() {
        super(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, CmsGetLogicalDeviceDirectory::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String ldName = resolveLdName();
        String referenceAfter = resolveReferenceAfter();

        List<String> lnNames;
        if (ldName != null && !ldName.isEmpty()) {
            SclLDevice device = server.findLDeviceByInst(ldName);
            if (device == null) {
                log.warn("[Server] LDevice not found: {}", ldName);
                return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            lnNames = device.getLnNames(referenceAfter);
        } else {
            lnNames = server.getAllLnNames(referenceAfter);
        }

        if (lnNames == null) {
            log.warn("[Server] referenceAfter not found: {}", referenceAfter);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsSubReference> refs = new CmsArray<>(CmsSubReference::new);
        for (String name : lnNames) {
            refs.add(new CmsSubReference(name));
        }

        CmsGetLogicalDeviceDirectory response = new CmsGetLogicalDeviceDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .lnReference(refs);
        response.moreFollows.set(false);

        log.debug("[Server] GetLogicalDeviceDirectory: {} entries", refs.size());
        return new CmsApdu(response);
    }

    private String resolveLdName(){
        return asdu.ldName != null ? asdu.ldName.get() : null;
    }

    private String resolveReferenceAfter(){
        return asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
    }
}