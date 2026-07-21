// Auto-generated. ASN.1 type: AnonymousReportPDUEntryEntryData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within ReportPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousReportPDUEntryEntryData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String fc = null;
    @JsonProperty public int id = 0;
    @JsonProperty public CmsData value = null;
    @JsonProperty public Integer reason = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousReportPDUEntryEntryData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousReportPDUEntryEntryData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousReportPDUEntryEntryData", enc, data), CmsAnonymousReportPDUEntryEntryData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
