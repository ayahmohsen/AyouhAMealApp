package com.ayouha.mealapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayouha.mealapp.pojo.Meal

@Dao
interface MealDao { //this function inserts new meals and updates if a meal with the same name is inserted.......@Update function if u want them separate
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun upsert(meal:Meal)

   @Delete
   suspend fun delete(meal: Meal)

   @Query("SELECT * FROM mealInformation")
   fun getAllMeals():LiveData<List<Meal>>
}
