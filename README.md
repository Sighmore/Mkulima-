# Shamba - Tea Farm Management App

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png" alt="Shamba Logo" width="120"/>
</p>

## Overview
Shamba is an intuitive Android application designed for tea farm managers to streamline tea plucking operations. Leveraging modern technology and a user-friendly interface, Shamba simplifies the tracking, recording, and analysis of tea farm activities, ensuring efficiency and productivity.

## Features
- 🌿 **Tea Plucking Records**
  - Easily record daily tea quantities for each plucker
  - Automatic calculation of payments based on company rates
  - Real-time preview of total payment amounts

- 💰 **Company-Specific Rates**
  - KTDA/SASINI: KSH 9.0 per kilo
  - TET/CHEML: KSH 8.0 per kilo
  - Custom rates: KSH 7.0 per kilo

- 📊 **Comprehensive Daily Statistics**
  - Total number of pluckers
  - Aggregate kilos plucked
  - Average yield per plucker

- 🌦 **Weather Forecast Integration**
  - Fetches weather data using Retrofit
  - Helps farmers plan tea plucking activities based on weather forecasts

- 📱 **Modern and Adaptive UI**
  - Material 3 design principles
  - Supports both Dark and Light themes
  - Smooth and intuitive navigation experience

## Screenshots
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/76637bcb-a867-4040-97f6-b287d829df1f" alt="Screenshot 1" width="250"/></td>
    <td><img src="https://github.com/user-attachments/assets/51579b8d-2529-49d4-ba69-ac826eb6dd23" alt="Screenshot 3" width="250"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/ad4fde10-70d2-49a9-a7e0-8e784234dfc6" alt="Screenshot 4" width="250"/></td>
    <td><img src="https://github.com/user-attachments/assets/85eac1dc-26bd-4e5a-8815-1e7fa4fae571" alt="Screenshot 5" width="250"/></td>
  </tr>
</table>

## GIF - App in Action
<p align="center">
  <img src="https://github.com/user-attachments/assets/a800e824-b84f-4ce1-bd5a-a658cec0be0b" alt="App GIF" width="300"/>
</p>

## Technology Stack
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM
- **Database**: Room
- **Language**: Kotlin
- **Navigation**: Jetpack Navigation
- **Concurrency**: Kotlin Coroutines & Flow
- **Networking**: Retrofit

## Project Structure
The app is developed following the MVVM (Model-View-ViewModel) architecture, ensuring a clean separation of concerns and maintainable codebase. Key packages include:
- **Model**: Manages data and business logic
- **ViewModel**: Handles UI-related data in a lifecycle-conscious way
- **View**: Renders the UI and user interactions

## Getting Started
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle and build the project.
4. Run the app on an emulator or physical device.

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests to improve the app.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

