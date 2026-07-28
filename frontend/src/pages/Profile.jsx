import { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import api from '../lib/api';
import { useAuth } from '../context/AuthContext';
import { 
  User, Package, IndianRupee, BookText, 
  Settings, CheckCircle, Download, Truck,
  Edit, Trash2, ShieldAlert, Star
} from 'lucide-react';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import DisputeModal from '../components/DisputeModal';
import ReviewModal from '../components/ReviewModal';

const profileSchema = z.object({
  name: z.string().min(2, 'Name must be at least 2 characters'),
  phone: z.string().regex(/^[6-9]\d{9}$/, 'Must be a valid 10-digit Indian mobile number'),
  collegeName: z.string().min(2, 'College name is required'),
  courseBranch: z.string().min(2, 'Course/Branch is required'),
});

const fetchMyOrders = async () => (await api.get('/orders/mine')).data;
const fetchMySales = async () => (await api.get('/orders/sales')).data;
const fetchMyListings = async () => (await api.get('/books/me')).data;
const fetchMyProfile = async () => (await api.get('/users/me')).data;
const fetchAuthoredReviewOrderIds = async () => (await api.get('/reviews/my-authored-order-ids')).data.data;

const EditProfileTab = () => {
  const queryClient = useQueryClient();
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  const { data, isLoading } = useQuery({
    queryKey: ['myProfile'],
    queryFn: fetchMyProfile,
  });

  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm({
    resolver: zodResolver(profileSchema)
  });

  useEffect(() => {
    if (data?.data) {
      reset({
        name: data.data.name,
        phone: data.data.phone,
        collegeName: data.data.collegeName,
        courseBranch: data.data.courseBranch,
      });
    }
  }, [data, reset]);

  const onSubmit = async (formData) => {
    try {
      setSuccessMsg("");
      setErrorMsg("");
      await api.put('/users/me', formData);
      setSuccessMsg("Profile updated successfully!");
      queryClient.invalidateQueries(['myProfile']);
    } catch (err) {
      setErrorMsg(err.response?.data?.error?.message || err.message || "Failed to update profile");
    }
  };

  if (isLoading) return <div className="p-8 text-center">Loading profile...</div>;

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6 max-w-2xl">
      <h2 className="text-xl font-bold mb-4">Edit Profile</h2>
      {successMsg && <div className="p-4 bg-green-50 text-green-700 rounded-xl">{successMsg}</div>}
      {errorMsg && <div className="p-4 bg-red-50 text-red-700 rounded-xl">{errorMsg}</div>}

      <div>
        <label className="block text-sm font-medium mb-1">Name</label>
        <input type="text" {...register('name')} className="w-full rounded-lg border p-2" />
        {errors.name && <p className="mt-1 text-sm text-red-500">{errors.name.message}</p>}
      </div>
      <div>
        <label className="block text-sm font-medium mb-1">Phone Number</label>
        <input type="tel" {...register('phone')} className="w-full rounded-lg border p-2" />
        {errors.phone && <p className="mt-1 text-sm text-red-500">{errors.phone.message}</p>}
      </div>
      <div>
        <label className="block text-sm font-medium mb-1">College Name</label>
        <input type="text" {...register('collegeName')} className="w-full rounded-lg border p-2" />
        {errors.collegeName && <p className="mt-1 text-sm text-red-500">{errors.collegeName.message}</p>}
      </div>
      <div>
        <label className="block text-sm font-medium mb-1">Course/Branch</label>
        <input type="text" {...register('courseBranch')} className="w-full rounded-lg border p-2" />
        {errors.courseBranch && <p className="mt-1 text-sm text-red-500">{errors.courseBranch.message}</p>}
      </div>

      <button disabled={isSubmitting} type="submit" className="px-6 py-2 bg-primary text-white rounded-xl">
        {isSubmitting ? "Saving..." : "Save Changes"}
      </button>
    </form>
  );
};

const MyListingsTab = () => {
  const queryClient = useQueryClient();
  const { data, isLoading } = useQuery({ queryKey: ['myListings'], queryFn: fetchMyListings });

  const removeListingMutation = useMutation({
    mutationFn: (bookId) => api.patch(`/books/${bookId}/status`, { status: 'REMOVED' }),
    onSuccess: () => queryClient.invalidateQueries(['myListings']),
    onError: () => alert("Failed to remove listing")
  });

  if (isLoading) return <div className="p-8 text-center">Loading listings...</div>;
  const listings = data?.data || [];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-bold">My Listings</h2>
        <Link to="/sell" className="px-4 py-2 bg-primary text-white rounded-lg text-sm">Add New Listing</Link>
      </div>

      {listings.length === 0 ? (
        <p className="text-slate-500">You haven't listed any books yet.</p>
      ) : (
        <div className="space-y-4">
          {listings.map(book => (
            <div key={book.id} className="flex justify-between items-center p-4 border rounded-xl">
              <div>
                <Link to={`/books/${book.id}`} className="font-bold hover:text-primary">{book.title}</Link>
                <div className="text-sm text-slate-500 flex gap-2 mt-1">
                  <span>Price: ₹{book.price}</span>
                  <span>•</span>
                  <span className={book.status === 'ACTIVE' ? 'text-green-600' : 'text-slate-500'}>
                    Status: {book.status}
                  </span>
                </div>
              </div>
              <div className="flex gap-2">
                <Link to={`/edit-listing/${book.id}`} className="p-2 bg-slate-100 rounded-lg hover:bg-slate-200" title="Edit">
                  <Edit className="h-4 w-4" />
                </Link>
                {book.status === 'ACTIVE' && (
                  <button 
                    onClick={() => {
                      if (window.confirm("Are you sure you want to remove this listing?")) {
                        removeListingMutation.mutate(book.id);
                      }
                    }}
                    className="p-2 bg-red-50 text-red-600 rounded-lg hover:bg-red-100" title="Remove"
                  >
                    <Trash2 className="h-4 w-4" />
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

const MyOrdersTab = () => {
  const queryClient = useQueryClient();
  const [isDisputeModalOpen, setIsDisputeModalOpen] = useState(false);
  const [selectedOrderIdForDispute, setSelectedOrderIdForDispute] = useState(null);
  const [reviewModalData, setReviewModalData] = useState(null);

  const { data, isLoading } = useQuery({ queryKey: ['myOrders'], queryFn: fetchMyOrders });
  const { data: reviewedOrderIds = [] } = useQuery({ queryKey: ['myAuthoredReviewOrderIds'], queryFn: fetchAuthoredReviewOrderIds });

  const confirmReceiptMutation = useMutation({
    mutationFn: (orderId) => api.patch(`/orders/${orderId}/status`, { newStatus: 'COMPLETED' }),
    onSuccess: () => queryClient.invalidateQueries(['myOrders']),
    onError: () => alert('Failed to confirm receipt')
  });

  const handleDownloadInvoice = async (orderId) => {
    try {
      const res = await api.get(`/invoices/${orderId}`);
      window.open(res.data.data.url, '_blank');
    } catch {
      alert('Invoice not found or not yet generated.');
    }
  };

  if (isLoading) return <div className="p-8 text-center">Loading orders...</div>;
  const orders = data?.data || [];

  return (
    <div className="space-y-6">
      <h2 className="text-xl font-bold mb-4">My Orders</h2>
      {orders.length === 0 ? (
        <p className="text-slate-500">You have not placed any orders yet.</p>
      ) : (
        <div className="space-y-4">
          {orders.map((order) => (
            <div key={order.id} className="p-4 border rounded-xl flex flex-col sm:flex-row gap-4 justify-between">
              <div>
                <p className="font-bold">Order #{order.id} - <span className="text-primary">₹{order.amount}</span></p>
                <p className="text-sm text-slate-500">Status: {order.status}</p>
                <Link to={`/books/${order.book.id}`} className="text-sm font-medium hover:underline block mt-2">
                  {order.book.title}
                </Link>
              </div>
              <div className="flex flex-col justify-end gap-2 shrink-0 sm:w-48">
                {order.status === 'HANDED_OVER' && (
                  <>
                    <button onClick={() => confirmReceiptMutation.mutate(order.id)} className="w-full py-2 bg-green-600 text-white rounded-lg text-sm flex justify-center gap-2">
                      <CheckCircle className="h-4 w-4" /> Confirm Receipt
                    </button>
                    {Date.now() - new Date(order.updatedAt).getTime() <= 48 * 60 * 60 * 1000 && (
                      <button 
                        onClick={() => {
                          setSelectedOrderIdForDispute(order.id);
                          setIsDisputeModalOpen(true);
                        }} 
                        className="w-full py-2 bg-orange-100 text-orange-700 hover:bg-orange-200 transition-colors rounded-lg text-sm flex justify-center gap-2 font-medium"
                      >
                        <ShieldAlert className="h-4 w-4" /> Report an issue
                      </button>
                    )}
                  </>
                )}
                {order.status === 'COMPLETED' && (
                  <div className="flex flex-col gap-2 w-full">
                    <button onClick={() => handleDownloadInvoice(order.id)} className="w-full py-2 border rounded-lg text-sm flex justify-center gap-2 hover:bg-slate-50">
                      <Download className="h-4 w-4" /> Invoice
                    </button>
                    {!reviewedOrderIds.includes(order.id) && (
                      <button 
                        onClick={() => setReviewModalData({ orderId: order.id, revieweeId: order.seller.id, name: order.seller.name })}
                        className="w-full py-2 bg-blue-50 text-blue-700 hover:bg-blue-100 transition-colors rounded-lg text-sm flex justify-center gap-2 font-medium border border-blue-200"
                      >
                        <Star className="h-4 w-4" /> Leave a Review
                      </button>
                    )}
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
      <DisputeModal 
        isOpen={isDisputeModalOpen} 
        onClose={() => setIsDisputeModalOpen(false)} 
        orderId={selectedOrderIdForDispute} 
      />
      <ReviewModal 
        isOpen={!!reviewModalData}
        onClose={() => {
          setReviewModalData(null);
          queryClient.invalidateQueries(['myAuthoredReviewOrderIds']);
        }}
        orderId={reviewModalData?.orderId}
        revieweeId={reviewModalData?.revieweeId}
        revieweeName={reviewModalData?.name}
      />
    </div>
  );
};

const MySalesTab = () => {
  const queryClient = useQueryClient();
  const [reviewModalData, setReviewModalData] = useState(null);

  const { data, isLoading } = useQuery({ queryKey: ['mySales'], queryFn: fetchMySales });
  const { data: reviewedOrderIds = [] } = useQuery({ queryKey: ['myAuthoredReviewOrderIds'], queryFn: fetchAuthoredReviewOrderIds });

  const updateStatusMutation = useMutation({
    mutationFn: ({ orderId, newStatus }) => api.patch(`/orders/${orderId}/status`, { newStatus }),
    onSuccess: () => queryClient.invalidateQueries(['mySales']),
    onError: () => alert('Failed to update status')
  });

  if (isLoading) return <div className="p-8 text-center">Loading sales...</div>;
  const orders = data?.data || [];

  return (
    <div className="space-y-6">
      <h2 className="text-xl font-bold mb-4">My Sales</h2>
      {orders.length === 0 ? (
        <p className="text-slate-500">You have not sold any books yet.</p>
      ) : (
        <div className="space-y-4">
          {orders.map((order) => (
            <div key={order.id} className="p-4 border rounded-xl flex flex-col sm:flex-row gap-4 justify-between">
              <div>
                <p className="font-bold">Order #{order.id} - <span className="text-green-600">₹{order.amount}</span></p>
                <p className="text-sm text-slate-500">Status: {order.status} | Buyer: {order.buyer.name}</p>
                <Link to={`/books/${order.book.id}`} className="text-sm font-medium hover:underline block mt-2">
                  {order.book.title}
                </Link>
              </div>
              <div className="flex flex-col justify-end gap-2 shrink-0 sm:w-48">
                {order.status === 'PAID' && (
                  <button onClick={() => updateStatusMutation.mutate({ orderId: order.id, newStatus: 'CONFIRMED_BY_SELLER' })} className="w-full py-2 bg-primary text-white rounded-lg text-sm flex justify-center gap-2">
                    <CheckCircle className="h-4 w-4" /> Confirm
                  </button>
                )}
                {order.status === 'CONFIRMED_BY_SELLER' && (
                  <button onClick={() => updateStatusMutation.mutate({ orderId: order.id, newStatus: 'HANDED_OVER' })} className="w-full py-2 bg-blue-600 text-white rounded-lg text-sm flex justify-center gap-2">
                    <Truck className="h-4 w-4" /> Handed Over
                  </button>
                )}
                {order.status === 'COMPLETED' && !reviewedOrderIds.includes(order.id) && (
                  <button 
                    onClick={() => setReviewModalData({ orderId: order.id, revieweeId: order.buyer.id, name: order.buyer.name })}
                    className="w-full py-2 bg-blue-50 text-blue-700 hover:bg-blue-100 transition-colors rounded-lg text-sm flex justify-center gap-2 font-medium border border-blue-200"
                  >
                    <Star className="h-4 w-4" /> Leave a Review
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
      <ReviewModal 
        isOpen={!!reviewModalData}
        onClose={() => {
          setReviewModalData(null);
          queryClient.invalidateQueries(['myAuthoredReviewOrderIds']);
        }}
        orderId={reviewModalData?.orderId}
        revieweeId={reviewModalData?.revieweeId}
        revieweeName={reviewModalData?.name}
      />
    </div>
  );
};

const Profile = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('profile');

  if (!isAuthenticated) {
    navigate('/login');
    return null;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="flex flex-col md:flex-row gap-8">
        
        {/* Sidebar */}
        <div className="w-full md:w-64 shrink-0 space-y-2">
          <button 
            onClick={() => setActiveTab('profile')}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl font-medium transition-colors ${activeTab === 'profile' ? 'bg-primary text-white' : 'text-slate-600 hover:bg-slate-100'}`}
          >
            <Settings className="h-5 w-5" /> Edit Profile
          </button>
          <button 
            onClick={() => setActiveTab('listings')}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl font-medium transition-colors ${activeTab === 'listings' ? 'bg-primary text-white' : 'text-slate-600 hover:bg-slate-100'}`}
          >
            <BookText className="h-5 w-5" /> My Listings
          </button>
          <button 
            onClick={() => setActiveTab('orders')}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl font-medium transition-colors ${activeTab === 'orders' ? 'bg-primary text-white' : 'text-slate-600 hover:bg-slate-100'}`}
          >
            <Package className="h-5 w-5" /> My Orders
          </button>
          <button 
            onClick={() => setActiveTab('sales')}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl font-medium transition-colors ${activeTab === 'sales' ? 'bg-primary text-white' : 'text-slate-600 hover:bg-slate-100'}`}
          >
            <IndianRupee className="h-5 w-5" /> My Sales
          </button>
        </div>

        {/* Content */}
        <div className="flex-1 bg-white dark:bg-slate-900 rounded-2xl p-6 md:p-8 shadow-sm border border-slate-200 dark:border-slate-800">
          {activeTab === 'profile' && <EditProfileTab />}
          {activeTab === 'listings' && <MyListingsTab />}
          {activeTab === 'orders' && <MyOrdersTab />}
          {activeTab === 'sales' && <MySalesTab />}
        </div>
      </div>
    </div>
  );
};

export default Profile;
