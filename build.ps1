# ============================================================
#  本机（这台电脑）专用的构建入口脚本 —— PowerShell 版本
# ------------------------------------------------------------
#  为什么需要这个脚本：
#    1) 本机 JAVA_HOME 指向的是 JDK 1.8，而 AGP 9 / Kotlin 2.2
#       要求 JDK 17 及以上，所以这里显式改成 Android Studio 自带的 JBR。
#    2) 本机 ~/.gradle 所在的用户目录在当前开发环境里不可写，
#       所以把 Gradle 缓存目录指到项目内的 .gradle-user-home。
#    3) 本机目前没有外网，依赖全部走已经下载好的本地缓存。
#
#  用法：  .\build.ps1 assembleDebug
#          .\build.ps1 clean assembleDebug
#          .\build.ps1 --stop
# ============================================================

$ErrorActionPreference = 'Stop'

$ProjectDir = $PSScriptRoot

$env:JAVA_HOME        = 'D:\Android Studio\jbr'
$env:ANDROID_HOME     = 'D:\sdk'
$env:ANDROID_SDK_ROOT = 'D:\sdk'
$env:GRADLE_USER_HOME = Join-Path $ProjectDir '.gradle-user-home'

if (-not (Test-Path $env:GRADLE_USER_HOME)) {
    New-Item -ItemType Directory -Path $env:GRADLE_USER_HOME -Force | Out-Null
}

Write-Host "[build.ps1] JAVA_HOME        = $env:JAVA_HOME"
Write-Host "[build.ps1] ANDROID_HOME     = $env:ANDROID_HOME"
Write-Host "[build.ps1] GRADLE_USER_HOME = $env:GRADLE_USER_HOME"
Write-Host ""

$gradleArgs = if ($args.Count -gt 0) { $args } else { @('assembleDebug') }

& (Join-Path $ProjectDir 'gradlew.bat') @gradleArgs
exit $LASTEXITCODE
