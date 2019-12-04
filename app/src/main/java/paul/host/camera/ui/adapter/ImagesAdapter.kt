package paul.host.camera.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import paul.host.camera.R
import paul.host.camera.ui.navigation.MainNavigationListener

class ImagesAdapter(private val listener: MainNavigationListener?) :
    RecyclerView.Adapter<ImagesViewHolder>() {
    private var list = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImagesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.setImage(list[position])
        holder.itemView.setOnClickListener {
            listener?.goToImageFromProject(list[position])
        }
    }

    fun setList(list: List<String>?) {
        this.list = list?.let {
            notifyDataSetChanged()
            it.toMutableList()
        } ?: this.list
    }
}

@Suppress("MemberVisibilityCanBePrivate")
class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView = itemView.findViewById<ImageView>(R.id.image_view)
    fun setImage(url: String) {
        Glide.with(itemView.context)
            .load(url)
            .centerCrop()
//            .placeholder(R.drawable.placeholder)
            .into(imageView)
    }
}