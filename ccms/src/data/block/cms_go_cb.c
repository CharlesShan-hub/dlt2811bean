#include "data/block/cms_go_cb.h"
#include "data/string/cms_visible_string.h"

int cms_go_cb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_go_cb_t *pdu = (const cms_go_cb_t*)ptr;
    int err;

    /* 1. goEna — BOOLEAN */
    if (!pdu->goEna) return CMS_ERR;
    err = cms_boolean_encode_stream(s, pdu->goEna);
    if (err) return err;

    /* 2. goID — VisibleString129 */
    if (!pdu->goID) return CMS_ERR;
    err = cms_visible_string_encode_stream(s, pdu->goID, CMS_GO_CB_GO_ID_MAX_LEN);
    if (err) return err;

    /* 3. datSet — ObjectReference */
    if (!pdu->datSet) return CMS_ERR;
    err = cms_object_reference_encode_stream(s, pdu->datSet);
    if (err) return err;

    /* 4. confRev — INT32U */
    if (!pdu->confRev) return CMS_ERR;
    err = cms_int32u_encode_stream(s, pdu->confRev);
    if (err) return err;

    /* 5. ndsCom — BOOLEAN */
    if (!pdu->ndsCom) return CMS_ERR;
    err = cms_boolean_encode_stream(s, pdu->ndsCom);
    if (err) return err;

    /* 6. dstAddress — PHYCOMADDR OPTIONAL */
    {
        int present = (pdu->dstAddress_present && pdu->dstAddress_present->value) && pdu->dstAddress;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_phy_com_addr_encode_stream(s, pdu->dstAddress);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_go_cb_decode_stream(per_stream_t *s, void *ptr) {
    cms_go_cb_t *pdu = (cms_go_cb_t*)ptr;
    int err;

    /* 1. goEna */
    if (!pdu->goEna) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu->goEna);
    if (err) return err;

    /* 2. goID */
    if (!pdu->goID) return CMS_ERR;
    err = cms_visible_string_decode_stream(s, pdu->goID, CMS_GO_CB_GO_ID_MAX_LEN);
    if (err) return err;

    /* 3. datSet */
    if (!pdu->datSet) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu->datSet);
    if (err) return err;

    /* 4. confRev */
    if (!pdu->confRev) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu->confRev);
    if (err) return err;

    /* 5. ndsCom */
    if (!pdu->ndsCom) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu->ndsCom);
    if (err) return err;

    /* 6. dstAddress — PHYCOMADDR OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (bit.value && pdu->dstAddress) {
            err = cms_phy_com_addr_decode_stream(s, pdu->dstAddress);
            if (err) return err;
        }
        if (pdu->dstAddress_present) pdu->dstAddress_present->value = bit.value;
    }

    return CMS_OK;
}

int cms_go_cb_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_go_cb_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_go_cb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_go_cb_decode_stream(&s, ptr);
}
