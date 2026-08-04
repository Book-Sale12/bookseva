import { useState, useRef, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Send, MessageSquare, X } from 'lucide-react';
import api from '../lib/api';
import { useAuth } from '../context/AuthContext';

const ChatBox = ({ bookId, receiverId, receiverName, isOpen, onClose }) => {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [content, setContent] = useState('');
  const messagesEndRef = useRef(null);

  const { data, isLoading } = useQuery({
    queryKey: ['messages', bookId, receiverId],
    queryFn: async () => {
      const res = await api.get(`/messages/book/${bookId}/user/${receiverId}`);
      return res.data.data;
    },
    enabled: isOpen && !!bookId && !!receiverId,
    refetchInterval: isOpen ? 5000 : false, // Poll every 5 seconds if open
  });

  const sendMessageMutation = useMutation({
    mutationFn: (newMsg) => api.post('/messages', newMsg),
    onSuccess: () => {
      setContent('');
      queryClient.invalidateQueries(['messages', bookId, receiverId]);
    }
  });

  const handleSend = (e) => {
    e.preventDefault();
    if (!content.trim()) return;
    sendMessageMutation.mutate({ bookId, receiverId, content: content.trim() });
  };

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
    if (!isOpen) {
      setContent('');
    }
  }, [data, isOpen]);

  if (!isOpen) return null;

  return (
    <div className="fixed bottom-4 right-4 w-80 sm:w-96 h-[500px] max-h-[calc(100vh-2rem)] bg-white dark:bg-slate-900 rounded-t-2xl rounded-bl-2xl shadow-2xl border border-slate-200 dark:border-slate-800 flex flex-col overflow-hidden z-50 transition-all transform origin-bottom-right">
      <div className="bg-primary px-4 py-3 flex items-center justify-between text-white shrink-0">
        <div className="flex items-center gap-2">
          <MessageSquare className="h-5 w-5" />
          <h3 className="font-heading font-medium">Chat with {receiverName}</h3>
        </div>
        <button onClick={onClose} className="text-white/80 hover:text-white hover:bg-white/10 rounded-full p-1 transition-colors">
          <X className="h-5 w-5" />
        </button>
      </div>

      <div className="flex-1 p-4 overflow-y-auto bg-slate-50 dark:bg-slate-900 space-y-3">
        {isLoading ? (
          <div className="text-center text-sm text-slate-500 py-4">Loading messages...</div>
        ) : data?.length === 0 ? (
          <div className="h-full flex flex-col items-center justify-center text-slate-400">
            <MessageSquare className="h-8 w-8 mb-2 opacity-50" />
            <p className="text-sm">No messages yet.</p>
            <p className="text-xs mt-1 text-center px-4">Send a message to start the conversation.</p>
          </div>
        ) : (
          data?.map((msg) => {
            const isMe = String(msg.senderId) === String(user?.id);
            return (
              <div key={msg.id} className={`flex flex-col ${isMe ? 'items-end' : 'items-start'}`}>
                <div className={`max-w-[80%] rounded-2xl px-4 py-2 text-sm ${isMe ? 'bg-primary text-white rounded-tr-sm' : 'bg-white dark:bg-slate-800 text-slate-800 dark:text-slate-200 border border-slate-200 dark:border-slate-700 rounded-tl-sm shadow-sm'}`}>
                  {msg.content}
                </div>
                <span className="text-[10px] text-slate-400 mt-1 mx-1">
                  {new Date(msg.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </span>
              </div>
            );
          })
        )}
        <div ref={messagesEndRef} />
      </div>

      <form onSubmit={handleSend} className="p-3 bg-white dark:bg-slate-900 border-t border-slate-200 dark:border-slate-800 flex items-center gap-2 shrink-0">
        <input
          type="text"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="Type a message..."
          className="flex-1 bg-slate-100 dark:bg-slate-800 border-transparent rounded-full px-4 py-2 text-sm focus:ring-primary focus:border-transparent dark:text-white"
        />
        <button
          type="submit"
          disabled={!content.trim() || sendMessageMutation.isPending}
          className="p-2 rounded-full bg-primary text-white hover:bg-primary/90 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <Send className="h-4 w-4" />
        </button>
      </form>
    </div>
  );
};

export default ChatBox;
