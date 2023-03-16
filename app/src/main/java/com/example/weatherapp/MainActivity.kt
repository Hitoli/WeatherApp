package com.example.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.weatherapp.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val city:String= "Lucknow"
    val API:String= "31f52babe7ae39a51b8b8ba00f33c922"
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        weatherTask().execute()
    }
    inner class weatherTask(): AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.loader.visibility= View.VISIBLE
            binding.maincontainer.visibility=View.GONE
            binding.errotext.visibility=View.GONE
        }
        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response= URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$API").readText(Charsets.UTF_8)
            }catch (e:java.lang.Exception){
                Log.e("error",e.toString())
                response=null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonobj = JSONObject(result)
                val main = jsonobj.getJSONObject("main");
                val sys = jsonobj.getJSONObject("sys");
                val wind = jsonobj.getJSONObject("wind");
                val weather = jsonobj.getJSONArray("weather").getJSONObject(0);
                val updateat: Long = jsonobj.getLong("dt");
                val updatedattext =
                    "Updated at:" + SimpleDateFormat("dd/mm/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updateat)
                    )
                val temp = main.getString("temp") + "°C"
                val tempmin = "Min Temp:" + main.getString("temp_min") + "°C"
                val tempmax = "Max Temp:" + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windspeec = wind.getString("speed")
                val weatherdescription = weather.getString("description")
                val address = jsonobj.getString("name") + ", " + sys.getString("country")
                binding.address.text = address
                binding.updatedAt.text = updatedattext
                binding.status.text = weatherdescription.capitalize()
                binding.temp.text = temp
                binding.tempMin.text = tempmin
                binding.tempMax.text = tempmax
                binding.Sunrise.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                binding.Sunset.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                binding.wind.text = windspeec
                binding.humidity.text = humidity
                binding.Pressure.text = pressure

                binding.loader.visibility = View.GONE
                binding.maincontainer.visibility = View.VISIBLE
            }catch (e:java.lang.Exception){
                Log.e("error",e.toString())
                binding.loader.visibility = View.GONE
                binding.errotext.visibility = View.VISIBLE

            }

        }

    }


}