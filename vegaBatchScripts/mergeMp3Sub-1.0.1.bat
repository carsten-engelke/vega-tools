@ECHO OFF
@ECHO MergeMP3Subfolder 1.0.1 (C)opyright Carsten Engelke 2016
set /p %input="Merge (S)ubfolders or this (D)irectory?"
if "%input%" == "S" call :mergesubdir
if "%input%" == "D" call :mergeme "%cd"
pause
exit

:mergesubdir
for /d %%d IN (*) DO call :gosub "%%d"
goto :eof

:gosub
echo merging directory: %~1
cd %~1
call :mergeme "%~1"
cd ..
set file=%~1
move "%cd%\%file%\%file%.mp3" "%cd%\%file%.mp3"
goto :eof

:mergeme
copy /b *.mp3 "%~1.mp3"
goto :eof