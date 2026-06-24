#ifndef CMSPER_TYPES_H
#define CMSPER_TYPES_H

#include <stdint.h>
#include <stdbool.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * @file cms_types.h
 * @brief Core error codes for the PER codec library.
 *
 * All per_* and cms_* functions return per_error_t to indicate success
 * or a specific error condition.
 */
typedef enum {
    PER_OK               = 0,   /* Operation completed successfully. */
    PER_ERR_OVERFLOW     = -1,  /* Buffer overflow — output buffer too small. */
    PER_ERR_RANGE        = -2,  /* Value out of constrained range. */
    PER_ERR_INVALID_ARG  = -3,  /* NULL pointer or invalid argument. */
    PER_ERR_TRUNCATED    = -4,  /* Input truncated during decode. */
    PER_ERR_LENGTH       = -5,  /* Length constraint violation. */
    PER_ERR_OOM          = -6,  /* Out of memory (dynamic mode only). */
} per_error_t;

#ifdef __cplusplus
}
#endif

#endif
