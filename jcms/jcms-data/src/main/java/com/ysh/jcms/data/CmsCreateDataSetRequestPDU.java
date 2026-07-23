// Auto-generated. ASN.1 type: CreateDataSetRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * CreateDataSet-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     memberData          [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference       [0] IMPLICIT ObjectReference,
 *         fc              [1] IMPLICIT FunctionalConstraint
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsCreateDataSetRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String dataset_reference = null;
    @JsonProperty public String reference_after = null;
    @JsonProperty public java.util.List<CmsAnonymousCreateDataSetRequestPDUMemberData> member_data = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("CreateDataSetRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsCreateDataSetRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("CreateDataSetRequestPDU", enc, data), CmsCreateDataSetRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
