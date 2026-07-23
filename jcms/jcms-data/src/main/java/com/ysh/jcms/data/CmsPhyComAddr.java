// Auto-generated. ASN.1 type: PhyComAddr

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * PhyComAddr ::= SEQUENCE {
 *     addr        [0] IMPLICIT OCTET STRING (SIZE(6)),
 *     priority    [1] IMPLICIT Int8U,
 *     vid         [2] IMPLICIT Int16U,
 *     appid       [3] IMPLICIT Int16U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsPhyComAddr extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] addr = null;
    @JsonProperty public int priority = 0;
    @JsonProperty public int vid = 0;
    @JsonProperty public int appid = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("PhyComAddr", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsPhyComAddr decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("PhyComAddr", enc, data), CmsPhyComAddr.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
