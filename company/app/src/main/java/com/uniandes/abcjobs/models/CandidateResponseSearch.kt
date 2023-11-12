package com.uniandes.abcjobs.models;

import com.google.gson.annotations.SerializedName

public class CandidateResponseSearch {
    @SerializedName("person_id")
    var person_id: String? = null

    @SerializedName("first_name")
    var first_name: String? = null

    @SerializedName("last_name")
    var last_name: String? = null

    @SerializedName("age")
    var age: String? = null

    @SerializedName("roles")
    var roles: String? = null

    @SerializedName("technologies")
    var technologies: String? = null

    @SerializedName("titles")
    var titles: String? = null

    @SerializedName("abilities")
    var abilities: String? = null

    @SerializedName("score")
    var score: String? = null
}
