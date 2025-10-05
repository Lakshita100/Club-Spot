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


