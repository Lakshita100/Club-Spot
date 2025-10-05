# ClubSpot: Full-Stack College Club Management System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-green)](https://spring.io/projects/spring-boot) 
[![React](https://img.shields.io/badge/React-18-blue)](https://reactjs.org/) 
[![Java](https://img.shields.io/badge/Java-21-blue)](https://openjdk.org/) 
[![MySQL](https://img.shields.io/badge/MySQL-8%2B-orange)](https://www.mysql.com/) 

## Overview

ClubSpot is a **full-stack web application** for managing college clubs, events, and AICTE (All India Council for Technical Education) compliance. The **backend** (Spring Boot) provides a secure REST API for data handling, authentication, and business logic. The **frontend** (React) offers an intuitive UI for users (admins/members) to browse clubs, register for events, track attendances, and view AICTE hours.

**Core Functionality**:
- Admins: Manage users, clubs, events.
- Members: View/join events, register attendance, track personal AICTE credits.
- AICTE Focus: Categorize events (e.g., TECHNICAL workshops), auto-accumulate hours on attendance.

This monorepo structure includes both backend and frontend for easy development. Deploy as separate services or bundled (e.g., via Docker).

**Demo**: [Live Preview](https://clubspot.example.com) (placeholder; host on Vercel/Netlify for frontend, Heroku for backend).

## Features

### Backend Features
- **Authentication**: JWT-based login/register (roles: ADMIN/MEMBER).
- **Club Management**: CRUD operations with images/certificates.
- **Event Management**: Schedule events with status (UPCOMING/ONGOING/COMPLETED/CANCELLED), AICTE categories/types, capacity limits.
- **Attendance Tracking**: Register/confirm attendance; auto-update user AICTE hours.
- **Queries**: Filter by club, date, status, search; stats (total hours, counts).
- **Security**: Role-based access, input validation, CORS.

### Frontend Features
- **Dashboard**: Personalized view (upcoming events, AICTE progress).
- **Club/Event Browsing**: Search, filter, details with maps/images.
- **User   Profile**: View/edit profile, attendance history, AICTE certificate download.
- **Responsive UI**: Mobile-friendly (Bootstrap/Tailwind); dark mode toggle.
- **Real-Time**: Event status updates (via polling or WebSockets if extended).
- **Forms**: Secure login/register, event creation/attendance with validation.
- **Charts**: Visualize AICTE hours/events (Chart.js).

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.4.0 (Web, Data JPA, Security, Validation).
- **Database**: MySQL 8+ (HikariCP pool; Hibernate ORM).
- **Security**: Spring Security + JWT.
- **Build**: Maven 3.9+.
- **Language**: Java 21.
- **Other**: Lombok, Jackson (JSON), SLF4J (logging).

### Frontend
- **Framework**: React 18 (with Hooks/Context for state).
- **UI Library**: Bootstrap 5 or Tailwind CSS (responsive components).
- **HTTP Client**: Axios (API calls with interceptors for JWT).
- **State Management**: React Context (or Redux for complex apps).
- **Routing**: React Router v6.
- **Charts**: Chart.js (AICTE stats).
- **Forms**: React Hook Form + Yup (validation).
- **Build**: Vite (fast dev server) or Create React App.
- **Testing**: Jest + React Testing Library.
- **Other**: Axios, React Icons, Date-fns (dates).

### Full-Stack Integration
- **API Communication**: Frontend calls backend at `/api/*` (CORS enabled).
- **Deployment**: Backend (Heroku/Railway), Frontend (Vercel/Netlify), DB (PlanetScale/MySQL Cloud).

## Prerequisites

### Backend
- **Java 21**: [OpenJDK](https://openjdk.org/) or [Oracle](https://www.oracle.com/java/technologies/downloads/).
- **Maven 3.9+**: [Download](https://maven.apache.org/install.html).
- **MySQL 8+**: [Download](https://dev.mysql.com/downloads/mysql/) (Server + Workbench).
  - Create DB/user: `CREATE DATABASE clubspot_db; CREATE USER 'clubspot'@'localhost' IDENTIFIED BY 'clubspotpass'; GRANT ALL ON clubspot_db.* TO 'clubspot'@'localhost';`

### Frontend
- **Node.js 18+**: [Download](https://nodejs.org/) (includes npm).
- **npm/yarn**: For package management.
- **IDE**: VS Code (with extensions: ES7 React/Redux, Prettier).

### General
- **Git**: For version control.
- **API Client**: Thunder Client (VS Code) or Postman for backend testing.
- **Browser**: Chrome/Firefox (DevTools for debugging).

## Quick Start

### 1. Clone & Setup
> git clone https://github.com/Lakshita100/Club-Spot.git
> cd club-spot


### 2. Backend Setup
1. **DB Init**:
   - Start MySQL; run `backend/src/main/resources/schema.sql` in Workbench.
2. **Config** (`backend/src/main/resources/application.properties`)
3. **Run**:
   cd backend 
   mvn clean install 
   mvn spring-boot:run # http://localhost:8080

- Test: POST `/api/auth/login` → `{"username":"admin1","password":"password"}`.

### 3. Frontend Setup
1. **Install Deps**:
   cd frontend 
   npm install # Or yarn install
2. **Env Config** (`.env` file):
   REACT_APP_API_URL=http://localhost:8080/api
3. **Run**:
   npm start # http://localhost:3000

- Auto-opens browser; proxies API calls to backend.

### 4. Full Test
- Frontend: Login (admin1/password) → Dashboard shows events.
- Backend Logs: Watch for API calls (e.g., GET /events).

## Detailed Setup

### Backend (See Previous README Sections)
- **Structure**: `backend/src/main/java/com/clubspot/` (entities, services, controllers).
- **Schema.sql**: Initializes tables (users, clubs, events, attendances, memberships) with sample data.
- **Security**: JWT in headers; protect routes in controllers.
- **CORS**: Add `@CrossOrigin(origins = "http://localhost:3000")` for frontend.

- **Key Files**:
- **App.js**: `<BrowserRouter><Routes><Route path="/" element={<Dashboard />} /></Routes></BrowserRouter>`.
- **services/authService.js**:
  ```js
  import axios from 'axios';
  const API_URL = process.env.REACT_APP_API_URL;
  export const login = async (credentials) => {
    const { data } = await axios.post(`${API_URL}/auth/login`, credentials);
    localStorage.setItem('token', data.token);
    return data;
  };
  ```
- **context/AuthContext.js**: Provides user/token; use in components (e.g., `useAuth()` hook).
- **pages/Events.js**: Fetches `/api/events/upcoming/1` → Displays cards with RSVP button.
- **Validation**: Use Yup schemas in forms (e.g., event creation).
- **State Management**: Context for auth/events; Redux if scaling (e.g., global cart for attendances).
- **Styling**: Bootstrap classes or Tailwind (e.g., `className="card event-card"`).
- **Error Handling**: Axios interceptors for 401 (redirect to login); toasts (react-toastify).
- **Responsive**: Use Bootstrap grid; media queries for mobile.

- **Build for Prod**:
  npm run build # Creates /build folder

- Serve via backend (add to static resources) or separately.

## API Documentation

(Backend-focused; Frontend consumes these.)

Base: `http://localhost:8080/api`

- **Auth**: POST `/auth/login`, `/auth/register`.
- **Events**: GET/POST `/events`, `/events/upcoming/{clubId}`, `/events/search?query=...`.
- **Clubs**: GET/POST `/clubs`.
- **Attendances**: POST `/attendances/{eventId}`, GET `/attendances/user/{userId}`.
- **Users**: GET/PUT `/users/{id}`.

**Frontend Integration Example** (in EventList.js):
```js
import { useEffect, useState } from 'react';
import { getUpcomingEvents } from '../services/eventService';

const EventList = () => {
const [events, setEvents] = useState([]);
useEffect(() => {
  getUpcomingEvents(1).then(setEvents);  // Calls /api/events/upcoming/1
}, []);
return (
  <div className="row">
    {events.map(event => <EventCard key={event.id} event={event} />)}
  </div>
);
};

Testing
Backend
Unit/Integration: mvn test (JUnit for services/repos; MockMvc for controllers).
API: Thunder Client collection (login → events → attendance).
Frontend
Unit: npm test (Jest: Test components, e.g., render(<LoginForm />);).
E2E: Cypress (install: npm i cypress → npx cypress open): Simulate login, event RSVP.
Manual: Run both servers; test flows (register → create event → attend).
Deployment
Backend
Heroku/Railway: Push code; set env vars (DB URL, JWT secret).
Docker:
dockerfile

Run
Copy code
# backend/Dockerfile
FROM openjdk:21-jdk-slim
COPY target/club-spot-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
Build: docker build -t clubspot-backend .
Run: docker run -p 8080:8080 --env-file .env clubspot-backend.
DB: Use RDS (AWS) or PlanetScale (MySQL-compatible).
Frontend
Vercel/Netlify: npm run build → Deploy /build (auto-detects React).
Docker:
dockerfile

Run
Copy code
# frontend/Dockerfile
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
Run: docker run -p 3000:80 clubspot-frontend.
Full-Stack
Monorepo Deploy: Use Docker Compose (backend + frontend + MySQL).
yaml

Run
Copy code
# docker-compose.yml
services:
  backend:
    build: ./backend
    ports: ["8080:8080"]
    depends_on: [db]
  frontend:
    build: ./frontend
    ports: ["3000:80"]
  db:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: clubspot_db
    ports: ["3306:3306"]
Run: docker-compose up.
CI/CD: GitHub Actions (build/test/deploy to Vercel/Heroku).
Troubleshooting
Backend
Zero-Dates/ FK Errors: Run updated schema.sql; drop/recreate DB.
Port Conflicts: Change server.port=8081.
JWT Issues: Check token expiry; verify SecurityConfig.
Frontend
CORS Errors: Ensure backend @CrossOrigin("http://localhost:3000").
API 404/500: Check REACT_APP_API_URL; backend running?
Build Fails: npm install again; clear cache (npm start -- --reset-cache).
State Not Updating: Use useEffect deps; console.log API responses.
Mobile Issues: Test in Chrome DevTools (device mode).
General
Logs: Backend (console); Frontend (browser console).
Clean: Backend (mvn clean); Frontend (rm -rf node_modules; npm install).
Versions Mismatch: Align Node/Java (e.g., no ES6+ in backend if needed).
If issues, share logs/screenshots (e.g., from browser network tab).

Contributing
Fork repo.
Branch: git checkout -b feature/event-search.
Commit: git commit -m "Add event search frontend".
Push/PR: Test both backend/frontend.
Suggestions: Add WebSockets for live updates, file uploads for event images, or mobile app (React Native).






