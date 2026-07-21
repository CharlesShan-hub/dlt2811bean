// Auto-generated. ASN.1 type: AnonymousGetGOOSEElementNumberRequestPDUMemberData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetGOOSEElementNumber-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String fc = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetGOOSEElementNumberRequestPDUMemberData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetGOOSEElementNumberRequestPDUMemberData", enc, data), CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
