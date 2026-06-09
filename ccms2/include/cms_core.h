#ifndef CMS2_CORE_H
#define CMS2_CORE_H

#include <stdint.h>
#include <stddef.h>
#include <stdbool.h>

#ifdef _MSC_VER
  #define CMS2_EXPORT __declspec(dllexport)
#else
  #define CMS2_EXPORT __attribute__((visibility("default")))
#endif

#define CMS2_OK       0
#define CMS2_ERR     -1

/* ==================== PER error codes ==================== */
typedef enum {
    PER2_OK               = 0,
    PER2_ERR_OVERFLOW     = -1,
    PER2_ERR_RANGE        = -2,
    PER2_ERR_INVALID_ARG  = -3,
    PER2_ERR_TRUNCATED    = -4,
    PER2_ERR_LENGTH       = -5,
    PER2_ERR_OOM          = -6,
} per2_error_t;

#endif
