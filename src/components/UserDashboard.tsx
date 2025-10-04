import React from 'react';
import { Calendar, Users, Trophy, Clock, ArrowRight, Star, TrendingUp } from 'lucide-react';
import { Button } from './ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Badge } from './ui/badge';
import { Progress } from './ui/progress';

interface UserDashboardProps {
  user: any;
  onNavigate: (page: string) => void;
}

export default function UserDashboard({ user, onNavigate }: UserDashboardProps) {
  const upcomingEvents = [
    {
      id: 1,
      title: "Tech Talk: AI in Education",
      date: "Dec 15, 2024",
      time: "2:00 PM",
      club: "Computer Science Club",
      registered: true
    },
    {
      id: 2,
      title: "Annual Cultural Fest",
      date: "Dec 20, 2024",
      time: "10:00 AM",
      club: "Cultural Committee",
      registered: false
    },
    {
      id: 3,
      title: "Coding Competition",
      date: "Dec 22, 2024",
      time: "9:00 AM",
      club: "Programming Club",
      registered: true
    }
  ];

  const myClubs = [
    {
      id: 1,
      name: "Computer Science Club",
      position: "Member",
      joinDate: "Sept 2024",
      status: "Active"
    },
    {
      id: 2,
      name: "Photography Club",
      position: "Vice President",
      joinDate: "Aug 2024",
      status: "Active"
    },
    {
      id: 3,
      name: "Environmental Club",
      position: "Member",
      joinDate: "Oct 2024",
      status: "Active"
    }
  ];

  const achievements = [
    {
      title: "Event Enthusiast",
      description: "Attended 10+ events",
      icon: "🏆",
      earned: true
    },
    {
      title: "Club Leader",
      description: "Hold leadership position",
      icon: "👑",
      earned: true
    },
    {
      title: "AICTE Champion",
      description: "Complete 50 AICTE hours",
      icon: "⭐",
      earned: false
    }
  ];

  return (
    <div className="max-w-7xl mx-auto p-6 space-y-8">
      {/* Welcome Header */}
      <div className="bg-gradient-to-r from-blue-600 to-green-600 rounded-2xl text-white p-8">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl mb-2">Hi, {user?.name}! 👋</h1>
            <p className="text-blue-100 text-lg">Welcome back to your Club Spot dashboard</p>
          </div>
          <div className="text-right">
            <div className="text-sm text-blue-100">Student ID</div>
            <div className="text-xl">{user?.studentId}</div>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card className="border-l-4 border-l-blue-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">AICTE Hours</CardTitle>
            <Clock className="h-4 w-4 text-blue-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900 mb-1">{user?.aicteHours || 45}</div>
            <div className="text-xs text-gray-500 mb-2">of 75 required</div>
            <Progress value={(user?.aicteHours || 45) / 75 * 100} className="h-2" />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-green-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">Events Attended</CardTitle>
            <Calendar className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900 mb-1">{user?.eventsAttended || 12}</div>
            <div className="text-xs text-green-600 flex items-center">
              <TrendingUp className="h-3 w-3 mr-1" />
              +3 this month
            </div>
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-yellow-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">Events Registered</CardTitle>
            <Trophy className="h-4 w-4 text-yellow-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900 mb-1">{user?.eventsRegistered || 5}</div>
            <div className="text-xs text-gray-500">Upcoming events</div>
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-purple-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">Clubs Joined</CardTitle>
            <Users className="h-4 w-4 text-purple-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900 mb-1">{user?.clubsJoined || 3}</div>
            <div className="text-xs text-gray-500">Active memberships</div>
          </CardContent>
        </Card>
      </div>

      <div className="grid lg:grid-cols-3 gap-8">
        {/* Upcoming Events */}
        <div className="lg:col-span-2">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <div>
                <CardTitle className="flex items-center gap-2">
                  <Calendar className="h-5 w-5 text-blue-600" />
                  Upcoming Events
                </CardTitle>
                <CardDescription>Events you're registered for and recommendations</CardDescription>
              </div>
              <Button
                variant="outline"
                size="sm"
                onClick={() => onNavigate('events')}
                className="flex items-center gap-2"
              >
                View All
                <ArrowRight size={14} />
              </Button>
            </CardHeader>
            <CardContent className="space-y-4">
              {upcomingEvents.map((event) => (
                <div key={event.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                  <div className="flex-1">
                    <h4 className="text-gray-900 mb-1">{event.title}</h4>
                    <p className="text-sm text-gray-600 mb-2">{event.club}</p>
                    <div className="flex items-center gap-4 text-xs text-gray-500">
                      <span>{event.date}</span>
                      <span>{event.time}</span>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    {event.registered ? (
                      <Badge variant="secondary" className="bg-green-100 text-green-700">
                        Registered
                      </Badge>
                    ) : (
                      <Button size="sm" variant="outline">
                        Register
                      </Button>
                    )}
                  </div>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>

        {/* Quick Links & Achievements */}
        <div className="space-y-6">
          {/* Quick Links */}
          <Card>
            <CardHeader>
              <CardTitle>Quick Links</CardTitle>
              <CardDescription>Navigate to frequently used sections</CardDescription>
            </CardHeader>
            <CardContent className="space-y-3">
              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() => onNavigate('events')}
              >
                <Calendar className="mr-2 h-4 w-4" />
                Event Calendar
              </Button>
              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() => onNavigate('clubs')}
              >
                <Users className="mr-2 h-4 w-4" />
                Browse Clubs
              </Button>
              <Button
                variant="outline"
                className="w-full justify-start"
              >
                <Trophy className="mr-2 h-4 w-4" />
                My Certificates
              </Button>
            </CardContent>
          </Card>

          {/* Achievements */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Star className="h-5 w-5 text-yellow-500" />
                Achievements
              </CardTitle>
              <CardDescription>Your badges and milestones</CardDescription>
            </CardHeader>
            <CardContent className="space-y-3">
              {achievements.map((achievement, index) => (
                <div
                  key={index}
                  className={`flex items-center gap-3 p-3 rounded-lg ${
                    achievement.earned ? 'bg-yellow-50 border border-yellow-200' : 'bg-gray-50'
                  }`}
                >
                  <div className={`text-2xl ${achievement.earned ? 'grayscale-0' : 'grayscale'}`}>
                    {achievement.icon}
                  </div>
                  <div>
                    <div className={`text-sm ${achievement.earned ? 'text-gray-900' : 'text-gray-500'}`}>
                      {achievement.title}
                    </div>
                    <div className="text-xs text-gray-500">{achievement.description}</div>
                  </div>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>
      </div>

      {/* My Clubs */}
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <div>
            <CardTitle className="flex items-center gap-2">
              <Users className="h-5 w-5 text-purple-600" />
              My Clubs
            </CardTitle>
            <CardDescription>Clubs you're currently a member of</CardDescription>
          </div>
          <Button
            variant="outline"
            size="sm"
            onClick={() => onNavigate('clubs')}
            className="flex items-center gap-2"
          >
            Discover More
            <ArrowRight size={14} />
          </Button>
        </CardHeader>
        <CardContent>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
            {myClubs.map((club) => (
              <div key={club.id} className="p-4 bg-white border rounded-lg shadow-sm">
                <div className="flex items-start justify-between mb-2">
                  <h4 className="text-gray-900">{club.name}</h4>
                  <Badge variant="secondary" className="text-xs">
                    {club.status}
                  </Badge>
                </div>
                <p className="text-sm text-blue-600 mb-1">{club.position}</p>
                <p className="text-xs text-gray-500">Joined {club.joinDate}</p>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}