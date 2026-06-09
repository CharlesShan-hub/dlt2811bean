#include "svc/directory/cms2_get_all_cb_values.h"
#include <stdlib.h>
#include <string.h>

int cms2_get_all_cb_values_request_encode(
    const cms2_get_all_cb_values_request_t *pdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    per_error_t err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS2_ERR;
    uint16_t req_val = *(const uint16_t*)pdu->req_id;
    err = per_encode_constrained_int(&s, req_val, 0, 65535);
    if (err) return CMS2_ERR;

    /* 2. reference — CHOICE { ldName, lnReference }
     *    委托给子结构的编码函数 */
    if (!pdu->reference) return CMS2_ERR;
    err = cms2_reference_choice_encode(&s,
        (const cms2_reference_choice_t*)pdu->reference);
    if (err) return CMS2_ERR;

    /* 3. acsiClass — ACSIClass = INTEGER (0..10) */
    if (!pdu->acsi_class) return CMS2_ERR;
    int32_t acsi_val = *(const int32_t*)pdu->acsi_class;
    err = per_encode_constrained_int(&s, acsi_val, 0, 10);
    if (err) return CMS2_ERR;

    /* 4. referenceAfter — OPTIONAL */
    int ref_after_present = pdu->ref_after_present
        ? *(const int32_t*)pdu->ref_after_present : 0;

    err = per_stream_write_bit(&s, ref_after_present ? 1 : 0);
    if (err) return CMS2_ERR;

    if (ref_after_present && pdu->ref_after) {
        const uint8_t *vptr = *(const uint8_t *const*)pdu->ref_after;
        if (vptr) {
            err = per_encode_visible_string(&s, vptr, 129);
            if (err) return CMS2_ERR;
        }
    }

    *out_len = (int)per_stream_tell(&s);
    return CMS2_OK;
}

int cms2_get_all_cb_values_request_decode(
    cms2_get_all_cb_values_request_t *pdu,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    per_error_t err;
    int64_t v;

    /* 1. reqId */
    if (!pdu->req_id) return CMS2_ERR;
    err = per_decode_constrained_int(&s, &v, 0, 65535);
    if (err) return CMS2_ERR;
    *(uint16_t*)pdu->req_id = (uint16_t)v;

    /* 2. reference — 委托给子结构 */
    if (!pdu->reference) return CMS2_ERR;
    err = cms2_reference_choice_decode(&s,
        (cms2_reference_choice_t*)pdu->reference);
    if (err) return CMS2_ERR;

    /* 3. acsiClass */
    if (!pdu->acsi_class) return CMS2_ERR;
    err = per_decode_constrained_int(&s, &v, 0, 10);
    if (err) return CMS2_ERR;
    *(int32_t*)pdu->acsi_class = (int32_t)v;

    /* 4. referenceAfter OPTIONAL */
    int ref_after_present;
    err = per_stream_read_bit(&s, &ref_after_present);
    if (err) return CMS2_ERR;
    if (pdu->ref_after_present) *(int32_t*)pdu->ref_after_present = ref_after_present;

    if (ref_after_present && pdu->ref_after) {
        uint8_t *vptr = *(uint8_t **)pdu->ref_after;
        if (!vptr) return CMS2_ERR;
        err = per_decode_visible_string(&s, vptr, 129);
        if (err) return CMS2_ERR;
        *(int32_t*)((uint8_t*)pdu->ref_after + 8) = (int32_t)strlen((const char*)vptr);
    }

    return CMS2_OK;
}

void cms2_get_all_cb_values_request_init(cms2_get_all_cb_values_request_t *pdu) {
    memset(pdu, 0, sizeof(*pdu));

    pdu->req_id = calloc(1, sizeof(uint16_t));
    pdu->acsi_class = calloc(1, sizeof(int32_t));
    pdu->ref_after_present = calloc(1, sizeof(int32_t));

    /* 分配 reference 子结构（自己管 choice + ld_name + ln_ref） */
    cms2_reference_choice_t *ref = calloc(1, sizeof(cms2_reference_choice_t));
    ref->choice = calloc(1, sizeof(int32_t));

    cms2_uint8_array_t *ld_name = calloc(1, sizeof(cms2_uint8_array_t));
    ld_name->value = calloc(65, 1);
    ld_name->len = 0;
    ref->ld_name = ld_name;

    cms2_uint8_array_t *ln_ref = calloc(1, sizeof(cms2_uint8_array_t));
    ln_ref->value = calloc(130, 1);
    ln_ref->len = 0;
    ref->ln_reference = ln_ref;

    pdu->reference = ref;

    /* ref_after */
    cms2_uint8_array_t *ref_after = calloc(1, sizeof(cms2_uint8_array_t));
    ref_after->value = calloc(130, 1);
    ref_after->len = 0;
    pdu->ref_after = ref_after;
}
