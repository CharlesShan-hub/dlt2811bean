// Auto-generated. ASN.1 type: QueryLogAfterErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * QueryLogAfter-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsQueryLogAfterErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsQueryLogAfterErrorPDU() {}
    public CmsQueryLogAfterErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("QueryLogAfterErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsQueryLogAfterErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("QueryLogAfterErrorPDU", enc, data);
            CmsQueryLogAfterErrorPDU r = new CmsQueryLogAfterErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
