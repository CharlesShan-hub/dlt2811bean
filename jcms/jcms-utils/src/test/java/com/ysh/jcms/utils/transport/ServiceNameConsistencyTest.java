package com.ysh.jcms.utils.transport;

import com.ysh.jcms.info.CmsServiceInfo;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Guards against service-code drift between the wire-level {@link ServiceName}
 * (jcms-utils transport) and the info-layer {@link CmsServiceInfo} (jcms-core).
 * Both derive from standard Table 1; they drifted apart once before (2026-08).
 */
public class ServiceNameConsistencyTest {

    @Test
    public void everyCmsServiceInfoCodeHasServiceName() {
        for (CmsServiceInfo s : CmsServiceInfo.values()) {
            int code = s.serviceCode();
            if (code == 0) continue; // unconfirmed services have no standard code
            assertNotNull("No ServiceName for CmsServiceInfo." + s.name()
                    + " (0x" + Integer.toHexString(code) + ")", ServiceName.fromCode(code));
        }
    }

    @Test
    public void everyServiceNameCodeHasCmsServiceInfo() {
        for (ServiceName sn : ServiceName.values()) {
            int code = sn.getCode();
            if (code == 0) continue; // placeholder, not a real wire code
            assertNotNull("No CmsServiceInfo for ServiceName." + sn.name()
                    + " (0x" + Integer.toHexString(code) + ")", CmsServiceInfo.byCode(code));
        }
    }
}
