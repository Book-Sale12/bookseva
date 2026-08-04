import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { ShieldAlert, X } from 'lucide-react';
import api from '../lib/api';

const DisputeModal = ({ isOpen, onClose, orderId }) => {
  const queryClient = useQueryClient();
  const [reason, setReason] = useState('');
  const [description, setDescription] = useState('');
  const [evidenceUrl, setEvidenceUrl] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const raiseDisputeMutation = useMutation({
    mutationFn: (data) => api.post(`/orders/${orderId}/disputes`, data),
    onSuccess: () => {
      setSuccess(true);
      queryClient.invalidateQueries(['myOrders']);
      setTimeout(() => {
        setSuccess(false);
        setReason('');
        setDescription('');
        setEvidenceUrl('');
        onClose();
      }, 2000);
    },
    onError: (err) => {
      setError(err.response?.data?.error?.message || err.response?.data?.message || 'Failed to submit dispute');
    }
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!reason.trim()) {
      setError('Please provide a reason');
      return;
    }
    
    const evidenceUrls = evidenceUrl.trim() ? [evidenceUrl.trim()] : [];
    
    raiseDisputeMutation.mutate({ reason, description, evidenceUrls });
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
            <div className="h-10 w-10 rounded-full bg-orange-100 text-orange-600 flex items-center justify-center">
              <ShieldAlert className="h-5 w-5" />
            </div>
            <h2 className="text-xl font-heading font-bold text-slate-900 dark:text-white">
              Report an Issue
            </h2>
          </div>
          
          <p className="text-sm text-slate-500 dark:text-slate-400 mb-6">
            If you received a different item, damaged item, or have other issues with Order #{orderId}, let us know.
          </p>

          {success ? (
            <div className="bg-green-50 text-green-600 p-4 rounded-xl text-center font-medium">
              Dispute raised successfully. We will review it shortly.
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">
              {error && (
                <div className="bg-red-50 text-red-600 p-3 rounded-xl text-sm mb-4">
                  {error}
                </div>
              )}
              
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                  Reason *
                </label>
                <select
                  value={reason}
                  onChange={(e) => setReason(e.target.value)}
                  required
                  className="w-full rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-3 text-slate-900 dark:text-white focus:ring-primary focus:border-primary text-sm"
                >
                  <option value="">Select a reason</option>
                  <option value="DIFFERENT_ITEM">Received a different item</option>
                  <option value="DAMAGED_ITEM">Item is damaged</option>
                  <option value="MISSING_PARTS">Missing parts/pages</option>
                  <option value="OTHER">Other issue</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                  Description
                </label>
                <textarea
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  rows="3"
                  className="w-full rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-3 text-slate-900 dark:text-white focus:ring-primary focus:border-primary resize-none text-sm"
                  placeholder="Explain the issue in detail..."
                ></textarea>
              </div>
              
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                  Evidence Photo URL (Optional)
                </label>
                <input
                  type="url"
                  value={evidenceUrl}
                  onChange={(e) => setEvidenceUrl(e.target.value)}
                  placeholder="https://imgur.com/your-image.jpg"
                  className="w-full rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary text-sm"
                />
                <p className="text-xs text-slate-500 mt-1">Provide a link to an image showing the issue.</p>
              </div>

              <div className="flex gap-3 pt-2">
                <button
                  type="button"
                  onClick={onClose}
                  className="flex-1 py-3 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 font-medium rounded-xl hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors text-sm"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={raiseDisputeMutation.isPending}
                  className="flex-1 py-3 bg-orange-600 text-white font-medium rounded-xl hover:bg-orange-700 transition-colors shadow-sm disabled:opacity-70 text-sm"
                >
                  {raiseDisputeMutation.isPending ? 'Submitting...' : 'Submit Dispute'}
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default DisputeModal;
