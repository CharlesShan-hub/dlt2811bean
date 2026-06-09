#ifndef TEST_UTILS_H
#define TEST_UTILS_H

#include <stdio.h>

extern int tests_passed;
extern int tests_failed;

#define TEST(name) do { printf("  %-40s ", name); } while(0)
#define PASS() do { printf("PASS\n"); tests_passed++; } while(0)
#define FAIL(msg) do { printf("FAIL: %s\n", msg); tests_failed++; } while(0)
#define ASSERT(cond) do { if (!(cond)) { FAIL(#cond); return; } } while(0)
#define ASSERT_EQ(a, b) do { if ((a) != (b)) { FAIL(#a " == " #b); return; } } while(0)

#endif
