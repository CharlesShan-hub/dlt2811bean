// Auto-generated. ASN.1 type: SelectEditSGErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SelectEditSG-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsSelectEditSGErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsSelectEditSGErrorPDU() {}
    public CmsSelectEditSGErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SelectEditSGErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSelectEditSGErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SelectEditSGErrorPDU", enc, data);
            CmsSelectEditSGErrorPDU r = new CmsSelectEditSGErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
