import React from 'react';
import { Users, Calendar, Trophy, Target, Heart, Globe, ArrowRight, CheckCircle } from 'lucide-react';
import { Button } from './ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';

interface LandingPageProps {
  onLogin: (userType: 'user' | 'admin') => void;
  onSignup: () => void;
}

export default function LandingPage({ onLogin, onSignup }: LandingPageProps) {
  const features = [
    {
      icon: <Users className="w-6 h-6 text-blue-600" />,
      title: "Club Management",
      description: "Join and manage multiple clubs with ease. Track your participation and contributions."
    },
    {
      icon: <Calendar className="w-6 h-6 text-green-600" />,
      title: "Event Planning",
      description: "Discover upcoming events, register instantly, and never miss important activities."
    },
    {
      icon: <Trophy className="w-6 h-6 text-yellow-600" />,
      title: "AICTE Hours Tracking",
      description: "Automatically track your AICTE activity hours and generate certificates."
    }
  ];

  const benefits = [
    "Real-time event notifications",
    "Digital certificates and badges",
    "Progress tracking dashboard",
    "Easy club discovery",
    "Seamless registration process",
    "Mobile-friendly interface"
  ];

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="bg-gradient-to-br from-blue-600 via-blue-700 to-blue-800 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h1 className="text-5xl md:text-6xl mb-6 bg-gradient-to-r from-white to-blue-100 bg-clip-text text-transparent">
              Welcome to Club Spot
            </h1>
            <p className="text-xl md:text-2xl text-blue-100 mb-8 max-w-3xl mx-auto">
              Your one-stop platform for college club management, event discovery, and academic activity tracking
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
              <Button
                onClick={onSignup}
                size="lg"
                className="bg-green-600 hover:bg-green-700 text-white px-8 py-3 text-lg flex items-center gap-2"
              >
                Get Started
                <ArrowRight size={20} />
              </Button>
              <Button
                onClick={() => onLogin('user')}
                variant="outline"
                size="lg"
                className="border-white text-white hover:bg-white hover:text-blue-600 px-8 py-3 text-lg"
              >
                Login as Student
              </Button>
              <Button
                onClick={() => onLogin('admin')}
                variant="outline"
                size="lg"
                className="border-white text-white hover:bg-white hover:text-blue-600 px-8 py-3 text-lg"
              >
                Admin Login
              </Button>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-4xl mb-4 text-gray-900">
              Everything You Need for Club Management
            </h2>
            <p className="text-xl text-gray-600 max-w-2xl mx-auto">
              Streamline your college experience with our comprehensive platform designed for students and administrators
            </p>
          </div>
          
          <div className="grid md:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <Card key={index} className="border-0 shadow-lg hover:shadow-xl transition-shadow duration-300">
                <CardHeader className="text-center pb-4">
                  <div className="w-16 h-16 bg-gray-50 rounded-full flex items-center justify-center mx-auto mb-4">
                    {feature.icon}
                  </div>
                  <CardTitle className="text-xl text-gray-900">{feature.title}</CardTitle>
                </CardHeader>
                <CardContent>
                  <CardDescription className="text-center text-gray-600 text-base leading-relaxed">
                    {feature.description}
                  </CardDescription>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* About Section */}
      <section className="py-20 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-4xl mb-6 text-gray-900">
                Our Mission & Vision
              </h2>
              <div className="space-y-6">
                <div className="flex items-start gap-4">
                  <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center flex-shrink-0">
                    <Target className="w-6 h-6 text-blue-600" />
                  </div>
                  <div>
                    <h3 className="text-xl mb-2 text-gray-900">Mission</h3>
                    <p className="text-gray-600 leading-relaxed">
                      To revolutionize college club management by providing a unified platform that enhances student engagement, 
                      simplifies administrative tasks, and fosters a vibrant campus community.
                    </p>
                  </div>
                </div>
                
                <div className="flex items-start gap-4">
                  <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center flex-shrink-0">
                    <Globe className="w-6 h-6 text-green-600" />
                  </div>
                  <div>
                    <h3 className="text-xl mb-2 text-gray-900">Vision</h3>
                    <p className="text-gray-600 leading-relaxed">
                      To become the leading platform for student activity management, empowering students to maximize their 
                      college experience while helping institutions track and improve student engagement.
                    </p>
                  </div>
                </div>

                <div className="flex items-start gap-4">
                  <div className="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center flex-shrink-0">
                    <Heart className="w-6 h-6 text-red-600" />
                  </div>
                  <div>
                    <h3 className="text-xl mb-2 text-gray-900">Values</h3>
                    <p className="text-gray-600 leading-relaxed">
                      We believe in transparency, accessibility, and community-driven development. Our platform is built 
                      with students' needs at the forefront, ensuring every feature adds real value to campus life.
                    </p>
                  </div>
                </div>
              </div>
            </div>
            
            <div className="bg-white rounded-2xl shadow-xl p-8">
              <h3 className="text-2xl mb-6 text-gray-900">Why Choose Club Spot?</h3>
              <div className="space-y-4">
                {benefits.map((benefit, index) => (
                  <div key={index} className="flex items-center gap-3">
                    <CheckCircle className="w-5 h-5 text-green-600 flex-shrink-0" />
                    <span className="text-gray-700">{benefit}</span>
                  </div>
                ))}
              </div>
              
              <div className="mt-8 pt-6 border-t border-gray-200">
                <div className="grid grid-cols-3 gap-4 text-center">
                  <div>
                    <div className="text-2xl text-blue-600 mb-1">500+</div>
                    <div className="text-sm text-gray-600">Students</div>
                  </div>
                  <div>
                    <div className="text-2xl text-green-600 mb-1">50+</div>
                    <div className="text-sm text-gray-600">Clubs</div>
                  </div>
                  <div>
                    <div className="text-2xl text-yellow-600 mb-1">200+</div>
                    <div className="text-sm text-gray-600">Events</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 bg-gradient-to-r from-green-600 to-blue-600 text-white">
        <div className="max-w-4xl mx-auto text-center px-4 sm:px-6 lg:px-8">
          <h2 className="text-4xl mb-6">
            Ready to Transform Your College Experience?
          </h2>
          <p className="text-xl text-green-100 mb-8">
            Join thousands of students who are already using Club Spot to enhance their campus life
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button
              onClick={onSignup}
              size="lg"
              className="bg-white text-green-600 hover:bg-gray-100 px-8 py-3 text-lg"
            >
              Create Your Account
            </Button>
            <Button
              onClick={() => onLogin('user')}
              variant="outline"
              size="lg"
              className="border-white text-white hover:bg-white hover:text-green-600 px-8 py-3 text-lg"
            >
              Already Have an Account?
            </Button>
          </div>
        </div>
      </section>
    </div>
  );
}