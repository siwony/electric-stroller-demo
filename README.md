# electric-stroller-demo
전기유모차 데모앱

## Overview
This is a Kotlin Android app using Jetpack Compose & Navigation that implements a stroller rental flow with 4 screens.

## Features
- **Navigation Flow**: Home → Renting → Rented → Returning
- **Auto-advance**: Each renting step advances automatically every 10 seconds (default)
- **Renting Steps** with Korean text:
  1. 유모차와 연결 중... (Connecting to stroller...)
  2. 사용자 인증 중... (Authenticating user...)
  3. 잠금 장치 해제 중... (Unlocking device...)
  4. 대여 완료! (Rental complete!)
- **ViewModel State Management**: States include IDLE, RENTING, RENTED, RETURNING, RETURNED
- **Debug Panel**: Edit delay time (default 10000ms) and toggle forceFail option on the Home screen

## Project Structure
```
app/
├── src/main/
│   ├── java/com/lightning/strollerrental/
│   │   ├── MainActivity.kt          # Main activity with Navigation setup
│   │   ├── StrollerViewModel.kt     # ViewModel with state management
│   │   ├── RentalState.kt          # Enum for rental states
│   │   ├── HomeScreen.kt           # Home screen with debug panel
│   │   ├── RentingScreen.kt        # Renting screen with 4 auto-advancing steps
│   │   ├── RentedScreen.kt         # Rented screen
│   │   └── ReturningScreen.kt      # Returning screen
│   ├── res/
│   │   └── values/strings.xml
│   └── AndroidManifest.xml
└── build.gradle.kts
```

## Building the App
```bash
./gradlew assembleDebug
```

## Running the App
```bash
./gradlew installDebug
```

## Dependencies
- Jetpack Compose (Material3)
- Navigation Compose
- Lifecycle ViewModel Compose
- Kotlin Coroutines
