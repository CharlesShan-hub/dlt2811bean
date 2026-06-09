#include "data/block/cms_reason_code.h"
#include "data/basic/cms_string.h"

/*
 * ReasonCode bit layout (7 bits):
 *   bit 0: reserved (always 0)
 *   bit 1: dataChange
 *   bit 2: qualityChange
 *   bit 3: dataUpdate
 *   bit 4: integrity
 *   bit 5: generalInterrogation
 *   bit 6: applicationTrigger
 */

int cms_reason_code_encode_stream(per_stream_t *s, const cms_reason_code_t *v){
    uint8_t buf[1];
    buf[0] = (uint8_t)(
        (v->data_change.value           ? 0x40 : 0) |
        (v->quality_change.value        ? 0x20 : 0) |
        (v->data_update.value           ? 0x10 : 0) |
        (v->integrity.value             ? 0x08 : 0) |
        (v->general_interrogation.value ? 0x04 : 0) |
        (v->application_trigger.value   ? 0x02 : 0)
    );
    cms_bit_string_fixed_t bs = { buf, 7 };
    return cms_bit_string_fixed_encode_stream(s, &bs);
}

int cms_reason_code_decode_stream(per_stream_t *s, cms_reason_code_t *v){
    uint8_t buf[1] = {0};
    cms_bit_string_fixed_t bs = { buf, 7 };
    int rc = cms_bit_string_fixed_decode_stream(s, &bs);
    if (rc) return rc;
    v->data_change.value           = (buf[0] >> 6) & 1;
    v->quality_change.value        = (buf[0] >> 5) & 1;
    v->data_update.value           = (buf[0] >> 4) & 1;
    v->integrity.value             = (buf[0] >> 3) & 1;
    v->general_interrogation.value = (buf[0] >> 2) & 1;
    v->application_trigger.value   = (buf[0] >> 1) & 1;
    return CMS_OK;
}

CMS_EXPORT int cms_reason_code_encode(const cms_reason_code_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_reason_code_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_reason_code_decode(cms_reason_code_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_reason_code_decode_stream(&r, v);
}
