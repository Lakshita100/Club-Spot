import React, { useState } from 'react';
import { Calendar, Clock, MapPin, Users, Filter, Search, ChevronLeft, ChevronRight } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Badge } from './ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from './ui/tabs';

interface EventsPageProps {
  user: any;
}

export default function EventsPage({ user }: EventsPageProps) {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedEvent, setSelectedEvent] = useState<any>(null);
  const [viewMode, setViewMode] = useState<'upcoming' | 'calendar'>('upcoming');
  const [currentDate, setCurrentDate] = useState(new Date());

  const events = [
    {
      id: 1,
      title: "Tech Talk: AI in Education",
      description: "Explore how artificial intelligence is transforming the education sector. Learn about the latest tools, applications, and future possibilities.",
      date: "2024-12-15",
      time: "14:00",
      duration: "2 hours",
      venue: "Auditorium A",
      club: "Computer Science Club",
      organizer: "Dr. Sarah Johnson",
      participants: 85,
      maxParticipants: 100,
      registered: true,
      category: "technical",
      aicteHours: 2,
      tags: ["AI", "Technology", "Education"],
      status: "upcoming"
    },
    {
      id: 2,
      title: "Annual Cultural Fest",
      description: "Celebrate diversity and talent at our annual cultural festival. Featuring performances, competitions, and cultural exhibitions.",
      date: "2024-12-20",
      time: "10:00",
      duration: "6 hours",
      venue: "Main Campus Ground",
      club: "Cultural Committee",
      organizer: "Cultural Team",
      participants: 250,
      maxParticipants: 300,
      registered: false,
      category: "cultural",
      aicteHours: 4,
      tags: ["Culture", "Performance", "Competition"],
      status: "upcoming"
    },
    {
      id: 3,
      title: "Coding Competition 2024",
      description: "Test your programming skills in our annual coding competition. Prizes for top performers and recognition for creative solutions.",
      date: "2024-12-22",
      time: "09:00",
      duration: "4 hours",
      venue: "Computer Lab 1",
      club: "Programming Club",
      organizer: "Alice Wilson",
      participants: 45,
      maxParticipants: 60,
      registered: true,
      category: "technical",
      aicteHours: 3,
      tags: ["Programming", "Competition", "Coding"],
      status: "upcoming"
    },
    {
      id: 4,
      title: "Photography Workshop",
      description: "Learn professional photography techniques from industry experts. Hands-on session with camera equipment and editing software.",
      date: "2024-12-18",
      time: "15:30",
      duration: "3 hours",
      venue: "Art Studio",
      club: "Photography Club",
      organizer: "Mike Davis",
      participants: 20,
      maxParticipants: 25,
      registered: false,
      category: "cultural",
      aicteHours: 2,
      tags: ["Photography", "Art", "Workshop"],
      status: "upcoming"
    },
    {
      id: 5,
      title: "Environment Awareness Drive",
      description: "Join our campus-wide environmental awareness campaign. Tree plantation, waste management, and sustainability workshops.",
      date: "2024-12-25",
      time: "08:00",
      duration: "5 hours",
      venue: "Campus Wide",
      club: "Environmental Club",
      organizer: "Emma Green",
      participants: 120,
      maxParticipants: 150,
      registered: false,
      category: "social",
      aicteHours: 3,
      tags: ["Environment", "Sustainability", "Community"],
      status: "upcoming"
    },
    {
      id: 6,
      title: "Robotics Showcase",
      description: "Past event: Display of innovative robotics projects by students. Interactive demonstrations and technical presentations.",
      date: "2024-11-15",
      time: "14:00",
      duration: "3 hours",
      venue: "Engineering Lab",
      club: "Robotics Club",
      organizer: "John Tech",
      participants: 95,
      maxParticipants: 100,
      registered: true,
      category: "technical",
      aicteHours: 2,
      tags: ["Robotics", "Technology", "Showcase"],
      status: "past"
    }
  ];

  const upcomingEvents = events.filter(event => event.status === 'upcoming');
  const pastEvents = events.filter(event => event.status === 'past');

  const filteredEvents = (eventList: any[]) => {
    return eventList.filter(event =>
      event.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      event.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
      event.club.toLowerCase().includes(searchQuery.toLowerCase())
    );
  };

  const handleRegister = (eventId: number) => {
    console.log(`Registering for event ${eventId}`);
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const formatTime = (timeString: string) => {
    const [hours, minutes] = timeString.split(':');
    const time = new Date();
    time.setHours(parseInt(hours), parseInt(minutes));
    return time.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: '2-digit',
      hour12: true
    });
  };

  const getCategoryColor = (category: string) => {
    switch (category) {
      case 'technical': return 'bg-blue-100 text-blue-700';
      case 'cultural': return 'bg-purple-100 text-purple-700';
      case 'social': return 'bg-green-100 text-green-700';
      case 'sports': return 'bg-orange-100 text-orange-700';
      default: return 'bg-gray-100 text-gray-700';
    }
  };

  const EventCard = ({ event }: { event: any }) => (
    <Card className="hover:shadow-lg transition-shadow duration-300">
      <CardHeader>
        <div className="flex items-start justify-between mb-2">
          <div className="flex-1">
            <CardTitle className="text-lg mb-2">{event.title}</CardTitle>
            <div className="flex flex-wrap gap-2 mb-2">
              <Badge className={getCategoryColor(event.category)}>
                {event.category}
              </Badge>
              {event.registered && (
                <Badge variant="default">Registered</Badge>
              )}
              <Badge variant="outline">{event.aicteHours} AICTE Hours</Badge>
            </div>
          </div>
        </div>
        
        <div className="space-y-2 text-sm text-gray-600">
          <div className="flex items-center gap-2">
            <Calendar className="w-4 h-4" />
            <span>{formatDate(event.date)}</span>
          </div>
          <div className="flex items-center gap-2">
            <Clock className="w-4 h-4" />
            <span>{formatTime(event.time)} • {event.duration}</span>
          </div>
          <div className="flex items-center gap-2">
            <MapPin className="w-4 h-4" />
            <span>{event.venue}</span>
          </div>
          <div className="flex items-center gap-2">
            <Users className="w-4 h-4" />
            <span>{event.participants}/{event.maxParticipants} participants</span>
          </div>
        </div>
      </CardHeader>
      
      <CardContent>
        <CardDescription className="mb-4 leading-relaxed">
          {event.description}
        </CardDescription>
        
        <div className="flex flex-wrap gap-1 mb-4">
          {event.tags.map((tag: string, index: number) => (
            <Badge key={index} variant="outline" className="text-xs">
              {tag}
            </Badge>
          ))}
        </div>
        
        <div className="flex items-center justify-between">
          <div className="text-sm text-gray-600">
            by {event.club}
          </div>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setSelectedEvent(event)}
            >
              View Details
            </Button>
            {!event.registered && event.status === 'upcoming' && (
              <Button
                size="sm"
                className="bg-blue-600 hover:bg-blue-700"
                onClick={() => handleRegister(event.id)}
              >
                Register
              </Button>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  );

  const EventDetailModal = ({ event, onClose }: { event: any; onClose: () => void }) => (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="p-6">
          <div className="flex items-start justify-between mb-4">
            <h2 className="text-2xl mb-2">{event.title}</h2>
            <Button variant="outline" size="sm" onClick={onClose}>✕</Button>
          </div>

          <div className="flex flex-wrap gap-2 mb-4">
            <Badge className={getCategoryColor(event.category)}>
              {event.category}
            </Badge>
            {event.registered && (
              <Badge variant="default">Registered</Badge>
            )}
            <Badge variant="outline">{event.aicteHours} AICTE Hours</Badge>
          </div>

          <div className="grid md:grid-cols-2 gap-6 mb-6">
            <div className="space-y-3">
              <h3 className="text-lg">Event Details</h3>
              <div className="space-y-2 text-sm">
                <div className="flex items-center gap-2">
                  <Calendar className="w-4 h-4 text-gray-500" />
                  <span>{formatDate(event.date)}</span>
                </div>
                <div className="flex items-center gap-2">
                  <Clock className="w-4 h-4 text-gray-500" />
                  <span>{formatTime(event.time)} • {event.duration}</span>
                </div>
                <div className="flex items-center gap-2">
                  <MapPin className="w-4 h-4 text-gray-500" />
                  <span>{event.venue}</span>
                </div>
                <div className="flex items-center gap-2">
                  <Users className="w-4 h-4 text-gray-500" />
                  <span>{event.participants}/{event.maxParticipants} participants</span>
                </div>
              </div>
            </div>

            <div className="space-y-3">
              <h3 className="text-lg">Organizer Info</h3>
              <div className="text-sm space-y-1">
                <div><strong>Club:</strong> {event.club}</div>
                <div><strong>Organizer:</strong> {event.organizer}</div>
                <div><strong>AICTE Hours:</strong> {event.aicteHours}</div>
              </div>
            </div>
          </div>

          <div className="mb-6">
            <h3 className="text-lg mb-2">Description</h3>
            <p className="text-gray-700 leading-relaxed">{event.description}</p>
          </div>

          <div className="mb-6">
            <h3 className="text-lg mb-2">Tags</h3>
            <div className="flex flex-wrap gap-2">
              {event.tags.map((tag: string, index: number) => (
                <Badge key={index} variant="outline">
                  {tag}
                </Badge>
              ))}
            </div>
          </div>

          {!event.registered && event.status === 'upcoming' && (
            <div className="border-t pt-4">
              <Button
                className="w-full bg-blue-600 hover:bg-blue-700"
                onClick={() => {
                  handleRegister(event.id);
                  onClose();
                }}
              >
                Register for this Event
              </Button>
            </div>
          )}
        </div>
      </div>
    </div>
  );

  return (
    <div className="max-w-7xl mx-auto p-6">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Events</h1>
        <p className="text-gray-600 text-lg">Discover and participate in campus events</p>
      </div>

      {/* Search */}
      <div className="relative mb-6">
        <Search className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
        <Input
          placeholder="Search events..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="pl-10"
        />
      </div>

      {/* Tabs */}
      <Tabs value={viewMode} onValueChange={(value: any) => setViewMode(value)} className="mb-6">
        <TabsList className="grid w-full grid-cols-2 max-w-md">
          <TabsTrigger value="upcoming">Upcoming Events</TabsTrigger>
          <TabsTrigger value="calendar">Past Events</TabsTrigger>
        </TabsList>

        <TabsContent value="upcoming" className="space-y-6">
          <div className="mb-4">
            <p className="text-gray-600">
              {filteredEvents(upcomingEvents).length} upcoming events
            </p>
          </div>
          
          <div className="grid md:grid-cols-2 lg:grid-cols-2 gap-6">
            {filteredEvents(upcomingEvents).map((event) => (
              <EventCard key={event.id} event={event} />
            ))}
          </div>
        </TabsContent>

        <TabsContent value="calendar" className="space-y-6">
          <div className="mb-4">
            <p className="text-gray-600">
              {filteredEvents(pastEvents).length} past events
            </p>
          </div>
          
          <div className="grid md:grid-cols-2 lg:grid-cols-2 gap-6">
            {filteredEvents(pastEvents).map((event) => (
              <EventCard key={event.id} event={event} />
            ))}
          </div>
        </TabsContent>
      </Tabs>

      {/* Event Detail Modal */}
      {selectedEvent && (
        <EventDetailModal
          event={selectedEvent}
          onClose={() => setSelectedEvent(null)}
        />
      )}
    </div>
  );
}