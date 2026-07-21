// Auto-generated. ASN.1 type: TimeStamp

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * TimeStamp ::= UtcTime
 * }</pre>
 */
@Data
public class CmsTimeStamp extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] value;
    public CmsTimeStamp() {}
    public CmsTimeStamp(byte[] value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("TimeStamp", enc, java.util.Base64.getEncoder().encodeToString(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsTimeStamp decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("TimeStamp", enc, data);
            CmsTimeStamp r = new CmsTimeStamp();
            r.value = java.util.Base64.getDecoder().decode(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
