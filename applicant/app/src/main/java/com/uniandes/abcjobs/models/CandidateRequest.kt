package com.uniandes.abcjobs.models

class CandidateRequest(
    document: String,
    documentType: String,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    username: String,
    password: String,
    birthDate: String,
    age: Int,
    originCountry: String,
    residenceCountry: String,
    residenceCity: String,
    address: String,
)
{
    var document = document
    var documentType = documentType
    var firstName = firstName
    var lastName = lastName
    var phoneNumber = phoneNumber
    var username = username
    var password = password
    var birthDate = birthDate
    var age =  age
    var originCountry = originCountry
    var residenceCountry = residenceCountry
    var residenceCity =  residenceCity
    var address = address
}