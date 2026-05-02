

This project is a modern Android application built to demonstrate a clean, scalable architecture and best practices in Android development.

 Architecture Decisions
The project follows the Clean Architecture principles combined with MVVM (Model-View-ViewModel).

UI Layer (Jetpack Compose): A fully declarative UI built with Google's modern toolkit. Navigation is handled via the new navigation3 API.

Domain/Repository Layer: Acts as a mediator between the data sources (Local & Remote) and the ViewModels. It ensures the UI remains agnostic of the data source.

Data Layer:

Remote: Uses Retrofit with Kotlinx Serialization for type-safe API communication.

Local: Uses Room Database for persistent storage and offline support (Favorites).

Dependency Injection: Powered by Koin. Koin was chosen for its lightweight nature, ease of use with Compose, and fast compile times.
.........


Tech Stack

Language: Kotlin

UI: Jetpack Compose (Material 3

Asynchronous Flow: Kotlin Coroutines & Flow

Dependency Injection: Koin

Networking: Retrofit + OkHttp + Kotlinx Serialization

Database: Room

Image Loading: Coil
