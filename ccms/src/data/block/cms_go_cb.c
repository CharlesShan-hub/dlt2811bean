#include "data/block/cms_go_cb.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_sequence.h"

int cms_go_cb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_go_cb_t *pdu = (const cms_go_cb_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (1 field: dstAddress) — X.691 §22 */
    bool opt_present[1] = {
        pdu->dstAddress_present && pdu->dstAddress_present->value && pdu->dstAddress
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 1);
    if (err) return err;

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

    /* 6. dstAddress — PHYCOMADDR OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_phy_com_addr_encode_stream(s, pdu->dstAddress);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_go_cb_decode_stream(per_stream_t *s, void *ptr) {
    cms_go_cb_t *pdu = (cms_go_cb_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (1 field: dstAddress) — X.691 §22 */
    bool opt_present[1] = {false};
    err = (int)per_decode_optional_bitmap(s, opt_present, 1);
    if (err) return err;
    if (pdu && pdu->dstAddress_present)
        pdu->dstAddress_present->value = opt_present[0] ? 1 : 0;

    /* 1. goEna */
    if (pdu && !pdu->goEna) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu ? pdu->goEna : NULL);
    if (err) return err;

    /* 2. goID */
    if (pdu && !pdu->goID) return CMS_ERR;
    err = cms_visible_string_decode_stream(s, pdu ? pdu->goID : NULL, CMS_GO_CB_GO_ID_MAX_LEN);
    if (err) return err;

    /* 3. datSet */
    if (pdu && !pdu->datSet) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu ? pdu->datSet : NULL);
    if (err) return err;

    /* 4. confRev */
    if (pdu && !pdu->confRev) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu ? pdu->confRev : NULL);
    if (err) return err;

    /* 5. ndsCom */
    if (pdu && !pdu->ndsCom) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu ? pdu->ndsCom : NULL);
    if (err) return err;

    /* 6. dstAddress — PHYCOMADDR OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_phy_com_addr_decode_stream(s, pdu ? pdu->dstAddress : NULL);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_go_cb_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_go_cb_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_go_cb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_go_cb_decode_stream(&s, ptr);
}
