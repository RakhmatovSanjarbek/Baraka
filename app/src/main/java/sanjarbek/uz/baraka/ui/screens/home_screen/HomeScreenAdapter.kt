package sanjarbek.uz.baraka.ui.screens.home_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sanjarbek.uz.baraka.core.data.dto.ProductModel
import sanjarbek.uz.baraka.databinding.ItemDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeScreenAdapter(
    private var moreOnClick: (productModel: ProductModel)->Unit
) : RecyclerView.Adapter<HomeScreenAdapter.ViewHolder>() {
    private val items = mutableListOf<ProductModel>()

    fun updateList(newItems: List<ProductModel>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvDate.text = item.date?.let { convertMillisToDate(it) }
        holder.binding.tvScore.text = (item.score ?: "No Description").toString()
        holder.binding.btnMore.setOnClickListener {
            moreOnClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root)

    class MyDiffCallback(
        private val oldList: List<ProductModel>,
        private val newList: List<ProductModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

    private fun convertMillisToDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy  HH:mm", Locale.getDefault())
        val date = Date(millis)
        return sdf.format(date)
    }
}