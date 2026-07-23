// Auto-generated. ASN.1 type: AssociateResponsePDUAuthenticationParameter

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAssociateResponsePDUAuthenticationParameter extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] signature_certificate = null;
    @JsonProperty public byte[] signed_time = null;
    @JsonProperty public byte[] signed_value = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AssociateResponsePDUAuthenticationParameter", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAssociateResponsePDUAuthenticationParameter decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AssociateResponsePDUAuthenticationParameter", enc, data), CmsAssociateResponsePDUAuthenticationParameter.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
