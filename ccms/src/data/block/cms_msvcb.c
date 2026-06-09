#include "data/block/cms_msvcb.h"
#include "data/string/cms_visible_string.h"

int cms_msvcb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_msvcb_t *pdu = (const cms_msvcb_t*)ptr;
    int err;

    /* 1. svEna — BOOLEAN */
    if (!pdu->svEna) return CMS_ERR;
    err = cms_boolean_encode_stream(s, pdu->svEna);
    if (err) return err;

    /* 2. msvID — VisibleString129 */
    if (!pdu->msvID) return CMS_ERR;
    err = cms_visible_string_encode_stream(s, pdu->msvID, CMS_MSVCB_MSV_ID_MAX_LEN);
    if (err) return err;

    /* 3. datSet — ObjectReference */
    if (!pdu->datSet) return CMS_ERR;
    err = cms_object_reference_encode_stream(s, pdu->datSet);
    if (err) return err;

    /* 4. confRev — INT32U */
    if (!pdu->confRev) return CMS_ERR;
    err = cms_int32u_encode_stream(s, pdu->confRev);
    if (err) return err;

    /* 5. smpMod — SmpMod OPTIONAL */
    {
        int present = (pdu->smpMod_present && pdu->smpMod_present->value) && pdu->smpMod;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_smp_mod_encode_stream(s, pdu->smpMod);
            if (err) return err;
        }
    }

    /* 6. smpRate — INT16U */
    if (!pdu->smpRate) return CMS_ERR;
    err = cms_int16u_encode_stream(s, pdu->smpRate);
    if (err) return err;

    /* 7. optFlds — MSVCBOptFlds */
    if (!pdu->optFlds) return CMS_ERR;
    err = cms_msvcb_opt_flds_encode_stream(s, pdu->optFlds);
    if (err) return err;

    /* 8. dstAddress — PHYCOMADDR OPTIONAL */
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

int cms_msvcb_decode_stream(per_stream_t *s, void *ptr) {
    cms_msvcb_t *pdu = (cms_msvcb_t*)ptr;
    int err;

    /* 1. svEna */
    if (!pdu->svEna) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu->svEna);
    if (err) return err;

    /* 2. msvID */
    if (!pdu->msvID) return CMS_ERR;
    err = cms_visible_string_decode_stream(s, pdu->msvID, CMS_MSVCB_MSV_ID_MAX_LEN);
    if (err) return err;

    /* 3. datSet */
    if (!pdu->datSet) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu->datSet);
    if (err) return err;

    /* 4. confRev */
    if (!pdu->confRev) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu->confRev);
    if (err) return err;

    /* 5. smpMod — SmpMod OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (bit.value && pdu->smpMod) {
            err = cms_smp_mod_decode_stream(s, pdu->smpMod);
            if (err) return err;
        }
        if (pdu->smpMod_present) pdu->smpMod_present->value = bit.value;
    }

    /* 6. smpRate */
    if (!pdu->smpRate) return CMS_ERR;
    err = cms_int16u_decode_stream(s, pdu->smpRate);
    if (err) return err;

    /* 7. optFlds */
    if (!pdu->optFlds) return CMS_ERR;
    err = cms_msvcb_opt_flds_decode_stream(s, pdu->optFlds);
    if (err) return err;

    /* 8. dstAddress — PHYCOMADDR OPTIONAL */
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

int cms_msvcb_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_msvcb_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_msvcb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_msvcb_decode_stream(&s, ptr);
}
