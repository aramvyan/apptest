package am.testapp.presentation.ui


import am.testapp.databinding.FragmentSelectMediaBinding
import am.testapp.presentation.root.BaseFragment
import am.testapp.presentation.view_models.MediaSharedViewModel
import am.testapp.shared.*
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController


class SelectMediaFragment :
    BaseFragment<FragmentSelectMediaBinding>(FragmentSelectMediaBinding::inflate) {

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            when (uris.size) {
                0 -> showMessage("No media selected")
                in 1..5 -> sharedViewModel.getUri(uris)
                else -> showMessage("Only 5 picture and video")
            }
        }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){

        if (it){
            pickMultipleMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageAndVideo
                )
            )
        }
    }

    private val sharedViewModel: MediaSharedViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    private fun initObserver() = with(sharedViewModel) {
        finishCreateFileLiveData.observe(viewLifecycleOwner, finishCreateFileObserver)
        getUrisLiveData.observe(viewLifecycleOwner, getUrisObserver)
    }

    private fun initView() {
        loadingProgress = binding.progressLayout

        binding.selectMediaButton.setSingleOnClickListener {
            requestPermission.launch(WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun openFragment() {
        val action =
            SelectMediaFragmentDirections.actionSelectMediaFragmentToMediaListFragment()
        this.findNavController().navigate(action)
    }

    private val finishCreateFileObserver = Observer<Unit> {
        openFragment()
        dismissLoading()
    }

    private val getUrisObserver = Observer<List<Uri>> {
        showLoading()
        binding.selectMediaButton.isEnabled = false
        sharedViewModel.getMedia(it)
    }
}