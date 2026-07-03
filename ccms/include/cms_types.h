#ifndef CMS_TYPES_H
#define CMS_TYPES_H

#include <stdint.h>
#include <stddef.h>
#include <stdbool.h>

#ifdef _MSC_VER
#define CMS_EXPORT __declspec(dllexport)
#else
#define CMS_EXPORT __attribute__((visibility("default")))
#endif

#define CMS_OK 0
/*
 * CMS_RETRY — returned by decode when a SEQUENCE OF has more elements
 * than pre-allocated slots (elements[i] == NULL). The count field has
 * already been written; caller can read it, resize, and retry.
 */
#define CMS_RETRY -2
#define CMS_ERR -1

#ifdef __cplusplus
extern "C" {
#endif

/* ==================== Generic Array (all pointers) ==================== */

typedef struct {
    void **elements;
    int32_t count;
} cms_array_t; /* sizeof=16 */

#ifdef __cplusplus
}
#endif

#endif
