#include "data/block/cms_msvcb_opt_flds.h"
#include "data/basic/cms_string.h"

/*
 * MSVCBOptFlds bit layout (5 bits):
 *   bit 0: refreshTime
 *   bit 1: reserved (always 0)
 *   bit 2: sampleRate
 *   bit 3: dataSetName
 *   bit 4: security
 */

int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const cms_msvcb_opt_flds_t *v){
    uint8_t buf[1];
    buf[0] = (uint8_t)(
        (v->refresh_time.value   ? 0x80 : 0) |
        (v->sample_rate.value    ? 0x20 : 0) |
        (v->data_set_name.value  ? 0x10 : 0) |
        (v->security.value       ? 0x08 : 0)
    );
    cms_bit_string_fixed_t bs = { buf, 5 };
    return cms_bit_string_fixed_encode_stream(s, &bs);
}

int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, cms_msvcb_opt_flds_t *v){
    uint8_t buf[1] = {0};
    cms_bit_string_fixed_t bs = { buf, 5 };
    int rc = cms_bit_string_fixed_decode_stream(s, &bs);
    if (rc) return rc;
    v->refresh_time.value  = (buf[0] >> 7) & 1;
    v->sample_rate.value   = (buf[0] >> 5) & 1;
    v->data_set_name.value = (buf[0] >> 4) & 1;
    v->security.value      = (buf[0] >> 3) & 1;
    return CMS_OK;
}

CMS_EXPORT int cms_msvcb_opt_flds_encode(const cms_msvcb_opt_flds_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_msvcb_opt_flds_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_msvcb_opt_flds_decode(cms_msvcb_opt_flds_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_msvcb_opt_flds_decode_stream(&r, v);
}
