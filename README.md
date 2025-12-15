# 📱 BMI & BMR Health Tracker (Android | Full‑Stack)

A **comprehensive Android health application** built using **Java in Android Studio** that calculates **Body Mass Index (BMI)** and **Basal Metabolic Rate (BMR)**. The app features a secure **dual‑user system (Admin & User)**, real‑time cloud synchronization using **Firebase Realtime Database**, and **dynamic visual diet plans** to promote health awareness.

---

## 📘 Table of Contents

* [Project Overview](#-project-overview)
* [Features](#-features)
* [Tech Stack](#-tech-stack)
* [Architecture](#-architecture)
* [Getting Started](#-getting-started)
* [Usage Guide](#-usage-guide)
* [Screenshots](#-screenshots)
* [Future Enhancements](#-future-enhancements)
* [Author](#-author)

---

## 🧠 Project Overview

The **BMI & BMR Health Tracker** is a full‑stack Android application designed to help users understand their health condition based on scientifically accepted formulas.

It not only calculates BMI and BMR but also:

* Stores user data securely in the cloud
* Provides **personalized diet charts**
* Enables **admin‑level monitoring and analytics**

This project demonstrates practical skills in **Android development, Firebase integration, MVC architecture, and custom UI components**.

---

## 🚀 Features

### 👤 User Features

* **Smart Calculator**
  Calculate BMI and BMR using the **Mifflin‑St Jeor Formula**

* **Unit Conversion**
  Toggle between:

  * Metric (Kg / Cm)
  * Imperial (Lbs / Feet + Inches)

* **Personalized Dashboard**

  * Greets user by name
  * Retains profile data (gender, age)

* **Dynamic Diet Charts**

  * Custom Pie Chart (Canvas Drawing)
  * Visualizes **Carbs, Protein, Fats, Vegetables**
  * Adjusts dynamically based on BMI category

* **Health Tips**
  Provides actionable advice based on BMI & BMR results

* **History Tracking**

  * Saves all calculations to Firebase
  * View complete history
  * Long‑press to delete old records

---

### 🛡️ Admin Features

* **Secure Admin Dashboard**
  Accessible only via a secret key during registration

* **User Management**

  * View all registered users in a RecyclerView

* **User Reports**

  * Tap any user to see full BMI & BMR history
  * Track health trends

* **Analytics**

  * View total number of registered users

---

## 🛠️ Tech Stack

| Layer          | Technology                         |
| -------------- | ---------------------------------- |
| Language       | Java                               |
| IDE            | Android Studio                     |
| UI             | XML, Material Design, CardView     |
| Custom UI      | Canvas (Pie Chart Drawing)         |
| Backend        | Firebase Realtime Database (NoSQL) |
| Authentication | Firebase Auth (Email/Password)     |
| Architecture   | MVC (Model‑View‑Controller)        |

---

## 🏗️ Architecture

The project follows the **MVC Pattern**:

* **Model** → User, HealthRecord, AdminData
* **View** → Activities, XML Layouts
* **Controller** → Activities & Adapters handling logic

This ensures:

* Clean separation of concerns
* Better maintainability
* Scalable development

---

## 🚀 Getting Started

### ✅ Prerequisites

* Android Studio installed
* A Firebase project created
* Android device or emulator

---

### 🔧 Installation

#### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/bmi-bmr-calculator.git
```

#### 2️⃣ Firebase Setup (Crucial Step)

1. Go to **Firebase Console**
2. Create a new project
3. Add an **Android App** with package name:

   ```text
   bd.edu.seu
   ```
4. Download **google-services.json**
5. Paste it inside:

```text
bmi-bmr-calculator/
 ├── app/
 │   ├── google-services.json   ← place here
 │   ├── src/
 │   └── ...
```

6. Enable:

   * **Authentication → Email/Password**
   * **Realtime Database → Test Mode**

---

### ▶️ Build & Run

1. Open the project in **Android Studio**
2. Sync Gradle files
3. Run on Emulator or Physical Device

---

## 📖 Usage Guide

### 📝 Registration

* **Regular User**

  * Enter name, email, password, gender
  * Click **Register**

* **Admin**

  * Check **Register as Admin**
  * Enter secret key:

    ```text
    TEACHER123
    ```

---

### 🧮 Calculating BMI & BMR

1. Select unit system (Kg/Lbs)
2. Enter Age, Weight, Height
3. Click **Calculate**
4. View BMI, BMR, and Health Status
5. Tap **View Diet Plan** for chart

---

### 🗂️ Managing History

* Click **My History**
* Scroll through past records
* **Long‑press** any item to delete

---

## 📸 Screenshots

*(Add screenshots here)*

* Login Screen
* <img width="391" height="829" alt="Screenshot 2025-12-15 221034" src="https://github.com/user-attachments/assets/ba7ac8a5-598a-465d-9b78-e5b64a5ec239" />

* Dashboard
* <img width="386" height="817" alt="Screenshot 2025-12-15 221213" src="https://github.com/user-attachments/assets/629f86c9-fe0d-4a95-95b5-a8b21911a43a" />

* Diet Chart
* <img width="373" height="823" alt="Screenshot 2025-12-15 221258" src="https://github.com/user-attachments/assets/7e784913-d14a-480d-8248-b6ff84ff96d5" />

* History Screen
<img width="368" height="808" alt="Screenshot 2025-12-15 221337" src="https://github.com/user-attachments/assets/c5d50f1b-def6-47cc-9c77-3930d5c2107f" />

---

## 🔮 Future Enhancements

* Google Fit integration
* Offline data caching
* Weekly & monthly analytics
* Dark mode support
* AI‑based health recommendations

---

## 👨‍💻 Author

**Abid Hasan**
🎓 CSE Student, Southeast University
💡 Interested in Android Development, Java, and Full‑Stack Engineering

---

⭐ *If you find this project helpful, consider starring the repository!*
