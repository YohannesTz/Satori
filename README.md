
# Satori - Enlighten Your Reading

Welcome to **Satori**, an app to explore and discover books using the Google Books API. "Satori" means "enlightenment" in Japanese, and the app aims to bring knowledge and light through books.

## Features

- **Search for Books**: Find books by title, author, or genre.
- **View Book Details**: Get detailed information about books including title, author, description, and cover image.
- **Favorite Books**: Save your favorite books for later.
- **Smooth UI**: Built with modern Jetpack Compose to ensure a smooth user experience.

## Tech Stack

- **Jetpack Compose**: For building the user interface.
- **MVVM Architecture**: The app uses Model-View-ViewModel pattern to separate UI from business logic.
- **Koin**: For dependency injection.
- **Ktor**: For handling network requests to Google Books API.
- **Room Database**: To store and manage your favorite books offline.
- **Coil**: For loading images efficiently in Compose.
- **AndroidX**: Core libraries like Lifecycle, Navigation, and DataStore for better state and data management.

## Libraries Used

### Core/AndroidX
- `androidx.core:core-ktx`: Kotlin extensions for Android core library.
- `androidx.lifecycle:lifecycle-runtime-ktx`: Lifecycle-aware components.
- `androidx.activity:activity-compose`: Compose support for Activity.
- `androidx.core:core-splashscreen`: For creating a splash screen.
- `androidx.lifecycle:lifecycle-viewmodel-compose`: ViewModel integration with Compose.
- `androidx.navigation:navigation-compose`: Navigation within Compose.
- `androidx.palette:palette-ktx`: Generate palette colors from book covers.
- `androidx.datastore:datastore-preferences`: Storing user preferences locally.

### Compose
- `androidx.compose`: All essential Compose libraries.
- `androidx.ui.graphics`: Compose support for drawing and graphics.
- `androidx.material3`: Modern Material 3 UI elements for Compose.

### Room Database
- `androidx.room`: For saving favorite books locally.
- `androidx.work`: Background tasks to sync data.
- `androidx.browser`: Opening links and web pages.
- `androidx.material3.window-size`: Window size class support for responsive UI.

### Coil
- `coil-compose`: Load book cover images in Compose.
- `coil-network-okhttp`: Network support for Coil.

### Koin (Dependency Injection)
- `koin.core`: Core Koin library for dependency injection.
- `koin.android`: Koin Android integration.
- `koin.compose`: Compose support for Koin DI.

### Networking with Ktor
- `ktor.client.okhttp`: OkHttp engine for Ktor.
- `ktor.serialization.kotlinx-json`: JSON serialization with Kotlinx.
- `ktor.client.content-negotiation`: Content negotiation for Ktor.
- `ktor.client.auth`: Handle authentication.
- `ktor.client.logging`: Logging for network requests.

### Placeholder
- `placeholder.material3`: Material 3 design for placeholder loading.

## App Architecture

The app follows MVVM (Model-View-ViewModel) architecture:

- **Model**: Represents the data layer, includes the Room database, repositories for fetching data from Google Books API.
- **ViewModel**: Handles all the business logic and communicates with the Model to get the necessary data.
- **View (Compose)**: Displays data on the screen and listens to changes from the ViewModel.

## Screenshots

<p align="center">
    <img src="/screenshots/1.png" width="30%" height="30%" alt="screenshot">
    <img src="/screenshots/2.png" width="30%" height="30%" alt="screenshot">
    <img src="/screenshots/3.png" width="30%" height="30%" alt="screenshot">
    <img src="/screenshots/4.png" width="30%" height="30%" alt="screenshot">
    <img src="/screenshots/5.png" width="30%" height="30%" alt="screenshot">
</p>


## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more information.
  
---  

Thank you for using **Satori**! I hope it helps you find and explore many books.

Satori - Enlightenment through reading 📚.