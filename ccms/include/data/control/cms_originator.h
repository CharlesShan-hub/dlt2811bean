#ifndef CMS_CONTROL_ORIGINATOR_H
#define CMS_CONTROL_ORIGINATOR_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/control/cms_or_cat.h"
#include "data/string/cms_octet_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Originator ::= SEQUENCE {
 *     orCat        [0] IMPLICIT INTEGER (0..8),
 *     orIdent      [1] IMPLICIT OCTET STRING (SIZE(0..64))
 * }  —  7.5.2
 *
 * All-pointer layout (sizeof = 2 * 8 = 16):
 *   [0] orCat    → cms_or_cat_t*
 *   [8] orIdent  → cms_uint8_array_t* (OCTET STRING, max 64)
 */

#define CMS_OR_IDENT_MAX_LEN 64

typedef struct {
    void *orCat;   /* cms_or_cat_t* */
    void *orIdent; /* cms_uint8_array_t* */
} cms_originator_t;

int cms_originator_encode_stream(per_stream_t *s, const void *ptr);
int cms_originator_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_originator_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_originator_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
