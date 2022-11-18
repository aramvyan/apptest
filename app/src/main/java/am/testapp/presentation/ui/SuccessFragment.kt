package am.testapp.presentation.ui

import am.testapp.R
import am.testapp.databinding.FragmentSuccessBinding
import am.testapp.presentation.root.BaseFragment
import android.os.Bundle
import android.view.View

class SuccessFragment : BaseFragment<FragmentSuccessBinding>(FragmentSuccessBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.successMessageText.apply {
            text = context.getString(R.string.success_message)
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.success_ic)
        }
    }
}