// Auto-generated. ASN.1 type: AnonymousSetLCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetLCBValues-ErrorPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetLCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Integer error = null;
    @JsonProperty public Integer log_ena = null;
    @JsonProperty public Integer dat_set = null;
    @JsonProperty public Integer trg_ops = null;
    @JsonProperty public Integer intg_pd = null;
    @JsonProperty public Integer log_ref = null;
    @JsonProperty public Integer opt_flds = null;
    @JsonProperty public Integer buf_tm = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetLCBValuesErrorPDUResult", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetLCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetLCBValuesErrorPDUResult", enc, data), CmsAnonymousSetLCBValuesErrorPDUResult.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
