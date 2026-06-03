#ifndef DATA_BLOCK_CMS_SGCB_H
#define DATA_BLOCK_CMS_SGCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_sequence.h"
#include "data/basic/cms_integer.h"
#include "data/common/cms_time_stamp.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SGCB (Setting Group Control Block)
 *
 * ASN.1 definition:
 * SGCB ::= SEQUENCE {
 *     numOfSG       [1] IMPLICIT INT8U,
 *     actSG         [2] IMPLICIT INT8U,
 *     editSG        [3] IMPLICIT INT8U,
 *     tActEdt       [4] IMPLICIT TimeStamp,
 *     resvTms       [5] IMPLICIT INT16U OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    uint8_t     numOfSG;               /* INT8U */
    uint8_t     actSG;                 /* INT8U */
    uint8_t     editSG;                /* INT8U */
    cms_utc_time_t tActEdt;            /* TimeStamp (alias for UtcTime) */
    uint16_t    resvTms;               /* INT16U OPTIONAL */
    int         resvTms_present;       /* 1 if present */
} cms_sgcb_t;

CMS_EXPORT int cms_sgcb_encode(const cms_sgcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_sgcb_decode(const uint8_t *in_buf, int in_len, cms_sgcb_t *value);
int cms_sgcb_encode_stream(per_stream_t *s, const cms_sgcb_t *value);
int cms_sgcb_decode_stream(per_stream_t *s, cms_sgcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
