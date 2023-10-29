package com.uniandes.abcjobs.models

class CreateAcademicInfoRequest (
    title: String,
    institution: String,
    country: String,
    monthStartDate: Int,
    yearStartDate: Int,
    monthEndDate: Int?,
    yearEndDate: Int?,

    description: String
) {
    var title = title
    var institution = institution
    var country = country
    var yearStartDate = yearStartDate
    var monthStartDate = monthStartDate
    var yearEndDate = yearEndDate
    var monthEndDate = monthEndDate
    var description = description
}