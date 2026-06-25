#ifndef CMS_BLOCK_SGCB_H
#define CMS_BLOCK_SGCB_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int8u.h"
#include "data/scalar/cms_int16u.h"
#include "data/common/cms_time_stamp.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * SGCB ::= SEQUENCE {
 *     numOfSG   [1] IMPLICIT INT8U,
 *     actSG     [2] IMPLICIT INT8U,
 *     editSG    [3] IMPLICIT INT8U,
 *     tActEdt   [4] IMPLICIT TimeStamp,
 *     resvTms   [5] IMPLICIT INT16U OPTIONAL
 * }  —  8.5
 *
 * All-pointer layout (sizeof = 5 * 8 = 40):
 *   [0]  numOfSG   → cms_int8u_t*
 *   [8]  actSG     → cms_int8u_t*
 *   [16] editSG    → cms_int8u_t*
 *   [24] tActEdt   → cms_time_stamp_t*
 *   [32] resvTms   → cms_int16u_t* (NULL → absent)
 */

typedef struct {
    cms_int8u_t       *numOfSG;
    cms_int8u_t       *actSG;
    cms_int8u_t       *editSG;
    cms_time_stamp_t  *tActEdt;
    cms_boolean_t     *resvTms_present;
    cms_int16u_t      *resvTms;   /* OPTIONAL */
} cms_sgcb_t;

int cms_sgcb_encode_stream(per_stream_t *s, const void *ptr);
int cms_sgcb_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_sgcb_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_sgcb_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
