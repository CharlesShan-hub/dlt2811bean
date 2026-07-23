// Auto-generated. ASN.1 type: AnonymousCreateDataSetRequestPDUMemberData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within CreateDataSet-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousCreateDataSetRequestPDUMemberData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String fc = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousCreateDataSetRequestPDUMemberData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousCreateDataSetRequestPDUMemberData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousCreateDataSetRequestPDUMemberData", enc, data), CmsAnonymousCreateDataSetRequestPDUMemberData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
