package com.uniandes.abcjobs.models


class RegisterResultTestRequest(
    test_name: String,
    candidate_document: String,
    observation: String?,
    points: Int,
)
{
    var test_name = test_name
    var candidate_document = candidate_document
    var observation = observation
    var points = points
}
