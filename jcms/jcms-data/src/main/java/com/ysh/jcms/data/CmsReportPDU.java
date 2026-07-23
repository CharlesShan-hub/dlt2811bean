// Auto-generated. ASN.1 type: ReportPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ReportPDU ::= SEQUENCE {
 *     rptID           [0] IMPLICIT VisibleString (SIZE (0..129)),
 *     optFlds         [1] IMPLICIT RcbOptFlds,
 *     sqNum           [2] IMPLICIT Int16U OPTIONAL,
 *     subSeqNum       [3] IMPLICIT Int16U OPTIONAL,
 *     moreSegmentsFollow [4] IMPLICIT BOOLEAN OPTIONAL,
 *     dataSet         [5] IMPLICIT ObjectReference OPTIONAL,
 *     bufOvfl         [6] IMPLICIT BOOLEAN OPTIONAL,
 *     confRev         [7] IMPLICIT Int32U OPTIONAL,
 *     entry           [8] IMPLICIT SEQUENCE {
 *         timeOfEntry     [0] IMPLICIT EntryTime OPTIONAL,
 *         entryID         [1] IMPLICIT EntryID OPTIONAL,
 *         entryData       [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *             reference   [0] IMPLICIT ObjectReference OPTIONAL,
 *             fc          [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *             id          [2] IMPLICIT Int16U,
 *             value       [3] IMPLICIT Data,
 *             reason      [4] IMPLICIT ReasonCode OPTIONAL
 *         }
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsReportPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String rpt_id = null;
    @JsonProperty public int opt_flds = 0;
    @JsonProperty public Integer sq_num = null;
    @JsonProperty public Integer sub_seq_num = null;
    @JsonProperty public Boolean more_segments_follow = null;
    @JsonProperty public String data_set = null;
    @JsonProperty public Boolean buf_ovfl = null;
    @JsonProperty public Integer conf_rev = null;
    @JsonProperty public CmsReportPDUEntry entry = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("ReportPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsReportPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("ReportPDU", enc, data), CmsReportPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
