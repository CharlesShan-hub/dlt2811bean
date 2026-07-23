// Auto-generated. ASN.1 type: GetEditSGValueErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetEditSGValue-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetEditSGValueErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetEditSGValueErrorPDU() {}
    public CmsGetEditSGValueErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetEditSGValueErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetEditSGValueErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetEditSGValueErrorPDU", enc, data);
            CmsGetEditSGValueErrorPDU r = new CmsGetEditSGValueErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
