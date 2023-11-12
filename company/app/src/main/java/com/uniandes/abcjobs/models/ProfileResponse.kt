package com.uniandes.abcjobs.models

import com.google.gson.annotations.SerializedName

class ProfileResponse {
    @SerializedName("projectId")
    var projectid: Number = 0

    @SerializedName("name")
    var name: String? = null
}