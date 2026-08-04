import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Flag, X } from 'lucide-react';
import api from '../lib/api';

const ReportModal = ({ isOpen, onClose, targetType, targetId }) => {
  const [reason, setReason] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const submitReportMutation = useMutation({
    mutationFn: (data) => api.post('/reports', data),
    onSuccess: () => {
      setSuccess(true);
      setTimeout(() => {
        setSuccess(false);
        setReason('');
        onClose();
      }, 2000);
    },
    onError: (err) => {
      setError(err.response?.data?.error?.message || 'Failed to submit report');
    }
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!reason.trim()) {
      setError('Please provide a reason');
      return;
    }
    submitReportMutation.mutate({ targetType, targetId, reason });
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-white dark:bg-slate-900 rounded-2xl shadow-xl border border-slate-200 dark:border-slate-800 w-full max-w-md overflow-hidden relative">
        <button onClick={onClose} className="absolute top-4 right-4 text-slate-400 hover:text-slate-600 dark:hover:text-white transition-colors">
          <X className="h-5 w-5" />
        </button>
        
        <div className="p-6">
          <div className="flex items-center gap-3 mb-2">
            <div className="h-10 w-10 rounded-full bg-red-100 text-red-600 flex items-center justify-center">
              <Flag className="h-5 w-5" />
            </div>
            <h2 className="text-xl font-heading font-bold text-slate-900 dark:text-white">
              Report {targetType === 'USER' ? 'User' : 'Listing'}
            </h2>
          </div>
          
          <p className="text-sm text-slate-500 dark:text-slate-400 mb-6">
            Please let us know why you are reporting this. Our moderation team will review it shortly.
          </p>

          {success ? (
            <div className="bg-green-50 text-green-600 p-4 rounded-xl text-center font-medium">
              Report submitted successfully. Thank you!
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">
              {error && (
                <div className="bg-red-50 text-red-600 p-3 rounded-xl text-sm mb-4">
                  {error}
                </div>
              )}
              
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
                  Reason for reporting *
                </label>
                <textarea
                  value={reason}
                  onChange={(e) => setReason(e.target.value)}
                  rows="4"
                  className="w-full rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-3 text-slate-900 dark:text-white focus:ring-primary focus:border-primary resize-none text-sm"
                  placeholder={`E.g., Fraudulent listing, inappropriate content...`}
                ></textarea>
              </div>

              <div className="flex gap-3 pt-2">
                <button
                  type="button"
                  onClick={onClose}
                  className="flex-1 py-3 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 font-medium rounded-xl hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={submitReportMutation.isPending}
                  className="flex-1 py-3 bg-red-600 text-white font-medium rounded-xl hover:bg-red-700 transition-colors shadow-sm disabled:opacity-70"
                >
                  {submitReportMutation.isPending ? 'Submitting...' : 'Submit Report'}
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default ReportModal;
