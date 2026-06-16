#ifndef CMS_FC_FUNCTIONAL_CONSTRAINT_H
#define CMS_FC_FUNCTIONAL_CONSTRAINT_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * FunctionalConstraint ::= VisibleString (SIZE(2))  —  7.4
 *
 * PER wire format is VisibleString (SIZE(2)), but the Java API
 * exposes FC as an enumerated int value for type safety.
 *
 * Mapping from int → 2-char FC code happens in encode/decode.
 * Constants match com.ysh.jcms.info.FunctionalConstraint ordinals.
 */

#define CMS_FC_ST   0
#define CMS_FC_MX   1
#define CMS_FC_SP   2
#define CMS_FC_SV   3
#define CMS_FC_CF   4
#define CMS_FC_DC   5
#define CMS_FC_SG   6
#define CMS_FC_SE   7
#define CMS_FC_SR   8
#define CMS_FC_OR   9
#define CMS_FC_BL   10
#define CMS_FC_EX   11
#define CMS_FC_XX   12

typedef struct { int value; } cms_functional_constraint_t;

int cms_functional_constraint_encode_stream(per_stream_t *s, const void *ptr);
int cms_functional_constraint_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_functional_constraint_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_functional_constraint_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
