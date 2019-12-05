package paul.host.camera.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import paul.host.camera.R
import paul.host.camera.data.model.ImageModel
import paul.host.camera.ui.navigation.MainNavigationListener

class ImagesAdapter(private val listener: MainNavigationListener?) :
    RecyclerView.Adapter<ImagesViewHolder>() {
    private var list = listOf<ImageModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImagesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.setImage(list[position].path)
        holder.itemView.setOnClickListener {
            listener?.goToImageFromProject(list[position].path)
        }
    }

    fun setList(list: List<ImageModel>) {
        this.list = list
        notifyDataSetChanged()
    }
}

@Suppress("MemberVisibilityCanBePrivate")
class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView = itemView.findViewById(R.id.image_view)
    fun setImage(url: String) {
        Glide.with(itemView.context)
            .load(url)
            .centerCrop()
//            .placeholder(R.drawable.placeholder)
            .into(imageView)
    }
}