#ifndef CMS2_TYPES_H
#define CMS2_TYPES_H

#include "cms_core.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/* ==================== Generic Array (all pointers) ==================== */

typedef struct {
    void  **elements;
    int32_t count;
} cms_array_t;          /* sizeof=16 */

#ifdef __cplusplus
}
#endif

#endif
