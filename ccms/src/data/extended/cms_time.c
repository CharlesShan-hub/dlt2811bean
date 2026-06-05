#include "data/extended/cms_time.h"
#include <string.h>

/* ---- internal stream version ---- */

int cms_time_quality_encode_stream(per_stream_t *s, const cms_time_quality_t *v){
    if (v->tagf.value < 0 || v->tagf.value > 2) return CMS_ERR;
    if (v->precision.value < 0 || v->precision.value > 31) return CMS_ERR;

    uint8_t raw = (uint8_t)(v->tagf.value)          /* bits 0-2 */
                | (uint8_t)(v->precision.value << 3); /* bits 3-7 */
    return per_encode_bit_string_fixed(s, &raw, 8);
}
int cms_time_quality_decode_stream(per_stream_t *s, cms_time_quality_t *v){
    uint8_t raw;
    int rc = per_decode_bit_string_fixed(s, &raw, 8);
    if (rc) return rc;
    v->tagf.value      = raw & 0x07;           /* bits 0-2 */
    v->precision.value = (raw >> 3) & 0x1F;    /* bits 3-7 */
    v->fraction.value  = 0;
    return CMS_OK;
}

int cms_utc_time_encode_stream(per_stream_t *s, const cms_utc_time_t *t){
    if (t->fraction_of_second.value > 16777215) return CMS_ERR;
    if (t->time_quality.tagf.value < 0 || t->time_quality.tagf.value > 2) return CMS_ERR;
    if (t->time_quality.precision.value < 0 || t->time_quality.precision.value > 31) return CMS_ERR;

    uint8_t bytes[8];
    uint32_t sec = t->seconds_since_epoch.value;
    uint32_t frac = t->fraction_of_second.value;
    bytes[0] = (uint8_t)(sec >> 24);
    bytes[1] = (uint8_t)(sec >> 16);
    bytes[2] = (uint8_t)(sec >> 8);
    bytes[3] = (uint8_t)(sec);
    bytes[4] = (uint8_t)(frac >> 16);
    bytes[5] = (uint8_t)(frac >> 8);
    bytes[6] = (uint8_t)(frac);
    bytes[7] = (uint8_t)(t->time_quality.tagf.value)
             | (uint8_t)(t->time_quality.precision.value << 3);
    return per_encode_octet_string_fixed(s, bytes, 8);
}
int cms_utc_time_decode_stream(per_stream_t *s, cms_utc_time_t *t){
    uint8_t bytes[8];
    int rc = per_decode_octet_string_fixed(s, bytes, 8);
    if (rc) return rc;
    t->seconds_since_epoch.value = ((uint32_t)bytes[0] << 24) | ((uint32_t)bytes[1] << 16)
                                 | ((uint32_t)bytes[2] << 8)  | (uint32_t)bytes[3];
    t->fraction_of_second.value = ((uint32_t)bytes[4] << 16) | ((uint32_t)bytes[5] << 8)
                                 | (uint32_t)bytes[6];
    t->time_quality.tagf.value = (int32_t)(bytes[7] & 0x07);
    t->time_quality.precision.value = (int32_t)((bytes[7] >> 3) & 0x1F);
    t->time_quality.fraction.value = 0;
    return CMS_OK;
}

int cms_binary_time_encode_stream(per_stream_t *s, const cms_binary_time_t *t){
    uint8_t bytes[6];
    uint32_t msOfDay = t->msOfDay.value;
    uint16_t daysSince1984 = t->daysSince1984.value;
    bytes[0] = (uint8_t)(msOfDay >> 24);
    bytes[1] = (uint8_t)(msOfDay >> 16);
    bytes[2] = (uint8_t)(msOfDay >> 8);
    bytes[3] = (uint8_t)(msOfDay);
    bytes[4] = (uint8_t)(daysSince1984 >> 8);
    bytes[5] = (uint8_t)(daysSince1984);
    return per_encode_octet_string_fixed(s, bytes, 6);
}
int cms_binary_time_decode_stream(per_stream_t *s, cms_binary_time_t *t){
    uint8_t bytes[6];
    int rc = per_decode_octet_string_fixed(s, bytes, 6);
    if (rc) return rc;
    t->msOfDay.value = ((uint32_t)bytes[0] << 24) | ((uint32_t)bytes[1] << 16)
                     | ((uint32_t)bytes[2] << 8)  | (uint32_t)bytes[3];
    t->daysSince1984.value = (uint16_t)(((uint16_t)bytes[4] << 8) | (uint16_t)bytes[5]);
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_time_quality_encode(const cms_time_quality_t *v, uint8_t *b, int *l){ 
    per_stream_t w = per_stream_new_write(b, (size_t)*l); 
    int rc = cms_time_quality_encode_stream(&w, v); 
    *l = (int)per_stream_bytes_written(&w); 
    return rc;
}
CMS_EXPORT int cms_time_quality_decode(cms_time_quality_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l); 
    return cms_time_quality_decode_stream(&r, v);
}
CMS_EXPORT int cms_utc_time_encode(const cms_utc_time_t *t, uint8_t *b, int *l){ 
    per_stream_t w = per_stream_new_write(b, (size_t)*l); 
    int rc = cms_utc_time_encode_stream(&w, t); 
    *l = (int)per_stream_bytes_written(&w); 
    return rc;
}
CMS_EXPORT int cms_utc_time_decode(cms_utc_time_t *t, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l); 
    return cms_utc_time_decode_stream(&r, t);
}
CMS_EXPORT int cms_binary_time_encode(const cms_binary_time_t *t, uint8_t *b, int *l){ 
    per_stream_t w = per_stream_new_write(b, (size_t)*l); 
    int rc = cms_binary_time_encode_stream(&w, t); 
    *l = (int)per_stream_bytes_written(&w); 
    return rc;
}
CMS_EXPORT int cms_binary_time_decode(cms_binary_time_t *t, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l); 
    return cms_binary_time_decode_stream(&r, t);
}
