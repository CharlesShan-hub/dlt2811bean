// Auto-generated. ASN.1 type: CreateDataSetErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * CreateDataSet-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsCreateDataSetErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsCreateDataSetErrorPDU() {}
    public CmsCreateDataSetErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("CreateDataSetErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsCreateDataSetErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("CreateDataSetErrorPDU", enc, data);
            CmsCreateDataSetErrorPDU r = new CmsCreateDataSetErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
