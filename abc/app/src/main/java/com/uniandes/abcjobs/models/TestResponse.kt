package com.uniandes.abcjobs.models
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class TestResponse {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("technology")
    var technology: String? = null

    @SerializedName("duration_minutes")
    var duration_minutes: Int? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("start_date")
    var start_date: String? = null

    @SerializedName("end_date")
    var end_date: String? = null

    @SerializedName("description")
    var description: String? = null

}