#include "svc/connection/cms2_associate.h"
#include "cms_types.h"
#include <stdlib.h>
#include <string.h>

int cms2_associate_request_encode(
    const cms2_associate_request_t *pdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);

    /* 1. req_id — Int16U */
    if (!pdu->req_id) return CMS2_ERR;
    uint16_t req_val = *(const uint16_t*)pdu->req_id;
    per_error_t err = per_encode_constrained_int(&s, req_val, 0, 65535);
    if (err) return CMS2_ERR;

    /* 2. sap_ref_present — OPTIONAL bit */
    int sap_present = pdu->sap_ref_present ? *(const int*)pdu->sap_ref_present : 0;
    err = per_encode_constrained_int(&s, sap_present, 0, 1);
    if (err) return CMS2_ERR;

    /* 3. auth_present — OPTIONAL bit */
    int auth_present = pdu->auth_present ? *(const int*)pdu->auth_present : 0;
    err = per_encode_constrained_int(&s, auth_present, 0, 1);
    if (err) return CMS2_ERR;

    /* 4. sap_ref — VisibleString129, if present */
    if (sap_present && pdu->sap_ref) {
        const uint8_t *vptr = *(const uint8_t *const*)pdu->sap_ref;
        if (vptr) {
            err = per_encode_visible_string(&s, vptr, 129);
            if (err) return CMS2_ERR;
        }
    }

    /* 5. auth_param — TODO */
    if (auth_present && pdu->auth_param) {
        (void)pdu->auth_param;
    }

    *out_len = (int)per_stream_tell(&s);
    return CMS2_OK;
}

int cms2_associate_request_decode(
    cms2_associate_request_t *pdu,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);

    /* 1. req_id */
    if (!pdu->req_id) return CMS2_ERR;
    int64_t req_val;
    per_error_t err = per_decode_constrained_int(&s, &req_val, 0, 65535);
    if (err) return CMS2_ERR;
    *(uint16_t*)pdu->req_id = (uint16_t)req_val;

    /* 2. OPTIONAL bits */
    int sap_present, auth_present;
    int64_t v;
    err = per_decode_constrained_int(&s, &v, 0, 1);
    if (err) return CMS2_ERR;
    sap_present = (int)v;
    err = per_decode_constrained_int(&s, &v, 0, 1);
    if (err) return CMS2_ERR;
    auth_present = (int)v;

    if (pdu->sap_ref_present) *(int*)pdu->sap_ref_present = sap_present;
    if (pdu->auth_present)    *(int*)pdu->auth_present = auth_present;

    /* 3. sap_ref */
    if (sap_present && pdu->sap_ref) {
        uint8_t *vptr = *(uint8_t **)pdu->sap_ref;
        if (vptr) {
            err = per_decode_visible_string(&s, vptr, 129);
            if (err) return CMS2_ERR;
            int32_t *plen = (int32_t*)((uint8_t*)pdu->sap_ref + 8);
            *plen = (int32_t)strlen((const char*)vptr);
        }
    }

    return CMS2_OK;
}

void cms2_associate_request_init(cms2_associate_request_t *pdu) {
    memset(pdu, 0, sizeof(*pdu));

    uint16_t *req = (uint16_t*)calloc(1, sizeof(uint16_t));
    *req = 0;
    pdu->req_id = req;

    int *sap_present = (int*)calloc(1, sizeof(int));
    *sap_present = 0;
    pdu->sap_ref_present = sap_present;

    cms2_uint8_array_t *sap = (cms2_uint8_array_t*)calloc(1, sizeof(cms2_uint8_array_t));
    sap->value = (uint8_t*)calloc(130, 1);
    sap->len = 0;
    pdu->sap_ref = sap;

    int *auth_present = (int*)calloc(1, sizeof(int));
    *auth_present = 0;
    pdu->auth_present = auth_present;

    pdu->auth_param = NULL;
}
