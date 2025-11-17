package tw.edu.pu.csim.s1131213.race

// 移除了 LaunchedEffect 和 delay
// 移除了 TextField
// 移除了 user 變數

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
// 移除了 AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
// 移除了 TextField
import androidx.compose.runtime.Composable
// 移除了 getValue, mutableStateOf, remember, setValue (因為 'user' 移除了)
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.embedding.EmbeddingBounds

@Composable
fun GameScreen(message: String,gameViewModel: GameViewModel) {

    val imageBitmaps = listOf(
        ImageBitmap.imageResource(R.drawable.horse0),
        ImageBitmap.imageResource(R.drawable.horse1),
        ImageBitmap.imageResource(R.drawable.horse2),
        ImageBitmap.imageResource(R.drawable.horse3)
    )

    // (修改 1) 移除 'user' 猜測變數 (因為遊戲不會停止)
    // var user by remember { mutableStateOf("") } // <-- 刪除

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Yellow)
    ){
        // (修改 2) 頂部文字現在會顯示分數和 "最新贏家"
        Text(
            text = "分數:${gameViewModel.score} | " +
                    if (gameViewModel.latestWinner > 0)
                        ":第${gameViewModel.latestWinner}馬獲勝"
                    else
                        "比賽進行中...",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top=16.dp)
        )

        // "賽馬遊戲(作者...)" 這行文字不變
        Text(
            text = "賽馬遊戲(作者：林哲旭) 。",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        Canvas (modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    gameViewModel.MoveCircle( dragAmount.x, dragAmount.y)
                }
            }
        ) {
            // ... (繪製圓形和馬匹的程式碼不變) ...
            drawCircle(
                color = Color.Red,
                radius = 100f,
                center = Offset(gameViewModel.circleX,gameViewModel.circleY)
            )
            for(i in 0..2){
                drawImage(
                    image = imageBitmaps[gameViewModel.horses[i].HorseNo],
                    dstOffset = IntOffset(
                        gameViewModel.horses[i].HorseX,
                        gameViewModel.horses[i].HorseY),
                    dstSize = IntSize(200,200)
                )
            }
        }

        // (修改 3) 移除 'if (gameViewModel.winner > 0)' 整個區塊
        // 包含 'LaunchedEffect' 和 'Box' 獲勝畫面
        // if (gameViewModel.winner > 0) { ... } // <-- 整個區塊刪除


        // (修改 4) 移除底部的 'TextField' 和 'Text'
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TextField(...) // <-- 刪除
            // Text("您預測獲勝的馬是:$user"...) // <-- 刪除

            Button(
                onClick = {
                    gameViewModel.gameRunning = true
                    gameViewModel.StartGame()
                },
                modifier = Modifier.padding(top = 16.dp),
                // (修改 5) 簡化 'enabled' 邏輯
                // 只有在遊戲 "尚未開始" 時才能按
                enabled = !gameViewModel.gameRunning
            ){
                Text(text = "遊戲開始")
            }
        }
    }
}