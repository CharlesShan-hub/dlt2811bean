#include "data/control/cms_originator.h"

int cms_originator_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_originator_t *pdu = (const cms_originator_t *) ptr;

    if (!pdu->orCat)
        return CMS_ERR;
    int err = cms_or_cat_encode_stream(s, pdu->orCat);
    if (err)
        return err;

    if (!pdu->orIdent)
        return CMS_ERR;
    err = cms_octet_string_encode_stream(s, pdu->orIdent, CMS_OR_IDENT_MAX_LEN);
    if (err)
        return err;

    return CMS_OK;
}

int cms_originator_decode_stream(per_stream_t *s, void *ptr) {
    cms_originator_t *pdu = (cms_originator_t *) ptr;
    if (pdu) {
        if (!pdu->orCat)
            return CMS_ERR;
    }
    int err = cms_or_cat_decode_stream(s, pdu ? pdu->orCat : NULL);
    if (err)
        return err;
    if (pdu) {
        if (!pdu->orIdent)
            return CMS_ERR;
    }
    err = cms_octet_string_decode_stream(s, pdu ? pdu->orIdent : NULL, CMS_OR_IDENT_MAX_LEN);
    if (err)
        return err;
    return CMS_OK;
}

int cms_originator_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err)
        return (int) err;
    int rc = cms_originator_encode_stream(&s, ptr);
    if (rc) {
        per_stream_free(&s);
        return rc;
    }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_originator_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    return cms_originator_decode_stream(&s, ptr);
}
