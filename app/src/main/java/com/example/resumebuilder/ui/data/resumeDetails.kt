package com.example.resumebuilder.ui.data

data class ResumeData(
    var fullName: String = "",
    var email: String = "",
    var phone: String = "",
    var summary: String = "",
    var education: List<Education> = listOf(),
    var experience: List<Experience> = listOf(),
    var skills: List<String> = listOf()
)

data class Education(
    var degree: String = "",
    var institution: String = "",
    var year: String = ""
)

data class Experience(
    var position: String = "",
    var company: String = "",
    var duration: String = "",
    var description: String = ""
)