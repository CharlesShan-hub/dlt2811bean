// Auto-generated. ASN.1 type: TimeQuality

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * TimeQuality ::= BIT STRING {
 *     leap-second-known            (0),
 *     clock-failure                (1),
 *     clock-not-synchronized       (2)
 * } (SIZE(8))
 * }</pre>
 */
@Data
public class CmsTimeQuality extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsTimeQuality() {}
    public CmsTimeQuality(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("TimeQuality", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsTimeQuality decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("TimeQuality", enc, data);
            CmsTimeQuality r = new CmsTimeQuality();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
