package com.ysh.dlt2811bean.transport.protocol.test;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

/**
 * Handler for Test service (SC=4).
 *
 * <p>Test is used for keep-alive. Responds positively with an empty ASDU.
 */
public class TestHandler extends AbstractCmsServiceHandler<CmsTest> {
    
    public TestHandler() {
        super(ServiceName.TEST, CmsTest::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsTest response = new CmsTest(MessageType.RESPONSE_POSITIVE);
        log.debug("Test received");
        return new CmsApdu(response);
    }
}