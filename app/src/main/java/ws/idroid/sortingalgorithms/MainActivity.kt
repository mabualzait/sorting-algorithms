package ws.idroid.sortingalgorithms


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private val numberOfRecords = 50001
    var mergeStartTime: Long = 0
    lateinit var array: IntArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        data.setOnClickListener {
            array = readNumbersFile()
            tv_data.apply {
                text = array.size.toString() + getString(R.string.loaded)
                setTextColor(resources.getColor(android.R.color.holo_green_dark))
            }
        }
        bubble.setOnClickListener {
            time.text = getString(R.string.please_wait)
            try {
                bubbleSort(array)
            } catch (e: UninitializedPropertyAccessException) {
                tv_data.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }
        insertion.setOnClickListener {
            time.text = getString(R.string.please_wait)
            try {
                insertionSort(array)
            } catch (e: UninitializedPropertyAccessException) {
                tv_data.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }
        selection.setOnClickListener {
            time.text = getString(R.string.please_wait)
            try {
                selectionSort(array)
            } catch (e: UninitializedPropertyAccessException) {
                tv_data.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }
        merge.setOnClickListener {
            time.text = getString(R.string.please_wait)
            try {
                mergeSort(array, 0, array.size - 1)
                val endTime = System.currentTimeMillis()
                time.text = String.format("%d ms", endTime - mergeStartTime)
                value_merge.text = String.format("%d ms", endTime - mergeStartTime)
                value_merge.setTextColor(resources.getColor(android.R.color.holo_green_dark))
            } catch (e: UninitializedPropertyAccessException) {
                tv_data.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }
    }

    private fun bubbleSort(inputArray: IntArray?) {
        val startTime = System.currentTimeMillis()
        val arraySize = inputArray!!.size
        var temp = 0
        for (i in 0 until arraySize) {
            for (j in 1 until arraySize - i) {
                if (inputArray[j - 1] > inputArray[j]) {
                    temp = inputArray[j - 1]
                    inputArray[j - 1] = inputArray[j]
                    inputArray[j] = temp
                }
            }
        }
        val endTime = System.currentTimeMillis()
        time.text = String.format("%d ms", endTime - startTime)
        value_bubble.text = String.format("%d ms", endTime - startTime)
    }

    private fun insertionSort(array: IntArray) {
        val startTime = System.currentTimeMillis()
        for (i in 1 until array.size) {
            val current = array[i]
            var j = i - 1
            while (j >= 0 && current < array[j]) {
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = current
        }
        val endTime = System.currentTimeMillis()
        time.text = String.format("%d ms", endTime - startTime)
        value_insertion.text = String.format("%d ms", endTime - startTime)
    }

    private fun selectionSort(array: IntArray) {
        val startTime = System.currentTimeMillis()
        for (i in array.indices) {
            var min = array[i]
            var minId = i
            for (j in i + 1 until array.size) {
                if (array[j] < min) {
                    min = array[j]
                    minId = j
                }
            }
            val temp = array[i]
            array[i] = min
            array[minId] = temp
        }
        val endTime = System.currentTimeMillis()
        time.text = String.format("%d ms", endTime - startTime)
        value_selection.text = String.format("%d ms", endTime - startTime)
    }

    private fun mergeSort(array: IntArray, left: Int, right: Int) {
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
}