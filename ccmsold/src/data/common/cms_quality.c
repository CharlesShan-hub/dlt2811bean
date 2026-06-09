#include "data/common/cms_quality.h"
#include "per/cms_string.h"

/*
 * Quality bit layout (13 bits, PER fixed BIT STRING):
 *
 *   bit  0..1: validity        (2-bit field)
 *   bit  2:    overflow
 *   bit  3:    outOfRange
 *   bit  4:    badReference
 *   bit  5:    oscillatory
 *   bit  6:    failure
 *   bit  7:    oldData
 *   bit  8:    inconsistent
 *   bit  9:    inaccurate
 *   bit 10:    substituted
 *   bit 11:    test
 *   bit 12:    operatorBlocked
 */


/* ==================== Stream API ==================== */

int cms_quality_encode_stream(per_stream_t *s, const cms_quality_t *v){
    uint8_t buf[2];
    buf[0] = (uint8_t)(
        ((v->validity.value & 0x03) << 6) |
        (v->overflow.value      ? 0x20 : 0) |
        (v->outOfRange.value    ? 0x10 : 0) |
        (v->badReference.value  ? 0x08 : 0) |
        (v->oscillatory.value   ? 0x04 : 0) |
        (v->failure.value       ? 0x02 : 0) |
        (v->oldData.value       ? 0x01 : 0)
    );
    buf[1] = (uint8_t)(
        (v->inconsistent.value      ? 0x80 : 0) |
        (v->inaccurate.value        ? 0x40 : 0) |
        (v->substituted.value       ? 0x20 : 0) |
        (v->test.value              ? 0x10 : 0) |
        (v->operatorBlocked.value   ? 0x08 : 0)
    );
    cms_bit_string_fixed_t bs = { buf, 13 };
    return cms_bit_string_fixed_encode_stream(s, &bs);
}

int cms_quality_decode_stream(per_stream_t *s, cms_quality_t *v){
    uint8_t buf[2] = {0, 0};
    cms_bit_string_fixed_t bs = { buf, 13 };
    int rc = cms_bit_string_fixed_decode_stream(s, &bs);
    if (rc) return rc;
    v->validity.value     = (buf[0] >> 6) & 0x03;
    v->overflow.value     = (buf[0] >> 5) & 1;
    v->outOfRange.value   = (buf[0] >> 4) & 1;
    v->badReference.value = (buf[0] >> 3) & 1;
    v->oscillatory.value  = (buf[0] >> 2) & 1;
    v->failure.value      = (buf[0] >> 1) & 1;
    v->oldData.value      =  buf[0]       & 1;
    v->inconsistent.value      = (buf[1] >> 7) & 1;
    v->inaccurate.value        = (buf[1] >> 6) & 1;
    v->substituted.value       = (buf[1] >> 5) & 1;
    v->test.value              = (buf[1] >> 4) & 1;
    v->operatorBlocked.value   = (buf[1] >> 3) & 1;
    return CMS_OK;
}

/* ==================== Buffer API ==================== */

CMS_EXPORT int cms_quality_encode(const cms_quality_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l); 
    int rc = cms_quality_encode_stream(&w, v); 
    *l = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_quality_decode(cms_quality_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l); 
    return cms_quality_decode_stream(&r, v); 
}
