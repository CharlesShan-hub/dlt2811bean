// Auto-generated. ASN.1 type: DataDefinition

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * DataDefinition ::= CHOICE {
 *     error              [0] IMPLICIT ServiceError,
 *     array              [1] IMPLICIT SEQUENCE {
 *         numberOfElement  [1] IMPLICIT Int32,
 *         elementType      [2] DataDefinition
 *     },
 *     structure          [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *         name             [0] IMPLICIT ObjectName,
 *         fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *         type             [2] DataDefinition
 *     },
 *     boolean            [3] IMPLICIT NULL,
 *     int8               [4] IMPLICIT NULL,
 *     int16              [5] IMPLICIT NULL,
 *     int32              [6] IMPLICIT NULL,
 *     int64              [7] IMPLICIT NULL,
 *     int8u              [8] IMPLICIT NULL,
 *     int16u             [9] IMPLICIT NULL,
 *     int32u             [10] IMPLICIT NULL,
 *     int64u             [11] IMPLICIT NULL,
 *     float32            [12] IMPLICIT NULL,
 *     float64            [13] IMPLICIT NULL,
 *     bit-string         [14] IMPLICIT INTEGER,
 *     octet-string       [15] IMPLICIT INTEGER,
 *     visible-string     [16] IMPLICIT INTEGER,
 *     unicode-string     [17] IMPLICIT INTEGER,
 *     utc-time           [18] IMPLICIT NULL,
 *     binary-time        [19] IMPLICIT NULL,
 *     quality            [20] IMPLICIT NULL,
 *     dbpos              [21] IMPLICIT NULL,
 *     tcmd               [22] IMPLICIT NULL,
 *     check              [23] IMPLICIT NULL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsDataDefinition extends CmsBase {
    public String _choice;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonIgnore public int error;
    @JsonIgnore public CmsDataDefinitionArray array;
    @JsonIgnore public java.util.List<CmsAnonymousDataDefinitionStructure> structure;
    @JsonIgnore public Object _boolean;
    @JsonIgnore public Object int8;
    @JsonIgnore public Object int16;
    @JsonIgnore public Object int32;
    @JsonIgnore public Object int64;
    @JsonIgnore public Object int8u;
    @JsonIgnore public Object int16u;
    @JsonIgnore public Object int32u;
    @JsonIgnore public Object int64u;
    @JsonIgnore public Object float32;
    @JsonIgnore public Object float64;
    @JsonIgnore public int bit_string;
    @JsonIgnore public int octet_string;
    @JsonIgnore public int visible_string;
    @JsonIgnore public int unicode_string;
    @JsonIgnore public Object utc_time;
    @JsonIgnore public Object binary_time;
    @JsonIgnore public Object quality;
    @JsonIgnore public Object dbpos;
    @JsonIgnore public Object tcmd;
    @JsonIgnore public Object check;
    @JsonAnyGetter
    public java.util.Map<String, Object> serializeChoice() {
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        if (_choice != null) {
            map.put("_choice", _choice);
            if ("error".equals(_choice)) map.put("error", error);
            if ("array".equals(_choice)) map.put("array", array);
            if ("structure".equals(_choice)) map.put("structure", structure);
            if ("boolean".equals(_choice)) map.put("boolean", _boolean);
            if ("int8".equals(_choice)) map.put("int8", int8);
            if ("int16".equals(_choice)) map.put("int16", int16);
            if ("int32".equals(_choice)) map.put("int32", int32);
            if ("int64".equals(_choice)) map.put("int64", int64);
            if ("int8u".equals(_choice)) map.put("int8u", int8u);
            if ("int16u".equals(_choice)) map.put("int16u", int16u);
            if ("int32u".equals(_choice)) map.put("int32u", int32u);
            if ("int64u".equals(_choice)) map.put("int64u", int64u);
            if ("float32".equals(_choice)) map.put("float32", float32);
            if ("float64".equals(_choice)) map.put("float64", float64);
            if ("bit_string".equals(_choice)) map.put("bit_string", bit_string);
            if ("octet_string".equals(_choice)) map.put("octet_string", octet_string);
            if ("visible_string".equals(_choice)) map.put("visible_string", visible_string);
            if ("unicode_string".equals(_choice)) map.put("unicode_string", unicode_string);
            if ("utc_time".equals(_choice)) map.put("utc_time", utc_time);
            if ("binary_time".equals(_choice)) map.put("binary_time", binary_time);
            if ("quality".equals(_choice)) map.put("quality", quality);
            if ("dbpos".equals(_choice)) map.put("dbpos", dbpos);
            if ("tcmd".equals(_choice)) map.put("tcmd", tcmd);
            if ("check".equals(_choice)) map.put("check", check);
        }
        return map;
    }
    @JsonAnySetter
    public void deserializeChoice(String key, Object value) {
        if ("_choice".equals(key)) return;
        this._choice = key;
        if ("error".equals(key)) {
            this.error = MAPPER.convertValue(value, int.class);
        }
        if ("array".equals(key)) {
            this.array = MAPPER.convertValue(value, CmsDataDefinitionArray.class);
        }
        if ("structure".equals(key)) {
            this.structure = MAPPER.convertValue(value, new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousDataDefinitionStructure>>() {});
        }
        if ("boolean".equals(key)) {
            this._boolean = MAPPER.convertValue(value, Object.class);
        }
        if ("int8".equals(key)) {
            this.int8 = MAPPER.convertValue(value, Object.class);
        }
        if ("int16".equals(key)) {
            this.int16 = MAPPER.convertValue(value, Object.class);
        }
        if ("int32".equals(key)) {
            this.int32 = MAPPER.convertValue(value, Object.class);
        }
        if ("int64".equals(key)) {
            this.int64 = MAPPER.convertValue(value, Object.class);
        }
        if ("int8u".equals(key)) {
            this.int8u = MAPPER.convertValue(value, Object.class);
        }
        if ("int16u".equals(key)) {
            this.int16u = MAPPER.convertValue(value, Object.class);
        }
        if ("int32u".equals(key)) {
            this.int32u = MAPPER.convertValue(value, Object.class);
        }
        if ("int64u".equals(key)) {
            this.int64u = MAPPER.convertValue(value, Object.class);
        }
        if ("float32".equals(key)) {
            this.float32 = MAPPER.convertValue(value, Object.class);
        }
        if ("float64".equals(key)) {
            this.float64 = MAPPER.convertValue(value, Object.class);
        }
        if ("bit_string".equals(key)) {
            this.bit_string = MAPPER.convertValue(value, int.class);
        }
        if ("octet_string".equals(key)) {
            this.octet_string = MAPPER.convertValue(value, int.class);
        }
        if ("visible_string".equals(key)) {
            this.visible_string = MAPPER.convertValue(value, int.class);
        }
        if ("unicode_string".equals(key)) {
            this.unicode_string = MAPPER.convertValue(value, int.class);
        }
        if ("utc_time".equals(key)) {
            this.utc_time = MAPPER.convertValue(value, Object.class);
        }
        if ("binary_time".equals(key)) {
            this.binary_time = MAPPER.convertValue(value, Object.class);
        }
        if ("quality".equals(key)) {
            this.quality = MAPPER.convertValue(value, Object.class);
        }
        if ("dbpos".equals(key)) {
            this.dbpos = MAPPER.convertValue(value, Object.class);
        }
        if ("tcmd".equals(key)) {
            this.tcmd = MAPPER.convertValue(value, Object.class);
        }
        if ("check".equals(key)) {
            this.check = MAPPER.convertValue(value, Object.class);
        }
    }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("DataDefinition", enc, MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDataDefinition decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("DataDefinition", enc, data), CmsDataDefinition.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
