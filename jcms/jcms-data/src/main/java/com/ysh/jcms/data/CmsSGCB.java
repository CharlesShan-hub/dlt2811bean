// Auto-generated. ASN.1 type: SGCB

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SGCB ::= SEQUENCE {
 *     numOfSG       [1] IMPLICIT Int8U,
 *     actSG         [2] IMPLICIT Int8U,
 *     editSG        [3] IMPLICIT Int8U,
 *     tActEdt       [4] IMPLICIT TimeStamp,
 *     resvTms       [5] IMPLICIT Int16U OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsSGCB extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int num_of_sg = 0;
    @JsonProperty public int act_sg = 0;
    @JsonProperty public int edit_sg = 0;
    @JsonProperty public byte[] t_act_edt = null;
    @JsonProperty public Integer resv_tms = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SGCB", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSGCB decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SGCB", enc, data), CmsSGCB.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
