// Auto-generated. ASN.1 type: AnonymousSetGoCBValuesRequestPDUGocb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetGoCBValues-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetGoCBValuesRequestPDUGocb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public Boolean go_ena = null;
    @JsonProperty public String go_id = null;
    @JsonProperty public String dat_set = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetGoCBValuesRequestPDUGocb", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetGoCBValuesRequestPDUGocb decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetGoCBValuesRequestPDUGocb", enc, data), CmsAnonymousSetGoCBValuesRequestPDUGocb.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
