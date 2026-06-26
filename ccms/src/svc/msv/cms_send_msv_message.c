#include "svc/msv/cms_send_msv_message.h"
#include "svc/other/cms_req_id.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/block/cms_smp_mod.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int8u.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

int cms_send_msv_message_encode(const cms_send_msv_message_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 1. OPTIONAL bitmap (4 fields: datSet, refTm, smpRate, smpMod) */
    bool opt_present[4] = {
        (pdu->dat_set_present && pdu->dat_set_present->value) && pdu->dat_set,
        (pdu->ref_tm_present && pdu->ref_tm_present->value) && pdu->ref_tm,
        (pdu->smp_rate_present && pdu->smp_rate_present->value) && pdu->smp_rate,
        (pdu->smp_mod_present && pdu->smp_mod_present->value) && pdu->smp_mod
    };
    err = (int)per_encode_optional_bitmap(&s, opt_present, 4);
    if (err) { per_stream_free(&s); return err; }

    /* 2. msvID — VisibleString(129) */
    if (!pdu->msv_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_visible_string_encode_stream(&s, pdu->msv_id, 129);
    if (err) { per_stream_free(&s); return err; }

    /* 3. datSet — ObjectReference OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_object_reference_encode_stream(&s, pdu->dat_set);
        if (err) { per_stream_free(&s); return err; }
    }

    /* 4. smpCnt — INT16U */
    if (!pdu->smp_cnt) { per_stream_free(&s); return CMS_ERR; }
    err = cms_int16u_encode_stream(&s, pdu->smp_cnt);
    if (err) { per_stream_free(&s); return err; }

    /* 5. confRev — INT32U */
    if (!pdu->conf_rev) { per_stream_free(&s); return CMS_ERR; }
    err = cms_int32u_encode_stream(&s, pdu->conf_rev);
    if (err) { per_stream_free(&s); return err; }

    /* 6. refTm — TimeStamp OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_time_stamp_encode_stream(&s, pdu->ref_tm);
        if (err) { per_stream_free(&s); return err; }
    }

    /* 7. smpSynch — INT8U */
    if (!pdu->smp_synch) { per_stream_free(&s); return CMS_ERR; }
    err = cms_int8u_encode_stream(&s, pdu->smp_synch);
    if (err) { per_stream_free(&s); return err; }

    /* 8. smpRate — INT16U OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_int16u_encode_stream(&s, pdu->smp_rate);
        if (err) { per_stream_free(&s); return err; }
    }

    /* 9. simulation — BOOLEAN */
    if (!pdu->simulation) { per_stream_free(&s); return CMS_ERR; }
    err = cms_boolean_encode_stream(&s, pdu->simulation);
    if (err) { per_stream_free(&s); return err; }

    /* 10. sample — SEQUENCE OF Data */
    if (!pdu->sample) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->sample->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_data_t *e = (cms_data_t*)pdu->sample->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_data_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    /* 11. smpMod — SmpMod OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_smp_mod_encode_stream(&s, pdu->smp_mod);
        if (err) { per_stream_free(&s); return err; }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_send_msv_message_decode(cms_send_msv_message_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (4 fields) */
    bool opt_present[4];
    err = (int)per_decode_optional_bitmap(&s, opt_present, 4);
    if (err) return err;
    if (pdu->dat_set_present) pdu->dat_set_present->value = opt_present[0];
    if (pdu->ref_tm_present) pdu->ref_tm_present->value = opt_present[1];
    if (pdu->smp_rate_present) pdu->smp_rate_present->value = opt_present[2];
    if (pdu->smp_mod_present) pdu->smp_mod_present->value = opt_present[3];

    /* 2. msvID */
    if (!pdu->msv_id) return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->msv_id, 129);
    if (err) return err;

    /* 3. datSet OPTIONAL */
    if (opt_present[0]) {
        if (!pdu->dat_set) return CMS_ERR;
        err = cms_object_reference_decode_stream(&s, pdu->dat_set);
        if (err) return err;
    }

    /* 4. smpCnt */
    if (!pdu->smp_cnt) return CMS_ERR;
    err = cms_int16u_decode_stream(&s, pdu->smp_cnt);
    if (err) return err;

    /* 5. confRev */
    if (!pdu->conf_rev) return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->conf_rev);
    if (err) return err;

    /* 6. refTm OPTIONAL */
    if (opt_present[1]) {
        if (!pdu->ref_tm) return CMS_ERR;
        err = cms_time_stamp_decode_stream(&s, pdu->ref_tm);
        if (err) return err;
    }

    /* 7. smpSynch */
    if (!pdu->smp_synch) return CMS_ERR;
    err = cms_int8u_decode_stream(&s, pdu->smp_synch);
    if (err) return err;

    /* 8. smpRate OPTIONAL */
    if (opt_present[2]) {
        if (!pdu->smp_rate) return CMS_ERR;
        err = cms_int16u_decode_stream(&s, pdu->smp_rate);
        if (err) return err;
    }

    /* 9. simulation */
    if (!pdu->simulation) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->simulation);
    if (err) return err;

    /* 10. sample */
    if (!pdu->sample) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        pdu->sample->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_data_t *e = (cms_data_t*)pdu->sample->elements[i];
            if (!e) return CMS_ERR;
            err = cms_data_decode_stream(&s, e);
            if (err) return err;
        }
    }

    /* 11. smpMod OPTIONAL */
    if (opt_present[3]) {
        if (!pdu->smp_mod) return CMS_ERR;
        err = cms_smp_mod_decode_stream(&s, pdu->smp_mod);
        if (err) return err;
    }

    return CMS_OK;
}
