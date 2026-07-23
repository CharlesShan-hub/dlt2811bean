// Auto-generated. ASN.1 type: AnonymousSetGoCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetGoCBValues-ErrorPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetGoCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Integer error = null;
    @JsonProperty public Integer go_ena = null;
    @JsonProperty public Integer go_id = null;
    @JsonProperty public Integer dat_set = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetGoCBValuesErrorPDUResult", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetGoCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetGoCBValuesErrorPDUResult", enc, data), CmsAnonymousSetGoCBValuesErrorPDUResult.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
