package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * Root interface for all CMS services.
 *
 * <p>Each service class is itself a {@link CmsAsdu}, so encoding/decoding
 * is done directly via the {@link CmsType} interface. The frame-level
 * encoding (APCH + ASDU) is handled by {@link CmsApdu}.
 *
 * <p>Usage:
 * <pre>{@code
 * // Encode
 * CmsAssociate service = new CmsAssociate(MessageType.REQUEST)
 *     .setServerAccessPointReference(...);
 * CmsApdu<CmsAssociate> apdu = new CmsApdu<>(service)
 *     .withServiceCode(service.getServiceCode())
 *     .withMessageType(MessageType.REQUEST);
 * byte[] frame = apdu.encode();
 *
 * // Decode
 * CmsApdu<CmsAssociate> apdu = new CmsApdu<>(new CmsAssociate()).decode(frame);
 * CmsAssociate service = apdu.getAsdu();
 * }</pre>
 */
public interface CmsService {

    /**
     * Get the service code identifying this service type.
     *
     * @return the service code
     */
    ServiceCode getServiceCode();
}
