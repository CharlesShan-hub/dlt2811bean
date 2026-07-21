// Auto-generated. ASN.1 type: ControlCode

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ControlCode ::= SEQUENCE {
 *     next        BOOLEAN,
 *     resp        BOOLEAN,
 *     err         BOOLEAN,
 *     pi          Int8U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsControlCode extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public boolean next = false;
    @JsonProperty public boolean resp = false;
    @JsonProperty public boolean err = false;
    @JsonProperty public int pi = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("ControlCode", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsControlCode decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("ControlCode", enc, data), CmsControlCode.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
