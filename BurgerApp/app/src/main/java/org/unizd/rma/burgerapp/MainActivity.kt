package org.unizd.rma.burgerapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewDetails: TextView
    private lateinit var editTextSearch: EditText
    private lateinit var buttonSearch: Button
    private lateinit var buttonShowAll: Button
    private lateinit var textViewSearchResult: TextView
    private val searchResultList2 = mutableListOf<String>()
    private val showAllResultList = mutableListOf<String>()


    private val burgerList = mutableListOf<String>()
    private lateinit var adapter: BurgerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewBurgers)
        textViewDetails = findViewById(R.id.textViewBurgerDetails)
        editTextSearch = findViewById(R.id.editTextSearch)
        buttonSearch = findViewById(R.id.buttonSearch)
        buttonShowAll = findViewById(R.id.buttonShowAll)
        textViewSearchResult = findViewById(R.id.textViewSearchResult)

        val buttonHideList: Button = findViewById(R.id.buttonHideList)
        buttonHideList.setOnClickListener {
            hideList()
        }

        // Setup RecyclerView adapter
        adapter = BurgerAdapter(burgerList, object : BurgerAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                showDetails(burgerList[position])
            }
        })

        // Set layout manager and adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Set click listeners
        buttonSearch.setOnClickListener {
            val searchId = editTextSearch.text.toString()
            fetchBurgers(searchId)
        }

        buttonShowAll.setOnClickListener {
            fetchBurgers(showAll = true)
        }
    }

    private fun hideList() {
        recyclerView.visibility = View.GONE
        textViewDetails.visibility = View.GONE
        textViewSearchResult.visibility = View.GONE
    }

    // Dodajte ovu liniju iznad onCreate metode kako biste definirali novu listu
    private val searchResultList = mutableListOf<String>()

    // U fetchBurgers metodi, zamijenite referencu na burgerList sa searchResultList
    // U fetchBurgers metodi, zamijenite referencu na burgerList sa searchResultList
    private fun fetchBurgers(searchId: String? = null, showAll: Boolean = false) {
        val apiUrl = "https://burgers-hub.p.rapidapi.com/burgers"
        val apiKey = "9898e1321dmshf860a0029e8e995p14970bjsnc48452cd535a" // Replace with your API key

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("X-RapidAPI-Key", apiKey)

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()

                    val jsonArray = JSONArray(response)
                    val tempList = mutableListOf<String>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val id = jsonObject.getString("id")
                        val name = jsonObject.getString("name")
                        val price = jsonObject.getString("price")

                        if (searchId.isNullOrBlank() || id == searchId) {
                            tempList.add("Name: $name - Price: $price$")
                        }

                        if (showAll) {
                            showAllResultList.add("$name: $price")
                        }
                    }

                    runOnUiThread {
                        if (!showAll) {
                            searchResultList2.clear()
                            searchResultList2.addAll(tempList)

                            adapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            textViewDetails.visibility = View.GONE
                            textViewSearchResult.visibility = View.VISIBLE
                            textViewSearchResult.text = searchResultList2.joinToString("\n")
                        } else {
                            showAllResultList.clear()
                            showAllResultList.addAll(tempList)

                            adapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            textViewDetails.visibility = View.GONE
                            textViewSearchResult.visibility = View.VISIBLE
                            textViewSearchResult.text = showAllResultList.joinToString("\n")
                        }
                    }
                } else {
                    throw Exception("Failed to fetch burgers: HTTP response code $responseCode")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





    private fun showDetails(burgerInfo: String) {
        recyclerView.visibility = View.GONE
        textViewDetails.visibility = View.VISIBLE
        textViewDetails.text = burgerInfo + "\n\n(Click to go back)"
    }
}
