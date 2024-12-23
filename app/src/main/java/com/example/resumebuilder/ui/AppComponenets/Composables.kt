package com.example.resumebuilder.ui.AppComponenets

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.resumebuilder.ui.ViewModels.ResumeViewModel
import com.example.resumebuilder.ui.data.Education
import com.example.resumebuilder.ui.data.Experience
import com.example.resumebuilder.ui.data.ResumeData
import com.example.resumebuilder.utils.PDFGenerator
import kotlinx.coroutines.launch

@Composable
private fun ResumeCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        content()
    }
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    minLines: Int = 1,
    maxLines: Int = 1,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        minLines = minLines,
        maxLines = maxLines,
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
        )
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun Chip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

private fun handlePdfGeneration(context: Context, resumeData: ResumeData) {
    try {
        val pdfGenerator = PDFGenerator(context)
        val file = pdfGenerator.generateResumePDF(resumeData)
        if (file != null) {
            Toast.makeText(
                context,
                "PDF saved to Downloads: ${file.name}",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context,
                "Error generating PDF",
                Toast.LENGTH_LONG
            ).show()
        }
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error: ${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }
}
@Composable
fun PersonalInfoSection(viewModel: ResumeViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Basic email validation
    val isEmailValid = email.isEmpty() || email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ResumeCard {
            Column(modifier = Modifier.padding(16.dp)) {
                FormTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    isError = name.isBlank(),
                    supportingText = if (name.isBlank()) "Name is required" else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = !isEmailValid,
                    supportingText = if (!isEmailValid) "Enter a valid email" else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = "Professional Summary",
                    minLines = 3,
                    maxLines = 5,
                    supportingText = "${summary.length}/500 characters"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updatePersonalInfo(name, email, phone, summary)
                Toast.makeText(context, "Personal information saved", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End),
            enabled = name.isNotBlank() && isEmailValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Information")
        }
    }
}

@Composable
fun EducationSection(viewModel: ResumeViewModel) {
    var degree by remember { mutableStateOf("") }
    var institution by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ResumeCard {
            Column(modifier = Modifier.padding(16.dp)) {
                FormTextField(
                    value = degree,
                    onValueChange = { degree = it },
                    label = "Degree",
                    isError = degree.isBlank()
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = institution,
                    onValueChange = { institution = it },
                    label = "Institution",
                    isError = institution.isBlank()
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = year,
                    onValueChange = { year = it },
                    label = "Year",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (degree.isNotBlank() && institution.isNotBlank()) {
                    viewModel.addEducation(Education(degree, institution, year))
                    degree = ""
                    institution = ""
                    year = ""
                    Toast.makeText(context, "Education added", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = degree.isNotBlank() && institution.isNotBlank()
        ) {
            Text("Add Education")
        }
    }
}

@Composable
fun ExperienceSection(viewModel: ResumeViewModel) {
    var position by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ResumeCard {
            Column(modifier = Modifier.padding(16.dp)) {
                FormTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = "Position",
                    isError = position.isBlank()
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = "Company",
                    isError = company.isBlank()
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = "Duration (e.g., 2020-2022)"
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Job Description",
                    minLines = 3,
                    maxLines = 5
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (position.isNotBlank() && company.isNotBlank()) {
                    viewModel.addExperience(Experience(position, company, duration, description))
                    position = ""
                    company = ""
                    duration = ""
                    description = ""
                    Toast.makeText(context, "Experience added", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = position.isNotBlank() && company.isNotBlank()
        ) {
            Text("Add Experience")
        }
    }
}

@Composable
fun SkillsSection(viewModel: ResumeViewModel) {
    var skill by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ResumeCard {
            Column(modifier = Modifier.padding(16.dp)) {
                FormTextField(
                    value = skill,
                    onValueChange = { skill = it },
                    label = "Skill",
                    isError = skill.isBlank()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (skill.isNotBlank()) {
                    viewModel.addSkill(skill)
                    skill = ""
                    Toast.makeText(context, "Skill added", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = skill.isNotBlank()
        ) {
            Text("Add Skill")
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreviewSection(viewModel: ResumeViewModel) {
    val context = LocalContext.current
    val resumeData = viewModel.resumeData.value
    val hasPermission = remember { mutableStateOf(false) }
    var isGeneratingPdf by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission.value = isGranted
        if (isGranted) {
            handlePdfGeneration(context, resumeData)
        } else {
            Toast.makeText(
                context,
                "Permission needed to save PDF",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        hasPermission.value = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ResumeCard {
            Column(modifier = Modifier.padding(24.dp)) {
                // Personal Information Section
                Text(
                    text = resumeData.fullName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = resumeData.email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = " | ",
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = resumeData.phone,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                if (resumeData.summary.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionTitle(text = "Professional Summary")
                    Text(
                        text = resumeData.summary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Education Section
        if (resumeData.education.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(text = "Education")
            resumeData.education.forEach { education ->
                ResumeCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = education.degree,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = education.institution,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Text(
                            text = education.year,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Experience Section
        if (resumeData.experience.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(text = "Experience")
            resumeData.experience.forEach { experience ->
                ResumeCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = experience.position,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = experience.duration,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Text(
                            text = experience.company,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = experience.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Skills Section
        if (resumeData.skills.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(text = "Skills")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    resumeData.skills.forEach { skill ->
                        Chip(
                            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                            text = skill,
                            onClick = { /* Optional: Add skill removal */ }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Generate PDF Button
        Button(
            onClick = {
                isGeneratingPdf = true
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q || hasPermission.value) {
                    handlePdfGeneration(context, resumeData)
                } else {
                    launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                isGeneratingPdf = false
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = !isGeneratingPdf
        ) {
            if (isGeneratingPdf) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate PDF")
            }
        }
    }
}