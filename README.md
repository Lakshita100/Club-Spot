<<<<<<< HEAD
# 🏫 ClubSpot – College Club Management System  

### 🚀 Full-Stack Web Application built with **React.js**, **Spring Boot 4.6.10 (Java 21)**, and **MySQL**

---

## 📖 Overview
**ClubSpot** is a web application designed for colleges to manage student clubs, events, and memberships.  
It provides two interfaces:
- 👩‍🎓 **Students:** register, browse clubs, and join.
- 🧑‍💼 **Admins:** create clubs, manage members, and view activities.

---

## 🧩 Tech Stack

| Layer | Technology |
|-------|-------------|
| **Frontend** | React.js (Axios, React Router, Tailwind CSS / Bootstrap) |
| **Backend** | Java 21 — Spring Boot 4.6.10 (Maven) |
| **Database** | MySQL 8 (Workbench) |
| **API Format** | REST (JSON) |
| **Build Tools** | npm & Maven |
| **Version Control** | Git + GitHub |

---

## 📁 Project Structure

ClubSpot/
│
├── club-spot-frontend/ # React frontend
│ ├── src/
│ ├── package.json
│ └── ...
│
├── club-spot-backend/ # Spring Boot backend (Maven)
│ ├── src/main/java/com/clubspot/
│ │ ├── controller/
│ │ ├── entity/
│ │ ├── repository/
│ │ ├── security/
│ │ ├── config/
│ │ ├── service/
│ │ └── ClubSpotBackendApplication.java
│ ├── src/main/resources/application.properties
│ ├── pom.xml
│ └── ...
│
├── club-spot-database/ # SQL schema and seed data
│ └── schema.sql
│
└── README.md


---

## ⚙️ Setup Instructions

### 🧰 Prerequisites
Ensure you have the following installed:
- **Java 21 (JDK 21 LTS)**
- **Maven 3.9+**
- **Node.js 18+ / npm**
- **MySQL 8+** (Workbench)
- **Git**

---

## 🗄️ Step 1 – Database Setup

Run the following SQL script in **MySQL Workbench**:

```sql
CREATE DATABASE clubspot;

USE clubspot;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE clubs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT
);

CREATE TABLE memberships (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    club_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (club_id) REFERENCES clubs(id)
);

⚙️ Step 2 – Backend Setup (Spring Boot 4.6.10 + Java 21)

Navigate to the backend folder:
> cd club-spot-backend

Update your database credentials in
src/main/resources/application.properties:

spring.application.name=clubspot
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/clubspot
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect


## Run the backend:
> mvn spring-boot:run

The backend will start at
👉 http://localhost:8080


💻 Step 3 – Frontend Setup (React JS)

Open a new terminal:
> cd club-spot-frontend


## Install dependencies:
> npm install

## Start the frontend:
> npm start

React will start on
👉 http://localhost:3000


🔗 Step 4 – Connecting React and Spring Boot

Use Axios in React to call backend APIs:

import axios from "axios";

axios.post("http://localhost:8080/api/users/login", {
  email,
  password,
})
.then(res => console.log(res.data))
.catch(err => console.error(err));

🧠 Example REST API Endpoints
Method	Endpoint	Description
POST	/api/users/register	Register a new user
POST	/api/users/login	Login existing user
GET	/api/clubs	Get all clubs
POST	/api/clubs/join	Join a club


🧱 Step 5 – Commit and Push Entire Project to GitHub

From the root folder (ClubSpot/):

git init
git add .
git commit -m "Initial commit – React + Spring Boot 4.6.10 (Java 21) + MySQL"
git branch -M main
git remote add origin https://github.com/<your-username>/ClubSpot.git
git push -u origin main

🚀 Future Enhancements

🔐 JWT Authentication (Spring Security)
🧑‍💼 Admin Dashboard with Role Management
🗓️ Event Scheduling & Notifications
☁️ Dockerized Deployment (AWS / Render / Railway)
🧑‍💻 Author

Lakshita Jain
💼 Full-Stack Developer (React + Spring Boot 4.6.10 + Java 21 + MySQL)
🌐 GitHub @Lakshita100

⚙️ Recommended JDK

Verify Java 21 is active:
> java -version

Expected output:
> openjdk version "21"
=======
# Club Spot - College Club Management Website

A modern, responsive web application for managing college clubs, events, and student activities. Built with React, TypeScript, and Tailwind CSS v4.

## Features

- **Landing Page**: Welcome section with mission/vision and call-to-action buttons
- **Authentication**: Dual login system for students and administrators
- **User Dashboard**: Personalized dashboard with AICTE hours tracking and event statistics
- **Clubs Management**: Browse, join, and manage club memberships
- **Events System**: Discover events, register, and track attendance
- **Admin Panel**: Comprehensive management tools for administrators
- **Responsive Design**: Mobile-friendly interface with modern UI components

## Tech Stack

- **Frontend**: React 18 with TypeScript
- **Styling**: Tailwind CSS v4 (Alpha)
- **UI Components**: Radix UI primitives with shadcn/ui
- **Icons**: Lucide React
- **Build Tool**: Create React App

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

1. Clone the repository:
```bash
git clone <your-repo-url>
cd club-spot
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

4. Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

## Project Structure

```
club-spot/
├── public/
│   ├── index.html
│   └── manifest.json
├── src/
│   ├── index.tsx
│   └── index.css
├── components/
│   ├── ui/              # shadcn/ui components
│   ├── figma/           # Custom utility components
│   ├── LandingPage.tsx
│   ├── AuthPage.tsx
│   ├── UserDashboard.tsx
│   ├── ClubsPage.tsx
│   ├── EventsPage.tsx
│   └── AdminDashboard.tsx
├── styles/
│   └── globals.css      # Global styles and Tailwind configuration
├── App.tsx              # Main application component
└── package.json
```

## Available Scripts

- `npm start` - Runs the app in development mode
- `npm test` - Launches the test runner
- `npm run build` - Builds the app for production
- `npm run eject` - Ejects from Create React App (one-way operation)

## Usage

### For Students:
1. Sign up or login with student credentials
2. Browse available clubs and join interesting ones
3. Discover and register for events
4. Track AICTE hours and certificates
5. View personalized dashboard with activity stats

### For Administrators:
1. Login with admin credentials
2. Manage club memberships and events
3. Create new events and track participation
4. View analytics and generate reports
5. Oversee multiple clubs from a unified dashboard

## Design System

The application uses a consistent design system with:
- **Colors**: Blue (#2563eb) primary, Green (#22c55e) secondary
- **Typography**: Inter/Poppins font family
- **Components**: Rounded buttons, soft shadows, clean cards
- **Icons**: Lucide React icon library

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


>>>>>>> 8295d3d368658a335772ecd9cb64c8272bd50941
