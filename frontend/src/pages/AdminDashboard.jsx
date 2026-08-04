import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Users, BookOpen, AlertTriangle, RefreshCw, Ban, CheckCircle, XCircle, DollarSign, Percent, ExternalLink, Search, Trash2, Save } from 'lucide-react';
import api from '../lib/api';
import { useAuth } from '../context/AuthContext';
import { Navigate } from 'react-router-dom';

const AdminDashboard = () => {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [activeTab, setActiveTab] = useState('dashboard');
  const [searchTerm, setSearchTerm] = useState('');
  const [activeSearch, setActiveSearch] = useState('');
  const [settingEdits, setSettingEdits] = useState({});
  const [resolutionMessages, setResolutionMessages] = useState({});

  // Redirect if not admin
  if (user?.role !== 'ADMIN') {
    return <Navigate to="/" replace />;
  }

  const { data: dashboardData, isLoading: isLoadingDashboard } = useQuery({
    queryKey: ['adminDashboard'],
    queryFn: async () => {
      const res = await api.get('/admin/dashboard');
      return res.data.data;
    },
    enabled: activeTab === 'dashboard',
  });

  const { data: usersData, isLoading: isLoadingUsers } = useQuery({
    queryKey: ['adminUsers'],
    queryFn: async () => {
      const res = await api.get('/admin/users');
      return res.data.data;
    },
    enabled: activeTab === 'users',
  });

  const updateUserStatus = useMutation({
    mutationFn: async ({ id, newStatus }) => {
      const res = await api.patch(`/admin/users/${id}/status`, { newStatus });
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['adminUsers'] });
    },
  });

  const handleStatusChange = (userId, newStatus) => {
    if (window.confirm(`Are you sure you want to change this user's status to ${newStatus}?`)) {
      updateUserStatus.mutate({ id: userId, newStatus });
    }
  };

  const { data: reportsData, isLoading: isLoadingReports } = useQuery({
    queryKey: ['adminReports'],
    queryFn: async () => {
      const res = await api.get('/admin/reports');
      return res.data.data;
    },
    enabled: activeTab === 'reports',
  });

  const resolveReport = useMutation({
    mutationFn: async ({ id, newStatus, message }) => {
      const res = await api.patch(`/admin/reports/${id}`, { newStatus, message });
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['adminReports'] });
      queryClient.invalidateQueries({ queryKey: ['adminDashboard'] });
    },
  });

  const handleReportAction = (reportId, newStatus) => {
    if (window.confirm(`Are you sure you want to mark this report as ${newStatus}?`)) {
      const message = resolutionMessages[reportId] || '';
      resolveReport.mutate({ id: reportId, newStatus, message });
    }
  };

  const handleResolutionMessageChange = (reportId, value) => {
    setResolutionMessages(prev => ({ ...prev, [reportId]: value }));
  };

  const { data: disputesData, isLoading: isLoadingDisputes } = useQuery({
    queryKey: ['adminDisputes'],
    queryFn: async () => {
      const res = await api.get('/admin/disputes');
      return res.data.data;
    },
    enabled: activeTab === 'disputes',
  });

  const resolveDispute = useMutation({
    mutationFn: async ({ id, newStatus }) => {
      const res = await api.patch(`/admin/disputes/${id}/resolve`, { newStatus, resolutionNotes: 'Resolved by Admin' });
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['adminDisputes'] });
      queryClient.invalidateQueries({ queryKey: ['adminDashboard'] });
    },
  });

  const handleDisputeAction = (disputeId, newStatus) => {
    if (window.confirm(`Are you sure you want to resolve this dispute as ${newStatus}?`)) {
      resolveDispute.mutate({ id: disputeId, newStatus });
    }
  };

  const { data: booksData, isLoading: isLoadingBooks } = useQuery({
    queryKey: ['adminBooks', activeSearch],
    queryFn: async () => {
      const res = await api.get(`/admin/books?search=${encodeURIComponent(activeSearch)}`);
      return res.data.data;
    },
    enabled: activeTab === 'listings',
  });

  const updateBookStatus = useMutation({
    mutationFn: async ({ id, newStatus }) => {
      const res = await api.patch(`/admin/books/${id}/status`, { newStatus });
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['adminBooks'] });
      queryClient.invalidateQueries({ queryKey: ['adminDashboard'] });
    },
  });

  const handleBookAction = (bookId, newStatus) => {
    if (window.confirm(`Are you sure you want to change this listing's status to ${newStatus}?`)) {
      updateBookStatus.mutate({ id: bookId, newStatus });
    }
  };

  const { data: settingsData, isLoading: isLoadingSettings } = useQuery({
    queryKey: ['adminSettings'],
    queryFn: async () => {
      const res = await api.get('/admin/settings');
      return res.data.data;
    },
    enabled: activeTab === 'settings',
  });

  const updateSetting = useMutation({
    mutationFn: async ({ key, newValue }) => {
      const res = await api.put(`/admin/settings/${key}`, { newValue });
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['adminSettings'] });
      alert("Setting updated successfully!");
    },
  });

  const handleSettingChange = (key, value) => {
    setSettingEdits(prev => ({ ...prev, [key]: value }));
  };

  const handleSettingSave = (key, defaultValue) => {
    const newValue = settingEdits[key] !== undefined ? settingEdits[key] : defaultValue;
    updateSetting.mutate({ key, newValue });
  };



  const renderDashboard = () => {
    if (isLoadingDashboard) return <div className="p-8">Loading dashboard...</div>;

    const stats = [
      { label: 'Total Users', value: dashboardData?.totalUsers || 0, icon: Users, color: 'text-blue-500', bg: 'bg-blue-100' },
      { label: 'Active Listings', value: dashboardData?.activeListings || 0, icon: BookOpen, color: 'text-green-500', bg: 'bg-green-100' },
      { label: 'Pending Reports', value: dashboardData?.pendingReports || 0, icon: AlertTriangle, color: 'text-red-500', bg: 'bg-red-100' },
    ];

    return (
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-6">
        {stats.map((stat, i) => (
          <div key={i} className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center gap-4">
            <div className={`h-12 w-12 rounded-xl flex items-center justify-center ${stat.bg} ${stat.color}`}>
              <stat.icon className="h-6 w-6" />
            </div>
            <div>
              <p className="text-sm text-slate-500">{stat.label}</p>
              <p className="text-2xl font-bold">{stat.value}</p>
            </div>
          </div>
        ))}
      </div>
    );
  };

  const renderUsers = () => {
    if (isLoadingUsers) return <div className="p-8">Loading users...</div>;

    return (
      <div className="mt-6 bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 border-b border-slate-200 text-sm text-slate-600">
                <th className="p-4 font-semibold">Name</th>
                <th className="p-4 font-semibold">Email</th>
                <th className="p-4 font-semibold">Role</th>
                <th className="p-4 font-semibold">Status</th>
                <th className="p-4 font-semibold">Trust Score</th>
                <th className="p-4 font-semibold text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {usersData?.map((u) => (
                <tr key={u.id} className="hover:bg-slate-50 transition-colors">
                  <td className="p-4 font-medium text-slate-800">{u.name}</td>
                  <td className="p-4 text-slate-600">{u.email}</td>
                  <td className="p-4">
                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${u.role === 'ADMIN' ? 'bg-purple-100 text-purple-700' : 'bg-slate-100 text-slate-700'}`}>
                      {u.role}
                    </span>
                  </td>
                  <td className="p-4">
                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                      u.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 
                      u.status === 'SUSPENDED' ? 'bg-orange-100 text-orange-700' : 
                      u.status === 'PENDING_VERIFICATION' ? 'bg-blue-100 text-blue-700' :
                      'bg-red-100 text-red-700'
                    }`}>
                      {u.status}
                    </span>
                  </td>
                  <td className="p-4 font-medium text-slate-700">{u.trustScore}</td>
                  <td className="p-4 flex justify-end gap-2">
                    {u.status !== 'ACTIVE' && (
                      <button 
                        onClick={() => handleStatusChange(u.id, 'ACTIVE')}
                        className="p-2 text-green-600 hover:bg-green-50 rounded-lg transition-colors tooltip-wrapper"
                        title="Reactivate/Activate"
                      >
                        <RefreshCw className="w-4 h-4" />
                      </button>
                    )}
                    {u.status !== 'SUSPENDED' && (
                      <button 
                        onClick={() => handleStatusChange(u.id, 'SUSPENDED')}
                        className="p-2 text-orange-600 hover:bg-orange-50 rounded-lg transition-colors"
                        title="Suspend"
                      >
                        <AlertTriangle className="w-4 h-4" />
                      </button>
                    )}
                    {u.status !== 'BANNED' && (
                      <button 
                        onClick={() => handleStatusChange(u.id, 'BANNED')}
                        className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                        title="Ban"
                      >
                        <Ban className="w-4 h-4" />
                      </button>
                    )}
                  </td>
                </tr>
              ))}
              {(!usersData || usersData.length === 0) && (
                <tr>
                  <td colSpan="6" className="p-8 text-center text-slate-500">No users found.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    );
  };

  const renderReports = () => {
    if (isLoadingReports) return <div className="p-8">Loading reports...</div>;

    const pendingReports = reportsData?.filter(r => r.status === 'PENDING') || [];

    return (
      <div className="mt-6 bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 border-b border-slate-200 text-sm text-slate-600">
                <th className="p-4 font-semibold">Reporter</th>
                <th className="p-4 font-semibold">Target</th>
                <th className="p-4 font-semibold">Reason</th>
                <th className="p-4 font-semibold">Status</th>
                <th className="p-4 font-semibold text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {pendingReports.map((r) => (
                <tr key={r.id} className="hover:bg-slate-50 transition-colors">
                  <td className="p-4 font-medium text-slate-800">{r.reporter?.name || 'Unknown'}</td>
                  <td className="p-4 text-slate-600">
                    <span className="font-semibold text-xs text-slate-500 uppercase">{r.targetType}</span> #{r.targetId}
                  </td>
                  <td className="p-4 text-slate-700 max-w-xs truncate" title={r.reason}>{r.reason}</td>
                  <td className="p-4">
                    <span className="px-2 py-1 rounded-full text-xs font-medium bg-orange-100 text-orange-700">
                      {r.status}
                    </span>
                  </td>
                  <td className="p-4 flex items-center justify-end gap-2">
                    <input 
                      type="text"
                      placeholder="Optional message..."
                      className="border border-slate-300 rounded-lg px-2 py-1 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 w-40"
                      value={resolutionMessages[r.id] || ''}
                      onChange={(e) => handleResolutionMessageChange(r.id, e.target.value)}
                    />
                    <button 
                      onClick={() => handleReportAction(r.id, 'RESOLVED')}
                      className="p-2 text-green-600 hover:bg-green-50 rounded-lg transition-colors tooltip-wrapper"
                      title="Resolve"
                    >
                      <CheckCircle className="w-4 h-4" />
                    </button>
                    <button 
                      onClick={() => handleReportAction(r.id, 'DISMISSED')}
                      className="p-2 text-slate-400 hover:bg-slate-100 rounded-lg transition-colors"
                      title="Dismiss"
                    >
                      <XCircle className="w-4 h-4" />
                    </button>
                  </td>
                </tr>
              ))}
              {pendingReports.length === 0 && (
                <tr>
                  <td colSpan="5" className="p-8 text-center text-slate-500">No pending reports found.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    );
  };

  const renderDisputes = () => {
    if (isLoadingDisputes) return <div className="p-8">Loading disputes...</div>;

    const openDisputes = disputesData?.filter(d => d.status === 'OPEN') || [];

    return (
      <div className="mt-6 bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 border-b border-slate-200 text-sm text-slate-600">
                <th className="p-4 font-semibold">Order ID</th>
                <th className="p-4 font-semibold">Raised By</th>
                <th className="p-4 font-semibold">Reason</th>
                <th className="p-4 font-semibold">Evidence</th>
                <th className="p-4 font-semibold text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {openDisputes.map((d) => (
                <tr key={d.id} className="hover:bg-slate-50 transition-colors">
                  <td className="p-4 font-medium text-slate-800">#{d.order?.id}</td>
                  <td className="p-4 text-slate-600">{d.raisedBy?.name || 'Unknown'}</td>
                  <td className="p-4 text-slate-700">
                    <div className="font-semibold text-sm">{d.reason}</div>
                    <div className="text-xs text-slate-500 max-w-xs truncate" title={d.description}>{d.description}</div>
                  </td>
                  <td className="p-4">
                    {d.evidenceUrls && d.evidenceUrls.length > 0 ? (
                      <div className="flex gap-1 flex-wrap">
                        {d.evidenceUrls.map((url, idx) => (
                          <a key={idx} href={url} target="_blank" rel="noopener noreferrer" className="text-blue-500 hover:text-blue-700 tooltip-wrapper" title="View Evidence">
                            <ExternalLink className="w-4 h-4" />
                          </a>
                        ))}
                      </div>
                    ) : (
                      <span className="text-slate-400 text-xs">No evidence</span>
                    )}
                  </td>
                  <td className="p-4 flex justify-end gap-2">
                    <button 
                      onClick={() => handleDisputeAction(d.id, 'RESOLVED_REFUND')}
                      className="p-2 text-green-600 hover:bg-green-50 rounded-lg transition-colors tooltip-wrapper"
                      title="Full Refund"
                    >
                      <DollarSign className="w-4 h-4" />
                    </button>
                    <button 
                      onClick={() => handleDisputeAction(d.id, 'RESOLVED_PARTIAL')}
                      className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors tooltip-wrapper"
                      title="Partial Refund"
                    >
                      <Percent className="w-4 h-4" />
                    </button>
                    <button 
                      onClick={() => handleDisputeAction(d.id, 'RESOLVED_DISMISSED')}
                      className="p-2 text-slate-400 hover:bg-slate-100 rounded-lg transition-colors tooltip-wrapper"
                      title="Dismiss"
                    >
                      <XCircle className="w-4 h-4" />
                    </button>
                  </td>
                </tr>
              ))}
              {openDisputes.length === 0 && (
                <tr>
                  <td colSpan="5" className="p-8 text-center text-slate-500">No open disputes found.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    );
  };

  const renderListings = () => {
    return (
      <div className="mt-6">
        <div className="flex gap-2 mb-6">
          <div className="relative flex-1 max-w-md">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <Search className="h-5 w-5 text-slate-400" />
            </div>
            <input 
              type="text" 
              placeholder="Search by title or seller..."
              className="block w-full pl-10 pr-3 py-2 border border-slate-300 rounded-xl leading-5 bg-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm transition-all"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && setActiveSearch(searchTerm)}
            />
          </div>
          <button 
            className="bg-blue-600 text-white px-5 py-2 rounded-xl hover:bg-blue-700 transition-colors font-medium shadow-sm flex items-center"
            onClick={() => setActiveSearch(searchTerm)}
          >
            Search
          </button>
        </div>

        {isLoadingBooks ? (
          <div className="p-8 text-center text-slate-500">Loading listings...</div>
        ) : (
          <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-200 text-sm text-slate-600">
                    <th className="p-4 font-semibold">Title</th>
                    <th className="p-4 font-semibold">Seller</th>
                    <th className="p-4 font-semibold">Price</th>
                    <th className="p-4 font-semibold">Status</th>
                    <th className="p-4 font-semibold text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-200">
                  {booksData?.map((b) => (
                    <tr key={b.id} className="hover:bg-slate-50 transition-colors">
                      <td className="p-4 font-medium text-slate-800">{b.title}</td>
                      <td className="p-4 text-slate-600">{b.seller?.name || 'Unknown'}</td>
                      <td className="p-4 text-slate-700">₹{b.price}</td>
                      <td className="p-4">
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                          b.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 
                          b.status === 'REMOVED' ? 'bg-red-100 text-red-700' : 
                          'bg-slate-100 text-slate-700'
                        }`}>
                          {b.status}
                        </span>
                      </td>
                      <td className="p-4 flex justify-end gap-2">
                        {b.status !== 'REMOVED' && (
                          <button 
                            onClick={() => handleBookAction(b.id, 'REMOVED')}
                            className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors tooltip-wrapper"
                            title="Remove Listing"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        )}
                        {b.status === 'REMOVED' && (
                          <button 
                            onClick={() => handleBookAction(b.id, 'ACTIVE')}
                            className="p-2 text-green-600 hover:bg-green-50 rounded-lg transition-colors tooltip-wrapper"
                            title="Restore Listing"
                          >
                            <RefreshCw className="w-4 h-4" />
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                  {(!booksData || booksData.length === 0) && (
                    <tr>
                      <td colSpan="5" className="p-8 text-center text-slate-500">No listings found.</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>
    );
  };

  const renderSettings = () => {
    if (isLoadingSettings) return <div className="p-8">Loading settings...</div>;

    return (
      <div className="mt-6 bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden p-6 max-w-2xl">
        <h2 className="text-xl font-bold mb-6 text-slate-800">Platform Settings</h2>
        <div className="space-y-6">
          {settingsData?.map((setting) => (
            <div key={setting.key} className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-4 bg-slate-50 rounded-xl border border-slate-100">
              <div className="flex-1">
                <label className="block text-sm font-semibold text-slate-700 mb-1 capitalize">
                  {setting.key.replace(/_/g, ' ').toLowerCase()}
                </label>
                <div className="text-xs text-slate-500">{setting.description || 'No description provided'}</div>
              </div>
              <div className="flex items-center gap-2">
                <input 
                  type="text"
                  className="w-32 p-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                  value={settingEdits[setting.key] !== undefined ? settingEdits[setting.key] : setting.value}
                  onChange={(e) => handleSettingChange(setting.key, e.target.value)}
                />
                <button 
                  onClick={() => handleSettingSave(setting.key, setting.value)}
                  disabled={updateSetting.isPending}
                  className="bg-blue-600 text-white p-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center justify-center disabled:opacity-50"
                  title="Save Setting"
                >
                  <Save className="w-4 h-4" />
                </button>
              </div>
            </div>
          ))}
          {(!settingsData || settingsData.length === 0) && (
            <div className="text-center text-slate-500 py-4">No settings found.</div>
          )}
        </div>
      </div>
    );
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-12">
      <h1 className="text-3xl font-bold mb-8">Admin Dashboard</h1>
      
      <div className="flex gap-4 border-b border-slate-200">
        <button 
          className={`pb-4 px-2 font-medium text-sm transition-colors border-b-2 ${activeTab === 'dashboard' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
          onClick={() => setActiveTab('dashboard')}
        >
          Overview
        </button>
        <button 
          className={`pb-4 px-2 font-medium text-sm transition-colors border-b-2 ${activeTab === 'users' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
          onClick={() => setActiveTab('users')}
        >
          Users
        </button>
        <button 
          className={`pb-4 px-2 font-medium text-sm transition-colors border-b-2 ${activeTab === 'reports' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
          onClick={() => setActiveTab('reports')}
        >
          Reports
        </button>
        <button 
          className={`pb-4 px-2 font-medium text-sm transition-colors border-b-2 ${activeTab === 'disputes' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
          onClick={() => setActiveTab('disputes')}
        >
          Disputes
        </button>
        <button 
          className={`pb-4 px-2 font-medium text-sm transition-colors border-b-2 ${activeTab === 'listings' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
          onClick={() => setActiveTab('listings')}
        >
          Listings
        </button>
        <button 
          className={`pb-4 px-2 font-medium text-sm transition-colors border-b-2 ${activeTab === 'settings' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
          onClick={() => setActiveTab('settings')}
        >
          Settings
        </button>
      </div>

      {activeTab === 'dashboard' && renderDashboard()}
      {activeTab === 'users' && renderUsers()}
      {activeTab === 'reports' && renderReports()}
      {activeTab === 'disputes' && renderDisputes()}
      {activeTab === 'listings' && renderListings()}
      {activeTab === 'settings' && renderSettings()}
    </div>
  );
};

export default AdminDashboard;
