package com.example.resumebuilder.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph

import com.example.resumebuilder.ui.data.ResumeData
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.io.FileOutputStream

class PDFGenerator(private val context: Context) {
    fun generateResumePDF(resumeData: ResumeData): File? {
        try {
            val fileName = "Resume_${System.currentTimeMillis()}.pdf"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!path.exists()) {
                path.mkdirs()
            }
            val file = File(path, fileName)

            FileOutputStream(file).use { outputStream ->
                val writer = PdfWriter(outputStream)
                val pdf = PdfDocument(writer)
                val document = Document(pdf)

                try {
                    // Header
                    document.add(
                        Paragraph(resumeData.fullName)
                            .setBold()
                            .setFontSize(24f)
                            .setTextAlignment(TextAlignment.CENTER)
                    )

                    document.add(
                        Paragraph("${resumeData.email} | ${resumeData.phone}")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12f)
                    )

                    // Summary
                    if (resumeData.summary.isNotBlank()) {
                        document.add(
                            Paragraph("Professional Summary")
                                .setBold()
                                .setFontSize(16f)
                                .setMarginTop(20f)
                        )
                        document.add(Paragraph(resumeData.summary))
                    }

                    // Education
                    if (resumeData.education.isNotEmpty()) {
                        document.add(
                            Paragraph("Education")
                                .setBold()
                                .setFontSize(16f)
                                .setMarginTop(20f)
                        )
                        resumeData.education.forEach { education ->
                            document.add(
                                Paragraph("${education.degree}")
                                    .setBold()
                            )
                            document.add(
                                Paragraph("${education.institution}, ${education.year}")
                            )
                            document.add(Paragraph(""))
                        }
                    }

                    // Experience
                    if (resumeData.experience.isNotEmpty()) {
                        document.add(
                            Paragraph("Professional Experience")
                                .setBold()
                                .setFontSize(16f)
                                .setMarginTop(20f)
                        )
                        resumeData.experience.forEach { experience ->
                            document.add(
                                Paragraph("${experience.position} at ${experience.company}")
                                    .setBold()
                            )
                            document.add(Paragraph(experience.duration))
                            document.add(Paragraph(experience.description))
                            document.add(Paragraph(""))
                        }
                    }

                    // Skills
                    if (resumeData.skills.isNotEmpty()) {
                        document.add(
                            Paragraph("Skills")
                                .setBold()
                                .setFontSize(16f)
                                .setMarginTop(20f)
                        )
                        document.add(Paragraph(resumeData.skills.joinToString(", ")))
                    }

                } finally {
                    document.close()
                }

                // Make the file visible in Downloads
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = android.net.Uri.fromFile(file)
                context.sendBroadcast(intent)

                return file
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                context,
                "Error generating PDF: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            return null
        }
    }
}