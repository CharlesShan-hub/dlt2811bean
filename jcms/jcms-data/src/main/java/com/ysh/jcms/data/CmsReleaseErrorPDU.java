// Auto-generated. ASN.1 type: ReleaseErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Release-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsReleaseErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsReleaseErrorPDU() {}
    public CmsReleaseErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("ReleaseErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsReleaseErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("ReleaseErrorPDU", enc, data);
            CmsReleaseErrorPDU r = new CmsReleaseErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
