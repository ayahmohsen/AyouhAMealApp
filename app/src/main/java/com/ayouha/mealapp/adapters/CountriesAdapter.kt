package com.ayouha.mealapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayouha.mealapp.databinding.CountryItemBinding
import com.ayouha.mealapp.pojo.Country

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

     var onItemClick: ((Country) -> Unit)? = null

    private var countriesList = ArrayList<Country>()

    fun setCountries(countries: List<Country>) {
        this.countriesList = ArrayList(countries)
        notifyDataSetChanged()
    }
  inner  class CountryViewHolder(val binding: CountryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countriesList[position]
        holder.binding.countryName.text = country.strArea

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(country)
        }

    }

    override fun getItemCount(): Int {
        return countriesList.size
    }


}
