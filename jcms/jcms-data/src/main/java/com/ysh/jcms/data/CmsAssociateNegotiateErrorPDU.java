// Auto-generated. ASN.1 type: AssociateNegotiateErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * AssociateNegotiate-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsAssociateNegotiateErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsAssociateNegotiateErrorPDU() {}
    public CmsAssociateNegotiateErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("AssociateNegotiateErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAssociateNegotiateErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("AssociateNegotiateErrorPDU", enc, data);
            CmsAssociateNegotiateErrorPDU r = new CmsAssociateNegotiateErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
