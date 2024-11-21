package com.ayouha.mealapp.retrofit

import com.ayouha.mealapp.pojo.CategoryList
import com.ayouha.mealapp.pojo.CountryList
import com.ayouha.mealapp.pojo.MealList
import com.ayouha.mealapp.pojo.MealsByCategoryList
import com.ayouha.mealapp.pojo.MealsByCountryList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") id:String) : Call<MealList>

    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName:String) : Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String) : Call<MealsByCategoryList>

    @GET("list.php?a=list")
    fun getCountries(): Call<CountryList>

    @GET("filter.php")
    fun getMealsByCountry(@Query("a") countryName: String) : Call<MealsByCountryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchQuery: String) : Call<MealList>
}