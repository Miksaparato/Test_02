package com.example.layout_experiment

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.layout_experiment.ui.theme.Layout_ExperimentTheme

/**
 * 程序入口：实验选择页。
 * 点击按钮跳转到对应实验的 Activity，不用每次都改代码。
 * 本页用 Compose 编写（和实验五同一套写法），前四个实验页仍是 XML 布局。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Layout_ExperimentTheme {
                ExperimentMenuScreen()
            }
        }
    }
}

@Composable
fun ExperimentMenuScreen() {
    // LocalContext.current：在 Compose 里获取当前的页面环境（相当于 XML 里的 this）
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        // spacedBy：让 Column 里每个子控件之间自动保持 12dp 间距
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Android 界面布局实验",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // 每个按钮的写法都一样：
        // onClick 里用 Intent 跳转到对应实验的 Activity
        Button(onClick = {
            context.startActivity(Intent(context, LinearLayoutActivity::class.java))
        }) {
            Text("实验一：线性布局")
        }

        Button(onClick = {
            context.startActivity(Intent(context, TableLayoutActivity::class.java))
        }) {
            Text("实验二：表格布局")
        }

        Button(onClick = {
            context.startActivity(Intent(context, ConstraintLayoutActivity::class.java))
        }) {
            Text("实验三：约束布局（计算器）")
        }

        Button(onClick = {
            context.startActivity(Intent(context, FlightActivity::class.java))
        }) {
            Text("实验四：约束布局（航班预订）")
        }

        Button(onClick = {
            context.startActivity(Intent(context, TodoActivity::class.java))
        }) {
            Text("实验五：Compose 待办列表")
        }
    }
}
