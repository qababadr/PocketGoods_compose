
# PocketGoods (Android - Jetpack Compose)

PocketGoods is an Android application built with **Kotlin** and **Jetpack Compose**, designed as the native Android version of PocketGoods.  
The project follows a modular architecture with **core**, **core_ui**, **productFeature**, **authenticationFeature**, **wishlistFeature** modules and **settingsFeature** modules, focusing on scalability, maintainability, and modern Android best practices.

---

## 🚀 Tech Stack
- **Language:** Kotlin  
- **UI Framework:** Jetpack Compose  
- **Dependency Injection:** Hilt  
- **Networking:** Ktor Client  
- **State Management:** Kotlin StateFlow / ViewModels  
- **Testing:**  
  - **Integration Tests:** Ktor Client Mock + JUnit + Turbine  
  - **End-to-End Tests:** Espresso / Compose Test  

---

## 🛠️ Features
- 📱 Fully responsive Compose UI for Android  
- 🔐 Authentication & authorization (Hilt + ViewModel)  
- 🛍️ Product listing & details (productFeature module)  
- ❤️ Wishlist management (wishlistFeature module)
- ⚙️ Application settings (settingsFeature module)
- ⚡ Modern Kotlin + Compose architecture  

---

## 📂 Project Structure
```
pocketgoods_android/
├── app/ # Main app module
├── core/ # Core logic
├── core_ui/ # Reusable UI components
├── productFeature/ # Product listing and details
├── authenticationFeature/ # Login / Signup
├── wishlistFeature/ # Wishlist management
├── settingsFeature/ # application settings management
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🤝 Contributing
Contributions are welcome! Please open an issue or submit a pull request.

---

## 📜 License
This project is licensed under the MIT License.
