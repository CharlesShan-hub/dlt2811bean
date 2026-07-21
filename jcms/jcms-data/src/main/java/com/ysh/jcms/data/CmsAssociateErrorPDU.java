// Auto-generated. ASN.1 type: AssociateErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Associate-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsAssociateErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsAssociateErrorPDU() {}
    public CmsAssociateErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("AssociateErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAssociateErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("AssociateErrorPDU", enc, data);
            CmsAssociateErrorPDU r = new CmsAssociateErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
