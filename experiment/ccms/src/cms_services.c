#include "cms_services.h"
#include "cms_apdu.h"
#include "per/cms_stream.h"
#include "per/cms_boolean.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include <string.h>
#include <stdlib.h>

/* ==================== Associate-Request ==================== */

/* Associate-Request */
CMS_EXPORT int cms_associate_request_encode(
    int64_t req_id,
    const char *sap_ref,
    int has_auth,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[1024];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    per_encode_constrained_int(&w, req_id, 0, 65535);
    per_encode_visible_string(&w, sap_ref, 129);
    per_encode_boolean(&w, has_auth ? 1 : 0);
    if (has_auth) {
        per_encode_boolean(&w, 0);
    }
    size_t asdu_len = per_stream_bytes_written(&w);

    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, false, CMS_SVC_ASSOCIATE,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;

    *out_len = (int)apdu_len;
    return CMS_OK;
}

CMS_EXPORT int cms_associate_request_decode(
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

    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *req_id = tmp;

    per_decode_visible_string(&r, sap_ref, 129);
    *sap_ref_cap = (int)strlen(sap_ref);

    bool b;
    per_decode_boolean(&r, &b);
    *has_auth = b ? 1 : 0;

    return CMS_OK;
}

/* ==================== Release-Request ==================== */

CMS_EXPORT int cms_release_request_encode(
    int64_t req_id,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[1024];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    per_encode_constrained_int(&w, req_id, 0, 65535);
    size_t asdu_len = per_stream_bytes_written(&w);

    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, false, CMS_SVC_RELEASE,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;

    *out_len = (int)apdu_len;
    return CMS_OK;
}

/* Release-Request */
CMS_EXPORT int cms_release_request_decode(
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

    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *req_id = tmp;

    return CMS_OK;
}

/* ==================== Abort ==================== */

/* Abort */
CMS_EXPORT int cms_abort_encode(
    int64_t req_id,
    int64_t abort_reason,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[1024];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    per_encode_constrained_int(&w, req_id, 0, 65535);
    per_encode_constrained_int(&w, abort_reason, 0, 8);
    size_t asdu_len = per_stream_bytes_written(&w);

    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, false, CMS_SVC_ABORT,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;

    *out_len = (int)apdu_len;
    return CMS_OK;
}

CMS_EXPORT int cms_abort_decode(
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

    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *req_id = tmp;

    per_decode_constrained_int(&r, &tmp, 0, 8);
    *abort_reason = tmp;

    return CMS_OK;
}
