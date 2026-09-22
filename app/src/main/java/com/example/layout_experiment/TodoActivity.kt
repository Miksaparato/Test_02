package com.example.layout_experiment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.layout_experiment.ui.theme.Layout_ExperimentTheme

/**
 * 实验五：Android Compose —— 课程学习任务（待办列表）
 *
 */

//定义任务的数据类（一行任务 = 任务名 + 是否完成）
data class Task(
    val title: String,        // 任务名
    val done: Boolean = false // 是否完成，默认未完成
)

// 主题色
private val ThemeRed = Color(0xFFD32F2F)

class TodoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Compose 用 setContent { } 代替 setContentView(R.layout.xxx)
        setContent {
            Layout_ExperimentTheme {
                TodoScreen()
            }
        }
    }
}

/**
 * 整个待办界面
 */
@Composable
fun TodoScreen() {
    // 定义状态
    // remember：界面旋转重建时保住数据不丢
    // mutableStateListOf：可观察的任务列表，增删改都会触发界面自动刷新
    val tasks = remember {
        mutableStateListOf(
            Task("学习 Column 和 Row", done = true), // 初始：3 项任务，完成 1 项
            Task("学习状态管理"),
            Task("完成 Compose 实验"),
        )
    }
    // 输入框的文字也做成状态，用户每敲一个字就更新
    var input by remember { mutableStateOf("") }

    // 实时统计已完成数量
    val doneCount = tasks.count { it.done }

    // 用 Column/Row 描述布局——对应 XML 里的垂直/水平 LinearLayout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "课程学习任务",
            color = ThemeRed,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp)) // 占位空白（间距）

        // 输入行：输入框 + 添加按钮
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it }, // 输入变化 → 更新状态
                placeholder = { Text("请输入学习任务") },
                modifier = Modifier.weight(1f),  // 占满剩余宽度
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    // 添加任务——改数据即可，界面自动刷新
                    if (input.isNotBlank()) {
                        tasks.add(Task(input.trim()))
                        input = ""                     // 清空输入框
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ThemeRed)
            ) {
                Text("添加")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 完成计数
        Text(
            text = "已完成：$doneCount / ${tasks.size}",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 任务列表
        tasks.forEachIndexed { index, task ->
            TaskRow(
                task = task,
                // 勾选回调：用 copy 生成"反选后"的新任务替换旧的（不可变数据风格）
                onToggle = { tasks[index] = task.copy(done = !task.done) },
                // 删除回调
                onDelete = { tasks.removeAt(index) }
            )
        }
    }
}

/**
 * 单个任务行：复选框 + 任务文字（完成时加删除线）+ 删除按钮
 */
@Composable
fun TaskRow(task: Task, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = task.done,
            onCheckedChange = { onToggle() },  // 点击勾选框 → 触发回调改数据
            colors = CheckboxDefaults.colors(checkedColor = ThemeRed)
        )
        // Modifier.weight(1f)：文字占满中间剩余宽度，把"删除"挤到最右
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            // 完成时显示删除线（XML 里做不到这点，得写代码判断；声明式 UI 一行搞定）
            style = TextStyle(
                textDecoration = if (task.done) TextDecoration.LineThrough else null
            )
        )
        TextButton(onClick = onDelete) {
            Text("删除", color = ThemeRed)
        }
    }
}
