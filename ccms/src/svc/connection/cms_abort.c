#include "svc/connection/cms_abort.h"

/* ==================== AbortPDU ==================== */
CMS_EXPORT int cms_abort_encode(
    const cms_abort_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 128);
    if (err) return CMS_ERR;
    cms_association_id_encode_stream(&w, &sdu->assoc_id);
    cms_abort_reason_encode_stream(&w, sdu->reason);
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_abort_decode(
    const uint8_t *in_buf, int in_len,
    cms_abort_t *sdu)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_association_id_decode_stream(&r, &sdu->assoc_id);
    cms_abort_reason_decode_stream(&r, &sdu->reason);
    return CMS_OK;
}