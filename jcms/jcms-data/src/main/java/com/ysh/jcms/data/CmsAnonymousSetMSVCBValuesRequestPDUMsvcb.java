// Auto-generated. ASN.1 type: AnonymousSetMSVCBValuesRequestPDUMsvcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetMSVCBValues-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetMSVCBValuesRequestPDUMsvcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public Boolean sv_ena = null;
    @JsonProperty public String msv_id = null;
    @JsonProperty public String dat_set = null;
    @JsonProperty public Integer smp_mod = null;
    @JsonProperty public Integer smp_rate = null;
    @JsonProperty public Integer opt_flds = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetMSVCBValuesRequestPDUMsvcb", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetMSVCBValuesRequestPDUMsvcb decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetMSVCBValuesRequestPDUMsvcb", enc, data), CmsAnonymousSetMSVCBValuesRequestPDUMsvcb.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
