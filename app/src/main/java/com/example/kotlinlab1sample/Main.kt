package com.example.kotlinlab1sample

import java.util.Scanner

// Global lists to store data in memory
val students = mutableListOf<Student>()
val courses = mutableListOf<Course>()
val enrollments = mutableListOf<Enrollment>()
val scanner = Scanner(System.`in`)

fun main() {
    println("=== Student Course Registration System ===")

    // Main Menu System - Part 2 (1 point)
    while (true) {
        showMenu()
        print("Enter choice (1-7): ")
        val choice = scanner.nextLine()

        when (choice) {
            "1" -> registerStudent()
            "2" -> createCourse()
            "3" -> enrollStudentInCourse()
            "4" -> viewCourseStudents()
            "5" -> viewAllStudents()
            "6" -> viewAllCourses()
            "7" -> {
                println("Goodbye!")
                return
            }
            else -> println("Invalid option. Try again.")
        }

        println() // Add spacing between menu cycles
    }
}

// Menu Display Function - Part 2 (1 point)
fun showMenu() {
    println("\n** Student Course Registration System ==**")
    println("1. Register new student")
    println("2. Create new course")
    println("3. Enroll student in course")
    println("4. View each course students")
    println("5. View all students")
    println("6. View all courses")
    println("7. Exit")
    println("HOW TO USE:")
    println("- Type a number (1-7) to select an option")
    println("- Press Enter to confirm your choice")
    println("- Type 7 or 'exit' to quit the application")
}

// Student Management - Part 3 (1 point)
fun registerStudent() {
    println("\n---------- NEW STUDENT REGISTRATION ----------")

    print("Enter Student ID (e.g., ST001): ")
    val id = scanner.nextLine().trim()

    // Check if student ID already exists
    if (students.any { it.id == id }) {
        println("Error: Student with ID $id already exists!")
        return
    }

    print("Enter Student Name: ")
    val name = scanner.nextLine().trim()
    if (name.isEmpty()) {
        println("Error: Student name cannot be empty!")
        return
    }

    print("Enter Email (optional): ")
    val email = scanner.nextLine().trim().ifBlank { null }

    print("Enter Major: ")
    val major = scanner.nextLine().trim()
    if (major.isEmpty()) {
        println("Error: Major cannot be empty!")
        return
    }

    val student = Student(id, name, email, major)
    students.add(student)
    println("Student $name registered successfully!")
}

fun viewAllStudents() {
    println("\n---------- ALL REGISTERED STUDENTS ----------")

    if (students.isEmpty()) {
        println("No students registered yet.")
        return
    }

    students.forEachIndexed { index, student ->
        val enrolledCourses = enrollments.count { it.studentId == student.id }
        println("${index + 1}. ID: ${student.id}")
        println("   Name: ${student.name}")
        println("   Email: ${student.email ?: "Not provided"}")
        println("   Major: ${student.major}")
        println("   Courses Enrolled: $enrolledCourses")
        println()
    }
}

// Course Management - Part 4 (1 point)
fun createCourse() {
    println("\n---------- CREATE NEW COURSE ----------")

    print("Enter Course ID (e.g., CS101): ")
    val courseId = scanner.nextLine().trim()

    // Check if course ID already exists
    if (courses.any { it.courseId == courseId }) {
        println("Error: Course with ID $courseId already exists!")
        return
    }

    print("Enter Course Name: ")
    val courseName = scanner.nextLine().trim()
    if (courseName.isEmpty()) {
        println("Error: Course name cannot be empty!")
        return
    }

    print("Enter Credits: ")
    val creditsInput = scanner.nextLine().trim()
    val credits = creditsInput.toIntOrNull()

    if (credits == null || credits <= 0) {
        println("Error: Credits must be a positive number!")
        return
    }

    val course = Course(courseId, courseName, credits)
    courses.add(course)
    println("Course '$courseName' created successfully!")
}

fun viewAllCourses() {
    println("\n---------- ALL COURSES ----------")

    if (courses.isEmpty()) {
        println("No courses available yet.")
        return
    }

    courses.forEachIndexed { index, course ->
        val enrollmentCount = enrollments.count { it.courseId == course.courseId }
        println("${index + 1}. Course ID: ${course.courseId}")
        println("   Course Name: ${course.courseName}")
        println("   Credits: ${course.credits}")
        println("   Students Enrolled: $enrollmentCount")
        println()
    }
}

// Enrollment System - Part 5 (1 point)
fun enrollStudentInCourse() {
    println("\n---------- ENROLL STUDENT IN COURSE ----------")

    if (students.isEmpty()) {
        println("Error: No students registered. Please register a student first.")
        return
    }

    if (courses.isEmpty()) {
        println("Error: No courses available. Please create a course first.")
        return
    }

    // Show available students
    println("Available Students:")
    students.forEach { student ->
        println("- ${student.id}: ${student.name}")
    }

    print("Enter Student ID: ")
    val studentId = scanner.nextLine().trim()

    val student = students.find { it.id == studentId }
    if (student == null) {
        println("Error: Student with ID $studentId not found!")
        return
    }

    // Show available courses
    println("\nAvailable Courses:")
    courses.forEach { course ->
        val enrolledCount = enrollments.count { it.courseId == course.courseId }
        println("- ${course.courseId}: ${course.courseName} (Enrolled: $enrolledCount)")
    }

    print("Enter Course ID: ")
    val courseId = scanner.nextLine().trim()

    val course = courses.find { it.courseId == courseId }
    if (course == null) {
        println("Error: Course with ID $courseId not found!")
        return
    }

    // Check if student is already enrolled
    val existingEnrollment = enrollments.find {
        it.studentId == studentId && it.courseId == courseId
    }

    if (existingEnrollment != null) {
        println("Error: Student ${student.name} is already enrolled in ${course.courseName}!")
        return
    }

    // Create enrollment
    val enrollment = Enrollment(studentId, courseId)
    enrollments.add(enrollment)
    println("Success: ${student.name} enrolled in ${course.courseName}!")
}

fun viewCourseStudents() {
    println("\n---------- VIEW STUDENTS IN COURSE ----------")

    if (courses.isEmpty()) {
        println("Error: No courses available.")
        return
    }

    // Show available courses
    println("Available Courses:")
    courses.forEach { course ->
        val enrolledCount = enrollments.count { it.courseId == course.courseId }
        println("- ${course.courseId}: ${course.courseName} (Enrolled: $enrolledCount)")
    }

    print("Enter Course ID: ")
    val courseId = scanner.nextLine().trim()

    val course = courses.find { it.courseId == courseId }
    if (course == null) {
        println("Error: Course with ID $courseId not found!")
        return
    }

    // Find enrollments for this course
    val courseEnrollments = enrollments.filter { it.courseId == courseId }

    println("\nStudents enrolled in ${course.courseName} (${course.courseId}):")

    if (courseEnrollments.isEmpty()) {
        println("No students enrolled in this course yet.")
        return
    }

    courseEnrollments.forEachIndexed { index, enrollment ->
        val student = students.find { it.id == enrollment.studentId }
        if (student != null) {
            println("${index + 1}. ${student.name} (ID: ${student.id}) - ${student.major}")
        }
    }
}