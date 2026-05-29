#include "cms_ffi.h"
#include "cms_apdu.h"
#include "gen_cms.h"
#include "cmsper/cmsper.h"
#include <string.h>
#include <stdlib.h>

/* ==================== Associate-Request ==================== */

int cms_ffi_encode_associate_request(
    int64_t req_id,
    const char *sap_ref,
    int has_auth,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[1024];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    Associate_Request assoc;
    memset(&assoc, 0, sizeof(assoc));
    assoc.reqId = req_id;
    assoc.serverAccessPointReference = (char *)sap_ref;
    assoc._has_authenticationParameter = has_auth;

    encode_Associate_Request(&w, &assoc);
    size_t asdu_len = per_stream_bytes_written(&w);

    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, false, CMS_SVC_ASSOCIATE,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;

    *out_len = (int)apdu_len;
    return CMS_OK;
}

int cms_ffi_decode_associate_request(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id,
    char *sap_ref, int *sap_ref_cap,
    int *has_auth)
{
    cms_apch_t apch;
    const uint8_t *asdu;
    size_t asdu_len;

    int ret = cms_apdu_decode(in_buf, (size_t)in_len, &apch, &asdu, &asdu_len);
    if (ret != 0) return CMS_ERR;

    per_stream_t r;
    per_stream_init_read(&r, asdu, asdu_len);

    Associate_Request assoc;
    memset(&assoc, 0, sizeof(assoc));
    decode_Associate_Request(&r, &assoc);

    *req_id = assoc.reqId;
    *has_auth = assoc._has_authenticationParameter;

    if (sap_ref && assoc.serverAccessPointReference) {
        size_t len = strlen(assoc.serverAccessPointReference);
        if ((int)len < *sap_ref_cap) {
            memcpy(sap_ref, assoc.serverAccessPointReference, len);
            sap_ref[len] = '\0';
            *sap_ref_cap = (int)len;
        }
        free(assoc.serverAccessPointReference);
    }

    return CMS_OK;
}

/* ==================== Release-Request ==================== */

int cms_ffi_encode_release_request(
    int64_t req_id,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[1024];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    Release_Request rel;
    memset(&rel, 0, sizeof(rel));
    rel.reqId = req_id;

    encode_Release_Request(&w, &rel);
    size_t asdu_len = per_stream_bytes_written(&w);

    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, false, CMS_SVC_RELEASE,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;

    *out_len = (int)apdu_len;
    return CMS_OK;
}

int cms_ffi_decode_release_request(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id)
{
    cms_apch_t apch;
    const uint8_t *asdu;
    size_t asdu_len;

    int ret = cms_apdu_decode(in_buf, (size_t)in_len, &apch, &asdu, &asdu_len);
    if (ret != 0) return CMS_ERR;

    per_stream_t r;
    per_stream_init_read(&r, asdu, asdu_len);

    Release_Request rel;
    memset(&rel, 0, sizeof(rel));
    decode_Release_Request(&r, &rel);

    *req_id = rel.reqId;
    return CMS_OK;
}

/* ==================== Abort ==================== */

int cms_ffi_encode_abort(
    int64_t req_id,
    int64_t abort_reason,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[1024];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    Abort abrt;
    memset(&abrt, 0, sizeof(abrt));
    abrt.reqId = req_id;
    abrt.abortReason = (AbortReason)abort_reason;

    encode_Abort(&w, &abrt);
    size_t asdu_len = per_stream_bytes_written(&w);

    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, false, CMS_SVC_ABORT,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;

    *out_len = (int)apdu_len;
    return CMS_OK;
}

int cms_ffi_decode_abort(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id,
    int64_t *abort_reason)
{
    cms_apch_t apch;
    const uint8_t *asdu;
    size_t asdu_len;

    int ret = cms_apdu_decode(in_buf, (size_t)in_len, &apch, &asdu, &asdu_len);
    if (ret != 0) return CMS_ERR;

    per_stream_t r;
    per_stream_init_read(&r, asdu, asdu_len);

    Abort abrt;
    memset(&abrt, 0, sizeof(abrt));
    decode_Abort(&r, &abrt);

    *req_id = abrt.reqId;
    *abort_reason = (int64_t)abrt.abortReason;
    return CMS_OK;
}
