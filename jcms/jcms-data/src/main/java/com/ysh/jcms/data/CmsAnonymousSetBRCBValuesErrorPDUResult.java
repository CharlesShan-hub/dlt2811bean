// Auto-generated. ASN.1 type: AnonymousSetBRCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetBRCBValues-ErrorPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetBRCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Integer error = null;
    @JsonProperty public Integer rpt_id = null;
    @JsonProperty public Integer rpt_ena = null;
    @JsonProperty public Integer dat_set = null;
    @JsonProperty public Integer opt_flds = null;
    @JsonProperty public Integer buf_tm = null;
    @JsonProperty public Integer trg_ops = null;
    @JsonProperty public Integer intg_pd = null;
    @JsonProperty public Integer gi = null;
    @JsonProperty public Integer purge_buf = null;
    @JsonProperty public Integer entry_id = null;
    @JsonProperty public Integer resv_tms = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetBRCBValuesErrorPDUResult", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetBRCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetBRCBValuesErrorPDUResult", enc, data), CmsAnonymousSetBRCBValuesErrorPDUResult.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
