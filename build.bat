@echo off
rem ============================================================
rem  本机（这台电脑）专用的构建入口脚本
rem ------------------------------------------------------------
rem  为什么需要这个脚本：
rem    1) 本机 JAVA_HOME 指向的是 JDK 1.8，而 AGP 9 / Kotlin 2.2
rem       要求 JDK 17 及以上，所以这里显式改成 Android Studio 自带的 JBR。
rem    2) 本机 ~/.gradle 所在的用户目录在当前开发环境里不可写，
rem       所以把 Gradle 缓存目录指到项目内的 .gradle-user-home。
rem    3) 本机目前没有外网，依赖全部走已经下载好的本地缓存。
rem
rem  用法：  build.bat assembleDebug
rem          build.bat clean assembleDebug
rem          build.bat --stop
rem ============================================================

setlocal

set "PROJECT_DIR=%~dp0"
set "PROJECT_DIR=%PROJECT_DIR:~0,-1%"

set "JAVA_HOME=D:\Android Studio\jbr"
set "ANDROID_HOME=D:\sdk"
set "ANDROID_SDK_ROOT=D:\sdk"
set "GRADLE_USER_HOME=%PROJECT_DIR%\.gradle-user-home"

if not exist "%GRADLE_USER_HOME%" mkdir "%GRADLE_USER_HOME%"

echo [build.bat] JAVA_HOME        = %JAVA_HOME%
echo [build.bat] ANDROID_HOME     = %ANDROID_HOME%
echo [build.bat] GRADLE_USER_HOME = %GRADLE_USER_HOME%
echo.

if "%~1"=="" (
  call "%PROJECT_DIR%\gradlew.bat" assembleDebug
) else (
  call "%PROJECT_DIR%\gradlew.bat" %*
)

endlocal & exit /b %ERRORLEVEL%
