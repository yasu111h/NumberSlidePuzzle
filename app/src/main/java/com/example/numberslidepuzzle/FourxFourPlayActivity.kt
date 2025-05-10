package com.example.numberslidepuzzle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.numberslidepuzzle.databinding.ActivityFourxFourPlayBinding
import com.example.numberslidepuzzle.databinding.ActivityPlayBinding
import kotlin.jvm.java
import kotlin.random.Random

class FourxFourPlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFourxFourPlayBinding
    private val images = listOf(R.drawable.space, R.drawable.no01, R.drawable.no02, R.drawable.no03, R.drawable.no04, R.drawable.no05, R.drawable.no06, R.drawable.no07,
                                R.drawable.no08, R.drawable.no09,R.drawable.no10, R.drawable.no11, R.drawable.no12, R.drawable.no13, R.drawable.no14, R.drawable.no15)
    private var cellNumbers = MutableList(16) { 0 }               // 各セルに配置されている数字
    private var movableCells = MutableList(16) { 0 }              // 0: 動かないセル, 1: 動かせるセル (効率化と可読性向上のためBoolean型ではなく0,1を使用)
    private var spaceCellIndex = 0                                // スペースセルの位置
    private var isGameClear = false                               // ゲームクリア

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFourxFourPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 盤面初期化
        resetCells()

        // ボタン押下処理
        val cellViews = listOf(
            binding.cell11, binding.cell12, binding.cell13, binding.cell14,
            binding.cell21, binding.cell22, binding.cell23, binding.cell24,
            binding.cell31, binding.cell32, binding.cell33, binding.cell34,
            binding.cell41, binding.cell42, binding.cell43, binding.cell44
        )
        cellViews.forEachIndexed { index, cell ->
            cell.setOnClickListener { moveCell(index) }
        }

        // リセットボタン
        binding.resetBtn.setOnClickListener{
            resetCells()
        }

        // ホームボタン
        binding.backBtn.setOnClickListener{
            startActivity(Intent(this@FourxFourPlayActivity, HomeActivity::class.java))
        }
    }

    // 盤面をリセット
    private fun resetCells() {
        isGameClear = false
        binding.clearText.visibility = View.INVISIBLE

        // 盤面をランダムに配置
        cellNumbers = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
        var swapCount = 1
        while (swapCount <= 12) {     // 数字セルの入れ替えを12回行う(偶数回入れ替えるとクリア可能なパターンになる)
            val i = Random.nextInt(cellNumbers.size - 1)
            val j = Random.nextInt(cellNumbers.size - 1)
            if (i != j) {
                val temp = cellNumbers[i]
                cellNumbers[i] = cellNumbers[j]
                cellNumbers[j] = temp
                Log.d("reset", "swapCount=$swapCount\n" +
                        "i=$i, cellNumbers[i]=$cellNumbers[i]\n" +
                        "j=$j, cellNumbers[j]=$cellNumbers[j]\n" +
                        "cellNumbers=$cellNumbers")
                swapCount += 1
            }
        }
        spaceCellIndex = cellNumbers.indexOf(0)
        updateImage()
    }

    // セル移動処理
    private fun moveCell(cellIndex: Int) {
        // ゲームクリア後または移動不可のセル選択時は何もしない
        if (isGameClear || movableCells[cellIndex] == 0) return

        // 選択されたセルとスペースセルを入れ替える
        val movingNumber = cellNumbers[cellIndex]
        cellNumbers[cellIndex] = 0
        cellNumbers[spaceCellIndex] = movingNumber
        spaceCellIndex = cellIndex
        updateImage()

        // ゲームクリアの判定
        if (cellNumbers == listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)) {
            isGameClear = true
            binding.clearText.visibility = View.VISIBLE
        }
    }

    // 画像表示を更新
    private fun updateImage() {

        // 移動可能なセルの指定
        val movableCellsList = listOf(
            //            11 12 13 14 21 22 23 24 31 32 33 34 41 42 43 44
            mutableListOf(0, 1, 0, 0, 1, 0, 0, 0, 0 ,0 ,0 ,0 ,0 ,0 ,0 ,0),  //11
            mutableListOf(1, 0, 1, 0, 0, 1, 0, 0, 0 ,0 ,0 ,0 ,0 ,0 ,0 ,0),  //12
            mutableListOf(0, 1, 0, 1, 0, 0, 1, 0, 0 ,0 ,0 ,0 ,0 ,0 ,0 ,0),  //13
            mutableListOf(0, 0, 1, 0, 0, 0, 0, 1, 0 ,0 ,0 ,0 ,0 ,0 ,0 ,0),  //14
            mutableListOf(1, 0, 0, 0, 0, 1, 0, 0, 1 ,0 ,0 ,0 ,0 ,0 ,0 ,0),  //21
            mutableListOf(0, 1, 0, 0, 1, 0, 1, 0, 0 ,1 ,0 ,0 ,0 ,0 ,0 ,0),  //22
            mutableListOf(0, 0, 1, 0, 0, 1, 0, 1, 0 ,0 ,1 ,0 ,0 ,0 ,0 ,0),  //23
            mutableListOf(0, 0, 0, 1, 0, 0, 1, 0, 0 ,0 ,0 ,1 ,0 ,0 ,0 ,0),  //24
            mutableListOf(0, 0, 0, 0, 1, 0, 0, 0, 0 ,1 ,0 ,0 ,1 ,0 ,0 ,0),  //31
            mutableListOf(0, 0, 0, 0, 0, 1, 0, 0, 1 ,0 ,1 ,0 ,0 ,1 ,0 ,0),  //32
            mutableListOf(0, 0, 0, 0, 0, 0, 1, 0, 0 ,1 ,0 ,1 ,0 ,0 ,1 ,0),  //33
            mutableListOf(0, 0, 0, 0, 0, 0, 0, 1, 0 ,0 ,1 ,0 ,0 ,0 ,0 ,1),  //34
            mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 1 ,0 ,0 ,0 ,0 ,1 ,0 ,0),  //41
            mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0 ,1 ,0 ,0 ,1 ,0 ,1 ,0),  //42
            mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0 ,0 ,1 ,0 ,0 ,1 ,0 ,1),  //43
            mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0 ,0 ,0 ,1 ,0 ,0 ,1 ,0),  //44
        )
        movableCells = movableCellsList[spaceCellIndex]

        // 画像表示を更新
        val cellViews = listOf(
            binding.cell11, binding.cell12, binding.cell13, binding.cell14,
            binding.cell21, binding.cell22, binding.cell23, binding.cell24,
            binding.cell31, binding.cell32, binding.cell33, binding.cell34,
            binding.cell41, binding.cell42, binding.cell43, binding.cell44
        )
        cellViews.forEachIndexed { index, cell ->
            cell.setImageResource(images[cellNumbers[index]])
        }
    }
}
