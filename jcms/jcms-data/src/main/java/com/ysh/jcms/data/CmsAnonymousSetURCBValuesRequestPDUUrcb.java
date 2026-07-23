// Auto-generated. ASN.1 type: AnonymousSetURCBValuesRequestPDUUrcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetURCBValues-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousSetURCBValuesRequestPDUUrcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String rpt_id = null;
    @JsonProperty public Boolean rpt_ena = null;
    @JsonProperty public String dat_set = null;
    @JsonProperty public Integer opt_flds = null;
    @JsonProperty public Integer buf_tm = null;
    @JsonProperty public Integer trg_ops = null;
    @JsonProperty public Integer intg_pd = null;
    @JsonProperty public Boolean gi = null;
    @JsonProperty public Boolean resv = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetURCBValuesRequestPDUUrcb", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetURCBValuesRequestPDUUrcb decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetURCBValuesRequestPDUUrcb", enc, data), CmsAnonymousSetURCBValuesRequestPDUUrcb.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
