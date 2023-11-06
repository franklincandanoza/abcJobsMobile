package com.uniandes.abcjobs.models
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class LoginResponse {
    @SerializedName("token")
    var token: String? = null

    @SerializedName("person_id")
    var personId: String? = null

    @SerializedName("role")
    var role: String? = null

}