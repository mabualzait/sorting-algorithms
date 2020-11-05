package ws.idroid.sortingalgorithms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.header.*
import ws.idroid.sortingalgorithms.MainActivity.Companion.ALGORITHM_KEY

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        back.setOnClickListener { finish() }
        val bundle = intent.extras
        val algoType = bundle?.get(ALGORITHM_KEY) as AlgorithmType
        when (algoType) {
            AlgorithmType.BUBBLE -> {
                work.setImageResource(R.drawable.bubble)
                header_title.text = getString(R.string.bubble_sort)
                graph.setImageResource(R.drawable.n_square)
                worst_case.text = getString(R.string.com_n_square)
                best_case.text = getString(R.string.o_n)
                average_case.text = getString(R.string.com_n_square)
            }
            AlgorithmType.INSERTION -> {
                header_title.text = getString(R.string.insertion_sort)
                graph.setImageResource(R.drawable.n)
                work.setImageResource(R.drawable.insertion)
                worst_case.text = getString(R.string.o_n)
                best_case.text = getString(R.string.o_n)
                average_case.text = getString(R.string.o_n)
            }
            AlgorithmType.SELECTION -> {
                header_title.text = getString(R.string.selection_sort)
                work.setImageResource(R.drawable.selection)
                worst_case.text = getString(R.string.com_n_square)
                best_case.text = getString(R.string.com_n_square)
                average_case.text = getString(R.string.com_n_square)
            }
            AlgorithmType.MERGE -> {
                header_title.text = getString(R.string.merge_sort)
                work.setImageResource(R.drawable.merge)
                header_title.text = getString(R.string.merge_sort)
                worst_case.text = getString(R.string.log_n)
                best_case.text = getString(R.string.log_n)
                average_case.text = getString(R.string.log_n)
            }
        }
    }
}