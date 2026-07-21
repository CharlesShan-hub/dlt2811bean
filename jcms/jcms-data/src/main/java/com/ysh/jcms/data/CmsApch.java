// Auto-generated. ASN.1 type: Apch

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Apch ::= SEQUENCE {
 *     cc          ControlCode,
 *     sc          Int8U,
 *     fl          Int16U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsApch extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public CmsControlCode cc = null;
    @JsonProperty public int sc = 0;
    @JsonProperty public int fl = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("Apch", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsApch decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("Apch", enc, data), CmsApch.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
