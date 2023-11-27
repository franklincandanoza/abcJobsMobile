package com.uniandes.abcjobs.models

import com.google.gson.annotations.SerializedName

class Interview(

    @SerializedName("profile_id")
    val profile: String,

    @SerializedName("start_timestamp")
    val where: String,

    @SerializedName("status")
    val status: String,


) {
    override fun toString(): String {
        return "Interview(profile=$profile, startTimestamp=$where)"
    }
}