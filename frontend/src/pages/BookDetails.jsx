import { useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';
import api from '../lib/api';
import { useAuth } from '../context/AuthContext';
import { BookText, ShoppingCart, ArrowLeft, MapPin, ShieldCheck, Tag, MessageSquare, Edit, Flag, ArrowRight } from 'lucide-react';
import ChatBox from '../components/ChatBox';
import ReportModal from '../components/ReportModal';
import StarRating from '../components/StarRating';

const fetchBook = async (id) => {
  const res = await api.get(`/books/${id}`);
  return res.data;
};

const BookDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const [activeImage, setActiveImage] = useState(0);
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [isReportModalOpen, setIsReportModalOpen] = useState(false);
  const [reportConfig, setReportConfig] = useState({ targetType: '', targetId: null });

  const openReportModal = (targetType, targetId) => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    setReportConfig({ targetType, targetId });
    setIsReportModalOpen(true);
  };

  const { data, isLoading, error } = useQuery({
    queryKey: ['book', id],
    queryFn: () => fetchBook(id),
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

  const { data: sellerProfile } = useQuery({
    queryKey: ['sellerProfile', data?.data?.seller?.id],
    queryFn: async () => {
      const res = await api.get(`/sellers/${data.data.seller.id}`);
      return res.data.data;
    },
    enabled: !!data?.data?.seller?.id,
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

  if (isLoading) {
    return <div className="min-h-screen flex items-center justify-center">{t('bookDetails.loading')}</div>;
  }

  if (error || !data?.data) {
    return (
      <div className="min-h-screen flex items-center justify-center flex-col gap-4">
        <p className="text-red-500">{t('bookDetails.failedToLoad')}</p>
        <Link to="/" className="text-primary hover:underline">{t('bookDetails.returnHome')}</Link>
      </div>
    );
  }

  const book = data.data;
  const isSeller = user?.id === book.seller.id;
  const isAvailable = book.status === 'ACTIVE' && book.quantity > 0;

  return (
    <>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Link to="/" className="inline-flex items-center gap-2 text-sm text-slate-500 hover:text-slate-900 dark:hover:text-white mb-8 transition-colors">
          <ArrowLeft className="h-4 w-4" /> Back to browse
        </Link>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-12">
          {/* Image Gallery */}
          <div className="space-y-4">
            <div className="aspect-[4/3] bg-slate-100 dark:bg-slate-800 rounded-2xl overflow-hidden border border-slate-200 dark:border-slate-700 relative">
              {book.images && book.images.length > 0 ? (
                <img 
                  src={book.images[activeImage].url} 
                  alt={book.title}
                  className="w-full h-full object-contain"
                />
              ) : (
                <div className="w-full h-full flex items-center justify-center text-slate-400">
                  <BookText className="h-16 w-16 opacity-50" />
                </div>
              )}
              {!isAvailable && (
                <div className="absolute inset-0 bg-white/60 dark:bg-slate-900/60 backdrop-blur-sm flex items-center justify-center">
                  <span className="text-2xl font-heading font-bold text-slate-900 dark:text-white rotate-[-15deg] border-4 border-slate-900 dark:border-white px-6 py-2 rounded-lg">
                    {book.status === 'SOLD' ? 'SOLD OUT' : 'UNAVAILABLE'}
                  </span>
                </div>
              )}
            </div>
            
            {book.images && book.images.length > 1 && (
              <div className="grid grid-cols-5 gap-2">
                {book.images.map((img, index) => (
                  <button
                    key={img.id}
                    onClick={() => setActiveImage(index)}
                    className={`aspect-square rounded-lg overflow-hidden border-2 transition-all ${activeImage === index ? 'border-primary ring-2 ring-primary/20' : 'border-transparent hover:border-slate-300'}`}
                  >
                    <img src={img.url} alt={`Thumbnail ${index + 1}`} className="w-full h-full object-cover" />
                  </button>
                ))}
              </div>
            )}
          </div>

          {/* Book Details */}
          <div className="flex flex-col">
            <div className="mb-2 flex items-center gap-2">
              <span className="px-3 py-1 bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 text-xs font-semibold rounded-full border border-slate-200 dark:border-slate-700">
                {book.category}
              </span>
              <span className={`px-3 py-1 text-xs font-semibold rounded-full border ${
                book.condition === 'EXCELLENT' ? 'bg-green-50 text-green-700 border-green-200 dark:bg-green-900/30 dark:text-green-400' :
                book.condition === 'GOOD' ? 'bg-blue-50 text-blue-700 border-blue-200 dark:bg-blue-900/30 dark:text-blue-400' :
                'bg-orange-50 text-orange-700 border-orange-200 dark:bg-orange-900/30 dark:text-orange-400'
              }`}>
                Condition: {book.condition}
              </span>
            </div>
            
            <div className="flex items-start justify-between gap-4 mb-2">
              <h1 className="text-3xl sm:text-4xl font-heading font-bold text-slate-900 dark:text-white leading-tight">
                {book.title}
              </h1>
              {!isSeller && (
                <button 
                  onClick={() => openReportModal('BOOK', book.id)}
                  className="p-2 text-slate-400 hover:text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-full transition-colors flex-shrink-0"
                  title="Report this listing"
                >
                  <Flag className="h-5 w-5" />
                </button>
              )}
            </div>
            <p className="text-lg text-slate-600 dark:text-slate-400 mb-6">
              by <span className="font-medium text-slate-900 dark:text-slate-300">{book.author}</span>
            </p>

            <div className="bg-slate-50 dark:bg-slate-800/50 p-6 rounded-2xl border border-slate-100 dark:border-slate-800 mb-8 flex items-center justify-between">
              <div>
                <p className="text-sm text-slate-500 dark:text-slate-400 mb-1">Selling Price</p>
                <p className="text-4xl font-bold text-slate-900 dark:text-white">₹{book.price}</p>
              </div>
              <div className="text-right">
                <p className="text-sm text-slate-500 dark:text-slate-400 mb-1">Original Price</p>
                <p className="text-lg text-slate-400 dark:text-slate-500 line-through">₹{book.mrp}</p>
                <p className="text-sm font-medium text-green-600 dark:text-green-400 mt-1">
                  You save ₹{book.mrp - book.price} ({Math.round(((book.mrp - book.price) / book.mrp) * 100)}%)
                </p>
              </div>
            </div>

            <div className="space-y-6 mb-8 flex-1">
              <div>
                <h3 className="font-heading font-semibold text-lg text-slate-900 dark:text-white mb-2">Description</h3>
                <p className="text-slate-600 dark:text-slate-400 leading-relaxed whitespace-pre-wrap">
                  {book.description}
                </p>
              </div>

              {book.isbn && (
                <div>
                  <h3 className="font-heading font-semibold text-lg text-slate-900 dark:text-white mb-2 flex items-center gap-2">
                    <Tag className="h-4 w-4" /> ISBN
                  </h3>
                  <p className="text-slate-600 dark:text-slate-400 font-mono">{book.isbn}</p>
                </div>
              )}

              <div className="pt-6 border-t border-slate-200 dark:border-slate-800">
                <h3 className="font-heading font-semibold text-lg text-slate-900 dark:text-white mb-4">Seller Info</h3>
                <div className="flex items-center gap-4">
                  <Link to={`/seller/${book.seller.id}`} className="flex items-center gap-4 group flex-1">
                    <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center text-primary font-bold text-xl group-hover:bg-primary/20 transition-colors">
                      {book.seller.name.charAt(0)}
                    </div>
                    <div>
                      <p className="font-medium text-slate-900 dark:text-white flex items-center gap-2 group-hover:text-primary transition-colors">
                        {book.seller.name}
                        {book.seller.trustScore >= 90 && (
                          <ShieldCheck className="h-4 w-4 text-green-500" title="Highly Trusted" />
                        )}
                      </p>
                      {sellerProfile ? (
                        <div className="flex items-center gap-1.5 mt-0.5">
                          {sellerProfile.averageRating ? (
                            <>
                              <StarRating rating={sellerProfile.averageRating} size={14} />
                              <span className="text-sm font-medium text-slate-700 dark:text-slate-300">
                                {sellerProfile.averageRating.toFixed(1)}
                              </span>
                              <span className="text-xs text-slate-500 dark:text-slate-400">
                                ({sellerProfile.totalReviews} review{sellerProfile.totalReviews !== 1 ? 's' : ''})
                              </span>
                            </>
                          ) : (
                            <span className="text-sm text-slate-500 dark:text-slate-400">No reviews yet</span>
                          )}
                        </div>
                      ) : (
                        <div className="h-5 w-32 bg-slate-200 dark:bg-slate-700 animate-pulse rounded mt-1"></div>
                      )}
                    </div>
                  </Link>
                  {!isSeller && (
                    <button 
                      onClick={() => openReportModal('USER', book.seller.id)}
                      className="ml-auto text-xs text-slate-400 hover:text-red-500 flex items-center gap-1 transition-colors"
                    >
                      <Flag className="h-3 w-3" /> Report Seller
                    </button>
                  )}
                </div>
                
                <div className="flex items-center gap-2 mt-4">
                  <button 
                    onClick={() => {
                      if (!isAuthenticated) navigate('/login');
                      else setIsChatOpen(true);
                    }}
                    className="text-sm font-medium text-primary hover:text-primary/80 transition-colors flex items-center gap-1"
                  >
                    <MessageSquare className="h-4 w-4" /> {t('bookDetails.contactSeller')}
                  </button>
                </div>
              </div>
            </div>

            <div className="mt-auto">
              {!isAuthenticated ? (
                <button
                  onClick={() => navigate('/login')}
                  className="w-full py-4 bg-slate-900 dark:bg-white text-white dark:text-slate-900 font-medium rounded-xl hover:bg-slate-800 dark:hover:bg-slate-100 transition-colors shadow-sm"
                >
                  Log in to buy
                </button>
              ) : isSeller ? (
                <Link to={`/edit-listing/${book.id}`} className="w-full flex items-center justify-center gap-2 py-4 bg-slate-800 text-white font-medium rounded-xl hover:bg-slate-700 transition-colors shadow-sm">
                  <Edit className="h-5 w-5" /> {t('bookDetails.editListing')}
                </Link>
              ) : !isAvailable ? (
                <button disabled className="w-full py-4 bg-slate-200 dark:bg-slate-800 text-slate-500 dark:text-slate-400 font-medium rounded-xl cursor-not-allowed">
                  Out of Stock
                </button>
              ) : isInCart(book.id) ? (
                <button
                  onClick={() => navigate('/cart')}
                  className="w-full flex items-center justify-center gap-2 py-4 bg-green-500 text-white font-medium rounded-xl hover:bg-green-600 transition-colors shadow-sm"
                >
                  {t('home.goToCart')} <ArrowRight className="h-5 w-5" />
                </button>
              ) : (
                <button
                  onClick={() => addToCartMutation.mutate(book.id)}
                  disabled={addToCartMutation.isPending}
                  className="w-full flex items-center justify-center gap-2 py-4 bg-primary text-white font-medium rounded-xl hover:bg-primary/90 transition-colors shadow-md shadow-primary/20 hover:shadow-lg disabled:opacity-70"
                >
                  {addToCartMutation.isPending ? (
                    t('home.adding')
                  ) : (
                    <>
                      <ShoppingCart className="h-5 w-5" /> {t('bookDetails.addToCart')}
                    </>
                  )}
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
      
      {isAuthenticated && !isSeller && (
        <ChatBox 
          bookId={book.id} 
          receiverId={book.seller.id} 
          receiverName={book.seller.name}
          isOpen={isChatOpen} 
          onClose={() => setIsChatOpen(false)} 
        />
      )}
      <ReportModal 
        isOpen={isReportModalOpen}
        onClose={() => setIsReportModalOpen(false)}
        targetType={reportConfig.targetType}
        targetId={reportConfig.targetId}
      />
    </>
  );
};

export default BookDetails;
