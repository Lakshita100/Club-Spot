import React, { useState } from 'react';
import { Users, Search, Filter, MapPin, Calendar, Star, UserPlus } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Badge } from './ui/badge';
import { ImageWithFallback } from './figma/ImageWithFallback';

interface ClubsPageProps {
  user: any;
}

export default function ClubsPage({ user }: ClubsPageProps) {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [selectedClub, setSelectedClub] = useState<any>(null);

  const categories = [
    { id: 'all', name: 'All Clubs', count: 12 },
    { id: 'technical', name: 'Technical', count: 4 },
    { id: 'cultural', name: 'Cultural', count: 3 },
    { id: 'sports', name: 'Sports', count: 2 },
    { id: 'social', name: 'Social Service', count: 3 }
  ];

  const clubs = [
    {
      id: 1,
      name: "Computer Science Club",
      category: "technical",
      description: "Explore the latest in technology, programming, and computer science. Join us for coding competitions, tech talks, and innovative projects.",
      image: "https://images.unsplash.com/photo-1748366416622-8bd7b3530dd5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx0ZWNobm9sb2d5JTIwY29tcHV0ZXIlMjBzY2llbmNlfGVufDF8fHx8MTc1OTI1Mjk5NHww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
      members: 150,
      rating: 4.8,
      isJoined: true,
      position: "Member",
      upcomingEvents: [
        { name: "AI Workshop", date: "Dec 15" },
        { name: "Coding Contest", date: "Dec 22" }
      ],
      achievements: ["Best Technical Club 2023", "Most Active Club"],
      president: "Alice Johnson",
      established: "2018"
    },
    {
      id: 2,
      name: "Photography Club",
      category: "cultural",
      description: "Capture moments, create memories. Learn photography techniques, participate in photo walks, and showcase your artistic vision.",
      image: "https://images.unsplash.com/photo-1656134291125-2eea7954a295?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwaG90b2dyYXBoeSUyMGNhbWVyYSUyMGFydHxlbnwxfHx8fDE3NTkyNTI5OTd8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
      members: 85,
      rating: 4.6,
      isJoined: true,
      position: "Vice President",
      upcomingEvents: [
        { name: "Nature Photography", date: "Dec 18" },
        { name: "Portfolio Review", date: "Dec 25" }
      ],
      achievements: ["Best Cultural Club 2023"],
      president: "Bob Smith",
      established: "2019"
    },
    {
      id: 3,
      name: "Environmental Club",
      category: "social",
      description: "Make a difference for our planet. Join environmental initiatives, sustainability projects, and awareness campaigns.",
      image: "https://images.unsplash.com/photo-1638267351732-1b7f2707bb3b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxlbnZpcm9ubWVudGFsJTIwbmF0dXJlJTIwZ3JlZW58ZW58MXx8fHwxNzU5MjUzMDAwfDA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
      members: 120,
      rating: 4.7,
      isJoined: true,
      position: "Member",
      upcomingEvents: [
        { name: "Tree Plantation", date: "Dec 20" },
        { name: "Clean Campus Drive", date: "Dec 28" }
      ],
      achievements: ["Green Campus Award 2023"],
      president: "Emma Davis",
      established: "2017"
    },
    {
      id: 4,
      name: "Robotics Club",
      category: "technical",
      description: "Build the future with robotics and automation. Design, program, and compete with cutting-edge robotic systems.",
      image: "https://images.unsplash.com/photo-1663162550938-60f70fab5d31?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx0ZWNobm9sb2d5JTIwY29tcHV0ZXIlMjBzY2llbmNlfGVufDF8fHx8MTc1OTI1Mjk5NHww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
      members: 95,
      rating: 4.9,
      isJoined: false,
      upcomingEvents: [
        { name: "Robot Competition", date: "Dec 16" },
        { name: "Arduino Workshop", date: "Dec 23" }
      ],
      achievements: ["National Robotics Winner 2023"],
      president: "John Wilson",
      established: "2020"
    },
    {
      id: 5,
      name: "Drama Club",
      category: "cultural",
      description: "Express yourself through theater and performance. Join our productions, improve acting skills, and entertain audiences.",
      image: "https://images.unsplash.com/photo-1663162550938-60f70fab5d31?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx0ZWNobm9sb2d5JTIwY29tcHV0ZXIlMjBzY2llbmNlfGVufDF8fHx8MTc1OTI1Mjk5NHww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
      members: 70,
      rating: 4.5,
      isJoined: false,
      upcomingEvents: [
        { name: "Annual Play", date: "Dec 21" },
        { name: "Acting Workshop", date: "Dec 27" }
      ],
      achievements: ["Best Performance 2023"],
      president: "Sarah Brown",
      established: "2016"
    },
    {
      id: 6,
      name: "Cricket Club",
      category: "sports",
      description: "Play the gentleman's game. Practice regularly, compete in tournaments, and represent your college with pride.",
      image: "https://images.unsplash.com/photo-1663162550938-60f70fab5d31?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx0ZWNobm9sb2d5JTIwY29tcHV0ZXIlMjBzY2llbmNlfGVufDF8fHx8MTc1OTI1Mjk5NHww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral",
      members: 45,
      rating: 4.4,
      isJoined: false,
      upcomingEvents: [
        { name: "Inter-College Match", date: "Dec 19" },
        { name: "Practice Session", date: "Dec 24" }
      ],
      achievements: ["District Champions 2023"],
      president: "Mike Johnson",
      established: "2015"
    }
  ];

  const filteredClubs = clubs.filter(club => {
    const matchesSearch = club.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         club.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = selectedCategory === 'all' || club.category === selectedCategory;
    return matchesSearch && matchesCategory;
  });

  const handleJoinClub = (clubId: number) => {
    // Mock join functionality
    console.log(`Joining club ${clubId}`);
  };

  const ClubCard = ({ club }: { club: any }) => (
    <Card className="overflow-hidden hover:shadow-lg transition-shadow duration-300">
      <div className="relative">
        <ImageWithFallback
          src={club.image}
          alt={club.name}
          className="w-full h-48 object-cover"
        />
        <div className="absolute top-2 right-2">
          <Badge variant={club.isJoined ? "default" : "secondary"}>
            {club.isJoined ? "Joined" : "Available"}
          </Badge>
        </div>
      </div>
      <CardHeader>
        <div className="flex items-start justify-between">
          <div>
            <CardTitle className="text-lg">{club.name}</CardTitle>
            <div className="flex items-center gap-2 mt-1">
              <div className="flex items-center gap-1">
                <Star className="w-4 h-4 text-yellow-500 fill-current" />
                <span className="text-sm text-gray-600">{club.rating}</span>
              </div>
              <span className="text-gray-300">•</span>
              <div className="flex items-center gap-1">
                <Users className="w-4 h-4 text-gray-500" />
                <span className="text-sm text-gray-600">{club.members} members</span>
              </div>
            </div>
          </div>
        </div>
        {club.isJoined && (
          <Badge variant="outline" className="w-fit">
            {club.position}
          </Badge>
        )}
      </CardHeader>
      <CardContent>
        <CardDescription className="text-sm leading-relaxed mb-4">
          {club.description}
        </CardDescription>
        <div className="flex items-center justify-between">
          <Button
            variant="outline"
            size="sm"
            onClick={() => setSelectedClub(club)}
          >
            View Details
          </Button>
          {!club.isJoined && (
            <Button
              size="sm"
              className="bg-blue-600 hover:bg-blue-700"
              onClick={() => handleJoinClub(club.id)}
            >
              <UserPlus className="w-4 h-4 mr-1" />
              Join Club
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  );

  const ClubDetailModal = ({ club, onClose }: { club: any; onClose: () => void }) => (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="relative">
          <ImageWithFallback
            src={club.image}
            alt={club.name}
            className="w-full h-64 object-cover"
          />
          <Button
            variant="outline"
            size="sm"
            className="absolute top-4 right-4 bg-white"
            onClick={onClose}
          >
            ✕
          </Button>
        </div>
        
        <div className="p-6">
          <div className="flex items-start justify-between mb-4">
            <div>
              <h2 className="text-2xl mb-2">{club.name}</h2>
              <div className="flex items-center gap-4 text-sm text-gray-600">
                <div className="flex items-center gap-1">
                  <Star className="w-4 h-4 text-yellow-500 fill-current" />
                  <span>{club.rating}</span>
                </div>
                <div className="flex items-center gap-1">
                  <Users className="w-4 h-4" />
                  <span>{club.members} members</span>
                </div>
                <span>Est. {club.established}</span>
              </div>
            </div>
            {club.isJoined && (
              <Badge variant="default">{club.position}</Badge>
            )}
          </div>

          <p className="text-gray-700 mb-6 leading-relaxed">
            {club.description}
          </p>

          <div className="grid md:grid-cols-2 gap-6 mb-6">
            <div>
              <h3 className="text-lg mb-3">Upcoming Events</h3>
              <div className="space-y-2">
                {club.upcomingEvents.map((event: any, index: number) => (
                  <div key={index} className="flex items-center justify-between p-3 bg-gray-50 rounded">
                    <span className="text-sm">{event.name}</span>
                    <span className="text-xs text-gray-500">{event.date}</span>
                  </div>
                ))}
              </div>
            </div>

            <div>
              <h3 className="text-lg mb-3">Achievements</h3>
              <div className="space-y-2">
                {club.achievements.map((achievement: string, index: number) => (
                  <div key={index} className="flex items-center gap-2">
                    <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                    <span className="text-sm">{achievement}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <div className="border-t pt-4">
            <div className="flex items-center justify-between">
              <div>
                <span className="text-sm text-gray-600">President: </span>
                <span className="text-sm">{club.president}</span>
              </div>
              {!club.isJoined && (
                <Button
                  className="bg-blue-600 hover:bg-blue-700"
                  onClick={() => {
                    handleJoinClub(club.id);
                    onClose();
                  }}
                >
                  <UserPlus className="w-4 h-4 mr-2" />
                  Join This Club
                </Button>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );

  return (
    <div className="max-w-7xl mx-auto p-6">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Discover Clubs</h1>
        <p className="text-gray-600 text-lg">Find your passion and connect with like-minded students</p>
      </div>

      {/* Search and Filters */}
      <div className="flex flex-col lg:flex-row gap-4 mb-8">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
          <Input
            placeholder="Search clubs..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-10"
          />
        </div>
        <div className="flex gap-2 overflow-x-auto">
          {categories.map((category) => (
            <Button
              key={category.id}
              variant={selectedCategory === category.id ? "default" : "outline"}
              onClick={() => setSelectedCategory(category.id)}
              className="whitespace-nowrap"
            >
              {category.name} ({category.count})
            </Button>
          ))}
        </div>
      </div>

      {/* Results */}
      <div className="mb-4">
        <p className="text-gray-600">
          Showing {filteredClubs.length} of {clubs.length} clubs
        </p>
      </div>

      {/* Clubs Grid */}
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredClubs.map((club) => (
          <ClubCard key={club.id} club={club} />
        ))}
      </div>

      {/* Club Detail Modal */}
      {selectedClub && (
        <ClubDetailModal
          club={selectedClub}
          onClose={() => setSelectedClub(null)}
        />
      )}
    </div>
  );
}