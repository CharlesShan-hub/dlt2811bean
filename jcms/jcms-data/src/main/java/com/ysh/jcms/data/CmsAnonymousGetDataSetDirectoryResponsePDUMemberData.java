// Auto-generated. ASN.1 type: AnonymousGetDataSetDirectoryResponsePDUMemberData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetDataSetDirectory-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousGetDataSetDirectoryResponsePDUMemberData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String fc = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetDataSetDirectoryResponsePDUMemberData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetDataSetDirectoryResponsePDUMemberData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetDataSetDirectoryResponsePDUMemberData", enc, data), CmsAnonymousGetDataSetDirectoryResponsePDUMemberData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
