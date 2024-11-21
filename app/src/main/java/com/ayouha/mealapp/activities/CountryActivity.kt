package com.ayouha.mealapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ayouha.mealapp.HomeFragment
import com.ayouha.mealapp.R
import com.ayouha.mealapp.ViewModel.CountryMealsViewModel
import com.ayouha.mealapp.adapters.CountryMealsAdapter
import com.ayouha.mealapp.databinding.ActivityCountryBinding

class CountryActivity : AppCompatActivity() {
    lateinit var binding : ActivityCountryBinding
    lateinit var countryMealsViewModel: CountryMealsViewModel
    lateinit var countryMealsAdapter: CountryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        prepareRecyclerView()
        onCountryMealClick()

        countryMealsViewModel = ViewModelProvider(this)[CountryMealsViewModel::class.java]

        countryMealsViewModel.getMealsByCountry(intent.getStringExtra(HomeFragment.COUNTRY_NAME)!!)

        countryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList->
            binding.countryCount.text =
                "${intent.getStringExtra(HomeFragment.COUNTRY_NAME)} (${mealsList.size})"
            countryMealsAdapter.setMealsList(mealsList)
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun onCountryMealClick() {
        countryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        countryMealsAdapter = CountryMealsAdapter()
        binding.meals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = countryMealsAdapter
        }
    }
}