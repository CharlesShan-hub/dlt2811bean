#ifndef CMS_VISIBLE_STRING_255_H
#define CMS_VISIBLE_STRING_255_H

#include "cms_types.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * VisibleString255 ::= VisibleString (SIZE(0..255))
 *
 * 结构复用 cms_uint8_array_t { uint8_t* value; int32_t len; }
 * ============================================================
 */

#define CMS_VISIBLE_STRING_255_MAX_LEN 255

typedef cms_uint8_array_t cms_visible_string255_t;

#ifdef __cplusplus
}
#endif

#endif
