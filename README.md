# The Body Fragrances Android Frontend

This repository contains the native Android frontend for The Body Fragrances e-commerce platform, built in Android Studio (Java/Kotlin).

## Project description

The app allows users to browse perfume categories, view product details, place orders, and complete the customer flow from mobile. It is connected to a separate PHP/MySQL backend admin panel that manages products, categories, orders, and news/offers.

## What is included

- Android app source in `app/`
- Gradle build scripts and wrapper
- Layouts, drawables, and assets used by the app

## Run locally (Android Studio)

1. Open Android Studio and choose **Open** → select this repository folder.
2. Let Gradle sync and download dependencies.
3. Connect an Android device or use an emulator and Run the app.

## Configuration notes

- Do not commit `local.properties` (SDK path) or `.idea/`; they are ignored by `.gitignore`.
- Configure backend API URLs in `app/src/main/java/com/umt/ecommerce/utils/Constants.java`.

## Backend

The admin panel and APIs are maintained in a separate PHP repository. Configure API endpoints in the Android app before running.

## License

Add license info here if you want to open-source the project (e.g., MIT).