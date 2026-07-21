// Auto-generated. ASN.1 type: AnonymousSetDataValuesRequestPDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetDataValues-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetDataValuesRequestPDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String fc = null;
    @JsonProperty public CmsData value = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetDataValuesRequestPDUData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetDataValuesRequestPDUData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetDataValuesRequestPDUData", enc, data), CmsAnonymousSetDataValuesRequestPDUData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
