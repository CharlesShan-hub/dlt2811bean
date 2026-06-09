cmake_minimum_required(VERSION 3.14)
project(ccms2 VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

set(CMS2_SRC
    src/per/cms_stream.c
    src/per/cms_integer.c
    src/per/cms_string.c
    src/per/cms_choice.c
    src/cms2_basic.c
    src/cms2_associate.c
)

add_library(ccms2 STATIC ${CMS2_SRC})
target_include_directories(ccms2 PUBLIC include)

# Test
add_executable(test_associate tests/test_associate.c)
target_link_libraries(test_associate ccms2)
target_include_directories(test_associate PRIVATE include)
