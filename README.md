# Layout_Experiment —— Android 界面布局实验

一个 Android 界面布局练习项目，把课程里的五个实验放在同一个 App 里，用首页按钮切换。

- **技术栈**：Kotlin + Jetpack Compose + 传统 XML 布局
- **构建工具**：Gradle 9.5.0 / Android Gradle Plugin 9.3.2 / Kotlin 2.2.10
- **SDK 版本**：compileSdk 37、targetSdk 37、minSdk 24
- **应用 ID**：`com.example.layout_experiment`

---

## 一、实验内容

| 实验 | 界面 | 技术点 | 入口 |
| --- | --- | --- | --- |
| 实验一 | 4×4 网格 | 线性布局 `LinearLayout`（垂直嵌套水平） | `LinearLayoutActivity` |
| 实验二 | 学生信息表 | 表格布局 `TableLayout` | `TableLayoutActivity` |
| 实验三 | 计算器界面 | 约束布局 `ConstraintLayout` | `ConstraintLayoutActivity` |
| 实验四 | 航班预订界面 | 约束布局综合运用（配图、双箭头/单箭头） | `FlightActivity` |
| 实验五 | 课程学习任务（待办列表） | Jetpack Compose 纯 Kotlin 代码写界面 | `TodoActivity` |

程序启动后进入 **实验选择页**（`MainActivity`，用 Compose 编写），点按钮进入对应实验，不用每次改代码换入口。

实验一到四的界面是 XML 布局（放在 `app/src/main/res/layout/`），实验五全程用 Compose，没有 XML 布局文件。

> 参考文档：`实验2_Android界面布局.pdf`

---

## 二、本机环境要求

以下路径是**这台电脑**的实际情况，换电脑时请对照修改。

| 项目 | 本机路径 / 取值 | 说明 |
| --- | --- | --- |
| JDK | `D:\Android Studio\jbr` | Android Studio 自带的 JBR（JDK 25）。**必须用 JDK 17 及以上**，本机系统里的 `JAVA_HOME` 指向 JDK 1.8，太旧跑不了 AGP 9 |
| Android SDK | `D:\sdk` | 含 `platforms/android-37` 与 `build-tools/36.0.0` |
| Gradle 缓存 | `D:\Layout_Experiment\.gradle-user-home` | 本机用户目录下的 `~/.gradle` 不可写，因此缓存放在项目内 |
| 网络 | 当前不通外网 | 依赖全部走本地已下载的缓存（离线构建） |

---

## 三、编译与运行

### 一键构建（推荐）

直接用项目根目录的脚本，它会自动设好上面三个环境变量：

```bat
build.bat assembleDebug
```

其他常用命令：

```bat
build.bat clean assembleDebug     :: 清理后完整重建
build.bat --stop                  :: 停掉 Gradle 守护进程
```

PowerShell 版本是 `build.ps1`，用法相同：

```powershell
.\build.ps1 assembleDebug
```

构建成功后 APK 在：

```
app\build\outputs\apk\debug\app-debug.apk
```

### 也可以用 Android Studio 打开

直接 `File → Open` 选择项目根目录即可。注意 Android Studio 里要确认两点：

1. **Gradle JDK** 设为 `D:\Android Studio\jbr`（`Settings → Build → Build Tools → Gradle → Gradle JDK`）
2. **Gradle 用户主目录** 设为 `D:\Layout_Experiment\.gradle-user-home`（`Settings → Build → Build Tools → Gradle → Gradle user home`）

---

## 四、构建配置说明

### `gradle.properties`

在原有配置基础上，新增了 3 项本机适配（都写了中文注释）：

```properties
# 1) 用 Android Studio 自带的 JBR，绕开系统里的 JDK 1.8
org.gradle.java.home=D:\\Android Studio\\jbr

# 2) 无外网时使用本地缓存解析依赖
org.gradle.offline=true

# 3) 使用 Android SDK 自带、有数字签名的 aapt2
android.aapt2FromMavenOverride=D:\\sdk\\build-tools\\36.0.0\\aapt2.exe
```

### `local.properties`

该文件不纳入版本控制，记录本机 SDK 位置：

```properties
sdk.dir=D:/sdk
```

> 注意用**正斜杠**（或双反斜杠）。写成单个反斜杠 `D:\sdk` 会被 Properties 当成转义字符解析。

### `build.bat` / `build.ps1`

本机适配脚本，作用是把三个环境变量设好再调用 `gradlew`：

```bat
set "JAVA_HOME=D:\Android Studio\jbr"
set "ANDROID_HOME=D:\sdk"
set "GRADLE_USER_HOME=%~dp0.gradle-user-home"
```

### `.gitignore`

新增忽略两条，避免把大文件提交进仓库：

```
.gradle-user-home/   # 本机 Gradle 缓存，约 2.3 GB
app.zip              # 队友打包过来的构建产物压缩包
```

---

## 五、常见问题

### 1. 报错 `stableIds.txt: error: failed to open: 数据无效。(13)`

**原因**：AGP 默认会从 Maven 仓库下载一个 `aapt2.exe`，该文件**没有数字签名**，会被本机的安全软件（360安全卫士、联想电脑管家内置的火绒、Windows Defender）拦截，导致资源编译失败。

**解决**：已在 `gradle.properties` 里通过 `android.aapt2FromMavenOverride` 改用 Android SDK 自带、有有效签名的 aapt2。如果换电脑后路径不同，改这一行指向新机器的 `build-tools/<版本>/aapt2.exe` 即可。

排查时可对比两个文件的签名状态：

```powershell
Get-AuthenticodeSignature 'D:\sdk\build-tools\36.0.0\aapt2.exe'                    # Valid
Get-AuthenticodeSignature "$env:USERPROFILE\.gradle\caches\modules-2\files-2.1\com.android.tools.build\aapt2\...\aapt2.exe"  # NotSigned
```

### 2. 报错 `Unable to access jarfile ...\gradle\wrapper\gradle-wrapper.jar`

Gradle Wrapper 的文件缺失。用 git 恢复：

```bat
git checkout -- gradle
```

### 3. Gradle 启动报错，说无法写入 `.gradle` 目录

本机 `C:\Users\<用户名>\.gradle` 不可写。请确认是通过 `build.bat` 构建，或手动设置：

```bat
set GRADLE_USER_HOME=D:\Layout_Experiment\.gradle-user-home
```

### 4. 构建提示找不到依赖 / 无法连接仓库

当前机器不通外网。依赖已缓存在 `.gradle-user-home`，请保持 `org.gradle.offline=true`，不要删除该目录。若以后网络恢复，可以删掉这一行让它正常联网下载。

### 5. 换电脑后需要改哪些地方

| 文件 | 要改的内容 |
| --- | --- |
| `gradle.properties` | `org.gradle.java.home`、`android.aapt2FromMavenOverride`；有网络可删 `org.gradle.offline=true` |
| `local.properties` | `sdk.dir` 指向新机器的 SDK |
| `build.bat` / `build.ps1` | 里面的 `JAVA_HOME`、`ANDROID_HOME`、`GRADLE_USER_HOME` 三个路径 |

---

## 六、版本控制

- 远程仓库：`https://github.com/Miksaparato/Test_02.git`
- 队友原始提交：`309753a feat:完成实验`

---

## 七、项目结构

```
Layout_Experiment/
├── app/
│   ├── build.gradle.kts                 # 模块构建配置（含 constraintlayout 依赖）
│   └── src/main/
│       ├── AndroidManifest.xml          # 注册 6 个 Activity
│       ├── java/com/example/layout_experiment/
│       │   ├── MainActivity.kt              # 实验选择页（Compose）
│       │   ├── LinearLayoutActivity.kt      # 实验一
│       │   ├── TableLayoutActivity.kt       # 实验二
│       │   ├── ConstraintLayoutActivity.kt  # 实验三
│       │   ├── FlightActivity.kt            # 实验四
│       │   ├── TodoActivity.kt              # 实验五（Compose）
│       │   └── ui/theme/                    # Compose 主题（颜色/字体/主题）
│       └── res/
│           ├── layout/                  # 实验一到四的 XML 布局
│           ├── drawable/                # 图片素材与边框
│           └── values/                  # 字符串、颜色、主题
├── build.bat / build.ps1                # 本机一键构建脚本
├── build.gradle.kts                     # 顶层构建配置
├── settings.gradle.kts                  # 模块与仓库配置
├── gradle.properties                    # 含 3 项本机适配
├── local.properties                     # 本机 SDK 路径（不提交）
└── gradle/libs.versions.toml            # 依赖版本统一管理
```
