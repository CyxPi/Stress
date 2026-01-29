# Stress State Monitoring – Data Analysis and Relaxation Advice

An Android application for tracking daily stress levels, analyzing trends, and providing personalized relaxation recommendations.

## Tech Stack

- **Kotlin** – Primary language
- **MVVM Architecture** – Separation of concerns with ViewModel + StateFlow
- **Jetpack Compose** – Modern declarative UI
- **Room** – Local SQLite database for data persistence
- **Material 3** – UI components and theming
- **Navigation Compose** – Type-safe navigation
- **Minimum SDK 26+**

## Architecture

The app follows a **layered architecture** with clear separation:

```
app/src/main/java/com/example/stress/
├── data/                    # Data layer
│   ├── local/
│   │   ├── entity/          # Room entities
│   │   ├── dao/             # Data Access Objects
│   │   ├── database/         # Room database
│   │   └── SampleDataProvider.kt
│   └── repository/          # Repository implementations
├── domain/                  # Domain layer
│   ├── model/               # Domain models
│   └── usecase/             # Business logic (analysis, recommendations)
└── ui/                      # UI layer
    ├── components/          # Reusable UI components
    ├── navigation/          # Navigation setup
    ├── screens/             # Compose screens
    ├── theme/               # Material 3 theme
    └── viewmodel/           # ViewModels
```

### Architecture Decisions

1. **Data Layer**: Room provides type-safe, reactive queries. Entities are kept simple; mapping to domain models happens in the repository.

2. **Domain Layer**: Use cases encapsulate business logic. `StressAnalysisUseCase` performs rule-based analysis (averages, peaks, trends). The structure allows future ML integration without changing the interface.

3. **UI Layer**: ViewModels expose `StateFlow<UiState>` for reactive UI updates. Compose screens observe state and emit user actions.

4. **No DI Framework**: Manual dependency provision via `StressApplication`. Consider Hilt/Koin for larger projects.

## Core Features

### Stress Tracking
- Daily stress level (1–10 scale)
- Related factors: sleep duration, mood (1–5), physical activity (minutes), optional notes
- All data stored locally in Room

### Data Analysis
- **Averages**: Daily and weekly stress averages
- **Trends**: Improving / Stable / Worsening (rule-based comparison of first vs. second half of data)
- **Stress Peaks**: Top 5 highest-stress days highlighted
- **High Stress Days**: Count of days with stress ≥ 7

### Relaxation Module
- **Breathing Exercises**: Animated 4-7-8 or box breathing with visual timer
- **Mindfulness Tips**: Short, calming tips
- **Relaxation Techniques**: Text-based guidance (progressive muscle relaxation, grounding, etc.)
- **Dynamic Recommendations**: Content adapts to current stress level (low/medium/high)

## How to Use

1. **Dashboard**: View today's stress level and trend summary. Tap "Add" (FAB) to log a new entry.
2. **Add Entry**: Use sliders to set stress level, sleep, mood, activity. Add optional notes.
3. **History**: View stress level chart over time.
4. **Relaxation**: Access from dashboard when you have a stress entry. Tap breathing exercises to start the animated timer.
5. **Sample Data**: Tap the refresh icon on the dashboard to load 14 days of sample data for testing.

## Sample Data

Use the refresh icon (↻) on the dashboard to load 14 days of sample stress entries. Useful for:
- Testing the chart and analysis
- Demo purposes
- Verifying trend calculations

## Error Handling

- Repository operations are wrapped in `try/catch`; errors surface via `StressUiState.error`
- Snackbar displays error messages on the dashboard
- Flow `.catch {}` prevents unhandled exceptions in data streams

## TODOs for Future Improvements

- [ ] **ML Integration**: Add stress prediction model; `StressAnalysisUseCase` is structured for extension
- [ ] **Room Migrations**: Add proper migrations when schema changes (currently uses `fallbackToDestructiveMigration`)
- [ ] **Dependency Injection**: Migrate to Hilt for cleaner DI
- [ ] **Export Data**: CSV/JSON export for user data
- [ ] **Reminders**: Notifications to log daily stress
- [ ] **Widgets**: Home screen widget showing today's stress
- [ ] **Unit Tests**: ViewModel and use case tests
- [ ] **UI Tests**: Compose UI tests for critical flows

## Building

```bash
./gradlew assembleDebug
```

Requires JDK 17+ and Android SDK 35.
