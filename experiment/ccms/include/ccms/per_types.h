#ifndef CMSPER_TYPES_H
#define CMSPER_TYPES_H

#include <stdint.h>
#include <stdbool.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Error codes returned by all per_* functions */
typedef enum {
    PER_OK               = 0,
    PER_ERR_OVERFLOW     = -1,   /* buffer overflow */
    PER_ERR_RANGE        = -2,   /* value out of constrained range */
    PER_ERR_INVALID_ARG  = -3,   /* NULL pointer or invalid argument */
    PER_ERR_TRUNCATED    = -4,   /* input truncated during decode */
    PER_ERR_LENGTH       = -5,   /* length constraint violation */
} per_error_t;

#ifdef __cplusplus
}
#endif

#endif
