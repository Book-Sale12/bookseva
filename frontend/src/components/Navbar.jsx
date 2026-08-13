import { useState, useRef, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '../lib/api';
import { BookOpen, ShoppingCart, User as UserIcon, LogOut, Bell, CheckCircle, MessageSquare } from 'lucide-react';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef(null);
  const queryClient = useQueryClient();

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsDropdownOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const { data: unreadCount = 0 } = useQuery({
    queryKey: ['unreadNotificationsCount'],
    queryFn: async () => {
      if (!isAuthenticated) return 0;
      const res = await api.get('/notifications/unread-count');
      return res.data.data;
    },
    enabled: isAuthenticated,
    refetchInterval: 30000,
  });

  const { data: unreadMessagesCount = 0 } = useQuery({
    queryKey: ['unreadMessagesCount'],
    queryFn: async () => {
      if (!isAuthenticated) return 0;
      const res = await api.get('/messages/unread-count');
      return res.data.data;
    },
    enabled: isAuthenticated,
    refetchInterval: 30000,
  });

  const { data: notifications = [] } = useQuery({
    queryKey: ['notifications'],
    queryFn: async () => {
      if (!isAuthenticated) return [];
      const res = await api.get('/notifications');
      return res.data.data;
    },
    enabled: isAuthenticated && isDropdownOpen,
  });

  const markAsReadMutation = useMutation({
    mutationFn: async (id) => {
      await api.patch(`/notifications/${id}/read`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
      queryClient.invalidateQueries({ queryKey: ['unreadNotificationsCount'] });
    },
  });

  const handleNotificationClick = (notification) => {
    if (!notification.isRead) {
      markAsReadMutation.mutate(notification.id);
    }
  };

  const { data: cart } = useQuery({
    queryKey: ['cart'],
    queryFn: async () => {
      if (!isAuthenticated) return null;
      const res = await api.get('/cart');
      return res.data.data;
    },
    enabled: isAuthenticated,
  });

  const { data: currentUserProfile } = useQuery({
    queryKey: ['currentUserProfile'],
    queryFn: async () => {
      if (!isAuthenticated) return null;
      const res = await api.get('/users/me');
      return res.data.data;
    },
    enabled: isAuthenticated,
    refetchInterval: 30000,
  });

  const cartCount = cart?.items?.length || 0;
  const displayTrustScore = currentUserProfile?.trustScore ?? user?.trustScore ?? 100;

  return (
    <nav className="bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link to="/" className="flex-shrink-0 flex items-center gap-2">
              <BookOpen className="h-8 w-8 text-primary" />
              <span className="font-heading font-bold text-xl tracking-tight text-slate-900 dark:text-white">
                BookSeva
              </span>
            </Link>
          </div>

          <div className="flex items-center gap-4">
            {isAuthenticated ? (
              <>
                <Link
                  to="/sell"
                  className="hidden md:inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-primary hover:bg-primary/90 shadow-sm transition-colors"
                >
                  Sell a Book
                </Link>
                <Link
                  to="/profile"
                  className="text-sm font-medium text-slate-700 hover:text-primary dark:text-slate-300 dark:hover:text-primary transition-colors"
                >
                  Profile
                </Link>
                <div className="relative" ref={dropdownRef}>
                  <button
                    onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                    className="p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-300 relative transition-colors"
                    title="Notifications"
                  >
                    <Bell className="h-6 w-6" />
                    {unreadCount > 0 && (
                      <span className="absolute top-1 right-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold text-white shadow-sm ring-2 ring-white dark:ring-slate-900">
                        {unreadCount > 99 ? '99+' : unreadCount}
                      </span>
                    )}
                  </button>

                  {isDropdownOpen && (
                    <div className="absolute right-0 mt-2 w-80 sm:w-96 bg-white dark:bg-slate-800 rounded-xl shadow-xl border border-slate-200 dark:border-slate-700 overflow-hidden z-50 transform origin-top-right transition-all">
                      <div className="p-4 border-b border-slate-200 dark:border-slate-700 flex justify-between items-center bg-slate-50 dark:bg-slate-900/50">
                        <h3 className="font-semibold text-slate-800 dark:text-white">Notifications</h3>
                      </div>
                      <div className="max-h-[400px] overflow-y-auto">
                        {notifications.length === 0 ? (
                          <div className="p-8 text-center text-sm text-slate-500 dark:text-slate-400">
                            You have no notifications yet.
                          </div>
                        ) : (
                          <div className="divide-y divide-slate-100 dark:divide-slate-700/50">
                            {notifications.map((notification) => (
                              <div
                                key={notification.id}
                                onClick={() => handleNotificationClick(notification)}
                                className={`p-4 cursor-pointer transition-colors hover:bg-slate-50 dark:hover:bg-slate-700/50 flex gap-3 ${
                                  !notification.isRead ? 'bg-blue-50/30 dark:bg-blue-900/10' : ''
                                }`}
                              >
                                <div className="mt-1 flex-shrink-0">
                                  {!notification.isRead ? (
                                    <div className="h-2 w-2 mt-1.5 rounded-full bg-blue-500 shadow-[0_0_8px_rgba(59,130,246,0.5)]"></div>
                                  ) : (
                                    <CheckCircle className="h-4 w-4 text-slate-400" />
                                  )}
                                </div>
                                <div className="flex-1 min-w-0">
                                  <p className={`text-sm break-words ${!notification.isRead ? 'font-semibold text-slate-900 dark:text-white' : 'text-slate-700 dark:text-slate-300'}`}>
                                    {notification.message}
                                  </p>
                                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-1.5">
                                    {new Date(notification.createdAt).toLocaleString(undefined, {
                                      month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
                                    })}
                                  </p>
                                </div>
                              </div>
                            ))}
                          </div>
                        )}
                      </div>
                    </div>
                  )}
                </div>

                <Link
                  to="/inbox"
                  className="p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-300 relative transition-colors"
                  title="Messages Inbox"
                >
                  <MessageSquare className="h-6 w-6" />
                  {unreadMessagesCount > 0 && (
                    <span className="absolute top-1 right-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold text-white shadow-sm ring-2 ring-white dark:ring-slate-900">
                      {unreadMessagesCount > 99 ? '99+' : unreadMessagesCount}
                    </span>
                  )}
                </Link>

                <Link
                  to="/cart"
                  className="p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-300 relative transition-colors"
                  title="Cart"
                >
                  <ShoppingCart className="h-6 w-6" />
                  {cartCount > 0 && (
                    <span className="absolute top-1 right-1 flex h-4 w-4 items-center justify-center rounded-full bg-blue-600 text-[10px] font-bold text-white shadow-sm ring-2 ring-white dark:ring-slate-900">
                      {cartCount}
                    </span>
                  )}
                </Link>
                {user?.role === 'ADMIN' && (
                  <Link
                    to="/admin"
                    className="hidden md:inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-primary bg-primary/10 hover:bg-primary/20 transition-colors"
                  >
                    Admin Panel
                  </Link>
                )}
                <div className="flex items-center gap-2 pl-4 border-l border-slate-200 dark:border-slate-700">
                  <div className="flex flex-col items-end hidden md:flex">
                    <span className="text-sm font-medium text-slate-700 dark:text-slate-200">{user.name}</span>
                    <span className="text-xs text-slate-500 dark:text-slate-400">Trust Score: {displayTrustScore}</span>
                  </div>
                  <button
                    onClick={logout}
                    className="p-2 text-slate-500 hover:text-red-600 dark:text-slate-400 dark:hover:text-red-500 transition-colors"
                    title="Logout"
                  >
                    <LogOut className="h-5 w-5" />
                  </button>
                </div>
              </>
            ) : (
              <>
                <Link
                  to="/login"
                  className="text-sm font-medium text-slate-700 hover:text-primary dark:text-slate-300 dark:hover:text-primary transition-colors"
                >
                  Log in
                </Link>
                <Link
                  to="/register"
                  className="inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-primary hover:bg-primary/90 shadow-sm transition-colors"
                >
                  Sign up
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
