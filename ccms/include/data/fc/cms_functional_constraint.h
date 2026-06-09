#ifndef CMS_FC_FUNCTIONAL_CONSTRAINT_H
#define CMS_FC_FUNCTIONAL_CONSTRAINT_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/string/cms_visible_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * FunctionalConstraint ::= VisibleString (SIZE(2))  —  7.4
 * PER: fixed-size VisibleString, 2 bytes aligned, no length prefix.
 */

#define CMS_FUNCTIONAL_CONSTRAINT_LEN 2

typedef cms_uint8_array_t cms_functional_constraint_t;

int cms_functional_constraint_encode_stream(per_stream_t *s, const void *ptr);
int cms_functional_constraint_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_functional_constraint_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_functional_constraint_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
