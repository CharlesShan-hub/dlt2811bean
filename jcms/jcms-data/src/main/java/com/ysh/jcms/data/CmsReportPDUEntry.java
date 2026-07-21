// Auto-generated. ASN.1 type: ReportPDUEntry

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsReportPDUEntry extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] time_of_entry = null;
    @JsonProperty public byte[] entry_id = null;
    @JsonProperty public java.util.List<CmsAnonymousReportPDUEntryEntryData> entry_data = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("ReportPDUEntry", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsReportPDUEntry decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("ReportPDUEntry", enc, data), CmsReportPDUEntry.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
