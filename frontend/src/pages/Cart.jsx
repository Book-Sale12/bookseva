import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Link, useNavigate } from 'react-router-dom';
import api from '../lib/api';
import { useAuth } from '../context/AuthContext';
import { ShoppingCart, Trash2, ShieldCheck, ArrowRight, BookText } from 'lucide-react';

const fetchCart = async () => {
  const res = await api.get('/cart');
  return res.data.data;
};

const Cart = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isProcessing, setIsProcessing] = useState(false);

  const { data, isLoading, error } = useQuery({
    queryKey: ['cart'],
    queryFn: fetchCart,
    enabled: isAuthenticated,
  });

  const removeFromCartMutation = useMutation({
    mutationFn: (itemId) => api.delete(`/cart/items/${itemId}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cart'] });
    },
  });

  const checkoutMutation = useMutation({
    mutationFn: (itemIds) => api.post('/orders/checkout', { cartItemIds: itemIds }),
    onSuccess: (res) => {
      const payments = res.data.data;
      if (payments && payments.length > 0) {
        initiateRazorpay(payments[0]); // For MVP, assuming single item checkout or batched gateway order
      } else {
        alert('Order placed successfully (No payment required).');
        queryClient.invalidateQueries({ queryKey: ['cart'] });
        navigate('/orders');
      }
    },
    onError: (err) => {
      alert(err.response?.data?.error?.message || 'Checkout failed');
      setIsProcessing(false);
    }
  });

  const cancelOrderMutation = useMutation({
    mutationFn: (orderId) => api.post(`/orders/${orderId}/cancel`),
    onSuccess: () => {
      // Order cancelled successfully
      queryClient.invalidateQueries({ queryKey: ['cart'] });
    },
    onError: (err) => {
      console.error("Failed to cancel order after dismissal", err);
    }
  });

  const initiateRazorpay = (payment) => {
    const options = {
      key: import.meta.env.VITE_RAZORPAY_KEY_ID, // Use the public key from env
      amount: payment.amount * 100, // Amount in paise
      currency: "INR",
      name: "BookSeva",
      description: "Book Purchase",
      order_id: payment.gatewayOrderId, // The Razorpay order ID returned from backend
      handler: async function (response) {
        try {
          setIsProcessing(true);
          await api.post('/payments/verify', {
            razorpayOrderId: response.razorpay_order_id,
            razorpayPaymentId: response.razorpay_payment_id,
            razorpaySignature: response.razorpay_signature
          });
          alert('Payment Successful!');
          queryClient.invalidateQueries({ queryKey: ['cart'] });
          navigate('/orders');
        } catch (err) {
          alert(err.response?.data?.error?.message || 'Payment verification failed. If money was deducted, it will be refunded.');
        } finally {
          setIsProcessing(false);
        }
      },
      prefill: {
        name: "Test User",
        email: "test@example.com",
        contact: "9999999999"
      },
      theme: {
        color: "#aa3bff" // Primary color
      },
      modal: {
        ondismiss: function() {
          if (payment.order && payment.order.id) {
            cancelOrderMutation.mutate(payment.order.id);
          }
          setIsProcessing(false);
        }
      }
    };
    
    const rzp = new window.Razorpay(options);
    rzp.on('payment.failed', function (response){
      alert(response.error.description);
      setIsProcessing(false);
    });
    rzp.open();
  };

  const handleCheckout = () => {
    if (!data?.items?.length) return;
    setIsProcessing(true);
    const itemIds = data.items.map(item => item.id);
    checkoutMutation.mutate(itemIds);
  };

  if (!isAuthenticated) {
    return (
      <div className="min-h-[calc(100vh-4rem)] flex items-center justify-center p-8 text-center">
        <div>
          <ShoppingCart className="mx-auto h-16 w-16 text-slate-300 mb-4" />
          <h2 className="text-2xl font-bold text-slate-800 dark:text-white">Your Cart is Empty</h2>
          <p className="mt-2 text-slate-600 dark:text-slate-400">Log in to view your cart and checkout.</p>
          <Link to="/login" className="mt-6 inline-block px-6 py-3 bg-primary text-white font-medium rounded-xl hover:bg-primary/90 transition-colors">
            Log in
          </Link>
        </div>
      </div>
    );
  }

  if (isLoading) return <div className="p-8 text-center">Loading cart...</div>;
  if (error) return <div className="p-8 text-center text-red-500">Failed to load cart.</div>;

  const items = data?.items || [];
  const totalAmount = items.reduce((sum, item) => sum + item.book.price, 0);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <h1 className="text-3xl font-heading font-bold text-slate-900 dark:text-white mb-8 flex items-center gap-3">
        <ShoppingCart className="h-8 w-8 text-primary" /> Your Cart
      </h1>

      {items.length === 0 ? (
        <div className="bg-white dark:bg-slate-900 rounded-2xl p-12 text-center border border-slate-200 dark:border-slate-800 shadow-sm">
          <BookText className="mx-auto h-16 w-16 text-slate-300 mb-4" />
          <h2 className="text-2xl font-heading font-semibold text-slate-900 dark:text-white">Your cart is empty</h2>
          <p className="mt-2 text-slate-500 dark:text-slate-400 mb-8">Looks like you haven't added any books yet.</p>
          <Link to="/" className="inline-flex items-center gap-2 px-6 py-3 bg-primary text-white font-medium rounded-xl hover:bg-primary/90 transition-colors shadow-sm">
            Browse Books <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
      ) : (
        <div className="flex flex-col lg:flex-row gap-8">
          <div className="flex-1 space-y-4">
            {items.map((item) => (
              <div key={item.id} className="bg-white dark:bg-slate-900 p-4 sm:p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col sm:flex-row gap-6">
                <div className="w-24 h-32 bg-slate-100 dark:bg-slate-800 rounded-lg overflow-hidden shrink-0">
                  {item.book.images && item.book.images.length > 0 ? (
                    <img src={item.book.images[0].url} alt={item.book.title} className="w-full h-full object-cover" />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center text-slate-400">
                      <BookText className="h-8 w-8 opacity-50" />
                    </div>
                  )}
                </div>
                <div className="flex-1 flex flex-col">
                  <div className="flex justify-between items-start gap-4">
                    <div>
                      <Link to={`/books/${item.book.id}`} className="font-heading font-semibold text-lg text-slate-900 dark:text-white hover:text-primary transition-colors line-clamp-2">
                        {item.book.title}
                      </Link>
                      <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">by {item.book.author}</p>
                      <p className="text-xs text-slate-400 mt-1">Condition: {item.book.condition}</p>
                    </div>
                    <p className="font-bold text-lg text-slate-900 dark:text-white whitespace-nowrap">₹{item.book.price}</p>
                  </div>
                  <div className="mt-auto pt-4 flex items-center justify-between">
                    <p className="text-sm text-slate-500 dark:text-slate-400 flex items-center gap-1">
                      Sold by: <span className="font-medium text-slate-700 dark:text-slate-300">{item.book.seller.name}</span>
                    </p>
                    <button
                      onClick={() => removeFromCartMutation.mutate(item.id)}
                      disabled={removeFromCartMutation.isPending}
                      className="text-red-500 hover:text-red-600 p-2 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors disabled:opacity-50 flex items-center gap-1 text-sm font-medium"
                    >
                      <Trash2 className="h-4 w-4" /> Remove
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="w-full lg:w-96 shrink-0">
            <div className="bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm sticky top-24">
              <h3 className="font-heading font-semibold text-xl text-slate-900 dark:text-white mb-6">Order Summary</h3>
              
              <div className="space-y-4 text-sm text-slate-600 dark:text-slate-400 border-b border-slate-100 dark:border-slate-800 pb-6 mb-6">
                <div className="flex justify-between">
                  <span>Subtotal ({items.length} items)</span>
                  <span className="font-medium text-slate-900 dark:text-white">₹{totalAmount}</span>
                </div>
                <div className="flex justify-between text-green-600 dark:text-green-400">
                  <span>Platform Fee</span>
                  <span>Free</span>
                </div>
              </div>
              
              <div className="flex justify-between items-center mb-8">
                <span className="font-heading font-semibold text-lg text-slate-900 dark:text-white">Total</span>
                <span className="font-bold text-2xl text-slate-900 dark:text-white">₹{totalAmount}</span>
              </div>
              
              <button
                onClick={handleCheckout}
                disabled={isProcessing}
                className="w-full py-4 bg-primary text-white font-medium rounded-xl hover:bg-primary/90 transition-colors shadow-md shadow-primary/20 hover:shadow-lg disabled:opacity-70 flex justify-center items-center gap-2"
              >
                {isProcessing ? 'Processing...' : (
                  <>Secure Checkout <ShieldCheck className="h-5 w-5" /></>
                )}
              </button>
              
              <p className="mt-4 text-center text-xs text-slate-500 dark:text-slate-400">
                Payments are securely processed via Razorpay.
              </p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Cart;
