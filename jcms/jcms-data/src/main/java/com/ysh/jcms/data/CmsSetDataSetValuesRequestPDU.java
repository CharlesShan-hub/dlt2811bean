// Auto-generated. ASN.1 type: SetDataSetValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetDataSetValues-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     value               [2] IMPLICIT SEQUENCE OF Data
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsSetDataSetValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String dataset_reference = null;
    @JsonProperty public String reference_after = null;
    @JsonProperty public java.util.List<CmsData> value = new java.util.ArrayList<>();
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetDataSetValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetDataSetValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetDataSetValuesRequestPDU", enc, data), CmsSetDataSetValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
