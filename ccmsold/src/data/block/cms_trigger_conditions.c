#include "data/block/cms_trigger_conditions.h"
#include "data/basic/cms_string.h"

/*
 * TriggerConditions bit layout (6 bits):
 *   bit 0: reserved (always 0)
 *   bit 1: dataChange
 *   bit 2: qualityChange
 *   bit 3: dataUpdate
 *   bit 4: integrity
 *   bit 5: generalInterrogation
 */

int cms_trigger_conditions_encode_stream(per_stream_t *s, const cms_trigger_conditions_t *v){
    uint8_t buf[1];
    buf[0] = (uint8_t)(
        (v->data_change.value           ? 0x40 : 0) |
        (v->quality_change.value        ? 0x20 : 0) |
        (v->data_update.value           ? 0x10 : 0) |
        (v->integrity.value             ? 0x08 : 0) |
        (v->general_interrogation.value ? 0x04 : 0)
    );
    cms_bit_string_fixed_t bs = { buf, 6 };
    return cms_bit_string_fixed_encode_stream(s, &bs);
}

int cms_trigger_conditions_decode_stream(per_stream_t *s, cms_trigger_conditions_t *v){
    uint8_t buf[1] = {0};
    cms_bit_string_fixed_t bs = { buf, 6 };
    int rc = cms_bit_string_fixed_decode_stream(s, &bs);
    if (rc) return rc;
    v->data_change.value           = (buf[0] >> 6) & 1;
    v->quality_change.value        = (buf[0] >> 5) & 1;
    v->data_update.value           = (buf[0] >> 4) & 1;
    v->integrity.value             = (buf[0] >> 3) & 1;
    v->general_interrogation.value = (buf[0] >> 2) & 1;
    return CMS_OK;
}

CMS_EXPORT int cms_trigger_conditions_encode(const cms_trigger_conditions_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_trigger_conditions_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_trigger_conditions_decode(cms_trigger_conditions_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_trigger_conditions_decode_stream(&r, v);
}
