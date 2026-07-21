// Auto-generated. ASN.1 type: ConfirmEditSGValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ConfirmEditSGValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsConfirmEditSGValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsConfirmEditSGValuesErrorPDU() {}
    public CmsConfirmEditSGValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("ConfirmEditSGValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsConfirmEditSGValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("ConfirmEditSGValuesErrorPDU", enc, data);
            CmsConfirmEditSGValuesErrorPDU r = new CmsConfirmEditSGValuesErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
