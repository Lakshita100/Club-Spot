import React, { useState } from 'react';
import { Users, Calendar, Plus, Settings, BarChart3, UserPlus, Edit, Trash2, Eye } from 'lucide-react';
import { Button } from './ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Badge } from './ui/badge';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Textarea } from './ui/textarea';
import { Tabs, TabsContent, TabsList, TabsTrigger } from './ui/tabs';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from './ui/table';

interface AdminDashboardProps {
  user: any;
}

export default function AdminDashboard({ user }: AdminDashboardProps) {
  const [activeTab, setActiveTab] = useState('overview');
  const [showCreateEventModal, setShowCreateEventModal] = useState(false);
  const [newEvent, setNewEvent] = useState({
    title: '',
    description: '',
    date: '',
    time: '',
    venue: '',
    maxParticipants: '',
    aicteHours: '',
    category: 'technical'
  });

  const clubStats = {
    totalMembers: 150,
    activeClubs: 3,
    eventsThisMonth: 8,
    totalEvents: 25,
    aicteHoursAwarded: 420
  };

  const managedClubs = [
    {
      id: 1,
      name: "Computer Science Club",
      members: 85,
      status: "Active",
      lastEvent: "Dec 10, 2024",
      upcomingEvents: 3
    },
    {
      id: 2,
      name: "Robotics Club",
      members: 45,
      status: "Active",
      lastEvent: "Nov 28, 2024",
      upcomingEvents: 2
    },
    {
      id: 3,
      name: "AI Research Group",
      members: 20,
      status: "Active",
      lastEvent: "Dec 5, 2024",
      upcomingEvents: 1
    }
  ];

  const recentEvents = [
    {
      id: 1,
      title: "Tech Talk: AI in Education",
      date: "Dec 15, 2024",
      participants: 85,
      maxParticipants: 100,
      status: "Upcoming",
      club: "Computer Science Club"
    },
    {
      id: 2,
      title: "Robotics Workshop",
      date: "Dec 12, 2024",
      participants: 45,
      maxParticipants: 50,
      status: "Upcoming",
      club: "Robotics Club"
    },
    {
      id: 3,
      title: "Machine Learning Seminar",
      date: "Dec 8, 2024",
      participants: 60,
      maxParticipants: 60,
      status: "Completed",
      club: "AI Research Group"
    }
  ];

  const clubMembers = [
    {
      id: 1,
      name: "John Doe",
      email: "john.doe@student.edu",
      studentId: "ST2023001",
      joinDate: "Sept 2024",
      role: "Member",
      eventsAttended: 8,
      club: "Computer Science Club"
    },
    {
      id: 2,
      name: "Jane Smith",
      email: "jane.smith@student.edu",
      studentId: "ST2023002",
      joinDate: "Aug 2024",
      role: "Secretary",
      eventsAttended: 12,
      club: "Computer Science Club"
    },
    {
      id: 3,
      name: "Mike Johnson",
      email: "mike.j@student.edu",
      studentId: "ST2023003",
      joinDate: "Oct 2024",
      role: "Member",
      eventsAttended: 5,
      club: "Robotics Club"
    }
  ];

  const handleCreateEvent = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Creating event:', newEvent);
    setShowCreateEventModal(false);
    setNewEvent({
      title: '',
      description: '',
      date: '',
      time: '',
      venue: '',
      maxParticipants: '',
      aicteHours: '',
      category: 'technical'
    });
  };

  const CreateEventModal = () => (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-2xl">Create New Event</h2>
            <Button variant="outline" size="sm" onClick={() => setShowCreateEventModal(false)}>
              ✕
            </Button>
          </div>

          <form onSubmit={handleCreateEvent} className="space-y-4">
            <div className="grid md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="title">Event Title</Label>
                <Input
                  id="title"
                  value={newEvent.title}
                  onChange={(e) => setNewEvent({...newEvent, title: e.target.value})}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="category">Category</Label>
                <select
                  id="category"
                  value={newEvent.category}
                  onChange={(e) => setNewEvent({...newEvent, category: e.target.value})}
                  className="w-full p-2 border rounded-md"
                >
                  <option value="technical">Technical</option>
                  <option value="cultural">Cultural</option>
                  <option value="sports">Sports</option>
                  <option value="social">Social Service</option>
                </select>
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="description">Description</Label>
              <Textarea
                id="description"
                value={newEvent.description}
                onChange={(e) => setNewEvent({...newEvent, description: e.target.value})}
                rows={3}
                required
              />
            </div>

            <div className="grid md:grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label htmlFor="date">Date</Label>
                <Input
                  id="date"
                  type="date"
                  value={newEvent.date}
                  onChange={(e) => setNewEvent({...newEvent, date: e.target.value})}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="time">Time</Label>
                <Input
                  id="time"
                  type="time"
                  value={newEvent.time}
                  onChange={(e) => setNewEvent({...newEvent, time: e.target.value})}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="venue">Venue</Label>
                <Input
                  id="venue"
                  value={newEvent.venue}
                  onChange={(e) => setNewEvent({...newEvent, venue: e.target.value})}
                  required
                />
              </div>
            </div>

            <div className="grid md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="maxParticipants">Max Participants</Label>
                <Input
                  id="maxParticipants"
                  type="number"
                  value={newEvent.maxParticipants}
                  onChange={(e) => setNewEvent({...newEvent, maxParticipants: e.target.value})}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="aicteHours">AICTE Hours</Label>
                <Input
                  id="aicteHours"
                  type="number"
                  value={newEvent.aicteHours}
                  onChange={(e) => setNewEvent({...newEvent, aicteHours: e.target.value})}
                  required
                />
              </div>
            </div>

            <div className="flex gap-2 pt-4">
              <Button type="submit" className="bg-blue-600 hover:bg-blue-700">
                Create Event
              </Button>
              <Button type="button" variant="outline" onClick={() => setShowCreateEventModal(false)}>
                Cancel
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );

  return (
    <div className="max-w-7xl mx-auto p-6 space-y-8">
      {/* Welcome Header */}
      <div className="bg-gradient-to-r from-purple-600 to-blue-600 rounded-2xl text-white p-8">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl mb-2">Admin Dashboard</h1>
            <p className="text-purple-100 text-lg">Welcome, {user?.name}! Manage your clubs and events</p>
          </div>
          <div className="text-right">
            <div className="text-sm text-purple-100">Role</div>
            <div className="text-xl">Administrator</div>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-6">
        <Card className="border-l-4 border-l-blue-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">Total Members</CardTitle>
            <Users className="h-4 w-4 text-blue-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900">{clubStats.totalMembers}</div>
            <p className="text-xs text-gray-500">Across all clubs</p>
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-green-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">Active Clubs</CardTitle>
            <Settings className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900">{clubStats.activeClubs}</div>
            <p className="text-xs text-gray-500">Under management</p>
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-yellow-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">Events This Month</CardTitle>
            <Calendar className="h-4 w-4 text-yellow-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900">{clubStats.eventsThisMonth}</div>
            <p className="text-xs text-gray-500">December 2024</p>
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-purple-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">Total Events</CardTitle>
            <BarChart3 className="h-4 w-4 text-purple-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900">{clubStats.totalEvents}</div>
            <p className="text-xs text-gray-500">All time</p>
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-red-500">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm text-gray-600">AICTE Hours</CardTitle>
            <BarChart3 className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl text-gray-900">{clubStats.aicteHoursAwarded}</div>
            <p className="text-xs text-gray-500">Hours awarded</p>
          </CardContent>
        </Card>
      </div>

      {/* Management Tabs */}
      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList className="grid w-full grid-cols-4">
          <TabsTrigger value="overview">Overview</TabsTrigger>
          <TabsTrigger value="events">Events</TabsTrigger>
          <TabsTrigger value="members">Members</TabsTrigger>
          <TabsTrigger value="analytics">Analytics</TabsTrigger>
        </TabsList>

        <TabsContent value="overview" className="space-y-6">
          <div className="grid lg:grid-cols-2 gap-6">
            {/* Managed Clubs */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center justify-between">
                  <span>Managed Clubs</span>
                  <Button size="sm" variant="outline">
                    <Plus className="w-4 h-4 mr-1" />
                    Add Club
                  </Button>
                </CardTitle>
                <CardDescription>Clubs under your administration</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                {managedClubs.map((club) => (
                  <div key={club.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div>
                      <h4 className="text-gray-900">{club.name}</h4>
                      <p className="text-sm text-gray-600">{club.members} members • {club.upcomingEvents} upcoming events</p>
                    </div>
                    <Badge variant="secondary">{club.status}</Badge>
                  </div>
                ))}
              </CardContent>
            </Card>

            {/* Recent Activity */}
            <Card>
              <CardHeader>
                <CardTitle>Recent Events</CardTitle>
                <CardDescription>Latest event activities</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                {recentEvents.slice(0, 3).map((event) => (
                  <div key={event.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div>
                      <h4 className="text-gray-900">{event.title}</h4>
                      <p className="text-sm text-gray-600">{event.date} • {event.participants}/{event.maxParticipants} participants</p>
                    </div>
                    <Badge variant={event.status === 'Completed' ? 'default' : 'secondary'}>
                      {event.status}
                    </Badge>
                  </div>
                ))}
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="events" className="space-y-6">
          <div className="flex items-center justify-between">
            <h2 className="text-2xl">Event Management</h2>
            <Button
              onClick={() => setShowCreateEventModal(true)}
              className="bg-blue-600 hover:bg-blue-700"
            >
              <Plus className="w-4 h-4 mr-2" />
              Create Event
            </Button>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>All Events</CardTitle>
              <CardDescription>Manage your club events</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Event</TableHead>
                    <TableHead>Date</TableHead>
                    <TableHead>Participants</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {recentEvents.map((event) => (
                    <TableRow key={event.id}>
                      <TableCell>
                        <div>
                          <div className="text-gray-900">{event.title}</div>
                          <div className="text-sm text-gray-500">{event.club}</div>
                        </div>
                      </TableCell>
                      <TableCell>{event.date}</TableCell>
                      <TableCell>{event.participants}/{event.maxParticipants}</TableCell>
                      <TableCell>
                        <Badge variant={event.status === 'Completed' ? 'default' : 'secondary'}>
                          {event.status}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex gap-1">
                          <Button size="sm" variant="outline">
                            <Eye className="w-3 h-3" />
                          </Button>
                          <Button size="sm" variant="outline">
                            <Edit className="w-3 h-3" />
                          </Button>
                          <Button size="sm" variant="outline">
                            <Trash2 className="w-3 h-3" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="members" className="space-y-6">
          <div className="flex items-center justify-between">
            <h2 className="text-2xl">Member Management</h2>
            <Button variant="outline">
              <UserPlus className="w-4 h-4 mr-2" />
              Add Member
            </Button>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>Club Members</CardTitle>
              <CardDescription>Manage club memberships</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Name</TableHead>
                    <TableHead>Student ID</TableHead>
                    <TableHead>Club</TableHead>
                    <TableHead>Role</TableHead>
                    <TableHead>Events</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {clubMembers.map((member) => (
                    <TableRow key={member.id}>
                      <TableCell>
                        <div>
                          <div className="text-gray-900">{member.name}</div>
                          <div className="text-sm text-gray-500">{member.email}</div>
                        </div>
                      </TableCell>
                      <TableCell>{member.studentId}</TableCell>
                      <TableCell>{member.club}</TableCell>
                      <TableCell>
                        <Badge variant={member.role === 'Member' ? 'secondary' : 'default'}>
                          {member.role}
                        </Badge>
                      </TableCell>
                      <TableCell>{member.eventsAttended}</TableCell>
                      <TableCell>
                        <div className="flex gap-1">
                          <Button size="sm" variant="outline">
                            <Edit className="w-3 h-3" />
                          </Button>
                          <Button size="sm" variant="outline">
                            <Trash2 className="w-3 h-3" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="analytics" className="space-y-6">
          <h2 className="text-2xl">Analytics & Reports</h2>
          
          <div className="grid md:grid-cols-2 gap-6">
            <Card>
              <CardHeader>
                <CardTitle>Membership Growth</CardTitle>
                <CardDescription>Member registration trends</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="h-48 flex items-center justify-center bg-gray-50 rounded">
                  <p className="text-gray-500">Chart coming soon...</p>
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Event Participation</CardTitle>
                <CardDescription>Attendance patterns</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="h-48 flex items-center justify-center bg-gray-50 rounded">
                  <p className="text-gray-500">Chart coming soon...</p>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>
      </Tabs>

      {/* Create Event Modal */}
      {showCreateEventModal && <CreateEventModal />}
    </div>
  );
}