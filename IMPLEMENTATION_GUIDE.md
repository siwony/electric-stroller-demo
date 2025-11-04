# Electric Stroller Demo - Complete Implementation Guide

## Overview
This Android application demonstrates an electric stroller rental system with a complete UI flow, auto-advancing multi-step processes, and comprehensive debug capabilities. Built with Jetpack Compose and following modern Android development practices.

## Project Structure

```
app/src/main/java/com/lightning/strollerrental/
├── MainActivity.kt              # Main entry point with navigation
├── StrollerViewModel.kt         # State management and business logic
├── RentalState.kt              # State enum definition
├── DemoLogger.kt               # Logging utility
│
├── Screens:
│   ├── SplashScreen.kt         # Initial splash/permission screen
│   ├── HomeScreen.kt           # Home with debug panel
│   ├── RentingScreen.kt        # 4-step rental progress
│   ├── RentedScreen.kt         # Active rental with timer
│   ├── ReturningScreen.kt      # 4-step return progress
│   ├── ReturnedScreen.kt       # Return complete summary
│   └── SettingsScreen.kt       # Settings and info
│
└── Dialogs:
    ├── ErrorDialog.kt          # Error handling dialog
    └── CancelConfirmDialog.kt  # Back button confirmation
```

## State Flow Diagram

```
[SPLASH] → [IDLE/HOME] → [RENTING] → [RENTED] → [RETURNING] → [RETURNED] → [IDLE/HOME]
                ↓            ↓           ↓            ↓
            [SETTINGS]   [ERROR]     [ERROR]      [ERROR]
```

## Key Features

### 1. Auto-Advancing Multi-Step Processes
**Rental Process (4 steps, 10s each):**
1. 유모차와 연결 중... (Connecting to stroller)
2. 사용자 인증 중... (Authenticating user)
3. 잠금 장치 해제 중... (Unlocking device)
4. 대여 완료! (Rental complete)

**Return Process (4 steps, 10s each):**
1. 유모차와 연결 중... (Connecting to stroller)
2. 유모차 상태 확인 중... (Checking stroller status)
3. 잠금 장치 잠그는 중... (Locking device)
4. 반납 완료! (Return complete)

### 2. State Management
```kotlin
data class RentalUiState(
    val currentState: RentalState,      // Current flow state
    val strollerId: String?,             // Generated mock ID
    val startAt: Long?,                  // Rental start timestamp
    val elapsedSec: Int,                 // Elapsed time in seconds
    val currentStep: Int,                // Current step (0-3)
    val stepDelayMs: Long = 10000L,     // Configurable delay
    val forceFail: Boolean = false,      // Debug: force failure
    val errorMessage: String?,           // Error message if any
    val showSplash: Boolean = true       // Show splash on start
)
```

### 3. Debug Panel
Located on the home screen:
- **Delay Input**: Adjust step duration (default: 10000ms)
- **Force Fail Toggle**: Simulate failures for testing

### 4. Features Highlights

#### Active Rental Screen
- Displays stroller ID (format: "ST" + timestamp)
- Shows rental start time
- Real-time elapsed timer (updates every second)
- "반납하기" button to initiate return

#### Return Complete Screen
- Success checkmark icon
- Usage summary (stroller ID + elapsed time)
- Auto-navigation to home after 2 seconds
- Manual "홈으로" button

#### Settings Screen
- Demo mode information
- Log export (toast notification)
- Terms of service (mock dialog)
- Privacy policy (mock dialog)

#### Error Handling
- Comprehensive error dialogs
- Retry and cancel options
- Automatic state recovery

#### Back Button Handling
- During rental/return: Shows confirmation dialog
- "진행을 취소할까요?" (Cancel progress?)
- Option to continue or cancel operation

### 5. Logging System
All user interactions and flow events are logged:
```kotlin
DemoLogger.logScreenView("screen_name")    // Screen entry
DemoLogger.logClick("button_name")         // Button clicks
DemoLogger.logFlow(step, "screen")         // Progress steps
DemoLogger.logFlowError(code, step)        // Errors
```

## Technical Details

### Dependencies (build.gradle.kts)
```kotlin
// Core Android
implementation("androidx.core:core-ktx:1.17.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")

// Compose
implementation("androidx.activity:activity-compose:1.11.0")
implementation(platform("androidx.compose:compose-bom:2025.10.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// Navigation
implementation("androidx.navigation:navigation-compose:2.9.5")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
```

### Configuration
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Compile SDK**: 36
- **Kotlin Version**: 1.9.25
- **JVM Target**: 17

## How to Use

### For Developers
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on emulator or device

### For Demos
1. **Start App**: Opens splash screen
2. **Click "시작하기"**: Navigate to home
3. **Adjust Debug Panel** (optional):
   - Change delay (e.g., 2000ms for faster demo)
   - Toggle force fail for error testing
4. **Click "대여하기"**: Start rental flow
5. **Watch Auto-Progress**: 4 steps, 10s each
6. **Rented Screen**: View stroller info and timer
7. **Click "반납하기"**: Start return flow
8. **Watch Auto-Progress**: 4 steps, 10s each
9. **Return Complete**: Auto-return to home in 2s

### Testing Scenarios

#### Normal Flow
- Delay: 10000ms, Force Fail: OFF
- Complete rental → Complete return

#### Fast Demo
- Delay: 2000ms, Force Fail: OFF
- Quick demonstration mode

#### Error Testing
- Delay: 5000ms, Force Fail: ON
- Test error dialogs and retry flow

#### Back Button Testing
- Start rental → Press back → See confirmation
- Choose to cancel or continue

## Mock Data

### Stroller ID Generation
```kotlin
private fun generateStrollerId(): String {
    val timestamp = System.currentTimeMillis() % 10000
    return "ST$timestamp"
}
```
Format: `ST` + last 4 digits of timestamp (e.g., "ST3847")

### Time Display
- **Elapsed Time**: Updates every second
- **Format**: 
  - Under 1 hour: "MM:SS"
  - Over 1 hour: "HH:MM:SS"
  - Summary: "X시간 Y분 Z초"

## UI/UX Design

### Design Principles
- **Clean & Simple**: White background, minimal colors
- **Material3**: Modern Android design system
- **Korean Language**: All UI text in Korean
- **Accessibility**: Content descriptions on icons
- **Responsive**: Proper spacing and touch targets (min 48dp)

### Color Scheme
- Uses Material3 theme
- Primary color for important elements
- Surface variants for cards
- Proper contrast for readability

## Best Practices Implemented

1. **MVVM Architecture**: Clear separation of concerns
2. **Reactive State**: StateFlow for UI updates
3. **Coroutines**: Non-blocking async operations
4. **Composition**: Reusable composable functions
5. **Navigation**: Declarative navigation with Compose
6. **Error Handling**: Comprehensive error states
7. **Logging**: Structured logging for debugging
8. **Accessibility**: Content descriptions and readable UI

## Future Enhancements (Out of Scope)

The current implementation is complete for the demo requirements. Future versions could add:
- Actual BLE communication
- Real backend integration
- User authentication
- Payment processing
- Multi-language support
- iOS version
- Advanced animations
- Offline support
- Push notifications

## Troubleshooting

### Build Issues
- Ensure Gradle version 8.13+ is available
- Check Android SDK is installed
- Verify internet connection for dependencies

### Runtime Issues
- Check logcat for DemoLogger output
- Verify navigation flow in debug mode
- Use debug panel to adjust timing

## License & Credits

This is a demonstration application built according to specifications in `feature.md` and `require.md`.

**Package**: `com.lightning.strollerrental`  
**Version**: 1.1  
**Build**: Debug/Demo Mode

---

## Contact & Support

For questions about this implementation, refer to:
- `feature.md` - Feature specifications
- `require.md` - Detailed requirements
- `VERIFICATION_COMPLETE.md` - Implementation checklist
