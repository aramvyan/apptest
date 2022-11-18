package am.testapp.presentation.ui.media_list_fragment


import am.testapp.databinding.FragmentMediaListBinding
import am.testapp.presentation.root.BaseFragment
import am.testapp.presentation.ui.media_list_fragment.recycler.MediaListRecyclerAdapter
import am.testapp.presentation.view_models.MediaSharedViewModel
import am.testapp.shared.setSingleOnClickListener
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController


class MediaListFragment :
    BaseFragment<FragmentMediaListBinding>(FragmentMediaListBinding::inflate) {

    private val sharedViewModel: MediaSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    private fun initObserver() = with(sharedViewModel) {
        errorUploadLiveData.observe(viewLifecycleOwner, errorUploadObserver)
        successUploadLiveData.observe(viewLifecycleOwner, successUploadObserver)
    }

    private fun initView() {
        loadingProgress = binding.progressLayout
        binding.uploadButton.apply {
            setSingleOnClickListener {
                showLoading()
                sharedViewModel.uploadMedia()
            }
            isEnabled = false
        }

        val adapter = MediaListRecyclerAdapter()
        adapter.submitList(sharedViewModel.mediaListModel)
        binding.mediaListRecyclerView.adapter = adapter
    }

    private fun openFragment() {
        val action = MediaListFragmentDirections
            .actionMediaListFragmentToSuccessFragment()
        findNavController().navigate(action)
    }

    private val successUploadObserver = Observer<Unit> {
        dismissLoading()
        openFragment()
        binding.uploadButton.isEnabled = true
    }

    private val errorUploadObserver = Observer<Unit> {
        dismissLoading()
    }
}