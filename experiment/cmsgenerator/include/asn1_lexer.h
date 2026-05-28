#ifndef ASN1_LEXER_H
#define ASN1_LEXER_H

typedef enum {
    TOK_EOF,
    TOK_IDENT,        /* e.g. SEQUENCE, BRCB, Int32U */
    TOK_NUMBER,       /* e.g. 0, 128, 65535 */
    TOK_STRING,       /* e.g. "DLT2811-DataTypes" */
    TOK_OID,          /* e.g. { 1 2 156 2811 1 } */
    TOK_LBRACE,       /* { */
    TOK_RBRACE,       /* } */
    TOK_LBRACKET,     /* [ */
    TOK_RBRACKET,     /* ] */
    TOK_LPAREN,       /* ( */
    TOK_RPAREN,       /* ) */
    TOK_COMMA,
    TOK_DOT,
    TOK_DOTDOT,       /* .. */
    TOK_COLON,        /* ::= */
    TOK_EQUALS,       /* = */
    TOK_MINUS,        /* - */
    TOK_SEMICOLON,    /* -- comment prefix */
    TOK_BEGIN,
    TOK_END,
    TOK_DEFINITIONS,
    TOK_EXPLICIT,
    TOK_IMPLICIT,
    TOK_AUTOMATIC,
    TOK_TAGS,
    TOK_SEQUENCE,
    TOK_CHOICE,
    TOK_ENUMERATED,
    TOK_INTEGER,
    TOK_BOOLEAN,
    TOK_REAL,
    TOK_BIT_STRING,
    TOK_OCTET_STRING,
    TOK_VISIBLE_STRING,
    TOK_UTF8STRING,
    TOK_SIZE,
    TOK_OPTIONAL,
    TOK_DEFAULT,
    TOK_SIZED,        /* SIZE */
    TOK_INCLUDE,
    TOK_EXPORTS,
    TOK_IMPORTS,
    TOK_FROM,
    TOK_ALL,          /* ... extension marker (approximated) */
} token_kind_t;

typedef struct {
    token_kind_t kind;
    char        text[256];
    int         line;
} token_t;

typedef struct {
    const char *input;
    int         pos;
    int         line;
    int         error;  /* non-zero if lexical error */
} lexer_t;

void lexer_init(lexer_t *lx, const char *input);
token_t lexer_next(lexer_t *lx);
void lexer_skip_until_newline(lexer_t *lx);

#endif
