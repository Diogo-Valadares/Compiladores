@echo off
set /p resposta="Digite o número do teste"
call java -cp .;java-cup-11b-runtime.jar Main codigo%resposta%.txt
pause