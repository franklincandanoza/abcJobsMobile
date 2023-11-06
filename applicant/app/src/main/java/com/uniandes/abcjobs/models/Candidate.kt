package com.uniandes.abcjobs.models

data class Candidate(
val document: String,
val documentType: String,
val firstName: String,
val lastName: String,
val phoneNumber: String,
val username: String,
val password: String,
val birthDate: String,
val age: Int,
val originCountry: String,
val residenceCountry: String,
val residenceCity: String,
val address: String,
)