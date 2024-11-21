package com.ayouha.mealapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayouha.mealapp.ViewModel.HomeViewModel
import com.ayouha.mealapp.activities.CountryActivity
import com.ayouha.mealapp.activities.MainActivity
import com.ayouha.mealapp.activities.MealActivity
import com.ayouha.mealapp.adapters.CountriesAdapter
import com.ayouha.mealapp.adapters.MostPopularAdapter
import com.ayouha.mealapp.bottomsheet.MealBottomSheetFragment
import com.ayouha.mealapp.databinding.FragmentHomeBinding
import com.ayouha.mealapp.pojo.Meal
import com.ayouha.mealapp.pojo.MealsByCategory
import com.bumptech.glide.Glide


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var countriesAdapter: CountriesAdapter
   // private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.ayouha.mealapp.fragments.idMeal"
        const val MEAL_NAME = "com.ayouha.mealapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.ayouha.mealapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.ayouha.mealapp.fragments.categoryName"
        const val COUNTRY_NAME = "com.ayouha.mealapp.fragments.countryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
        countriesAdapter = CountriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        //prepareCategoriesRecyclerView()
        //viewModel.getCategories()
        //observeCategoriesLiveData()
        //onCategoryClick()

        prepareCountryListRecyclerView()
        viewModel.getCountries()
        observeCountriesLiveData()
        onCountryClick()

        onPopularItemLongClick()
        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    //    private fun onCategoryClick() {
//        categoriesAdapter.onItemClick = { category ->
//            val intent = Intent(activity,CategoryMealsActivity::class.java)
//            intent.putExtra(CATEGORY_NAME, category.strCategory)
//            startActivity(intent)
//        }
//    }

//    private fun prepareCategoriesRecyclerView() {
//        categoriesAdapter = CategoriesAdapter()
//        binding.recViewCategory.apply {
//            //have three items in one row use GridLayoutManager
//            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
//            adapter = categoriesAdapter
//        }
//    }

//    private fun observeCategoriesLiveData() {
//        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner,Observer{ categories->
//                categoriesAdapter.setCategoryList(categories)
//
//        })
//    }

    private fun prepareCountryListRecyclerView() {
        binding.recViewCountry.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = countriesAdapter
        }
    }

//    private fun observeCountriesLiveData() {
//        viewModel.observeCountriesLiveData().observe(viewLifecycleOwner, Observer { countries ->
//            countriesAdapter.setCountries(countries as ArrayList<Country>)
//        })
//    }

    private fun observeCountriesLiveData() {
        viewModel.observeCountriesLiveData().observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                countriesAdapter.setCountries(it)
            }
        })
    }
    private fun onCountryClick() {
            countriesAdapter.onItemClick = { country ->
            val intent = Intent(activity,CountryActivity::class.java)
            intent.putExtra(COUNTRY_NAME, country.strArea)
            startActivity(intent)
        }
   }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealList ->
            popularItemsAdapter.setMeals(mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            randomMeal.let {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MEAL_ID, randomMeal.idMeal)
                intent.putExtra(MEAL_NAME, randomMeal.strMeal)
                intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
                startActivity(intent)
            }
        }
    }
//      private fun observerRandomMeal(){
//          viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, object : Observer<Meal>{
//              override fun onChanged(value: Meal) {
//                  Glide.with(this@HomeFragment)
//                      .load(value.strMealThumb)
//                      .into(binding.imgRandomMeal)
//              }
//
//          })
//      }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, Observer { value ->
            Glide.with(this@HomeFragment)
                .load(value.strMealThumb)
                .into(binding.imgRandomMeal)

            randomMeal = value
        })
    }
}

