#! /bin/bash

echo 'Enter day: '
read -r day
echo 'Enter name: '
read -r name

mkdir "src/main/kotlin/day$day"
mkdir "src/test/kotlin/day$day"
mkdir "src/main/resources/day$day"
touch "src/main/resources/day$day/input.txt"

printf "package day%s\n\nimport utils.readInputLines\n\nclass %s {\n\n}\n\nfun main() {\n    val input = readInputLines(%d)\n    val %s = %s()\n}\n" "$day" "$name" "$day" "${name,}" "$name" > "src/main/kotlin/day$day/$name.kt"
printf "package day%s\n\nimport org.assertj.core.api.Assertions.assertThat\nimport org.junit.jupiter.api.Test\n\nclass %sTest {\n\n\t@Test\n    fun \`test\`() {\n        \n    }\n}\n" "$day" "$name" > "src/test/kotlin/day$day/${name}Test.kt"
