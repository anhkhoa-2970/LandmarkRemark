# Build LANDMARK REMARKER README


## Prerequisites
Before you begin, make sure you have the following installed:
- Android Studio (using Android support AGP 8.2)
- JDK (Java Development Kit)
- Android SDK


## Overview
This Android application, Landmark Remark, allows users to save notes associated with specific locations on a map and see note in map by click to marker. Users can view existing notes, add new notes, and search for notes by username or content. The app aims to provide a simple and intuitive way for users to mark and share interesting locations with others.


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
###  Approach
- The application is built using modern Android development practices, leveraging Jetpack Compose for UI development, Hilt for dependency injection, and Gson for JSON serialization/deserialization.
- Coroutines are used for asynchronous programming, allowing for efficient and non-blocking execution of tasks such as network requests and database operations.
- Use Firebase Authentication to create and save information for each user. I want to use UID as key to save collection in Firestore Database
- Firebase Firestore is utilized as the backend database for storing user data and notes, providing real-time synchronization and offline support.

### Time Allocation
- Approximately 2 hours were spent on initial project setup, including setting up the development environment and integrating necessary libraries.
- 3 hours dedicated to designing and implementing the user interface using Jetpack Compose, ensuring style and fit for the app.
- 4 hours were allocated to backend logic, including integration with Firebase Authentication and Firebase Firestore, user authentication, data management and in app logic.
- 3 hours was spent on testing, bug fixing, and code optimization to ensure a stable and efficient application.


## Notes
- My location:
![current_location](https://private-user-images.githubusercontent.com/63275374/331449287-5bb0f8eb-046e-4e53-ae7a-b3ad8fbf93cf.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTU5MTY1OTMsIm5iZiI6MTcxNTkxNjI5MywicGF0aCI6Ii82MzI3NTM3NC8zMzE0NDkyODctNWJiMGY4ZWItMDQ2ZS00ZTUzLWFlN2EtYjNhZDhmYmY5M2NmLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDA1MTclMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwNTE3VDAzMjQ1M1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTNhNGZjOGRlNjUxOWY2YWNlMDJiY2I1ZGE3NGEzOGQ0MDE3YjVmNDkwZWI1YzIwOTE0ZTJjMmEwYTM0MTFmZjYmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.FvfkRmLqyaNEz72BwngWzcYeP_a0riqu6yoGOheLAS8)
- User location:
![user_location](https://private-user-images.githubusercontent.com/63275374/331449493-cfe1f348-0ab2-47bb-943f-dae0c073a4e9.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTU5MTY1ODAsIm5iZiI6MTcxNTkxNjI4MCwicGF0aCI6Ii82MzI3NTM3NC8zMzE0NDk0OTMtY2ZlMWYzNDgtMGFiMi00N2JiLTk0M2YtZGFlMGMwNzNhNGU5LnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDA1MTclMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwNTE3VDAzMjQ0MFomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTkyNzUyYjkyYThiZDJhNzAwYTc3OTVmNWJlOGExMjc5M2VlMTUyMTBiZmM2ZDM3ODIzNmQwYWFmZmZlYTUxNzEmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.mVHGY61k-oBWiu15__bnGaBGVyShgBrSKChNpJuUrRY)
