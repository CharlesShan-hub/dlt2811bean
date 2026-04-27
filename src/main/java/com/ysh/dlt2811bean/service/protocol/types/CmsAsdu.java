package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * ASDU (Application Service Data Unit) — the payload of an APDU frame.
 *
 * <p>Subclasses implement {@link #encodeServiceData(PerOutputStream)} and
 * {@link #decodeServiceData(PerInputStream)} for their service-specific fields.
 * The encode/decode framework is handled by this base class.
 *
 * <p>For request-response services, subclasses should manage ReqID themselves
 * (see {@link AbstractCmsRR}).
 */
public abstract class CmsAsdu implements CmsType<CmsAsdu> {

    // ==================== Subclass Hooks ====================

    /**
     * Encode service-specific fields into the PER stream.
     */
    protected abstract void encodeServiceData(PerOutputStream pos);

    /**
     * Decode service-specific fields from the PER stream.
     */
    protected abstract void decodeServiceData(PerInputStream pis) throws Exception;

    /**
     * Create a copy of this ASDU, including all service-specific fields.
     */
    public abstract CmsAsdu copy();

    // ==================== CmsType ====================

    @Override
    public void encode(PerOutputStream pos) {
        encodeServiceData(pos);
    }

    @Override
    public CmsAsdu decode(PerInputStream pis) throws Exception {
        decodeServiceData(pis);
        return this;
    }
}
