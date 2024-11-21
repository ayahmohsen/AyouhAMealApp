package com.ayouha.mealapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayouha.mealapp.databinding.CategoryItemBinding
import com.ayouha.mealapp.pojo.Category
import com.bumptech.glide.Glide

class CategoriesAdapter():RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categoriesList = ArrayList<Category>()
    var onItemClick : ((Category) ->Unit)? = null

    fun setCategoryList(categoriesList:List<Category>){
        this.categoriesList = categoriesList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: CategoryItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        // Safely check if the categoriesList or its items are not null
        val category = categoriesList?.getOrNull(position)
        if (category != null) {
            // Safely load image if category thumbnail is not null
            Glide.with(holder.itemView)
                .load(category.strCategoryThumb)
                .into(holder.binding.imgCategory)

            holder.binding.categoryName.text = category.strCategory

            // Safely set click listener if onItemClick is not null
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(category)
            }
        } else {
            // Optionally handle case where the category is null (e.g., log or show placeholder)
            //holder.binding.imgCategory.setImageResource(R.drawable.placeholder) // example placeholder
            holder.binding.categoryName.text = "Unknown Category"
        }
    }


    override fun getItemCount(): Int {
        return categoriesList.size
    }
}