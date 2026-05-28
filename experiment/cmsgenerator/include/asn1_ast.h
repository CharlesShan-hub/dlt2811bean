#ifndef ASN1_AST_H
#define ASN1_AST_H

#include <stdint.h>

/* Type of definition */
typedef enum {
    DEF_SEQUENCE,
    DEF_CHOICE,
    DEF_ENUMERATED,
    DEF_INTEGER,     /* constrained INTEGER(lb..ub) */
    DEF_BOOLEAN,
    DEF_REAL,
    DEF_BIT_STRING,  /* BIT STRING (SIZE(n)) */
    DEF_OCTET_STRING,/* OCTET STRING with optional SIZE */
    DEF_VISIBLE_STRING,
    DEF_UTF8STRING,
    DEF_TYPE_REF,    /* reference to another defined type */
} def_kind_t;

typedef struct asn1_type asn1_type_t;
typedef struct asn1_field asn1_field_t;
typedef struct asn1_enum_value asn1_enum_value_t;
typedef struct asn1_def asn1_def_t;

/* A type expression */
struct asn1_type {
    def_kind_t kind;
    char type_name[128];   /* for TYPE_REF: the referenced type name */
    int has_lower;         /* INTEGER range or string/bitstring SIZE */
    int has_upper;
    int64_t lower_bound;
    int64_t upper_bound;
    /* for SEQUENCE/CHOICE: list of fields */
    int field_count;
    asn1_field_t **fields;
    int is_sequence_of;    /* SEQUENCE OF — fields[0] contains the element type */
};

/* A field in a SEQUENCE or alternative in CHOICE */
struct asn1_field {
    char name[128];
    char tag_str[32];      /* e.g. "[0]" or "" */
    asn1_type_t *type;
    int is_optional;
    int is_implicit;       /* IMPLICIT tag */
    char default_str[256]; /* DEFAULT value string */
};

/* An enum value */
struct asn1_enum_value {
    char name[128];
    int64_t value;
};

/* A top-level definition */
struct asn1_def {
    char name[128];
    def_kind_t kind;
    /* for SEQUENCE/CHOICE */
    int field_count;
    asn1_field_t **fields;
    /* for ENUMERATED */
    int enum_count;
    asn1_enum_value_t *enum_values;
    /* for INTEGER/BIT_STRING/STRING with constraints */
    int has_lower, has_upper;
    int64_t lower_bound, upper_bound;
    /* for TYPE_REF */
    char ref_name[128];
};

/* The whole ASN.1 module */
typedef struct {
    int def_count;
    asn1_def_t **defs;
} asn1_module_t;

void asn1_type_free(asn1_type_t *t);
void asn1_module_free(asn1_module_t *mod);

/* Look up a definition by name */
asn1_def_t *asn1_find_def(asn1_module_t *mod, const char *name);

#endif
