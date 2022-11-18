package am.testapp.presentation.ui.media_list_fragment.recycler

import am.testapp.databinding.MediaListRecyclerItemBinding
import am.testapp.models.MediaListModel
import am.testapp.shared.roundOffDecimal
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MediaListRecyclerAdapter: ListAdapter<MediaListModel, MediaListRecyclerAdapter.MediaListViewHolder>(
    MediaListDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListViewHolder {
        val binding = MediaListRecyclerItemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return MediaListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MediaListViewHolder(private val binding: MediaListRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(model: MediaListModel) = with(binding){
            mediaNameText.text = model.name
            mediaSizeText.text = model.size?.roundOffDecimal().toString() + "mb"
            mediaTypeText.text = "type/" + model.fileType?.value
            mediaImageView.setImageBitmap(model.thumbnailPath)
        }

    }
}

class MediaListDiffUtil : DiffUtil.ItemCallback<MediaListModel>() {

    override fun areItemsTheSame(oldItem: MediaListModel, newItem: MediaListModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MediaListModel, newItem: MediaListModel): Boolean {
        return oldItem == newItem
    }
}