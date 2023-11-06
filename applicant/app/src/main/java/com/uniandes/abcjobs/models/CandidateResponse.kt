package com.uniandes.abcjobs.models
import com.google.gson.annotations.SerializedName

class CandidateResponse {
    @SerializedName("document")
    var document: String? = null

    @SerializedName("documentType")
    var documentType: String? = null

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("phoneNumber")
    var phoneNumber: Number = 0

    @SerializedName("username")
    var username: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("role")
    var role: String? = null

    @SerializedName("birthDate")
    var birthDate: String? = null

    @SerializedName("age")
    var age: Number = 0

    @SerializedName("originCountry")
    var originCountry: String? = null

    @SerializedName("residenceCountry")
    var residenceCountry: String? = null

    @SerializedName("residenceCity")
    var residenceCity: String? = null

    @SerializedName("address")
    var address: String? = null

    //@SerializedName("academics")
    //var academics: ArrayList<AcademicResponse> = ArrayList()

    //@SerializedName("laborals")
    //var academics: ArrayList<LaboralResponse> = ArrayList()

    //@SerializedName("technicals)
    //var technicals: ArrayList<TechnicalResponse> = ArrayList()
}