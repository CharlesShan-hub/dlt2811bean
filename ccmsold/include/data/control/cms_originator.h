#ifndef DATA_CONTROL_CMS_ORIGINATOR_H
#define DATA_CONTROL_CMS_ORIGINATOR_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Originator ::= SEQUENCE {
 *     or-cat   [0] IMPLICIT ENUMERATED { ... (0..8) },
 *     or-ident [1] IMPLICIT OCTET STRING (SIZE(0..64))
 * }
 * ============================================================
 */

#define CMS_ORIGINATOR_ORCAT_NOT_SUPPORTED 0
#define CMS_ORIGINATOR_ORCAT_BAY_CONTROL 1
#define CMS_ORIGINATOR_ORCAT_STATION_CONTROL 2
#define CMS_ORIGINATOR_ORCAT_REMOTE_CONTROL 3
#define CMS_ORIGINATOR_ORCAT_AUTOMATIC_BAY 4
#define CMS_ORIGINATOR_ORCAT_AUTOMATIC_STATION 5
#define CMS_ORIGINATOR_ORCAT_AUTOMATIC_REMOTE 6
#define CMS_ORIGINATOR_ORCAT_MAINTENANCE 7
#define CMS_ORIGINATOR_ORCAT_PROCESS 8

#define CMS_ORIGINATOR_OR_IDENT_MAX_LEN 64

typedef struct {
    cms_int32_t         or_cat;
    cms_uint8_array_t   or_ident;
} cms_originator_t;

CMS_EXPORT int cms_originator_encode(const cms_originator_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_originator_decode(cms_originator_t *v, const uint8_t *in_buf, int in_len);
int cms_originator_encode_stream(per_stream_t *s, const cms_originator_t *v);
int cms_originator_decode_stream(per_stream_t *s, cms_originator_t *v);

#ifdef __cplusplus
}
#endif

#endif
