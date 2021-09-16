#! /bin/sh
cd "$(dirname "$0")"/.. || exit 1

MINCAMLC=java/mincamlc

echo "\e[33mStart syntax test\e[0m\n"

for test_case in tests/syntax/valid/*.ml
do
    echo "testing parser on: $test_case"
    $MINCAMLC -p "$test_case" 2> /dev/null 1> /dev/null
    if [ $? -eq 0 ]
    then
        echo "\e[32mOK\e[0m"
    else 
        echo "\e[31mKO\e[0m"
    fi
done

for test_case in tests/syntax/invalid/*.ml
do
    echo "testing parser on: $test_case"
    $MINCAMLC -p "$test_case" 2> /dev/null 1> /dev/null
    if [ $? -ne 0 ]
    then
        echo "\e[32mOK\e[0m"
    else 
        echo "\e[31mKO\e[0m"
    fi
done

echo "\e[33mEnd syntax test\e[0m\n"