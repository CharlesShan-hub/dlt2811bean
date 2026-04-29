package com.ysh.dlt2811bean.service.svc.test;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CMS Service Code 0x99 — Test (test service).
 *
 * Corresponds to Section 8.14 in GB/T 45906.3-2025: Test service parameters.
 *
 * Service code: 0x99 (153)
 * Service interface: Test
 * Category: Test service
 *
 * The Test service is used to verify communication connectivity between two endpoints.
 * It tests whether the link is operational and whether the other party can correctly
 * respond to requests. Upon receiving a Test frame, the receiver should immediately
 * return a Test frame. The Test service contains only the APCH header with no ASDU
 * portion, and the frame length (FL) is 0.
 *
 * This class supports two message types:
 * <ul>
 *   <li>REQUEST - Test request to check communication connectivity</li>
 *   <li>RESPONSE_POSITIVE - Test response confirming the link is operational</li>
 * </ul>
 * Note: The Test service does not have a negative response (RESPONSE_NEGATIVE) as
 * it is a simple connectivity test.
 *
 * ASDU field layout:
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ (No ASDU data, FL=0)                                         │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ (No ASDU data, FL=0)                                         │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * APCH Header structure for Test service:
 * <pre>
 * ┌──────────────────────────────────────────────────────────────┐
 * │ Protocol ID (PI=0x01, 1B)                                    │
 * │ Service Code (SC=0x99, 1B)                                   │
 * │ Frame Length (FL=0x0000, 2B)                                 │
 * │ Request ID (ReqID, 2B)                                       │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsTest extends CmsAsdu<CmsTest> {

    public CmsTest(){
        super(MessageType.REQUEST); // MessageType is not used
        removeField("reqId"); // test api there is no "reqId"
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.TEST;
    }

    @Override
    public CmsTest copy() {
        return new CmsTest();
    }

    @Override
    public String toString() {
        return "(CmsTest) {}"; // no fields and no new line
    }

    @SuppressWarnings("unchecked")
    public static CmsTest read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsTest) new CmsTest().decode(pis);
    }

    public static void write(PerOutputStream pos, CmsTest test) {
        test.encode(pos);
    }
}