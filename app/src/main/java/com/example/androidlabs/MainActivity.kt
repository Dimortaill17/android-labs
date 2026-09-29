package com.example.androidlabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidlabs.ui.theme.AndroidLabsTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLabsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LabScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LabScreen(modifier: Modifier = Modifier) {
    val numbers = remember {
        List(10) {
            Random.nextInt(-20, 21)
        }
    }
    Column(
        modifier = modifier.padding(24.dp)
    ) {
        Text(
            text = "Лабораторная работа №1",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Вариант 12\n",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text("Задание: Вычислить среднее значение всех четных элементов списка, находящихся в нечетных местах.\n")
        Text("Исходный список:")
        Text(numbers.joinToString(", ") + "\n")
        Text("Результат вычисления:")
    }
}

@Preview(showBackground = true)
@Composable
fun LabScreenPreview() {
    AndroidLabsTheme {
        LabScreen()
    }
}