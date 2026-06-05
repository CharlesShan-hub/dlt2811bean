#include "data/block/cms_rcb_opt_flds.h"
#include "data/basic/cms_string.h"

/*
 * RCBOptFlds bit layout (10 bits):
 *   bit 0: reserved (always 0)
 *   bit 1: sequenceNumber
 *   bit 2: reportTimeStamp
 *   bit 3: reasonForInclusion
 *   bit 4: dataSetName
 *   bit 5: dataReference
 *   bit 6: bufferOverflow
 *   bit 7: entryID
 *   bit 8: confRevision
 *   bit 9: segmentation
 */

int cms_rcb_opt_flds_encode_stream(per_stream_t *s, const cms_rcb_opt_flds_t *v){
    uint8_t buf[2] = {0, 0};
    buf[0] = (uint8_t)(
        (v->sequence_number.value      ? 0x40 : 0) |
        (v->report_time_stamp.value    ? 0x20 : 0) |
        (v->reason_for_inclusion.value ? 0x10 : 0) |
        (v->data_set_name.value        ? 0x08 : 0) |
        (v->data_reference.value       ? 0x04 : 0) |
        (v->buffer_overflow.value      ? 0x02 : 0) |
        (v->entry_id.value             ? 0x01 : 0)
    );
    buf[1] = (uint8_t)(
        (v->conf_revision.value        ? 0x80 : 0) |
        (v->segmentation.value         ? 0x40 : 0)
    );
    cms_bit_string_fixed_t bs = { buf, 10 };
    return cms_bit_string_fixed_encode_stream(s, &bs);
}

int cms_rcb_opt_flds_decode_stream(per_stream_t *s, cms_rcb_opt_flds_t *v){
    uint8_t buf[2] = {0, 0};
    cms_bit_string_fixed_t bs = { buf, 10 };
    int rc = cms_bit_string_fixed_decode_stream(s, &bs);
    if (rc) return rc;
    v->sequence_number.value      = (buf[0] >> 6) & 1;
    v->report_time_stamp.value    = (buf[0] >> 5) & 1;
    v->reason_for_inclusion.value = (buf[0] >> 4) & 1;
    v->data_set_name.value        = (buf[0] >> 3) & 1;
    v->data_reference.value       = (buf[0] >> 2) & 1;
    v->buffer_overflow.value      = (buf[0] >> 1) & 1;
    v->entry_id.value             =  buf[0]       & 1;
    v->conf_revision.value        = (buf[1] >> 7) & 1;
    v->segmentation.value         = (buf[1] >> 6) & 1;
    return CMS_OK;
}

CMS_EXPORT int cms_rcb_opt_flds_encode(const cms_rcb_opt_flds_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_rcb_opt_flds_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_rcb_opt_flds_decode(cms_rcb_opt_flds_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_rcb_opt_flds_decode_stream(&r, v);
}
