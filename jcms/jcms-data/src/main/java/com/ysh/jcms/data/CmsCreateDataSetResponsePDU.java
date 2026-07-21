// Auto-generated. ASN.1 type: CreateDataSetResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * CreateDataSet-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsCreateDataSetResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsCreateDataSetResponsePDU() {}
    public CmsCreateDataSetResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("CreateDataSetResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsCreateDataSetResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("CreateDataSetResponsePDU", enc, data);
            CmsCreateDataSetResponsePDU r = new CmsCreateDataSetResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
