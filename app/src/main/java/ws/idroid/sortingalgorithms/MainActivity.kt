package ws.idroid.sortingalgorithms


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ws.idroid.sortingalgorithms.model.Result
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private val numberOfRecords = 50001
    private var mergeStartTime: Long = 0
    private lateinit var array: IntArray
    private var clockBubble = 0
    private var clockInsertions = 0
    private var clockSelection = 0
    private var clockMerge = 0
    private var results = Result()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        back.visibility = View.GONE
        parallel.setOnClickListener {

        }
        data.setOnClickListener {
            loading.visibility = View.VISIBLE
            enableSortButtons()
            array = readNumbersFile()
            tv_data.apply {
                text = array.size.toString() + getString(R.string.loaded)
                setTextColor(resources.getColor(android.R.color.holo_green_dark))
            }
            loading.visibility = View.GONE
        }
        initViews()
    }

    private fun initViews() {
        bubble.setOnClickListener {
            doBubbleSort()
            more_bubble.isEnabled = true
            loading.visibility = View.GONE
        }
        more_bubble.setOnClickListener {
            openDetailsActivity(AlgorithmType.BUBBLE)
        }
        insertion.setOnClickListener {
            loading.visibility = View.VISIBLE
            doInsertionSort()
            more_insertion.isEnabled = true
            loading.visibility = View.GONE
        }
        more_insertion.setOnClickListener {
            openDetailsActivity(AlgorithmType.INSERTION)
        }
        selection.setOnClickListener {
            doSelectionSort()
            more_selection.isEnabled = true
        }
        more_selection.setOnClickListener {
            openDetailsActivity(AlgorithmType.SELECTION)
        }
        merge.setOnClickListener {
            doMergeSort()
            more_merge.isEnabled = true
        }
        more_merge.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(ALGORITHM_KEY, AlgorithmType.MERGE)
            intent.putExtra(RESULTS_KEY, results)
            startActivity(intent)
        }
    }

    private fun openDetailsActivity(type: AlgorithmType) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(ALGORITHM_KEY, type)
        intent.putExtra(RESULTS_KEY, results)
        startActivity(intent)
    }

    private fun doMergeSort() {
        try {
            time.text = getString(R.string.please_wait)
            mergeSort(array, 0, array.size - 1)
            val endTime = System.currentTimeMillis()
            time.text = String.format("%d ms", endTime - mergeStartTime)
            value_merge.text = String.format("%d ms", endTime - mergeStartTime)
            value_merge.setTextColor(resources.getColor(android.R.color.holo_green_dark))
            clock_merge.text = String.format("%d clock cycle", clockMerge)
            results.apply {
                merge = endTime - mergeStartTime
                mergeCycle = clockInsertions
            }
        } catch (e: UninitializedPropertyAccessException) {
            tv_data.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        }
    }

    private fun doSelectionSort() {
        try {
            time.text = getString(R.string.please_wait)
            GlobalScope.launch(Dispatchers.IO) { selectionSort(array) }
        } catch (e: UninitializedPropertyAccessException) {
            tv_data.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        }
    }

    private fun doInsertionSort() {
        try {
            time.text = getString(R.string.please_wait)
            GlobalScope.launch(Dispatchers.IO) {
                insertionSort(array)
            }
        } catch (e: UninitializedPropertyAccessException) {
            tv_data.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        }
    }

    private fun doBubbleSort() {
        try {
            loading.visibility = View.VISIBLE
            time.text = getString(R.string.please_wait)
            GlobalScope.launch(Dispatchers.IO) {
                bubbleSort(array)
            }
            loading.visibility = View.GONE
        } catch (e: UninitializedPropertyAccessException) {
            tv_data.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            loading.visibility = View.GONE
        }
    }

    private fun enableSortButtons() {
        bubble.isEnabled = true
        insertion.isEnabled = true
        selection.isEnabled = true
        merge.isEnabled = true
        parallel.isEnabled = true
    }

    private suspend fun bubbleSort(inputArray: IntArray?) {
        clockBubble = 0
        val startTime = System.currentTimeMillis()
        val arraySize = inputArray!!.size
        var temp: Int
        for (i in 0 until arraySize) {
            for (j in 1 until arraySize - i) {
                clockBubble++
                if (inputArray[j - 1] > inputArray[j]) {
                    clockBubble++
                    temp = inputArray[j - 1]
                    inputArray[j - 1] = inputArray[j]
                    inputArray[j] = temp
                }
            }
        }
        val endTime = System.currentTimeMillis()
        withContext(Dispatchers.Main) {
            time.text = String.format("%d ms", endTime - startTime)
            value_bubble.text = String.format("%d ms", endTime - startTime)
            clock_bubble.text = String.format("%d clock cycle", clockBubble)
        }
        results.apply {
            bubble = endTime - startTime
            bubbleCycle = clockBubble
        }
    }

    private suspend fun insertionSort(array: IntArray) {
        clockInsertions = 0
        val startTime = System.currentTimeMillis()
        for (i in 1 until array.size) {
            clockInsertions++
            val current = array[i]
            var j = i - 1
            while (j >= 0 && current < array[j]) {
                clockInsertions++
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = current
        }
        val endTime = System.currentTimeMillis()
        withContext(Dispatchers.Main) {
            time.text = String.format("%d ms", endTime - startTime)
            value_insertion.text = String.format("%d ms", endTime - startTime)
            clock_insertion.text = String.format("%d clock cycle", clockInsertions)
        }
        results.apply {
            insertion = endTime - startTime
            insertionCycle = clockInsertions
        }
    }

    private suspend fun selectionSort(array: IntArray) {
        clockSelection = 0
        val startTime = System.currentTimeMillis()
        for (i in array.indices) {
            var min = array[i]
            var minId = i
            for (j in i + 1 until array.size) {
                clockSelection++
                if (array[j] < min) {
                    // clockSelection++
                    min = array[j]
                    minId = j
                }
            }
            val temp = array[i]
            array[i] = min
            array[minId] = temp
        }
        val endTime = System.currentTimeMillis()
        withContext(Dispatchers.Main) {
            time.text = String.format("%d ms", endTime - startTime)
            value_selection.text = String.format("%d ms", endTime - startTime)
            clock_selection.text = String.format("%d clock cycle", clockSelection)
        }
        results.apply {
            selection = endTime - startTime
            selectionCycle = clockInsertions
        }
    }

    private fun mergeSort(array: IntArray, left: Int, right: Int) {
        clockMerge = 0
        mergeStartTime = System.currentTimeMillis()
        if (right <= left) return
        val mid = (left + right) / 2
        mergeSort(array, left, mid)
        mergeSort(array, mid + 1, right)
        merge(array, left, mid, right)
    }

    fun merge(array: IntArray, left: Int, mid: Int, right: Int) {
        val lengthLeft = mid - left + 1
        val lengthRight = right - mid
        val leftArray = IntArray(lengthLeft)
        val rightArray = IntArray(lengthRight)
        for (i in 0 until lengthLeft) leftArray[i] = array[left + i]
        for (i in 0 until lengthRight) rightArray[i] = array[mid + i + 1]
        var leftIndex = 0
        var rightIndex = 0
        for (i in left until right + 1) {
            clockMerge++
            if (leftIndex < lengthLeft && rightIndex < lengthRight) {
                if (leftArray[leftIndex] < rightArray[rightIndex]) {
                    array[i] = leftArray[leftIndex]
                    leftIndex++
                } else {
                    array[i] = rightArray[rightIndex]
                    rightIndex++
                }
            } else if (leftIndex < lengthLeft) {
                array[i] = leftArray[leftIndex]
                leftIndex++
            } else if (rightIndex < lengthRight) {
                array[i] = rightArray[rightIndex]
                rightIndex++
            }
        }
    }

    @Throws(Exception::class)
    private fun readNumbersFile(): IntArray {
        val inputStream = resources.openRawResource(R.raw.numbers)

        val bis = BufferedInputStream(inputStream)
        val array = IntArray(numberOfRecords)
        for (i in 0 until numberOfRecords) {
            array[i] = readInt(bis)
        }
        return array
    }

    @Throws(IOException::class)
    private fun readInt(`in`: InputStream): Int {
        var ret = 0
        var dig = false
        var c = 0
        while (`in`.read().also({ c = it }) != -1) {
            if (c >= '0'.toInt() && c <= '9'.toInt()) {
                dig = true
                ret = ret * 10 + c - '0'.toInt()
            } else if (dig) break
        }
        return ret
    }

    companion object {
        const val ALGORITHM_KEY = "ALGORITHM_KEY"
        const val RESULTS_KEY = "RESULTS_KEY"
    }
}