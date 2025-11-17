package tw.edu.pu.csim.s1131213.race

// 記得 import 這三個
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Horse(n:Int) {

    // (修正1) 馬匹的 X 座標必須是 "State" 才能被 UI 觀察
    var HorseX by mutableStateOf(0)
        private set // 讓外部 (ViewModel) 只能讀取，但內部可以透過 Run() 寫入

    // 馬匹的 Y 座標 (賽道) - 這是固定的，不需要 "State"
    var HorseY = 100 + 320 * n

    // (修正2) 馬匹的動畫幀數 (0,1,2,3) 也必須是 "State"
    var HorseNo by mutableStateOf(0)
        private set

    fun Run(){
        HorseNo ++
        if(HorseNo > 3){
            HorseNo = 0
        }

        HorseX += (10..30).random()
    }

    // (修正3) 這就是您缺少的函式！
    // GameViewModel 在 StartGame 迴圈中需要這個函式來重設 X 軸
    fun Reset() {
        HorseX = 0
    }
}