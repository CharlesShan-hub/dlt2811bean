package com.ysh.dlt2811bean.service.testutil;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.AsduFactory;

public final class AsduTestUtil {

    private AsduTestUtil() {}

    @SuppressWarnings("unchecked")
    public static <T extends CmsAsdu<T>> T roundTripViaApdu(CmsAsdu<T> request) throws Exception {
        CmsApdu apdu = new CmsApdu(request);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));
        return (T) decoded.getAsdu();
    }

    @SuppressWarnings("unchecked")
    public static <T extends CmsAsdu<T>> T roundTripViaAsdu(CmsAsdu<T> asdu) throws Exception {
        PerOutputStream pos = new PerOutputStream();
        asdu.encode(pos);
        T decoded = (T) AsduFactory.create(
            asdu.getServiceName(),
            asdu.messageType().isResponse(),
            asdu.messageType().isError());
        decoded.decode(new PerInputStream(pos.toByteArray()));
        return decoded;
    }

    public static byte[] encodeApdu(CmsAsdu<?> asdu) {
        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        return pos.toByteArray();
    }

    public static CmsApdu decodeApdu(byte[] data) throws Exception {
        return new CmsApdu().decode(new PerInputStream(data));
    }

    public static byte[] encodeAsdu(CmsAsdu<?> asdu) {
        PerOutputStream pos = new PerOutputStream();
        asdu.encode(pos);
        return pos.toByteArray();
    }
}
