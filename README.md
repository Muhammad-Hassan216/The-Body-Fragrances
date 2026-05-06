# The Body Fragrances (E-commerce Android App)

This repository contains `The Body Fragrances` Android application built with Android Studio (Java/Kotlin). It includes a native Android front-end and a PHP-backed admin panel (separate).

## What’s included

- Android app source: `app/`
- Gradle build scripts and wrapper
- Resources and assets used by the app

## Run locally (Android Studio)

1. Open Android Studio and choose **Open** → select this repository folder.
2. Let Gradle sync and download dependencies.
3. Connect an Android device or use an emulator and Run the app.

Notes:
- Do not commit `local.properties` (SDK path) or `.idea/` — they are ignored by `.gitignore`.
- Backend APIs are expected to be configured in `app/src/main/java/com/umt/ecommerce/utils/Constants.java`.

## Backend

The admin panel and APIs are separate (PHP). Configure API endpoints in the Android app before running.

## License

Add license info here if you want to open-source the project (e.g., MIT).