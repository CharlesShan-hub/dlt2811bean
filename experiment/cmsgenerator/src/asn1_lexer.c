#include "asn1_lexer.h"
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

void lexer_init(lexer_t *lx, const char *input) {
    lx->input = input;
    lx->pos = 0;
    lx->line = 1;
    lx->error = 0;
}

static int peek(lexer_t *lx) {
    return lx->input[lx->pos];
}

static int advance(lexer_t *lx) {
    int ch = lx->input[lx->pos];
    if (ch == '\n') lx->line++;
    if (ch != '\0') lx->pos++;
    return ch;
}

static int is_ident_start(int ch) {
    return isalpha(ch) || ch == '_';
}

static int is_ident_continue(int ch) {
    return isalnum(ch) || ch == '_' || ch == '-';
}

void lexer_skip_until_newline(lexer_t *lx) {
    while (peek(lx) && peek(lx) != '\n') advance(lx);
}

token_t lexer_next(lexer_t *lx) {
    token_t tok;
    memset(&tok, 0, sizeof(tok));
    tok.line = lx->line;

    /* skip whitespace and comments */
    while (1) {
        int ch = peek(lx);
        if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
            advance(lx);
            continue;
        }
        /* -- style comment */
        if (ch == '-' && lx->input[lx->pos + 1] == '-') {
            lexer_skip_until_newline(lx);
            continue;
        }
        break;
    }

    int ch = advance(lx);
    tok.line = lx->line;

    if (ch == '\0') {
        tok.kind = TOK_EOF;
        return tok;
    }

    /* identifiers and keywords */
    if (is_ident_start(ch)) {
        int i = 0;
        tok.text[i++] = (char)ch;
        while (is_ident_continue(peek(lx)) && i < 255) {
            tok.text[i++] = (char)advance(lx);
        }
        tok.text[i] = '\0';

        /* keyword matching */
        if (strcmp(tok.text, "BEGIN") == 0)           tok.kind = TOK_BEGIN;
        else if (strcmp(tok.text, "END") == 0)         tok.kind = TOK_END;
        else if (strcmp(tok.text, "DEFINITIONS") == 0) tok.kind = TOK_DEFINITIONS;
        else if (strcmp(tok.text, "EXPLICIT") == 0)    tok.kind = TOK_EXPLICIT;
        else if (strcmp(tok.text, "IMPLICIT") == 0)    tok.kind = TOK_IMPLICIT;
        else if (strcmp(tok.text, "AUTOMATIC") == 0)   tok.kind = TOK_AUTOMATIC;
        else if (strcmp(tok.text, "TAGS") == 0)        tok.kind = TOK_TAGS;
        else if (strcmp(tok.text, "SEQUENCE") == 0)    tok.kind = TOK_SEQUENCE;
        else if (strcmp(tok.text, "CHOICE") == 0)      tok.kind = TOK_CHOICE;
        else if (strcmp(tok.text, "ENUMERATED") == 0)  tok.kind = TOK_ENUMERATED;
        else if (strcmp(tok.text, "INTEGER") == 0)     tok.kind = TOK_INTEGER;
        else if (strcmp(tok.text, "BOOLEAN") == 0)     tok.kind = TOK_BOOLEAN;
        else if (strcmp(tok.text, "REAL") == 0)        tok.kind = TOK_REAL;
        else if (strcmp(tok.text, "BIT") == 0) {
            /* "BIT STRING" */
            while (peek(lx) == ' ') advance(lx);
            if (peek(lx) == 'S') {
                int i2 = 0;
                char buf[32];
                while (is_ident_continue(peek(lx)) && i2 < 31) buf[i2++] = (char)advance(lx);
                buf[i2] = '\0';
                if (strcmp(buf, "STRING") == 0) {
                    tok.kind = TOK_BIT_STRING;
                    strcpy(tok.text, "BIT STRING");
                }
            }
        }
        else if (strcmp(tok.text, "OCTET") == 0) {
            while (peek(lx) == ' ') advance(lx);
            if (peek(lx) == 'S') {
                int i2 = 0;
                char buf[32];
                while (is_ident_continue(peek(lx)) && i2 < 31) buf[i2++] = (char)advance(lx);
                buf[i2] = '\0';
                if (strcmp(buf, "STRING") == 0) {
                    tok.kind = TOK_OCTET_STRING;
                    strcpy(tok.text, "OCTET STRING");
                }
            }
        }
        else if (strcmp(tok.text, "VisibleString") == 0)   tok.kind = TOK_VISIBLE_STRING;
        else if (strcmp(tok.text, "UTF8String") == 0)      tok.kind = TOK_UTF8STRING;
        else if (strcmp(tok.text, "SIZE") == 0)            tok.kind = TOK_SIZED;
        else if (strcmp(tok.text, "OPTIONAL") == 0)        tok.kind = TOK_OPTIONAL;
        else if (strcmp(tok.text, "DEFAULT") == 0)         tok.kind = TOK_DEFAULT;
        else if (strcmp(tok.text, "INCLUDE") == 0)         tok.kind = TOK_INCLUDE;
        else if (strcmp(tok.text, "EXPORTS") == 0)         tok.kind = TOK_EXPORTS;
        else if (strcmp(tok.text, "IMPORTS") == 0)         tok.kind = TOK_IMPORTS;
        else if (strcmp(tok.text, "FROM") == 0)            tok.kind = TOK_FROM;
        else if (strcmp(tok.text, "ALL") == 0)             tok.kind = TOK_ALL;
        else tok.kind = TOK_IDENT;

        return tok;
    }

    /* numbers */
    if (isdigit(ch) || (ch == '-' && isdigit(peek(lx)))) {
        int i = 0;
        tok.text[i++] = (char)ch;
        while (isdigit(peek(lx)) && i < 255) {
            tok.text[i++] = (char)advance(lx);
        }
        tok.text[i] = '\0';
        tok.kind = TOK_NUMBER;
        return tok;
    }

    /* multi-char tokens */
    if (ch == ':' && peek(lx) == ':') { advance(lx);
        if (peek(lx) == '=') { advance(lx); tok.kind = TOK_COLON; strcpy(tok.text, "::="); }
        return tok;
    }
    if (ch == '.' && peek(lx) == '.') { advance(lx); tok.kind = TOK_DOTDOT; strcpy(tok.text, ".."); return tok; }

    /* single char */
    tok.text[0] = (char)ch; tok.text[1] = '\0';
    switch (ch) {
        case '{': tok.kind = TOK_LBRACE; break;
        case '}': tok.kind = TOK_RBRACE; break;
        case '[': tok.kind = TOK_LBRACKET; break;
        case ']': tok.kind = TOK_RBRACKET; break;
        case '(': tok.kind = TOK_LPAREN; break;
        case ')': tok.kind = TOK_RPAREN; break;
        case ',': tok.kind = TOK_COMMA; break;
        case ';': tok.kind = TOK_SEMICOLON; break;
        case '=': tok.kind = TOK_EQUALS; break;
        case '.': tok.kind = TOK_DOT; break;
        case '-': tok.kind = TOK_MINUS; break;
        default:  tok.kind = TOK_IDENT; break; /* fallback */
    }
    return tok;
}
