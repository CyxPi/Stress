# Architecture Decisions

## Overview

The Stress Monitoring app uses **MVVM** with a **layered architecture** (data, domain, ui) for maintainability and testability.

## Key Decisions

### 1. Layered Architecture

- **Data Layer**: Room entities, DAOs, database, repository. Handles persistence and data mapping.
- **Domain Layer**: Models and use cases. Business logic is isolated here.
- **UI Layer**: ViewModels, Compose screens, components. Observes state and emits actions.

**Rationale**: Clear separation allows independent testing and future ML integration without touching UI.

### 2. StateFlow for UI State

ViewModels expose `StateFlow<UiState>` instead of LiveData.

**Rationale**: StateFlow is Kotlin-first, supports cold flows, and integrates well with Compose's `collectAsState()`.

### 3. Rule-Based Stress Analysis

`StressAnalysisUseCase` uses simple rules (averages, first-half vs second-half comparison) instead of ML.

**Rationale**: Meets requirements without ML complexity. The use case interface is designed so an ML-based implementation can replace it later without changing callers.

### 4. Manual Dependency Provision

Dependencies are provided via `StressApplication` and passed to ViewModels in the NavHost.

**Rationale**: Keeps the project simple. Hilt/Koin can be added later for larger scale.

### 5. Custom Canvas Chart

Stress history uses a custom Canvas-based line chart instead of a charting library.

**Rationale**: Avoids external chart library version compatibility issues. Simple line chart is sufficient for the use case.

### 6. Calm Color Palette

Theme uses fixed sage green, lavender, and warm grays instead of dynamic Material You colors.

**Rationale**: Consistent, calming visual experience for a stress-focused app.

## Future ML Integration

To add ML-based stress prediction:

1. Create `StressPredictionUseCase` that wraps an ML model.
2. Add `predictNextWeekStress(entries: List<StressEntry>): Float` or similar.
3. Call from `StressViewModel` and display in UI.
4. `StressAnalysisUseCase` can remain for rule-based metrics; ML adds prediction on top.
