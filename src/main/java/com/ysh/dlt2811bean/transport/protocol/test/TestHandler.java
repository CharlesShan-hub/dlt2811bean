package com.ysh.dlt2811bean.transport.protocol.test;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for Test service (SC=4).
 *
 * <p>Test is used for keep-alive. Responds positively with an empty ASDU.
 */
public class TestHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(TestHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.TEST;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsTest response = new CmsTest(MessageType.RESPONSE_POSITIVE);
        log.debug("Test received");
        return new CmsApdu(response);
    }
}