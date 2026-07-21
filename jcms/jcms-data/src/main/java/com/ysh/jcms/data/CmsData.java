// Auto-generated. ASN.1 type: Data

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Data ::= CHOICE {
 *     error              [0] IMPLICIT ServiceError,
 *     array              [1] IMPLICIT SEQUENCE OF Data,
 *     structure          [2] IMPLICIT SEQUENCE OF Data,
 *     boolean            [3] IMPLICIT BOOLEAN,
 *     int8               [4] IMPLICIT Int8,
 *     int16              [5] IMPLICIT Int16,
 *     int32              [6] IMPLICIT Int32,
 *     int64              [7] IMPLICIT Int64,
 *     int8u              [8] IMPLICIT Int8U,
 *     int16u             [9] IMPLICIT Int16U,
 *     int32u             [10] IMPLICIT Int32U,
 *     int64u             [11] IMPLICIT Int64U,
 *     float32            [12] IMPLICIT Float32,
 *     float64            [13] IMPLICIT Float64,
 *     bit-string         [14] IMPLICIT BIT STRING,
 *     octet-string       [15] IMPLICIT OCTET STRING,
 *     visible-string     [16] IMPLICIT VisibleString,
 *     unicode-string     [17] IMPLICIT UTF8String,
 *     utc-time           [18] IMPLICIT UtcTime,
 *     binary-time        [19] IMPLICIT BinaryTime,
 *     quality            [20] IMPLICIT Quality,
 *     dbpos              [21] IMPLICIT Dbpos,
 *     tcmd               [22] IMPLICIT Tcmd,
 *     check              [23] IMPLICIT Check
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsData extends CmsBase {
    public String _choice;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonIgnore public int error;
    @JsonIgnore public java.util.List<CmsData> array;
    @JsonIgnore public java.util.List<CmsData> structure;
    @JsonIgnore public boolean _boolean;
    @JsonIgnore public int int8;
    @JsonIgnore public int int16;
    @JsonIgnore public int int32;
    @JsonIgnore public long int64;
    @JsonIgnore public int int8u;
    @JsonIgnore public int int16u;
    @JsonIgnore public int int32u;
    @JsonIgnore public long int64u;
    @JsonIgnore public byte[] float32;
    @JsonIgnore public byte[] float64;
    @JsonIgnore public byte[] bit_string;
    @JsonIgnore public byte[] octet_string;
    @JsonIgnore public String visible_string;
    @JsonIgnore public String unicode_string;
    @JsonIgnore public byte[] utc_time;
    @JsonIgnore public byte[] binary_time;
    @JsonIgnore public int quality;
    @JsonIgnore public int dbpos;
    @JsonIgnore public int tcmd;
    @JsonIgnore public int check;
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
            this.array = MAPPER.convertValue(value, new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsData>>() {});
        }
        if ("structure".equals(key)) {
            this.structure = MAPPER.convertValue(value, new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsData>>() {});
        }
        if ("boolean".equals(key)) {
            this._boolean = MAPPER.convertValue(value, boolean.class);
        }
        if ("int8".equals(key)) {
            this.int8 = MAPPER.convertValue(value, int.class);
        }
        if ("int16".equals(key)) {
            this.int16 = MAPPER.convertValue(value, int.class);
        }
        if ("int32".equals(key)) {
            this.int32 = MAPPER.convertValue(value, int.class);
        }
        if ("int64".equals(key)) {
            this.int64 = MAPPER.convertValue(value, long.class);
        }
        if ("int8u".equals(key)) {
            this.int8u = MAPPER.convertValue(value, int.class);
        }
        if ("int16u".equals(key)) {
            this.int16u = MAPPER.convertValue(value, int.class);
        }
        if ("int32u".equals(key)) {
            this.int32u = MAPPER.convertValue(value, int.class);
        }
        if ("int64u".equals(key)) {
            this.int64u = MAPPER.convertValue(value, long.class);
        }
        if ("float32".equals(key)) {
            this.float32 = MAPPER.convertValue(value, byte[].class);
        }
        if ("float64".equals(key)) {
            this.float64 = MAPPER.convertValue(value, byte[].class);
        }
        if ("bit_string".equals(key)) {
            this.bit_string = MAPPER.convertValue(value, byte[].class);
        }
        if ("octet_string".equals(key)) {
            this.octet_string = MAPPER.convertValue(value, byte[].class);
        }
        if ("visible_string".equals(key)) {
            this.visible_string = MAPPER.convertValue(value, String.class);
        }
        if ("unicode_string".equals(key)) {
            this.unicode_string = MAPPER.convertValue(value, String.class);
        }
        if ("utc_time".equals(key)) {
            this.utc_time = MAPPER.convertValue(value, byte[].class);
        }
        if ("binary_time".equals(key)) {
            this.binary_time = MAPPER.convertValue(value, byte[].class);
        }
        if ("quality".equals(key)) {
            this.quality = MAPPER.convertValue(value, int.class);
        }
        if ("dbpos".equals(key)) {
            this.dbpos = MAPPER.convertValue(value, int.class);
        }
        if ("tcmd".equals(key)) {
            this.tcmd = MAPPER.convertValue(value, int.class);
        }
        if ("check".equals(key)) {
            this.check = MAPPER.convertValue(value, int.class);
        }
    }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("Data", enc, MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("Data", enc, data), CmsData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
