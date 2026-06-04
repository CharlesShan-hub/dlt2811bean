#include "svc/connection/cms_abort.h"

/* ==================== AbortPDU ==================== */
CMS_EXPORT int cms_abort_encode(
    const cms_abort_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 128);
    if (err) return CMS_ERR;
    cms_octet_string_encode_stream(&w, sdu->assoc_id, sdu->assoc_id_len, 32);
    per_encode_constrained_int(&w, sdu->reason, 0, 5);
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_abort_decode(
    const uint8_t *in_buf, int in_len,
    cms_abort_t *sdu)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_octet_string_decode_stream(&r, sdu->assoc_id, &sdu->assoc_id_len, 32);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 5);
    sdu->reason = (int)tmp;
    return CMS_OK;
}