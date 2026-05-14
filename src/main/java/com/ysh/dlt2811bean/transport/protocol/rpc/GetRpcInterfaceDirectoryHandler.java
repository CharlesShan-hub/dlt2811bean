package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDirectory;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.List;

public class GetRpcInterfaceDirectoryHandler extends AbstractCmsServiceHandler<CmsGetRpcInterfaceDirectory> {

    private static final List<String> INTERFACES = List.of("IF1", "IF2");

    public GetRpcInterfaceDirectoryHandler() {
        super(ServiceName.GET_RPC_INTERFACE_DIRECTORY, CmsGetRpcInterfaceDirectory::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String after = asdu.referenceAfter.get();
        int startIdx = 0;
        if (after != null && !after.isEmpty()) {
            int idx = INTERFACES.indexOf(after);
            if (idx >= 0) {
                startIdx = idx + 1;
            }
        }

        CmsGetRpcInterfaceDirectory response = new CmsGetRpcInterfaceDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());

        for (int i = startIdx; i < INTERFACES.size(); i++) {
            response.addReference(INTERFACES.get(i));
        }

        log.debug("[Server] GetRpcInterfaceDirectory: {} entries", response.reference.size());
        return new CmsApdu(response);
    }
}