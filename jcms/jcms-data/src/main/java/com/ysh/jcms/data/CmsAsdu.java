// Auto-generated. ASN.1 type: Asdu

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Asdu ::= SEQUENCE {
 *     reqId       Int16U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAsdu extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int req_id = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("Asdu", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAsdu decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("Asdu", enc, data), CmsAsdu.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
