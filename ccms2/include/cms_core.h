#ifndef CMS2_CORE_H
#define CMS2_CORE_H

#include <stdint.h>
#include <stddef.h>
#include <stdbool.h>

#ifdef _MSC_VER
  #define CMS_EXPORT  __declspec(dllexport)
  #define CMS2_EXPORT __declspec(dllexport)
#else
  #define CMS_EXPORT  __attribute__((visibility("default")))
  #define CMS2_EXPORT __attribute__((visibility("default")))
#endif

#define CMS_OK       0
#define CMS_ERR     -1
#define CMS2_OK      0
#define CMS2_ERR    -1

/* ==================== PER error codes (from ccms per/cms_types.h) ==================== */
/* per_error_t is defined in per/cms_types.h — do not redefine here */

#endif
