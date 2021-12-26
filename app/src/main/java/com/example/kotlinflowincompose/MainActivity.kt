package com.example.kotlinflowincompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinflowincompose.ui.theme.KotlinFlowInComposeTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinFlowInComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    val text = remember { mutableStateOf("Hello $name") }
    var aaa by remember { mutableStateOf(0) }
    val isEmitting = remember { mutableStateOf(false) }
    val processFlow = remember {
        flow {
            if (!isEmitting.value)
                isEmitting.value = true
            else
                return@flow

            delay(2000L)
            aaa++
            emit("aaaaa $aaa")
            isEmitting.value = false
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Column {
        Text(text = text.value)
        Button(onClick = {
            coroutineScope.launch {
                processFlow.buffer().collect {
                    Log.e("emitted ", it)
                    text.value = it
                }
            }
        }) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KotlinFlowInComposeTheme {
        Greeting("Android")
    }
}