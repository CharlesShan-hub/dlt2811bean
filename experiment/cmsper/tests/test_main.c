#include "test_utils.h"

int tests_passed = 0;
int tests_failed = 0;

void test_stream(void);
void test_boolean(void);
void test_integer(void);
void test_enumerated(void);
void test_string(void);
void test_bit_string(void);
void test_choice(void);

int main(void) {
    printf("=== cmsper test suite ===\n\n");

    test_stream();
    test_boolean();
    test_integer();
    test_enumerated();
    test_string();
    test_bit_string();
    test_choice();

    printf("\nResults: %d passed, %d failed out of %d\n",
           tests_passed, tests_failed, tests_passed + tests_failed);
    return tests_failed > 0 ? 1 : 0;
}
