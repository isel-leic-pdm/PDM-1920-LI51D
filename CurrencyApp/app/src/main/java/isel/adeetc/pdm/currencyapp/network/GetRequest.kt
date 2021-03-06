package isel.adeetc.pdm.currencyapp.network

import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


class GetRequest(url: String, success: Response.Listener<Currencies>, error: Response.ErrorListener)
    : JsonRequest<Currencies>(Request.Method.GET, url, "", success, error) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<Currencies> {
        Log.v("CurrencyApplication", "parseNetworkResponse on thread ${Thread.currentThread().name}")
        val mapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val currenciesDto = mapper.readValue(String(response.data), Currencies::class.java)
        return Response.success(currenciesDto, null)
    }
}
