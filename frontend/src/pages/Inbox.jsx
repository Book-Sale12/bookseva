import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import api from '../lib/api';
import { useAuth } from '../context/AuthContext';
import { MessageSquare, BookOpen, Clock } from 'lucide-react';
import ChatBox from '../components/ChatBox';

const Inbox = () => {
  const { user, isAuthenticated } = useAuth();
  const [selectedConv, setSelectedConv] = useState(null);

  const { data: conversations, isLoading, error } = useQuery({
    queryKey: ['conversations'],
    queryFn: async () => {
      const res = await api.get('/messages/conversations');
      return res.data.data;
    },
    enabled: isAuthenticated,
  });

  if (!isAuthenticated) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 text-center">
        <p className="text-slate-600 dark:text-slate-400">Please log in to view your messages.</p>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex items-center gap-3 mb-8">
        <div className="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-xl">
          <MessageSquare className="w-6 h-6 text-blue-600 dark:text-blue-400" />
        </div>
        <h1 className="text-3xl font-heading font-bold text-slate-900 dark:text-white">Messages Inbox</h1>
      </div>

      <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-200 dark:border-slate-700 overflow-hidden">
        {isLoading ? (
          <div className="p-8 text-center text-slate-500">Loading conversations...</div>
        ) : error ? (
          <div className="p-8 text-center text-red-500">Failed to load conversations.</div>
        ) : !conversations || conversations.length === 0 ? (
          <div className="p-12 text-center flex flex-col items-center">
            <MessageSquare className="w-12 h-12 text-slate-300 dark:text-slate-600 mb-4" />
            <p className="text-lg text-slate-600 dark:text-slate-400 font-medium">No messages yet</p>
            <p className="text-sm text-slate-500 mt-1">When buyers or sellers message you, they'll appear here.</p>
          </div>
        ) : (
          <div className="divide-y divide-slate-200 dark:divide-slate-700">
            {conversations.map((conv) => (
              <div 
                key={`${conv.bookId}_${conv.otherUserId}`}
                onClick={() => setSelectedConv(conv)}
                className="p-4 sm:p-6 hover:bg-slate-50 dark:hover:bg-slate-700/50 transition-colors cursor-pointer flex gap-4 items-center"
              >
                <div className="w-16 h-16 bg-slate-100 dark:bg-slate-700 rounded-lg overflow-hidden flex-shrink-0 flex items-center justify-center">
                  {conv.bookImageUrl ? (
                    <img src={conv.bookImageUrl} alt={conv.bookTitle} className="w-full h-full object-cover" />
                  ) : (
                    <BookOpen className="w-6 h-6 text-slate-400" />
                  )}
                </div>
                
                <div className="flex-1 min-w-0">
                  <div className="flex justify-between items-start mb-1">
                    <h3 className="text-base font-semibold text-slate-900 dark:text-white truncate">
                      {conv.otherUserName}
                    </h3>
                    <span className="text-xs text-slate-500 flex items-center gap-1 whitespace-nowrap">
                      <Clock className="w-3 h-3" />
                      {new Date(conv.lastMessageAt).toLocaleDateString()}
                    </span>
                  </div>
                  <p className="text-sm text-primary font-medium truncate mb-1">
                    Book: {conv.bookTitle}
                  </p>
                  <p className="text-sm text-slate-600 dark:text-slate-400 truncate">
                    {conv.lastMessage}
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {selectedConv && (
        <ChatBox
          bookId={selectedConv.bookId}
          receiverId={selectedConv.otherUserId}
          receiverName={selectedConv.otherUserName}
          isOpen={true}
          onClose={() => setSelectedConv(null)}
        />
      )}
    </div>
  );
};

export default Inbox;
