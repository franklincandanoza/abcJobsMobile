package com.uniandes.abcjobs.models

import com.google.gson.annotations.SerializedName


class Project {
    @SerializedName("projectId")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

}