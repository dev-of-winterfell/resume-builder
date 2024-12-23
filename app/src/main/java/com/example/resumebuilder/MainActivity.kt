package com.example.resumebuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import com.example.resumebuilder.ui.ViewModels.ResumeViewModel
import com.example.resumebuilder.ui.theme.ResumeBuilderTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.resumebuilder.ui.AppComponenets.EducationSection
import com.example.resumebuilder.ui.AppComponenets.ExperienceSection
import com.example.resumebuilder.ui.AppComponenets.PersonalInfoSection
import com.example.resumebuilder.ui.AppComponenets.PreviewSection
import com.example.resumebuilder.ui.AppComponenets.SkillsSection

enum class ResumeSection {
    PERSONAL,
    EDUCATION,
    EXPERIENCE,
    SKILLS,
    PREVIEW
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResumeBuilderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResumeBuilderApp()
                }
            }
        }
    }
}
@Composable
fun ResumeBuilderApp(viewModel: ResumeViewModel = viewModel()) {  // Add default parameter with viewModel()
    var currentSection by remember { mutableStateOf(ResumeSection.PERSONAL) }

    Scaffold { paddingValues ->  // Add Scaffold for proper edge-to-edge support
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // Apply padding from Scaffold
                .padding(16.dp)
        ) {
            // Navigation tabs
            TabRow(selectedTabIndex = currentSection.ordinal) {
                ResumeSection.values().forEach { section ->
                    Tab(
                        selected = currentSection == section,
                        onClick = { currentSection = section },
                        text = { Text(section.name) }
                    )
                }
            }

            // Content based on selected section
            when (currentSection) {
                ResumeSection.PERSONAL -> PersonalInfoSection(viewModel)
                ResumeSection.EDUCATION -> EducationSection(viewModel)
                ResumeSection.EXPERIENCE -> ExperienceSection(viewModel)
                ResumeSection.SKILLS -> SkillsSection(viewModel)
                ResumeSection.PREVIEW -> PreviewSection(viewModel)
            }
        }
    }
}