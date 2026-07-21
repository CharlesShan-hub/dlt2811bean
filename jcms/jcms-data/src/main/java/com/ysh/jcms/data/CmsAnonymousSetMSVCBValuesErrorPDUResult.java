// Auto-generated. ASN.1 type: AnonymousSetMSVCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetMSVCBValues-ErrorPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetMSVCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Integer error = null;
    @JsonProperty public Integer sv_ena = null;
    @JsonProperty public Integer msv_id = null;
    @JsonProperty public Integer dat_set = null;
    @JsonProperty public Integer smp_mod = null;
    @JsonProperty public Integer smp_rate = null;
    @JsonProperty public Integer opt_flds = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetMSVCBValuesErrorPDUResult", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetMSVCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetMSVCBValuesErrorPDUResult", enc, data), CmsAnonymousSetMSVCBValuesErrorPDUResult.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
