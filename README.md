# StudyHive — Android App

A native Android companion for the StudyHive platform built with Jetpack Compose, Kotlin coroutines, and the Supabase Kotlin SDK. It mirrors every feature of the web frontend and talks to the same Spring Boot API.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Auth | Supabase Kotlin SDK v3 (`auth-kt`) + Custom Tabs OAuth |
| Networking | Retrofit 2 + OkHttp 4 + Gson |
| Architecture | MVVM — `ViewModel` + `StateFlow` |
| Async | Kotlin Coroutines |
| Min SDK | API 26 (Android 8.0) |
| Target SDK | API 35 |
| Build | Gradle 9.1 (Kotlin DSL) |

---

## Project Structure

```
app/src/main/java/com/example/studyhive_android/
│
├── StudyHiveApplication.kt          # Initialises Supabase + Retrofit on app start
├── MainActivity.kt                  # Root activity, deep-link handler, manual nav graph
│
├── data/
│   ├── model/                       # Plain data classes (DTOs)
│   │   ├── StudyGroupDto.kt / CreateGroupRequest.kt
│   │   ├── SessionDto.kt / CreateSessionRequest.kt
│   │   ├── CourseDto.kt
│   │   ├── UserDto.kt / UpdateProfileRequest.kt
│   │   └── AuthResponse.kt / LoginRequest.kt / SignupRequest.kt
│   │
│   ├── network/                     # Retrofit interfaces + singletons
│   │   ├── RetrofitClient.kt        # OkHttp + auth interceptor singleton
│   │   ├── SupabaseClient.kt        # Supabase SDK singleton
│   │   ├── GroupApi.kt
│   │   ├── SessionApi.kt
│   │   ├── CourseApi.kt
│   │   ├── UserApi.kt
│   │   └── AuthApi.kt
│   │
│   └── repository/                  # One repository per domain area
│       ├── AuthRepository.kt        # Sign-in, sign-up, sign-out, backend bootstrap
│       ├── GroupRepository.kt       # CRUD + join/leave/membership
│       ├── SessionRepository.kt
│       ├── CourseRepository.kt
│       └── UserRepository.kt
│
└── ui/
    ├── screens/                     # Composable screens
    │   ├── LoginScreen.kt
    │   ├── SignupScreen.kt
    │   ├── DashboardScreen.kt
    │   ├── BrowseGroupScreen.kt
    │   ├── MyGroupsScreen.kt
    │   ├── CreateGroupScreen.kt
    │   ├── ProfileScreen.kt
    │   └── ResetPasswordScreen.kt
    │
    ├── viewmodels/                  # StateFlow-driven ViewModels
    │   ├── AuthViewModel.kt
    │   ├── DashboardViewModel.kt
    │   ├── BrowseGroupsViewModel.kt
    │   ├── MyGroupsViewModel.kt
    │   ├── CreateGroupViewModel.kt
    │   └── ProfileViewModel.kt
    │
    └── theme/
        ├── Color.kt                 # Brand palette matching the web (#2563EB primary)
        ├── Theme.kt                 # Light + dark Material 3 schemes
        └── Type.kt
```

---

## Screens & Features

| Screen | Description |
|---|---|
| Login | Email/password · Google OAuth · GitHub OAuth (Custom Tabs) |
| Sign Up | Email registration · social sign-up |
| Dashboard | Welcome card, stat chips (groups / sessions / courses), upcoming sessions, group cards |
| Browse Groups | Search bar, dynamic filter chips (All / course codes / meeting modes), join/leave inline |
| My Groups | Groups owned or joined, with next-session preview card per group |
| Create Group | Title, description, course dropdown, mode selector, location, max-members |
| Profile | Edit name/bio/major, course enrolment management, password change, delete account |

---

## Authentication Flow

The flow mirrors `AuthContext.tsx` from the web app:

1. `SupabaseClient.init()` is called once in `StudyHiveApplication.onCreate()`.
2. `AuthRepository.sessionFlow` (mapped from `auth.sessionStatus`) emits session changes.
3. `AuthViewModel` observes the flow; on a new session it calls `AuthRepository.bootstrapBackendProfile()` → `POST /api/user` (up to 5 retries with exponential back-off).
4. On success, `authState` becomes `Authenticated` and `MainActivity` navigates to the Dashboard.
5. OAuth redirects use the deep-link scheme `studyhive://login-callback`, handled in `MainActivity.onNewIntent()` via `client.handleDeeplinks(intent)`.

---

## Networking

`RetrofitClient` is a singleton that:

- Reads `API_BASE_URL` from `BuildConfig` (set from `local.properties` at build time).
- Attaches a Supabase JWT `Authorization: Bearer` header on every request via an `Interceptor` (equivalent to the Axios interceptor in the web app).
- Enables `BODY`-level OkHttp logging in debug builds only.

```kotlin
// RetrofitClient auth interceptor
private val authInterceptor = Interceptor { chain ->
    val token = runBlocking { SupabaseClient.getAccessToken() }
    val request = chain.request().newBuilder()
        .apply { if (!token.isNullOrBlank()) header("Authorization", "Bearer $token") }
        .build()
    chain.proceed(request)
}
```

---

## Setup

### Prerequisites

- Android Studio Ladybug or newer
- JDK 11 (set via `compileOptions` in `build.gradle.kts`)
- A Supabase project configured with:
  - Google and/or GitHub OAuth providers
  - Redirect URL: `studyhive://login-callback`

### local.properties

Add the following to `local.properties` (never commit this file):

```properties
SUPABASE_URL=https://your-project.supabase.co
SUPABASE_ANON_KEY=your-anon-key
API_BASE_URL=https://your-api.example.com/
```

For local development with the emulator, set `API_BASE_URL=http://10.0.2.2:8080/` (the emulator's alias for `localhost`). Cleartext traffic to `10.0.2.2` and `localhost` is permitted via `res/xml/network_security_config.xml`.

### Build & Run

Open the project in Android Studio and run the `:app` configuration, or via the command line:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

---

## Architecture Notes

Navigation is handled manually in `MainActivity.kt` with a `currentScreen` state variable and a simple `backStack` list — no Jetpack Navigation component. Each screen composable is stateless with respect to navigation; it receives callbacks (`onBack`, `onCreateGroup`, etc.) as parameters.

ViewModels expose a single `uiState: StateFlow<XxxUiState>` data class. Screens collect this with `collectAsStateWithLifecycle()` and read fields directly — no separate event channels.

Repositories return plain values or throw on error; ViewModels catch exceptions and surface them as nullable `error: String?` fields on the UI state.

---

## Running Tests

```bash
./gradlew test                   # JVM unit tests
./gradlew connectedAndroidTest   # Instrumented tests (requires a device/emulator)
```
