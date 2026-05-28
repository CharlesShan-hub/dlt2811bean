# Test executable
add_executable(test_cmsper
    test_main.c
    test_stream.c
    test_boolean.c
    test_integer.c
    test_enumerated.c
    test_string.c
    test_bit_string.c
    test_choice.c
)

target_link_libraries(test_cmsper PRIVATE cmsper)
target_include_directories(test_cmsper PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/../include)

add_test(NAME cmsper COMMAND test_cmsper)
