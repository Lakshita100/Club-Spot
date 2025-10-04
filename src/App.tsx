import React, { useState } from 'react';
import { Users, Calendar, Trophy, Settings, UserPlus, LogIn, Home, Menu, X } from 'lucide-react';
import { Button } from './components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './components/ui/card';
import { Badge } from './components/ui/badge';
import LandingPage from './components/LandingPage';
import AuthPage from './components/AuthPage';
import UserDashboard from './components/UserDashboard';
import ClubsPage from './components/ClubsPage';
import EventsPage from './components/EventsPage';
import AdminDashboard from './components/AdminDashboard';

type Page = 'landing' | 'auth' | 'user-dashboard' | 'clubs' | 'events' | 'admin-dashboard';
type AuthMode = 'login' | 'signup';
type UserType = 'user' | 'admin';

export default function App() {
  const [currentPage, setCurrentPage] = useState<Page>('landing');
  const [authMode, setAuthMode] = useState<AuthMode>('login');
  const [userType, setUserType] = useState<UserType>('user');
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleAuth = (userData: any) => {
    setIsAuthenticated(true);
    setCurrentUser(userData);
    if (userData.role === 'admin') {
      setCurrentPage('admin-dashboard');
    } else {
      setCurrentPage('user-dashboard');
    }
  };

  const handleLogout = () => {
    setIsAuthenticated(false);
    setCurrentUser(null);
    setCurrentPage('landing');
  };

  const Navigation = () => (
    <nav className="bg-white shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <div 
              className="flex-shrink-0 flex items-center cursor-pointer"
              onClick={() => setCurrentPage('landing')}
            >
              <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center mr-3">
                <span className="text-white font-bold">CS</span>
              </div>
              <span className="text-xl text-gray-900">Club Spot</span>
            </div>
            
            {isAuthenticated && (
              <div className="hidden md:ml-8 md:flex md:space-x-4">
                <Button
                  variant={currentPage === 'user-dashboard' || currentPage === 'admin-dashboard' ? 'default' : 'ghost'}
                  onClick={() => setCurrentPage(currentUser?.role === 'admin' ? 'admin-dashboard' : 'user-dashboard')}
                  className="flex items-center gap-2"
                >
                  <Home size={16} />
                  Dashboard
                </Button>
                <Button
                  variant={currentPage === 'clubs' ? 'default' : 'ghost'}
                  onClick={() => setCurrentPage('clubs')}
                  className="flex items-center gap-2"
                >
                  <Users size={16} />
                  Clubs
                </Button>
                <Button
                  variant={currentPage === 'events' ? 'default' : 'ghost'}
                  onClick={() => setCurrentPage('events')}
                  className="flex items-center gap-2"
                >
                  <Calendar size={16} />
                  Events
                </Button>
              </div>
            )}
          </div>

          <div className="flex items-center">
            {isAuthenticated ? (
              <div className="flex items-center space-x-4">
                <div className="hidden md:flex items-center space-x-2">
                  <span className="text-sm text-gray-700">Hi, {currentUser?.name}!</span>
                  <Badge variant={currentUser?.role === 'admin' ? 'destructive' : 'secondary'}>
                    {currentUser?.role || 'Student'}
                  </Badge>
                </div>
                <Button onClick={handleLogout} variant="outline" size="sm">
                  Logout
                </Button>
              </div>
            ) : (
              <div className="hidden md:flex space-x-2">
                <Button
                  onClick={() => {
                    setAuthMode('login');
                    setUserType('user');
                    setCurrentPage('auth');
                  }}
                  variant="ghost"
                  size="sm"
                  className="flex items-center gap-2"
                >
                  <LogIn size={16} />
                  Login
                </Button>
                <Button
                  onClick={() => {
                    setAuthMode('signup');
                    setUserType('user');
                    setCurrentPage('auth');
                  }}
                  size="sm"
                  className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700"
                >
                  <UserPlus size={16} />
                  Sign Up
                </Button>
              </div>
            )}

            {/* Mobile menu button */}
            <div className="md:hidden ml-2">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              >
                {mobileMenuOpen ? <X size={20} /> : <Menu size={20} />}
              </Button>
            </div>
          </div>
        </div>

        {/* Mobile menu */}
        {mobileMenuOpen && (
          <div className="md:hidden border-t border-gray-200 py-2">
            {isAuthenticated ? (
              <div className="space-y-2">
                <div className="px-4 py-2 text-sm text-gray-700 border-b">
                  Hi, {currentUser?.name}! ({currentUser?.role || 'Student'})
                </div>
                <Button
                  variant="ghost"
                  onClick={() => {
                    setCurrentPage(currentUser?.role === 'admin' ? 'admin-dashboard' : 'user-dashboard');
                    setMobileMenuOpen(false);
                  }}
                  className="w-full justify-start"
                >
                  <Home size={16} className="mr-2" />
                  Dashboard
                </Button>
                <Button
                  variant="ghost"
                  onClick={() => {
                    setCurrentPage('clubs');
                    setMobileMenuOpen(false);
                  }}
                  className="w-full justify-start"
                >
                  <Users size={16} className="mr-2" />
                  Clubs
                </Button>
                <Button
                  variant="ghost"
                  onClick={() => {
                    setCurrentPage('events');
                    setMobileMenuOpen(false);
                  }}
                  className="w-full justify-start"
                >
                  <Calendar size={16} className="mr-2" />
                  Events
                </Button>
                <Button
                  onClick={() => {
                    handleLogout();
                    setMobileMenuOpen(false);
                  }}
                  variant="outline"
                  className="w-full"
                >
                  Logout
                </Button>
              </div>
            ) : (
              <div className="space-y-2">
                <Button
                  onClick={() => {
                    setAuthMode('login');
                    setUserType('user');
                    setCurrentPage('auth');
                    setMobileMenuOpen(false);
                  }}
                  variant="ghost"
                  className="w-full justify-start"
                >
                  <LogIn size={16} className="mr-2" />
                  Login
                </Button>
                <Button
                  onClick={() => {
                    setAuthMode('signup');
                    setUserType('user');
                    setCurrentPage('auth');
                    setMobileMenuOpen(false);
                  }}
                  className="w-full bg-blue-600 hover:bg-blue-700"
                >
                  <UserPlus size={16} className="mr-2" />
                  Sign Up
                </Button>
              </div>
            )}
          </div>
        )}
      </div>
    </nav>
  );

  const renderPage = () => {
    switch (currentPage) {
      case 'landing':
        return (
          <LandingPage
            onLogin={(userType: UserType) => {
              setAuthMode('login');
              setUserType(userType);
              setCurrentPage('auth');
            }}
            onSignup={() => {
              setAuthMode('signup');
              setUserType('user');
              setCurrentPage('auth');
            }}
          />
        );
      case 'auth':
        return (
          <AuthPage
            mode={authMode}
            userType={userType}
            onAuth={handleAuth}
            onSwitchMode={(mode) => setAuthMode(mode)}
            onSwitchUserType={(type) => setUserType(type)}
            onBack={() => setCurrentPage('landing')}
          />
        );
      case 'user-dashboard':
        return <UserDashboard user={currentUser} onNavigate={(page) => setCurrentPage(page as Page)} />;
      case 'clubs':
        return <ClubsPage user={currentUser} />;
      case 'events':
        return <EventsPage user={currentUser} />;
      case 'admin-dashboard':
        return <AdminDashboard user={currentUser} />;
      default:
        return <LandingPage onLogin={() => {}} onSignup={() => {}} />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      <main className="flex-1">
        {renderPage()}
      </main>
    </div>
  );
}