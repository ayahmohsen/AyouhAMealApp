package com.ayouha.mealapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayouha.mealapp.pojo.MealsByCountry
import com.ayouha.mealapp.pojo.MealsByCountryList
import com.ayouha.mealapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryMealsViewModel : ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealsByCountry>>()

    fun getMealsByCountry(countryName:String){
        RetrofitInstance.api.getMealsByCountry(countryName).enqueue(object : Callback<MealsByCountryList>{
            override fun onResponse(
                call: Call<MealsByCountryList>,
                response: Response<MealsByCountryList>
            ) {
                response.body()?.let { mealsList->
                    mealsLiveData.postValue(mealsList.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCountryList>, t: Throwable) {
                Log.e("CountryMealsViewModel",t.message.toString())
            }
        })
    }
    fun observeMealsLiveData():LiveData<List<MealsByCountry>>{
        return mealsLiveData
    }
}