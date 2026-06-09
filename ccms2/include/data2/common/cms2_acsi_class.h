#ifndef CMS2_ACSI_CLASS_H
#define CMS2_ACSI_CLASS_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ACSIClass ::= INTEGER {
 *     reserved       (0),
 *     data-object    (1),
 *     data-set       (2),
 *     brcb           (3),
 *     urcb           (4),
 *     lcb            (5),
 *     log            (6),
 *     sgecb          (7),
 *     gocb           (8),
 *     msvcb          (10)
 * } (0..10)
 *
 * In all-pointer mode, a cms2_acsi_class_t is just { int32_t value; }
 * but it's accessed through a void* pointer in the parent struct.
 */
#define CMS2_ACSI_CLASS_RESERVED       0
#define CMS2_ACSI_CLASS_DATA_OBJECT    1
#define CMS2_ACSI_CLASS_DATA_SET       2
#define CMS2_ACSI_CLASS_BRCB           3
#define CMS2_ACSI_CLASS_URCB           4
#define CMS2_ACSI_CLASS_LCB            5
#define CMS2_ACSI_CLASS_LOG            6
#define CMS2_ACSI_CLASS_SGECB          7
#define CMS2_ACSI_CLASS_GOCB           8
#define CMS2_ACSI_CLASS_MSVCB         10

typedef struct { int32_t value; } cms2_acsi_class_t;

#ifdef __cplusplus
}
#endif

#endif
