import { useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ShieldCheck, MapPin, MessageSquare, BookText, ShoppingCart, ArrowRight, ArrowLeft } from 'lucide-react';
import api from '../lib/api';
import { useAuth } from '../context/AuthContext';
import StarRating from '../components/StarRating';
import ChatBox from '../components/ChatBox';

const fetchSellerProfile = async (sellerId) => {
  const res = await api.get(`/sellers/${sellerId}`);
  return res.data.data;
};

const fetchSellerReviews = async (sellerId, page = 0) => {
  const res = await api.get(`/sellers/${sellerId}/reviews`, { params: { page, size: 5 } });
  return res.data.data;
};

const fetchSellerBooks = async (sellerId) => {
  const res = await api.get(`/sellers/${sellerId}/books`);
  return res.data.data;
};

const SellerProfile = () => {
  const { sellerId } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { isAuthenticated, user } = useAuth();
  
  const [reviewPage, setReviewPage] = useState(0);
  const [isChatOpen, setIsChatOpen] = useState(false);

  const { data: profile, isLoading: isLoadingProfile, error: profileError } = useQuery({
    queryKey: ['sellerProfile', sellerId],
    queryFn: () => fetchSellerProfile(sellerId),
  });

  const { data: reviewsData, isLoading: isLoadingReviews } = useQuery({
    queryKey: ['sellerReviews', sellerId, reviewPage],
    queryFn: () => fetchSellerReviews(sellerId, reviewPage),
  });

  const { data: books, isLoading: isLoadingBooks } = useQuery({
    queryKey: ['sellerBooks', sellerId],
    queryFn: () => fetchSellerBooks(sellerId),
  });

  const { data: cart } = useQuery({
    queryKey: ['cart'],
    queryFn: async () => {
      if (!isAuthenticated) return null;
      const res = await api.get('/cart');
      return res.data.data;
    },
    enabled: isAuthenticated,
  });

  const isInCart = (bookId) => {
    return cart?.items?.some(item => item.book.id === bookId);
  };

  const addToCartMutation = useMutation({
    mutationFn: (bookId) => api.post('/cart/items', { bookId, quantity: 1 }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cart'] });
    },
  });

  const handleAddToCart = (e, book) => {
    e.preventDefault(); // Prevent navigating to book details
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    addToCartMutation.mutate(book.id);
  };

  if (isLoadingProfile) {
    return <div className="min-h-screen flex items-center justify-center">Loading seller profile...</div>;
  }

  if (profileError || !profile) {
    return (
      <div className="min-h-screen flex items-center justify-center flex-col gap-4">
        <p className="text-red-500">Failed to load seller profile. They might not exist.</p>
        <Link to="/" className="text-primary hover:underline">Return Home</Link>
      </div>
    );
  }

  const isSelf = isAuthenticated && user?.id === profile.id;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <Link to={-1} className="inline-flex items-center gap-2 text-sm text-slate-500 hover:text-slate-900 dark:hover:text-white mb-6 transition-colors">
        <ArrowLeft className="h-4 w-4" /> Go back
      </Link>

      {/* Profile Header */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-200 dark:border-slate-700 p-8 mb-8">
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
          <div className="flex items-center gap-6">
            <div className="h-24 w-24 rounded-full bg-primary/10 flex items-center justify-center text-primary font-bold text-4xl border-4 border-primary/20">
              {profile.name.charAt(0)}
            </div>
            <div>
              <h1 className="text-3xl font-heading font-bold text-slate-900 dark:text-white flex items-center gap-2">
                {profile.name}
                {profile.trustScore >= 90 && (
                  <ShieldCheck className="h-6 w-6 text-green-500" title="Highly Trusted" />
                )}
              </h1>
              <p className="text-slate-500 dark:text-slate-400 flex items-center gap-1 mt-1 text-lg">
                <MapPin className="h-4 w-4" /> {profile.collegeName}
              </p>
              <div className="flex items-center gap-2 mt-2">
                <StarRating rating={profile.averageRating} size={18} />
                <span className="text-slate-600 dark:text-slate-300 font-medium">
                  {profile.averageRating ? profile.averageRating.toFixed(1) : 'No reviews'}
                </span>
                <span className="text-slate-400 dark:text-slate-500 text-sm">
                  ({profile.totalReviews} reviews)
                </span>
              </div>
            </div>
          </div>
          
          <div className="flex flex-col items-end gap-4 w-full md:w-auto">
            <div className="bg-slate-50 dark:bg-slate-700/50 rounded-xl p-4 flex gap-6 text-center border border-slate-100 dark:border-slate-600">
              <div>
                <p className="text-2xl font-bold text-slate-900 dark:text-white">{profile.trustScore}</p>
                <p className="text-xs text-slate-500 dark:text-slate-400 uppercase tracking-wider font-semibold">Trust Score</p>
              </div>
              <div className="w-px bg-slate-200 dark:bg-slate-600"></div>
              <div>
                <p className="text-2xl font-bold text-slate-900 dark:text-white">{profile.totalBooksSold}</p>
                <p className="text-xs text-slate-500 dark:text-slate-400 uppercase tracking-wider font-semibold">Books Sold</p>
              </div>
            </div>
            
            {!isSelf && (
              <button 
                onClick={() => {
                  if (!isAuthenticated) navigate('/login');
                  else setIsChatOpen(true);
                }}
                className="w-full md:w-auto px-6 py-2.5 bg-primary text-white font-medium rounded-lg hover:bg-primary/90 transition-colors flex items-center justify-center gap-2 shadow-sm"
              >
                <MessageSquare className="h-4 w-4" /> Contact Seller
              </button>
            )}
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* Active Listings Column */}
        <div className="lg:col-span-2">
          <h2 className="text-2xl font-heading font-bold text-slate-900 dark:text-white mb-6 flex items-center gap-2">
            <BookText className="h-6 w-6 text-primary" /> Active Listings ({profile.activeListings})
          </h2>
          
          {isLoadingBooks ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 animate-pulse">
              {[...Array(4)].map((_, i) => (
                <div key={i} className="bg-slate-200 dark:bg-slate-800 h-80 rounded-xl"></div>
              ))}
            </div>
          ) : !books || books.length === 0 ? (
            <div className="text-center py-12 bg-white dark:bg-slate-800 rounded-xl border border-slate-200 dark:border-slate-700">
              <BookText className="mx-auto h-12 w-12 text-slate-400 mb-4" />
              <h3 className="text-lg font-medium text-slate-900 dark:text-white">No active listings</h3>
              <p className="text-slate-500 dark:text-slate-400 mt-2">This seller doesn't have any books for sale right now.</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
              {books.map((book) => (
                <Link key={book.id} to={`/books/${book.id}`} className="group bg-white dark:bg-slate-800 rounded-xl border border-slate-200 dark:border-slate-700 overflow-hidden hover:shadow-md hover:border-primary/50 transition-all flex flex-col">
                  <div className="aspect-[4/3] bg-slate-100 dark:bg-slate-700 relative overflow-hidden shrink-0">
                    {book.images && book.images.length > 0 ? (
                      <img 
                        src={book.images[0].url} 
                        alt={book.title}
                        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                      />
                    ) : (
                      <div className="w-full h-full flex items-center justify-center text-slate-400">
                        <BookText className="h-12 w-12 opacity-50" />
                      </div>
                    )}
                    <div className="absolute top-2 right-2 bg-white/90 dark:bg-slate-900/90 backdrop-blur-sm px-2 py-1 rounded-md text-xs font-semibold text-slate-700 dark:text-slate-300 shadow-sm border border-slate-200/50 dark:border-slate-700/50">
                      {book.condition}
                    </div>
                  </div>
                  <div className="p-4 flex flex-col flex-1">
                    <h3 className="font-heading font-semibold text-lg text-slate-900 dark:text-white line-clamp-1 group-hover:text-primary transition-colors">
                      {book.title}
                    </h3>
                    <p className="text-sm text-slate-500 dark:text-slate-400 line-clamp-1 mb-4">
                      by {book.author}
                    </p>
                    
                    <div className="flex items-end justify-between mt-auto mb-4">
                      <div>
                        <p className="text-xs text-slate-500 dark:text-slate-400 line-through">
                          MRP: ₹{book.mrp}
                        </p>
                        <p className="text-xl font-bold text-slate-900 dark:text-white">
                          ₹{book.price}
                        </p>
                      </div>
                      <span className="text-xs font-medium bg-primary/10 text-primary px-2 py-1 rounded-md">
                        {Math.round(((book.mrp - book.price) / book.mrp) * 100)}% OFF
                      </span>
                    </div>

                    {!isSelf && (
                      isInCart(book.id) ? (
                        <button
                          onClick={(e) => { e.preventDefault(); navigate('/cart'); }}
                          className="w-full mt-2 flex items-center justify-center gap-2 py-2.5 bg-green-500 text-white font-medium rounded-lg hover:bg-green-600 transition-colors"
                        >
                          Go to Cart <ArrowRight className="h-4 w-4" />
                        </button>
                      ) : (
                        <button
                          onClick={(e) => handleAddToCart(e, book)}
                          disabled={addToCartMutation.isPending || (book.status !== 'ACTIVE' || book.quantity <= 0)}
                          className="w-full mt-2 flex items-center justify-center gap-2 py-2.5 bg-primary/10 text-primary font-medium rounded-lg hover:bg-primary hover:text-white transition-colors disabled:opacity-50"
                        >
                          {(addToCartMutation.isPending && addToCartMutation.variables === book.id) ? (
                            'Adding...'
                          ) : (
                            <>
                              <ShoppingCart className="h-4 w-4" /> Add to Cart
                            </>
                          )}
                        </button>
                      )
                    )}
                  </div>
                </Link>
              ))}
            </div>
          )}
        </div>

        {/* Reviews Column */}
        <div className="lg:col-span-1">
          <h2 className="text-2xl font-heading font-bold text-slate-900 dark:text-white mb-6">
            Reviews
          </h2>

          <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-200 dark:border-slate-700 overflow-hidden">
            {isLoadingReviews ? (
              <div className="p-6 space-y-4 animate-pulse">
                {[1, 2, 3].map(i => (
                  <div key={i} className="h-24 bg-slate-100 dark:bg-slate-700 rounded-xl"></div>
                ))}
              </div>
            ) : !reviewsData?.content?.length ? (
              <div className="p-8 text-center">
                <p className="text-slate-500 dark:text-slate-400">No reviews yet.</p>
              </div>
            ) : (
              <div className="divide-y divide-slate-100 dark:divide-slate-700/50">
                {reviewsData.content.map(review => (
                  <div key={review.id} className="p-6">
                    <div className="flex items-center justify-between mb-2">
                      <p className="font-semibold text-slate-900 dark:text-white">
                        {review.reviewerName}
                      </p>
                      <span className="text-xs text-slate-400">
                        {new Date(review.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                    <StarRating rating={review.rating} size={14} className="mb-3" />
                    <p className="text-slate-600 dark:text-slate-400 text-sm leading-relaxed">
                      {review.comment}
                    </p>
                  </div>
                ))}
                
                {reviewsData.totalPages > 1 && (
                  <div className="p-4 flex justify-between items-center bg-slate-50 dark:bg-slate-800/50">
                    <button
                      onClick={() => setReviewPage(p => Math.max(0, p - 1))}
                      disabled={reviewPage === 0}
                      className="px-3 py-1 text-sm bg-white dark:bg-slate-700 border border-slate-200 dark:border-slate-600 rounded disabled:opacity-50"
                    >
                      Prev
                    </button>
                    <span className="text-xs text-slate-500">
                      Page {reviewPage + 1} of {reviewsData.totalPages}
                    </span>
                    <button
                      onClick={() => setReviewPage(p => p + 1)}
                      disabled={reviewPage >= reviewsData.totalPages - 1}
                      className="px-3 py-1 text-sm bg-white dark:bg-slate-700 border border-slate-200 dark:border-slate-600 rounded disabled:opacity-50"
                    >
                      Next
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>

      </div>

      {isAuthenticated && !isSelf && (
        <ChatBox 
          bookId={null} // General chat
          receiverId={profile.id} 
          receiverName={profile.name}
          isOpen={isChatOpen} 
          onClose={() => setIsChatOpen(false)} 
        />
      )}
    </div>
  );
};

export default SellerProfile;
