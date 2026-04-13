@echo off
setlocal enableextensions
pushd %~dp0

cd ..
call gradlew clean shadowJar

java -jar build\libs\duke.jar -test < text-ui-test\input.txt > text-ui-test\ACTUAL.TXT

cd text-ui-test

FC ACTUAL.TXT EXPECTED.TXT >NUL && ECHO Test passed! || Echo Test failed!
