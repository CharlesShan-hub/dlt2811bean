// Auto-generated. ASN.1 type: LogEntryEntryData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsLogEntryEntryData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousLogEntryEntryData> value;
    public CmsLogEntryEntryData() {}
    public CmsLogEntryEntryData(java.util.List<CmsAnonymousLogEntryEntryData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("LogEntryEntryData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsLogEntryEntryData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("LogEntryEntryData", enc, data);
            CmsLogEntryEntryData r = new CmsLogEntryEntryData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousLogEntryEntryData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
