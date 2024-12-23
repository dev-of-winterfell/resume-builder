package com.example.resumebuilder.ui.ViewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resumebuilder.ui.data.Education
import com.example.resumebuilder.ui.data.Experience
import com.example.resumebuilder.ui.data.ResumeData

class ResumeViewModel : ViewModel() {
    private val _resumeData = mutableStateOf(ResumeData())
    val resumeData: State<ResumeData> = _resumeData

    fun updatePersonalInfo(name: String, email: String, phone: String, summary: String) {
        _resumeData.value = _resumeData.value.copy(
            fullName = name,
            email = email,
            phone = phone,
            summary = summary
        )
    }

    fun addEducation(education: Education) {
        _resumeData.value = _resumeData.value.copy(
            education = _resumeData.value.education + education
        )
    }

    fun addExperience(experience: Experience) {
        _resumeData.value = _resumeData.value.copy(
            experience = _resumeData.value.experience + experience
        )
    }

    fun addSkill(skill: String) {
        _resumeData.value = _resumeData.value.copy(
            skills = _resumeData.value.skills + skill
        )
    }
}
