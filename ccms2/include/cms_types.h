#ifndef CMS2_TYPES_H
#define CMS2_TYPES_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

/* ==================== Leaf Types (inline value) ==================== */

typedef struct { int      value; } cms2_boolean_t;     /* sizeof=4 */
typedef struct { int8_t   value; } cms2_int8_t;        /* sizeof=1 */
typedef struct { uint8_t  value; } cms2_int8u_t;       /* sizeof=1 */
typedef struct { int16_t  value; } cms2_int16_t;       /* sizeof=2 */
typedef struct { uint16_t value; } cms2_int16u_t;      /* sizeof=2 */
typedef struct { int32_t  value; } cms2_int32_t;       /* sizeof=4 */
typedef struct { uint32_t value; } cms2_int32u_t;      /* sizeof=4 */

/* ==================== String Types (pointer + len) ==================== */

typedef struct {
    uint8_t *value;     /* 8 bytes */
    int32_t  len;       /* 4 bytes */
} cms2_uint8_array_t;   /* sizeof=16 */

/* ==================== Generic Array (all pointers) ==================== */

/* 
 * Generic array: elements is a pointer to an array of void* pointers.
 * sizeof = 16 (void** + int32_t + padding)
 */
typedef struct {
    void  **elements;
    int32_t count;
} cms2_array_t;

/* ==================== Generic CHOICE (all pointers) ==================== */

/*
 * Generic CHOICE base:
 *   choice   = which alternative (0..n-1)
 *   alts     = array of void* pointers, one per alternative
 * sizeof = 4 + 8 * n  (choice + n pointers)
 *
 * Concrete subclasses embed this at the top.
 */
typedef struct {
    int32_t  choice;
    void    *alts[1];    /* flexible; actual size determined by subclass */
} cms2_choice_base_t;

#ifdef __cplusplus
}
#endif

#endif
