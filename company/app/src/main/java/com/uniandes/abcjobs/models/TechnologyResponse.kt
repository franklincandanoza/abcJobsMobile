package com.uniandes.abcjobs.models

import com.google.gson.annotations.SerializedName

class TechnologyResponse {
    @SerializedName("technologyId")
    var technologyId: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("category")
    var category: String? = null
}