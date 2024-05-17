# Build LANDMARK REMARKER README


## Prerequisites
Before you begin, make sure you have the following installed:
- Android Studio (using Android support AGP 8.2)
- JDK (Java Development Kit)
- Android SDK


## Overview
This Android application, Landmark Remark, allows users to save notes associated with specific locations on a map. Users can view existing notes, add new notes, and search for notes by username or content. The app aims to provide a simple and intuitive way for users to mark and share interesting locations with others.


## Technology Stack
1. Android (Kotlin)
2. Jetpack Compose
3. Hilt
4. Gson
5. Coroutines
6. Firebase Database
7. Firebase Authentication 

## Source Code
The entire source code of the application can be found in the app directory.


## Compilation and Execution
- The code is designed to compile and run in Android Studio IDE with the specified technology stack.
- The application can be built using the provided Gradle build script (build.gradle) and executed on an Android device or emulator.


## README Overview
1. Approach
   The application is built using modern Android development practices, leveraging Jetpack Compose for UI development, Hilt for dependency injection, and Gson for JSON serialization/deserialization.
   Coroutines are used for asynchronous programming, allowing for efficient and non-blocking execution of tasks such as network requests and database operations.
   Use Firebase Authentication to create and save information for each user. I want to use UID as key to save collection in Firestore Database
   Firebase Firestore is utilized as the backend database for storing user data and notes, providing real-time synchronization and offline support.
2. Time Allocation
   Approximately 2 hours were spent on initial project setup, including setting up the development environment and integrating necessary libraries.
   3 hours dedicated to designing and implementing the user interface using Jetpack Compose, ensuring style and fit for the app.
   4 hours were allocated to backend logic, including integration with Firebase Authentication and Firebase Firestore, user authentication, data management and in app logic.
   3 hours was spent on testing, bug fixing, and code optimization to ensure a stable and efficient application.
## Notes

