package com.example.layout_experiment

import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * 实验二：表格布局（TableLayout）
 * 对应 activity_table_layout.xml，仿深色菜单界面
 */
class TableLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_layout)
    }
}
