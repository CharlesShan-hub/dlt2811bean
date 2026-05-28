#ifndef ASN1_PARSER_H
#define ASN1_PARSER_H

#include "asn1_ast.h"
#include "asn1_lexer.h"

typedef struct {
    lexer_t   lexer;
    token_t   cur;
    int       error;
    char      error_msg[256];
} parser_t;

void parser_init(parser_t *p, const char *input);
void parser_advance(parser_t *p);
int parser_expect(parser_t *p, token_kind_t kind);
asn1_module_t *parser_parse_module(parser_t *p);

#endif
