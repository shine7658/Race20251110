package tw.edu.pu.csim.s1131213.race

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel: ViewModel(){

    var screenWidthPx by mutableStateOf(0f)
        private set

    var screenHeightPx by mutableStateOf(0f)
        private set

    // (修改 1) gameRunning 預設為 false
    var gameRunning by mutableStateOf(false)

    var circleX by mutableStateOf(0f)
    var circleY by mutableStateOf(0f)

    val horses = mutableListOf<Horse>()

    // (修改 2) 移除 'winner'，改用 'latestWinner' 來顯示訊息
    // var winner by mutableStateOf(0) // <-- 刪除
    var latestWinner by mutableStateOf(0) // <-- 新增 (0 = 尚未有贏家)
        private set

    var score by mutableStateOf(0f)
        private set

    // 設定螢幕寬度與高度
    fun SetGameSize(w: Float, h: Float) {
        screenWidthPx = w
        screenHeightPx = h

        for(i in 0..2){
            horses.add(Horse(i))
        }
    }

    fun StartGame() {
        //回到初使位置
        circleX = 100f
        circleY = screenHeightPx - 100f

        viewModelScope.launch {
            // (修改 3) 迴圈條件不再檢查 winner
            while (gameRunning) {
                delay(100)
                circleX += 10

                if (circleX >= screenWidthPx - 100){
                    circleX = 100f
                    score += 1
                }

                // (修改 4) 勝利/重置邏輯 (核心修改)
                for(i in 0..2){
                    horses[i].Run()

                    // (A) 檢查勝利
                    // 終點線 (馬的寬度是 200)
                    if (horses[i].HorseX >= screenWidthPx - 200){

                        // (B) 設定最新贏家 (GameScreen 會顯示)
                        latestWinner = i + 1

                        // (C) "不要停止" -> 立刻重置 "所有" 馬匹
                        for (h in horses) {
                            h.Reset()
                        }

                        // (D) 跳出 for 迴圈，因為比賽已重置
                        break
                    }
                }
            }
        }
    }

    fun MoveCircle(x: Float, y: Float) {
        circleX += x
        circleY += y
    }

    // (修改 5) acknowledgeWin() 已不再需要，可以刪除
    // fun acknowledgeWin() { ... } // <-- 刪除
}