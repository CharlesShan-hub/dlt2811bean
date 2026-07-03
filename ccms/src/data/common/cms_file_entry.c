#include "data/common/cms_file_entry.h"

int cms_file_entry_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_file_entry_t *pdu = (const cms_file_entry_t*)ptr;

    /* 1. fileName — VisibleString129 */
    if (!pdu->fileName) return CMS_ERR;
    int err = cms_visible_string_encode_stream(s, pdu->fileName, 129);
    if (err) return err;

    /* 2. fileSize — INT32U */
    if (!pdu->fileSize) return CMS_ERR;
    err = cms_int32u_encode_stream(s, pdu->fileSize);
    if (err) return err;

    /* 3. lastModified — UtcTime */
    if (!pdu->lastModified) return CMS_ERR;
    err = cms_utc_time_encode_stream(s, pdu->lastModified);
    if (err) return err;

    /* 4. checkSum — INT32U */
    if (!pdu->checkSum) return CMS_ERR;
    err = cms_int32u_encode_stream(s, pdu->checkSum);
    if (err) return err;

    return CMS_OK;
}

int cms_file_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_file_entry_t *pdu = (cms_file_entry_t*)ptr;

    /* 1. fileName — VisibleString129 */
    if (pdu && !pdu->fileName) return CMS_ERR;
    int err = cms_visible_string_decode_stream(s, pdu ? pdu->fileName : NULL, 129);
    if (err) return err;

    /* 2. fileSize — INT32U */
    if (pdu && !pdu->fileSize) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu ? pdu->fileSize : NULL);
    if (err) return err;

    /* 3. lastModified — UtcTime */
    if (pdu && !pdu->lastModified) return CMS_ERR;
    err = cms_utc_time_decode_stream(s, pdu ? pdu->lastModified : NULL);
    if (err) return err;

    /* 4. checkSum — INT32U */
    if (pdu && !pdu->checkSum) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu ? pdu->checkSum : NULL);
    if (err) return err;

    return CMS_OK;
}

int cms_file_entry_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_file_entry_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_file_entry_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_file_entry_decode_stream(&s, ptr);
}
