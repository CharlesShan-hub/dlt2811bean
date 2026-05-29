#ifndef CMS_CORE_H
#define CMS_CORE_H

#include <stdint.h>
#include <stddef.h>

#ifdef _MSC_VER
  #define CMS_EXPORT __declspec(dllexport)
#else
  #define CMS_EXPORT __attribute__((visibility("default")))
#endif

#define CMS_OK      0
#define CMS_ERR    -1
#define CMS_ERR_BUF_TOO_SMALL  -2

#endif
