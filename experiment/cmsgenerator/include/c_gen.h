#ifndef C_GEN_H
#define C_GEN_H

#include "asn1_ast.h"
#include <stdio.h>

void gen_header(FILE *out, asn1_module_t *mod, const char *module_name);
void gen_source(FILE *out, asn1_module_t *mod, const char *module_name);

#endif
