@echo off
echo Compilar Analizador lexico e sintatico
pause
call java -jar jflex-full-1.9.1.jar ./especificacao.flex
pause
call java -jar java-cup-11b.jar -parser Parser ./especificacao.cup
pause
call javac -Xlint -d ./src/ -classpath .;java-cup-11b-runtime.jar *.java
pause
