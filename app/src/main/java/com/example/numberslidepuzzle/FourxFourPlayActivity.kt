package com.example.numberslidepuzzle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.numberslidepuzzle.databinding.ActivityPlayBinding
import kotlin.jvm.java
import kotlin.random.Random

class FourxFourPlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding
    private val images = listOf(R.drawable.space, R.drawable.no1, R.drawable.no2, R.drawable.no3, R.drawable.no4, R.drawable.no5, R.drawable.no6, R.drawable.no7, R.drawable.no8)
    private var cellNumbers = MutableList(9) { 0 }               // 各セルに配置されている数字
    private var movableCells = MutableList(9) { false }          // 動かせるセルかどうか
    private var spaceCellIndex = 0                               // スペースセルの位置
    private var isGameClear = false                              // ゲームクリア

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 盤面初期化
        resetCells()

        // ボタン押下処理
        val cellViews = listOf(
            binding.cell11, binding.cell12, binding.cell13, binding.cell21, binding.cell22, binding.cell23, binding.cell31, binding.cell32, binding.cell33
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
        cellNumbers = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 0)
        var swapCount = 1
        while (swapCount <= 10) {     // 数字セルの入れ替えを10回行う(偶数回入れ替えるとクリア可能なパターンになる)
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
        if (isGameClear || !movableCells[cellIndex]) return

        // 選択されたセルとスペースセルを入れ替える
        val movingNumber = cellNumbers[cellIndex]
        cellNumbers[cellIndex] = 0
        cellNumbers[spaceCellIndex] = movingNumber
        spaceCellIndex = cellIndex
        updateImage()

        // ゲームクリアの判定
        if (cellNumbers == listOf(1, 2, 3, 4, 5, 6, 7, 8, 0)) {
            isGameClear = true
            binding.clearText.visibility = View.VISIBLE
        }
    }

    // 画像表示を更新
    private fun updateImage() {

        // 移動可能なセルの指定
        val movableCellsList = listOf(
            mutableListOf(false, true, false, true, false, false, false, false, false),
            mutableListOf(true, false, true, false, true, false, false, false, false),
            mutableListOf(false, true, false, false, false, true, false, false, false),
            mutableListOf(true, false, false, false, true, false, true, false, false),
            mutableListOf(false, true, false, true, false, true, false, true, false),
            mutableListOf(false, false, true, false, true, false, false, false, true),
            mutableListOf(false, false, false, true, false, false, false, true, false),
            mutableListOf(false, false, false, false, true, false, true, false, true),
            mutableListOf(false, false, false, false, false, true, false, true, false)
        )
        movableCells = movableCellsList[spaceCellIndex]

        // 画像表示を更新
        val cellViews = listOf(
            binding.cell11, binding.cell12, binding.cell13, binding.cell21, binding.cell22, binding.cell23, binding.cell31, binding.cell32, binding.cell33
        )
        cellViews.forEachIndexed { index, cell ->
            cell.setImageResource(images[cellNumbers[index]])
        }
    }
}
