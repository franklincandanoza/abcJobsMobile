package com.uniandes.abcjobs.models

import com.google.gson.annotations.SerializedName


class Company {
    @SerializedName("taxpayerId")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

}