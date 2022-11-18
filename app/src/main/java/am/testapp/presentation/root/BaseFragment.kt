package am.testapp.presentation.root

import am.testapp.R
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private lateinit var mutableBinding: VB
    val binding get() = mutableBinding


    protected var loadingProgress: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mutableBinding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    @SuppressLint("InflateParams")
    protected fun showMessage(message: String) {
        dismissLoading()
        val snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
        val customSnackView = layoutInflater.inflate(R.layout.layout_snack_bar, null)
        val messageTextView = customSnackView.findViewById<TextView>(R.id.tvBannerMessage)
        messageTextView.text = message
        val snackBarLayout = snackbar.view as Snackbar.SnackbarLayout

        val params = snackBarLayout.layoutParams as FrameLayout.LayoutParams
        snackBarLayout.setPadding(0, 0, 0, 0)
        params.gravity = Gravity.TOP
        snackbar.view.layoutParams = params
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        snackBarLayout.addView(customSnackView, 0)
        snackbar.show()
    }


    protected fun showLoading() {
        loadingProgress?.isVisible = true
    }

    protected fun dismissLoading() {
        loadingProgress?.isVisible = false

    }
}