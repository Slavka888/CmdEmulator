@echo off
ECHO Running emulator with VFS path only...
java -cp "D:\prog\java\ConfigProject\CmdEmulator\out\production\CmdEmulator" Stages.Emulator --vfs D:\prog\java\ConfigProject\CmdEmulator\for_tasks\vfs\test1.xml
IF ERRORLEVEL 1 ECHO Error occurred during execution
pause