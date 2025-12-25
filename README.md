# 📱 BMI & BMR Calculator  
### 🤖 Android Application | ☕ Java | 🔥 Firebase | 🌐 Full-Stack Project


A full-stack Android health tracking application developed using **Java in Android Studio**.  
This app helps users calculate **Body Mass Index (BMI)** and **Basal Metabolic Rate (BMR)**, track health history, and follow personalized diet plans with **real-time Firebase cloud synchronization**.

---

## ✨ Highlights

- Accurate BMI & BMR calculation  
- Ideal weight goal suggestion  
- Smart age-based input validation  
- Firebase Authentication & Database  
- Admin & User role system  
- Custom diet pie chart visualization  

---

## 🧠 About the Project

The **BMI & BMR Health Tracker** is designed to raise health awareness using scientifically accepted formulas.

Instead of only showing numbers, the app:
- Explains health status clearly
- Suggests ideal weight goals
- Visualizes diet distribution
- Stores data securely for future tracking

This project follows proper **software engineering practices** and **MVC architecture**.

---

## 🚀 Core Features

### 🔹 Health Calculations
- BMI calculation with category (Underweight, Normal, Overweight, Obese)
- BMR calculation using **Mifflin-St Jeor Formula**
- Ideal weight range based on height

### 🔹 Smart Validation System
- Age limit: **5–120 years**
- Height & weight realism check
- Age group logic:
  - Child
  - Teenager
  - Adult
  - Senior

### 🔹 User Experience
- Metric & Imperial unit support
- Clean Material UI
- Dynamic diet chart using Canvas
- History tracking with delete option

---

## 👤 User Module

- Register & Login using Firebase Auth
- Calculate BMI, BMR & weight goals
- Save calculation history
- View personalized diet plan
- Real-time data sync with Firebase

---

## 🛡️ Admin Module

- Secure admin access
- Admin key: `TEACHER123`
- View all registered users
- Monitor health trends and records

---

## 🛠️ Tech Stack

| Category | Technology |
|--------|------------|
| Language | Java |
| IDE | Android Studio |
| UI | XML, Material Design |
| Charts | Canvas (Custom Pie Chart) |
| Backend | Firebase Realtime Database |
| Auth | Firebase Authentication |
| Architecture | MVC |

---

## 🏗️ Architecture Overview

The project follows **MVC (Model-View-Controller)** pattern:

- **Model**
  - `User.java`
  - `BmiRecord.java`
- **View**
  - XML Layouts
  - `PieChartView.java`
- **Controller**
  - `MainActivity.java`
  - Logic handlers

---

## 🚀 Getting Started

### 🔧 Clone the Repository
## 🔥 Firebase Setup, Usage & Screenshots

### 🔧 Firebase Setup
- Create a Firebase project
- Add Android app with package name: `bd.edu.seu`
- Place `google-services.json` inside the `app/` folder
- Enable **Email/Password Authentication**
- Enable **Firebase Realtime Database**

---

### 📖 How to Use the App
- Select unit system (Metric / Imperial)
- Enter **Age, Weight, and Height**
- App validates inputs automatically
- Tap **Calculate**
- View **BMI, BMR & Ideal Weight Goal**
- Open **Diet Plan** to see nutrition breakdown

---

### 📸 Screenshots
**Login Screen** 

<img width="373" height="811" alt="Screenshot 2025-12-22 205319" src="https://github.com/user-attachments/assets/0be3a4e4-f25e-45a1-bcea-0585779bf49a" />

**Dashboard**

<img width="361" height="806" alt="Screenshot 2025-12-22 205154" src="https://github.com/user-attachments/assets/0ce62d6a-cf39-4520-aeeb-74e92098a2f8" />

**Diet Chart**

<img width="365" height="811" alt="Screenshot 2025-12-22 205214" src="https://github.com/user-attachments/assets/7ee2fca6-6119-417c-8fb4-50290175dd55" />

**History**

<img width="357" height="807" alt="Screenshot 2025-12-22 205235" src="https://github.com/user-attachments/assets/1cd65723-37df-432b-9159-64c8e983c235" />

**Health Tips**

<img width="350" height="773" alt="Screenshot 2025-12-22 205302" src="https://github.com/user-attachments/assets/4bd1d6ca-40ae-40af-8109-7add8b97aef9" />


---

## 👨‍💻 Author

**Abid Hasan**  
🎓 CSE Student, Southeast University  
💡 Interests: Android Development, Java & Full-Stack Engineering
```bash
git clone https://github.com/your-username/BMI_and_BMR_calculator_Project.git
