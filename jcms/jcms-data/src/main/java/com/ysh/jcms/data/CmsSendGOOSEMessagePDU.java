// Auto-generated. ASN.1 type: SendGOOSEMessagePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SendGOOSEMessage-PDU ::= SEQUENCE {
 *     goID            [0] IMPLICIT VisibleString (SIZE (0..129)),
 *     datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *     goRef           [2] IMPLICIT ObjectReference OPTIONAL,
 *     t               [3] IMPLICIT TimeStamp,
 *     stNum           [4] IMPLICIT Int32U,
 *     sqNum           [5] IMPLICIT Int32U,
 *     simulation      [6] IMPLICIT BOOLEAN,
 *     confRev         [7] IMPLICIT Int32U,
 *     ndsCom          [8] IMPLICIT BOOLEAN,
 *     data            [9] IMPLICIT SEQUENCE OF Data
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsSendGOOSEMessagePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String go_id = null;
    @JsonProperty public String dat_set = null;
    @JsonProperty public String go_ref = null;
    @JsonProperty public byte[] t = null;
    @JsonProperty public int st_num = 0;
    @JsonProperty public int sq_num = 0;
    @JsonProperty public boolean simulation = false;
    @JsonProperty public int conf_rev = 0;
    @JsonProperty public boolean nds_com = false;
    @JsonProperty public java.util.List<CmsData> data = new java.util.ArrayList<>();
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SendGOOSEMessagePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSendGOOSEMessagePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SendGOOSEMessagePDU", enc, data), CmsSendGOOSEMessagePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
