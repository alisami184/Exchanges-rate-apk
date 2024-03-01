package com.example.fetchonlinedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fetchonlinedata.databinding.ActivityMainBinding
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCurrencyData().start()
    }

    private fun fetchCurrencyData(): Thread {
        return Thread {
            val url = URL("https://open.er-api.com/v6/latest/aud")
            val connection = url.openConnection() as HttpURLConnection

            if (connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem,"UTF-8")
                val request = Gson().fromJson(inputStreamReader, Request::class.java)
                updateUI(request)
                inputStreamReader.close()
                inputSystem.close()
            } else {
                binding.baseCurrency.text = "Failed Connection"
            }
        }
    }

    private fun updateUI(request: Request)
    {
        runOnUiThread{
            kotlin.run {
                binding.LastUpdated.text = request.time_last_update_utc
                binding.baseCurrency.text = String.format("AUD; %.2f",request.rates.AUD)
                binding.nzd.text = String.format("NZD: %.2f",request.rates.NZD)
                binding.usd.text = String.format("USD: %.2f",request.rates.USD)
                binding.gbp.text = String.format("GBP: %.2f",request.rates.GBP)

            }
        }
    }

}