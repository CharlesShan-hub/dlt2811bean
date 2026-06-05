#ifndef DATA_BLOCK_CMS_SGCB_H
#define DATA_BLOCK_CMS_SGCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/common/cms_time_stamp.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SGCB ::= SEQUENCE {
 *     numOfSG   [1] IMPLICIT INT8U,
 *     actSG     [2] IMPLICIT INT8U,
 *     editSG    [3] IMPLICIT INT8U,
 *     tActEdt   [4] IMPLICIT TimeStamp,
 *     resvTms   [5] IMPLICIT INT16U OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_int8u_t      numOfSG;
    cms_int8u_t      actSG;
    cms_int8u_t      editSG;
    cms_utc_time_t   tActEdt;
    cms_int16u_t     resvTms;
    cms_boolean_t    resvTms_present;
} cms_sgcb_t;

CMS_EXPORT int cms_sgcb_encode(const cms_sgcb_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_sgcb_decode(cms_sgcb_t *v, const uint8_t *in_buf, int in_len);
int cms_sgcb_encode_stream(per_stream_t *s, const cms_sgcb_t *v);
int cms_sgcb_decode_stream(per_stream_t *s, cms_sgcb_t *v);

#ifdef __cplusplus
}
#endif

#endif
