package paul.host.camera.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import paul.host.camera.R
import paul.host.camera.data.model.TimeLapseProjectModel
import paul.host.camera.ui.navigation.MainNavigationListener

class ProjectsAdapter(private val listener: MainNavigationListener?) :
    RecyclerView.Adapter<ProjectViewHolder>() {
    private var list = mutableListOf<TimeLapseProjectModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProjectViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val item = list[position]
        if (!item.images.isNullOrEmpty()) holder.setImage(item.images.first())
        holder.setTitle(item.name)
        holder.itemView.setOnClickListener {
            listener?.goToProjectFromProjectsList(list[position].id, false)
        }
    }

    fun setList(list: List<TimeLapseProjectModel>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }
}

@Suppress("MemberVisibilityCanBePrivate")
class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView = itemView.findViewById<ImageView>(R.id.image_view)
    val title = itemView.findViewById<TextView>(R.id.project_title)
    fun setImage(url: String?) {
        url?.let {
            Glide.with(itemView.context)
                .load(url)
                .centerCrop()
//            .placeholder(R.drawable.placeholder)
                .into(imageView)
        }
    }

    fun setTitle(title: String) {
        this.title.text = title
    }
}