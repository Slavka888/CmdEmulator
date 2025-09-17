@echo off
ECHO Running emulator with VFS and script
java -cp "D:\prog\java\ConfigProject\CmdEmulator\out\production\CmdEmulator" Stages.Emulator --vfs D:\prog\java\ConfigProject\CmdEmulator\for_tasks\vfs --script D:\prog\java\ConfigProject\CmdEmulator\for_tasks\script\script.txt
IF ERRORLEVEL 1 ECHO Error occurred during execution
pause