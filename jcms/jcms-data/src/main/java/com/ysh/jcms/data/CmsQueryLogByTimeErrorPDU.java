// Auto-generated. ASN.1 type: QueryLogByTimeErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * QueryLogByTime-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsQueryLogByTimeErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsQueryLogByTimeErrorPDU() {}
    public CmsQueryLogByTimeErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("QueryLogByTimeErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsQueryLogByTimeErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("QueryLogByTimeErrorPDU", enc, data);
            CmsQueryLogByTimeErrorPDU r = new CmsQueryLogByTimeErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
