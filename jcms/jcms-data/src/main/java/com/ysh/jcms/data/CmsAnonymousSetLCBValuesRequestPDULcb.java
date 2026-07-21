// Auto-generated. ASN.1 type: AnonymousSetLCBValuesRequestPDULcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetLCBValues-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetLCBValuesRequestPDULcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public Boolean log_ena = null;
    @JsonProperty public String dat_set = null;
    @JsonProperty public Integer trg_ops = null;
    @JsonProperty public Integer intg_pd = null;
    @JsonProperty public String log_ref = null;
    @JsonProperty public Integer opt_flds = null;
    @JsonProperty public Integer buf_tm = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetLCBValuesRequestPDULcb", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetLCBValuesRequestPDULcb decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetLCBValuesRequestPDULcb", enc, data), CmsAnonymousSetLCBValuesRequestPDULcb.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
