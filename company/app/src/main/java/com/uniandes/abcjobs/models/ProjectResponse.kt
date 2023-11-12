package com.uniandes.abcjobs.models
import com.google.gson.annotations.SerializedName


class ProjectResponse {
    @SerializedName("id")
    var id: Number = 0

    @SerializedName("project_name")
    var project_name: String? = null

    @SerializedName("start_date")
    var start_date: String? = null

    @SerializedName("active")
    var active: String? = null

    @SerializedName("details")
    var details: Number = 0

    @SerializedName("company_id")
    var company_id: String? = null
}