package ws.idroid.sortingalgorithms.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Result : Parcelable {
    var bubble: Long = 0
    var insertion: Long = 0
    var selection: Long = 0
    var merge: Long = 0
    var bubbleCycle = 0
    var insertionCycle = 0
    var selectionCycle = 0
    var mergeCycle = 0
}