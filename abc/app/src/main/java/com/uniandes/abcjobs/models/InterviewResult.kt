package com.uniandes.abcjobs.models

import com.google.gson.annotations.SerializedName


class InterviewResult {
    @SerializedName("full_name")
    var fullname: String? = null

    @SerializedName("profile_id")
    var profile: String? = null

    @SerializedName("qualification")
    var qualification: String? = null

    override fun toString(): String {
        return "InterviewResult(profile=$profile, qualification=$qualification, fullname=$fullname)"
    }

}