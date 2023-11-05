package com.uniandes.abcjobs.models

class CreateWorkingInfoRequest (
    position: String,
    company: String,
    country: String,
    monthStartDate: Int,
    yearStartDate: Int,
    monthEndDate: Int?,
    yearEndDate: Int?,
    address: String,
    telephone: String,
    description: String
) {
    var position = position
    var company = company
    var country = country
    var yearStartDate = yearStartDate
    var monthStartDate = monthStartDate
    var yearEndDate = yearEndDate
    var monthEndDate = monthEndDate
    var address = address
    var telephone = telephone
    var description = description
}