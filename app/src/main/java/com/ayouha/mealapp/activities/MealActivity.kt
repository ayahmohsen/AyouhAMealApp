package com.ayouha.mealapp.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ayouha.mealapp.HomeFragment
import com.ayouha.mealapp.R
import com.ayouha.mealapp.ViewModel.MealViewModel
import com.ayouha.mealapp.ViewModel.MealViewModelFactory
import com.ayouha.mealapp.databinding.ActivityMealBinding
import com.ayouha.mealapp.db.MealDatabase
import com.ayouha.mealapp.pojo.Meal
import com.bumptech.glide.Glide


class MealActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding: ActivityMealBinding
    private lateinit var youtubeLink:String
    private lateinit var mealMvvm:MealViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)

       // mealMvvm =ViewModelProvider(this)[MealViewModel::class.java]
        mealMvvm =ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]



        getMealInformationFromIntent()

        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

//        onYoutubeImageClick()
        onFavoriteClick()
        }

    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun onYoutubeImageClick() {
//        binding.webview.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
//            startActivity(intent)
//        }
//    }
    private var mealToSave:Meal?=null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                onResponseCase()
                val meal = value
                mealToSave = meal
                binding.categoryIcon.text = "Category : ${meal!!.strCategory}"
                binding.country.text = "Country : ${meal.strArea}"
                binding.instructionsSteps.text = meal.strInstructions

                youtubeLink = meal.strYoutube.toString()

                prepareWebView(youtubeLink)


            }
        })
    }

    private fun prepareWebView(url: String) {
        val webView = findViewById<WebView>(R.id.webview)

        // Enable JavaScript and other settings
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        // Ensure links open inside WebView
        webView.webViewClient = WebViewClient()

        // Load HTML with iframe embedding the YouTube video
        val youtubeVideoId = extractYoutubeVideoId(url)

        // HTML content for the iframe
        val iframeHtml = """<iframe width="100%" height="100%" 
                src="https://www.youtube.com/embed/$youtubeVideoId" 
                frameborder="0"
                allowfullscreen>
            </iframe>"""


        // Load the HTML string in WebView
        webView.loadData(iframeHtml, "text/html", "utf-8")
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
             .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.instructions.visibility = View.INVISIBLE
        binding.categoryIcon.visibility = View.INVISIBLE
        binding.country.visibility = View.INVISIBLE
        binding.webview.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.instructions.visibility = View.VISIBLE
        binding.categoryIcon.visibility = View.VISIBLE
        binding.country.visibility = View.VISIBLE
        binding.webview.visibility = View.VISIBLE
    }

 private fun extractYoutubeVideoId(youtubeUrl: String): String? {
     val uri = Uri.parse(youtubeUrl)
     return if (youtubeUrl.contains("youtu.be")) {
         uri.lastPathSegment // For youtube URLs, the video ID is in the last path segment
     } else {
         uri.getQueryParameter("v") // For standard YouTube URLs
     }
 }
}
