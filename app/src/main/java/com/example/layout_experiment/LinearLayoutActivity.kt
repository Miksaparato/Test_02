package com.example.layout_experiment

import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * 实验一：线性布局（LinearLayout）
 * 对应 activity_linear_layout.xml，4 行 × 4 列网格界面
 * 这是 App 启动后第一个显示的页面
 */
class LinearLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_layout)
    }
}
