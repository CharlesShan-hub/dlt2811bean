#include "data/control/cms_check.h"
#include "data/basic/cms_string.h"

/* Check bit layout:
 *   bit 0: syncheck
 *   bit 1: interlockCheck
 */

int cms_check_encode_stream(per_stream_t *s, const cms_check_t *v){
    uint8_t buf[1];
    buf[0] = (uint8_t)(
        (v->syncheck.value         ? 0x80 : 0) |
        (v->interlock_check.value  ? 0x40 : 0)
    );
    cms_bit_string_fixed_t bs = { buf, 2 };
    return cms_bit_string_fixed_encode_stream(s, &bs);
}

int cms_check_decode_stream(per_stream_t *s, cms_check_t *v){
    uint8_t buf[1] = {0};
    cms_bit_string_fixed_t bs = { buf, 2 };
    int rc = cms_bit_string_fixed_decode_stream(s, &bs);
    if (rc) return rc;
    v->syncheck.value        = (buf[0] >> 7) & 1;
    v->interlock_check.value = (buf[0] >> 6) & 1;
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_check_encode(const cms_check_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_check_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_check_decode(cms_check_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_check_decode_stream(&r, v);
}
