package com.ayouha.mealapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayouha.mealapp.databinding.MealItemBinding
import com.ayouha.mealapp.pojo.MealsByCountry
import com.bumptech.glide.Glide

class CountryMealsAdapter : RecyclerView.Adapter<CountryMealsAdapter.CountryMealsViewModel>() {
    private var mealsList = ArrayList<MealsByCountry>()
    var onItemClick : ((MealsByCountry) ->Unit)? = null

    fun setMealsList(mealsList: List<MealsByCountry>){
        this.mealsList = mealsList as ArrayList<MealsByCountry>
        notifyDataSetChanged()
    }

    inner class CountryMealsViewModel(val binding: MealItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryMealsViewModel {
        return CountryMealsViewModel(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CountryMealsViewModel, position: Int) {
        val m = mealsList[position]
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgMeal)

        holder.binding.mealName.text = mealsList[position].strMeal

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(m)
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}