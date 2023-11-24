package com.uniandes.abcjobs.models

import com.google.gson.annotations.SerializedName

class ProjectMemberResponse {
    @SerializedName("id")
    var id: Number = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("profile")
    var profile: String? = null

    @SerializedName("active")
    var active: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("person_id")
    var person_id: String? = null

    @SerializedName("project_id")
    var project_id: String? = null
}