package com.example.androidlabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidlabs.ui.theme.AndroidLabsTheme

@Composable
fun LabScreen(modifier: Modifier = Modifier) {
    val processor = remember {
        ListProcessor()
    }

    val numbers = remember {
        processor.generateNumbers()
    }

    val result = remember {
        mutableStateOf<String?>(null)
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
        Text(
            text = "Задание:",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Вычислить среднее значение всех четных элементов списка, " +
                    "находящихся в нечетных местах.\n"
        )
        Text(
            text = "Исходный список:",
            fontWeight = FontWeight.Bold
        )
        Text(numbers.joinToString(", ") + "\n")

        Button(
            onClick = {
                result.value = processor.getResult(numbers)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Вычислить")
        }

        Text(
            text = "\nРезультат вычисления:",
            fontWeight = FontWeight.Bold
        )
        result.value?.let {
            Text(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LabScreenPreview() {
    AndroidLabsTheme {
        LabScreen()
    }
}