#include "asn1_parser.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

void parser_init(parser_t *p, const char *input) {
    lexer_init(&p->lexer, input);
    p->error = 0;
    p->error_msg[0] = '\0';
    parser_advance(p);
}

void parser_advance(parser_t *p) {
    p->cur = lexer_next(&p->lexer);
}

int parser_expect(parser_t *p, token_kind_t kind) {
    if (p->cur.kind == kind) { parser_advance(p); return 1; }
    snprintf(p->error_msg, sizeof(p->error_msg),
             "line %d: expected token kind %d, got '%s'", p->cur.line, kind, p->cur.text);
    p->error = 1;
    return 0;
}

static int peek_kind(parser_t *p, token_kind_t k) { return p->cur.kind == k; }

/* Forward declarations */
static asn1_type_t *parse_type(parser_t *p);
static asn1_field_t *parse_field(parser_t *p);
static void parse_enum_body(parser_t *p, asn1_enum_value_t **values, int *count);

/* parse INTEGER constraint: (lb..ub) */
static int parse_range(parser_t *p, int64_t *lb, int64_t *ub) {
    if (!peek_kind(p, TOK_LPAREN)) return 0;
    parser_advance(p); /* ( */
    int has_lb = 0;
    if (peek_kind(p, TOK_NUMBER) || peek_kind(p, TOK_MINUS)) {
        int neg = 0;
        if (peek_kind(p, TOK_MINUS)) { neg = 1; parser_advance(p); }
        if (peek_kind(p, TOK_NUMBER)) {
            *lb = atoll(p->cur.text);
            if (neg) *lb = -*lb;
            has_lb = 1;
            parser_advance(p);
        }
    }
    if (has_lb && peek_kind(p, TOK_DOTDOT)) {
        parser_advance(p); /* .. */
        int neg2 = 0;
        if (peek_kind(p, TOK_MINUS)) { neg2 = 1; parser_advance(p); }
        if (peek_kind(p, TOK_NUMBER)) {
            *ub = atoll(p->cur.text);
            if (neg2) *ub = -*ub;
            parser_advance(p);
        }
        parser_expect(p, TOK_RPAREN);
        return 1;
    }
    if (has_lb) { *ub = *lb; parser_expect(p, TOK_RPAREN); return 1; }
    /* just (number) — single value */
    parser_expect(p, TOK_RPAREN);
    return 1;
}

/* parse SIZE(expr) — may be wrapped in parentheses: (SIZE(lb..ub)) or SIZE(lb) */
static int parse_size_constraint(parser_t *p, int64_t *lb, int64_t *ub) {
    /* optional outer ( */
    int has_paren = 0;
    if (peek_kind(p, TOK_LPAREN)) { has_paren = 1; parser_advance(p); }
    if (!peek_kind(p, TOK_SIZED)) {
        if (has_paren) { /* put back? can't, treat as consumed */ }
        return 0;
    }
    parser_advance(p); /* SIZE */
    parser_expect(p, TOK_LPAREN);
    /* could be SIZE(0..255), SIZE(8), SIZE(0..65535) */
    if (peek_kind(p, TOK_NUMBER) || peek_kind(p, TOK_MINUS)) {
        int neg = 0;
        if (peek_kind(p, TOK_MINUS)) { neg = 1; parser_advance(p); }
        if (peek_kind(p, TOK_NUMBER)) {
            *lb = atoll(p->cur.text);
            if (neg) *lb = -*lb;
            parser_advance(p);
        }
        if (peek_kind(p, TOK_DOTDOT)) {
            parser_advance(p);
            int neg2 = 0;
            if (peek_kind(p, TOK_MINUS)) { neg2 = 1; parser_advance(p); }
            if (peek_kind(p, TOK_NUMBER)) {
                *ub = atoll(p->cur.text);
                if (neg2) *ub = -*ub;
                parser_advance(p);
            }
        } else {
            *ub = *lb;
        }
    }
    parser_expect(p, TOK_RPAREN); /* close SIZE(... */
    if (has_paren) parser_expect(p, TOK_RPAREN); /* close outer (...) */
    return 1;
}

/* parse ENUMERATED { ... } */
static asn1_type_t *parse_enumerated_body(parser_t *p) {
    asn1_type_t *t = calloc(1, sizeof(asn1_type_t));
    t->kind = DEF_ENUMERATED;
    /* We'll just return a simple type; details are handled at def level */
    return t;
}

/* parse SEQUENCE { ... } body, returns first field to link */
static void parse_sequence_body(parser_t *p, asn1_field_t ***fields, int *count) {
    int cap = 16;
    *fields = calloc(cap, sizeof(asn1_field_t *));
    *count = 0;
    int last_pos = -1;

    parser_expect(p, TOK_LBRACE);
    while (!peek_kind(p, TOK_RBRACE) && !peek_kind(p, TOK_EOF)) {
        if (p->lexer.pos == last_pos) { parser_advance(p); continue; }
        last_pos = p->lexer.pos;

        if (*count >= cap) {
            cap *= 2;
            *fields = realloc(*fields, cap * sizeof(asn1_field_t *));
        }
        asn1_field_t *f = parse_field(p);
        if (f) {
            (*fields)[*count] = f;
            (*count)++;
        }
        if (peek_kind(p, TOK_COMMA)) {
            parser_advance(p);
        }
    }
    parser_expect(p, TOK_RBRACE);
}

/* parse a type expression */
static asn1_type_t *parse_type(parser_t *p) {
    asn1_type_t *t = calloc(1, sizeof(asn1_type_t));

    if (peek_kind(p, TOK_SEQUENCE)) {
        t->kind = DEF_SEQUENCE;
        parser_advance(p);
        /* SEQUENCE OF — consume "OF TypeName" */
        if (peek_kind(p, TOK_IDENT) && strcmp(p->cur.text, "OF") == 0) {
            parser_advance(p);
            t->is_sequence_of = 1;
            t->fields = calloc(1, sizeof(asn1_field_t *));
            t->field_count = 1;
            asn1_field_t *elem = calloc(1, sizeof(asn1_field_t));
            strcpy(elem->name, "items");
            elem->type = parse_type(p);
            t->fields[0] = elem;
        } else if (peek_kind(p, TOK_LBRACE)) {
            parse_sequence_body(p, &t->fields, &t->field_count);
        }
        return t;
    }
    if (peek_kind(p, TOK_CHOICE)) {
        t->kind = DEF_CHOICE;
        parser_advance(p);
        if (peek_kind(p, TOK_LBRACE)) {
            parse_sequence_body(p, &t->fields, &t->field_count);
        }
        return t;
    }
    if (peek_kind(p, TOK_ENUMERATED)) {
        t->kind = DEF_ENUMERATED;
        parser_advance(p);
        /* consume optional { val1(0), val2(1), ... } body */
        if (peek_kind(p, TOK_LBRACE)) {
            asn1_enum_value_t *dummy_vals = NULL;
            int dummy_count = 0;
            parse_enum_body(p, &dummy_vals, &dummy_count);
            free(dummy_vals);
        }
        return t;
    }
    if (peek_kind(p, TOK_INTEGER)) {
        t->kind = DEF_INTEGER;
        parser_advance(p);
        parse_range(p, &t->lower_bound, &t->upper_bound);
        return t;
    }
    if (peek_kind(p, TOK_BOOLEAN)) {
        t->kind = DEF_BOOLEAN;
        parser_advance(p);
        return t;
    }
    if (peek_kind(p, TOK_REAL)) {
        t->kind = DEF_REAL;
        parser_advance(p);
        return t;
    }
    if (peek_kind(p, TOK_BIT_STRING)) {
        t->kind = DEF_BIT_STRING;
        parser_advance(p);
        parse_size_constraint(p, &t->lower_bound, &t->upper_bound);
        return t;
    }
    if (peek_kind(p, TOK_OCTET_STRING)) {
        t->kind = DEF_OCTET_STRING;
        parser_advance(p);
        parse_size_constraint(p, &t->lower_bound, &t->upper_bound);
        return t;
    }
    if (peek_kind(p, TOK_VISIBLE_STRING)) {
        t->kind = DEF_VISIBLE_STRING;
        parser_advance(p);
        parse_size_constraint(p, &t->lower_bound, &t->upper_bound);
        return t;
    }
    if (peek_kind(p, TOK_UTF8STRING)) {
        t->kind = DEF_UTF8STRING;
        parser_advance(p);
        parse_size_constraint(p, &t->lower_bound, &t->upper_bound);
        return t;
    }
    /* Type reference (identifier) — may have constraint like (SIZE(...)) */
    if (peek_kind(p, TOK_IDENT) || peek_kind(p, TOK_BIT_STRING) || peek_kind(p, TOK_OCTET_STRING)
        || peek_kind(p, TOK_VISIBLE_STRING) || peek_kind(p, TOK_UTF8STRING)) {
        t->kind = DEF_TYPE_REF;
        strncpy(t->type_name, p->cur.text, sizeof(t->type_name) - 1);
        parser_advance(p);
        /* optional constraint on referenced type: (SIZE(lb..ub)) or (lb..ub) */
        if (peek_kind(p, TOK_LPAREN)) {
            /* Try SIZE(...) first */
            if (!parse_size_constraint(p, &t->lower_bound, &t->upper_bound)) {
                /* fallback: plain range constraint */
                parse_range(p, &t->lower_bound, &t->upper_bound);
            }
            t->has_lower = 1; t->has_upper = 1;
        }
        return t;
    }

    free(t);
    return NULL;
}

/* parse a field: name [tag] type [OPTIONAL] [DEFAULT value] */
static asn1_field_t *parse_field(parser_t *p) {
    asn1_field_t *f = calloc(1, sizeof(asn1_field_t));

    /* field name (ASN.1: name comes BEFORE [tag]) */
    if (!peek_kind(p, TOK_IDENT)) {
        free(f);
        return NULL;
    }
    strncpy(f->name, p->cur.text, sizeof(f->name) - 1);
    parser_advance(p);

    /* optional tag: [0] IMPLICIT, [1], etc. — after name, before type */
    if (peek_kind(p, TOK_LBRACKET)) {
        parser_advance(p);
        int i = 0;
        while (!peek_kind(p, TOK_RBRACKET) && i < 30) {
            f->tag_str[i++] = p->cur.text[0];
            parser_advance(p);
        }
        f->tag_str[i] = '\0';
        parser_expect(p, TOK_RBRACKET);
        if (peek_kind(p, TOK_IDENT) && strcmp(p->cur.text, "IMPLICIT") == 0) {
            f->is_implicit = 1;
            parser_advance(p);
        }
    }

    /* type */
    f->type = parse_type(p);

    /* OPTIONAL / DEFAULT */
    if (peek_kind(p, TOK_OPTIONAL)) {
        f->is_optional = 1;
        parser_advance(p);
    } else if (peek_kind(p, TOK_DEFAULT)) {
        parser_advance(p);
        /* read default value as text */
        int i = 0;
        while (!peek_kind(p, TOK_COMMA) && !peek_kind(p, TOK_RBRACE)
               && !peek_kind(p, TOK_EOF) && peek_kind(p, TOK_IDENT) && i < 255) {
            f->default_str[i++] = p->cur.text[0];
            parser_advance(p);
        }
        f->default_str[i] = '\0';
    }

    return f;
}

/* parse ENUMERATED { val1(0), val2(1), ... } */
static void parse_enum_body(parser_t *p, asn1_enum_value_t **values, int *count) {
    int cap = 32;
    *values = calloc(cap, sizeof(asn1_enum_value_t));
    *count = 0;

    parser_expect(p, TOK_LBRACE);
    while (!peek_kind(p, TOK_RBRACE) && !peek_kind(p, TOK_EOF)) {
        if (peek_kind(p, TOK_IDENT)) {
            asn1_enum_value_t *ev = &(*values)[*count];
            strncpy(ev->name, p->cur.text, sizeof(ev->name) - 1);
            parser_advance(p);
            if (peek_kind(p, TOK_LPAREN)) {
                parser_advance(p);
                if (peek_kind(p, TOK_NUMBER)) {
                    ev->value = atoll(p->cur.text);
                    parser_advance(p);
                }
                parser_expect(p, TOK_RPAREN);
            }
            (*count)++;
            if (*count >= cap) {
                cap *= 2;
                *values = realloc(*values, cap * sizeof(asn1_enum_value_t));
            }
        }
        if (peek_kind(p, TOK_COMMA)) parser_advance(p);
    }
    parser_expect(p, TOK_RBRACE);
}

/* parse a top-level definition: Name ::= type */
static asn1_def_t *parse_definition(parser_t *p) {
    /* Accept TOK_IDENT or any keyword that ASN.1 allows as definition name */
    int is_name = peek_kind(p, TOK_IDENT) || peek_kind(p, TOK_VISIBLE_STRING)
               || peek_kind(p, TOK_UTF8STRING);
    if (is_name) {
        asn1_def_t *d = calloc(1, sizeof(asn1_def_t));
        strncpy(d->name, p->cur.text, sizeof(d->name) - 1);
        parser_advance(p);
        if (!parser_expect(p, TOK_COLON)) { free(d); return NULL; }

        /* now parse the type */
        if (peek_kind(p, TOK_SEQUENCE)) {
            d->kind = DEF_SEQUENCE;
            parser_advance(p);
            if (peek_kind(p, TOK_LBRACE)) {
                parse_sequence_body(p, &d->fields, &d->field_count);
            }
        } else if (peek_kind(p, TOK_CHOICE)) {
            d->kind = DEF_CHOICE;
            parser_advance(p);
            if (peek_kind(p, TOK_LBRACE)) {
                parse_sequence_body(p, &d->fields, &d->field_count);
            }
        } else if (peek_kind(p, TOK_ENUMERATED)) {
            d->kind = DEF_ENUMERATED;
            parser_advance(p);
            parse_enum_body(p, &d->enum_values, &d->enum_count);
        } else if (peek_kind(p, TOK_INTEGER)) {
            d->kind = DEF_INTEGER;
            parser_advance(p);
            if (parse_range(p, &d->lower_bound, &d->upper_bound)) { d->has_lower = 1; d->has_upper = 1; }
        } else if (peek_kind(p, TOK_BOOLEAN)) {
            d->kind = DEF_BOOLEAN;
            parser_advance(p);
        } else if (peek_kind(p, TOK_REAL)) {
            d->kind = DEF_REAL;
            parser_advance(p);
        } else if (peek_kind(p, TOK_BIT_STRING)) {
            d->kind = DEF_BIT_STRING;
            parser_advance(p);
            if (parse_size_constraint(p, &d->lower_bound, &d->upper_bound)) { d->has_lower = 1; d->has_upper = 1; }
        } else if (peek_kind(p, TOK_OCTET_STRING)) {
            d->kind = DEF_OCTET_STRING;
            parser_advance(p);
            if (parse_size_constraint(p, &d->lower_bound, &d->upper_bound)) { d->has_lower = 1; d->has_upper = 1; }
        } else if (peek_kind(p, TOK_VISIBLE_STRING)) {
            d->kind = DEF_VISIBLE_STRING;
            parser_advance(p);
            if (parse_size_constraint(p, &d->lower_bound, &d->upper_bound)) { d->has_lower = 1; d->has_upper = 1; }
        } else if (peek_kind(p, TOK_UTF8STRING)) {
            d->kind = DEF_UTF8STRING;
            parser_advance(p);
            if (parse_size_constraint(p, &d->lower_bound, &d->upper_bound)) { d->has_lower = 1; d->has_upper = 1; }
        } else if (peek_kind(p, TOK_IDENT)) {
            d->kind = DEF_TYPE_REF;
            strncpy(d->ref_name, p->cur.text, sizeof(d->ref_name) - 1);
            parser_advance(p);
        } else {
            snprintf(p->error_msg, sizeof(p->error_msg),
                     "line %d: unexpected token '%s' in definition", p->cur.line, p->cur.text);
            p->error = 1;
            free(d);
            return NULL;
        }

        return d;
    }
    return NULL;
}

asn1_module_t *parser_parse_module(parser_t *p) {
    asn1_module_t *mod = calloc(1, sizeof(asn1_module_t));
    int cap = 64;
    mod->defs = calloc(cap, sizeof(asn1_def_t *));
    mod->def_count = 0;

    /* skip module header: Name { OID } DEFINITIONS [tag-default] ::= BEGIN */
    int is_ident = peek_kind(p, TOK_IDENT);
    if (is_ident) {
        parser_advance(p);
    }
    if (peek_kind(p, TOK_LBRACE)) {
        while (!peek_kind(p, TOK_RBRACE) && !peek_kind(p, TOK_EOF)) parser_advance(p);
        if (peek_kind(p, TOK_RBRACE)) parser_advance(p);
    }
    if (peek_kind(p, TOK_DEFINITIONS)) {
        parser_advance(p);
    }
    /* skip optional tag-default (AUTOMATIC|EXPLICIT|IMPLICIT TAGS) between DEFINITIONS and ::= */
    while (peek_kind(p, TOK_AUTOMATIC) || peek_kind(p, TOK_EXPLICIT)
           || peek_kind(p, TOK_IMPLICIT) || peek_kind(p, TOK_TAGS)) {
        parser_advance(p);
    }
    if (peek_kind(p, TOK_COLON)) {
        parser_advance(p);   /* ::= */
    }
    if (peek_kind(p, TOK_BEGIN)) {
        parser_advance(p);
    }

    /* skip IMPORTS / EXPORTS sections */
    if (peek_kind(p, TOK_IMPORTS)) {
        while (!peek_kind(p, TOK_SEMICOLON) && !peek_kind(p, TOK_EOF)) parser_advance(p);
        if (peek_kind(p, TOK_SEMICOLON)) parser_advance(p);
    }
    if (peek_kind(p, TOK_EXPORTS)) {
        while (!peek_kind(p, TOK_SEMICOLON) && !peek_kind(p, TOK_EOF)) parser_advance(p);
        if (peek_kind(p, TOK_SEMICOLON)) parser_advance(p);
    }

    /* parse definitions until END */
    int last_pos = -1;
    while (!peek_kind(p, TOK_END) && !peek_kind(p, TOK_EOF)) {
        if (p->lexer.pos == last_pos) { parser_advance(p); }
        last_pos = p->lexer.pos;

        if (mod->def_count >= cap) {
            cap *= 2;
            mod->defs = realloc(mod->defs, cap * sizeof(asn1_def_t *));
        }
        asn1_def_t *d = parse_definition(p);
        if (d) {
            mod->defs[mod->def_count++] = d;
        } else {
            parser_advance(p);
        }
    }

    /* consume END token */
    if (peek_kind(p, TOK_END)) {
        parser_advance(p);
    }

    return mod;
}

void asn1_type_free(asn1_type_t *t) {
    if (!t) return;
    for (int i = 0; i < t->field_count; i++) {
        if (t->fields[i]) {
            asn1_type_free(t->fields[i]->type);
            free(t->fields[i]);
        }
    }
    free(t->fields);
    free(t);
}

void asn1_module_free(asn1_module_t *mod) {
    for (int i = 0; i < mod->def_count; i++) {
        asn1_def_t *d = mod->defs[i];
        for (int j = 0; j < d->field_count; j++) {
            if (d->fields[j]) {
                asn1_type_free(d->fields[j]->type);
                free(d->fields[j]);
            }
        }
        free(d->fields);
        free(d->enum_values);
        free(d);
    }
    free(mod->defs);
    free(mod);
}

asn1_def_t *asn1_find_def(asn1_module_t *mod, const char *name) {
    for (int i = 0; i < mod->def_count; i++) {
        if (strcmp(mod->defs[i]->name, name) == 0) return mod->defs[i];
    }
    return NULL;
}
