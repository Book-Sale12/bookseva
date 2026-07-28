import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Star, X } from 'lucide-react';
import api from '../lib/api';

const ReviewModal = ({ isOpen, onClose, orderId, revieweeId, revieweeName }) => {
  const queryClient = useQueryClient();
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const [comment, setComment] = useState('');
  const [error, setError] = useState('');

  const submitReviewMutation = useMutation({
    mutationFn: (data) => api.post('/reviews', data),
    onSuccess: () => {
      queryClient.invalidateQueries(['orders']);
      queryClient.invalidateQueries(['userReviews', revieweeId]);
      onClose();
    },
    onError: (err) => {
      setError(err.response?.data?.error?.message || 'Failed to submit review');
    }
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (rating === 0) {
      setError('Please select a rating');
      return;
    }
    submitReviewMutation.mutate({ orderId, revieweeId, rating, comment });
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-white dark:bg-slate-900 rounded-2xl shadow-xl border border-slate-200 dark:border-slate-800 w-full max-w-md overflow-hidden relative">
        <button onClick={onClose} className="absolute top-4 right-4 text-slate-400 hover:text-slate-600 dark:hover:text-white transition-colors">
          <X className="h-5 w-5" />
        </button>
        
        <div className="p-6">
          <h2 className="text-xl font-heading font-bold text-slate-900 dark:text-white mb-2">
            Rate {revieweeName}
          </h2>
          <p className="text-sm text-slate-500 dark:text-slate-400 mb-6">
            How was your experience buying/selling with them?
          </p>

          {error && (
            <div className="bg-red-50 text-red-600 p-3 rounded-xl text-sm mb-4">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="flex justify-center gap-2">
              {[1, 2, 3, 4, 5].map((star) => (
                <button
                  key={star}
                  type="button"
                  onClick={() => setRating(star)}
                  onMouseEnter={() => setHoverRating(star)}
                  onMouseLeave={() => setHoverRating(0)}
                  className="p-1 focus:outline-none"
                >
                  <Star 
                    className={`h-10 w-10 transition-colors ${
                      (hoverRating || rating) >= star 
                        ? 'fill-amber-400 text-amber-400' 
                        : 'text-slate-300 dark:text-slate-700'
                    }`} 
                  />
                </button>
              ))}
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
                Leave a comment (Optional)
              </label>
              <textarea
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                rows="4"
                className="w-full rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-3 text-slate-900 dark:text-white focus:ring-primary focus:border-primary resize-none text-sm"
                placeholder="Write your review here..."
              ></textarea>
            </div>

            <button
              type="submit"
              disabled={submitReviewMutation.isPending}
              className="w-full py-3 bg-primary text-white font-medium rounded-xl hover:bg-primary/90 transition-colors shadow-sm disabled:opacity-70"
            >
              {submitReviewMutation.isPending ? 'Submitting...' : 'Submit Review'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ReviewModal;
