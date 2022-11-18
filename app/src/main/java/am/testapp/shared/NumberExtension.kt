package am.testapp.shared

import android.content.res.Resources
import android.util.TypedValue
import java.math.RoundingMode
import java.text.DecimalFormat


fun Number.roundOffDecimal(): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).toDouble()
}
val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)

