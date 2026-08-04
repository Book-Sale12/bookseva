import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Link, useNavigate } from 'react-router-dom';
import api from '../lib/api';
import { Search, Filter, BookText, MapPin, Tag, ShoppingCart, ArrowRight } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const fetchBooks = async ({ queryKey }) => {
  const [_key, params] = queryKey;
  const cleanParams = Object.fromEntries(
    Object.entries(params).filter(([_, v]) => v !== '')
  );
  // Point to the standard books endpoint and unwrap the standard {success, data} envelope
  const res = await api.get('/books', { params: cleanParams });
  return res.data.data;
};

const Home = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [searchTerm, setSearchTerm] = useState('');
  const [searchParams, setSearchParams] = useState({
    title: '',
    category: '',
    condition: '',
    minPrice: '',
    maxPrice: '',
    sortBy: 'NEWEST',
    myCampusOnly: isAuthenticated,
    page: 0,
    size: 20
  });

  const finalParams = {
    ...searchParams,
    collegeName: searchParams.myCampusOnly && user ? user.collegeName : '',
    courseBranch: searchParams.myCampusOnly && user ? user.courseBranch : ''
  };
  delete finalParams.myCampusOnly;

  const { data, isLoading, error } = useQuery({
    queryKey: ['books', finalParams],
    queryFn: fetchBooks,
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

  const handleSearch = (e) => {
    e.preventDefault();
    setSearchParams(prev => ({ ...prev, title: searchTerm, page: 0 }));
  };

  const addToCartMutation = useMutation({
    mutationFn: (bookId) => api.post('/cart/items', { bookId, quantity: 1 }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cart'] });
    },
  });

  const handleAddToCart = (e, book) => {
    e.preventDefault();
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    if (user?.id === book.seller?.id) {
        alert("You cannot add your own book to the cart.");
        return;
    }
    addToCartMutation.mutate(book.id);
  };

  const handlePageChange = (newPage) => {
    setSearchParams(prev => ({ ...prev, page: newPage }));
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const categories = [
    { label: "Engineering", value: "ENGINEERING" },
    { label: "Medical", value: "MEDICAL" },
    { label: "Science", value: "SCIENCE" },
    { label: "Commerce", value: "COMMERCE" },
    { label: "Arts & Humanities", value: "ARTS_HUMANITIES" },
    { label: "Diploma", value: "DIPLOMA" },
    { label: "Law", value: "LAW" },
    { label: "Management (MBA/BBA)", value: "MANAGEMENT" },
    { label: "Computer Applications (BCA/MCA)", value: "COMPUTER_APPLICATIONS" },
    { label: "Other", value: "OTHER" },
  ];
  const conditions = ["EXCELLENT", "GOOD", "FAIR", "POOR", "DONATE"];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Search Header */}
      <div className="bg-primary/10 dark:bg-primary/5 rounded-2xl p-8 mb-8 text-center border border-primary/20">
        <h1 className="text-3xl font-heading font-bold text-slate-900 dark:text-white mb-4">
          Find Your Next Textbook
        </h1>
        <form onSubmit={handleSearch} className="max-w-2xl mx-auto flex gap-2">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 h-5 w-5" />
            <input
              type="text"
              placeholder="Search by title, author, or ISBN..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 focus:ring-2 focus:ring-primary focus:border-transparent dark:text-white shadow-sm"
            />
          </div>
          <button
            type="submit"
            className="px-6 py-3 bg-primary text-white font-medium rounded-xl hover:bg-primary/90 transition-colors shadow-sm"
          >
            Search
          </button>
        </form>
      </div>

      <div className="flex flex-col md:flex-row gap-8">
        {/* Filters Sidebar */}
        <div className="w-full md:w-64 shrink-0 space-y-6">
          <div className="bg-white dark:bg-slate-800 p-5 rounded-xl border border-slate-200 dark:border-slate-700 shadow-sm">
            <h3 className="font-heading font-semibold text-lg flex items-center gap-2 mb-4 text-slate-900 dark:text-white">
              <Filter className="h-5 w-5 text-primary" /> Filters
            </h3>
            
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Category</label>
                <select 
                  className="w-full rounded-md border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 px-3 py-2 text-sm dark:text-white focus:ring-primary"
                  value={searchParams.category}
                  onChange={(e) => setSearchParams(prev => ({...prev, category: e.target.value, page: 0}))}
                >
                  <option value="">All Categories</option>
                  {categories.map(c => <option key={c.value} value={c.value}>{c.label}</option>)}
                </select>
              </div>
              
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Condition</label>
                <select 
                  className="w-full rounded-md border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 px-3 py-2 text-sm dark:text-white focus:ring-primary"
                  value={searchParams.condition}
                  onChange={(e) => setSearchParams(prev => ({...prev, condition: e.target.value, page: 0}))}
                >
                  <option value="">All Conditions</option>
                  {conditions.map(c => <option key={c} value={c}>{c.charAt(0) + c.slice(1).toLowerCase()}</option>)}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Price Range</label>
                <div className="flex items-center gap-2">
                  <input
                    type="number"
                    placeholder="Min"
                    className="w-full rounded-md border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 px-3 py-2 text-sm dark:text-white focus:ring-primary"
                    value={searchParams.minPrice}
                    onChange={(e) => setSearchParams(prev => ({...prev, minPrice: e.target.value, page: 0}))}
                  />
                  <span className="text-slate-500">-</span>
                  <input
                    type="number"
                    placeholder="Max"
                    className="w-full rounded-md border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 px-3 py-2 text-sm dark:text-white focus:ring-primary"
                    value={searchParams.maxPrice}
                    onChange={(e) => setSearchParams(prev => ({...prev, maxPrice: e.target.value, page: 0}))}
                  />
                </div>
              </div>
              
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Sort By</label>
                <select 
                  className="w-full rounded-md border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 px-3 py-2 text-sm dark:text-white focus:ring-primary"
                  value={searchParams.sortBy}
                  onChange={(e) => setSearchParams(prev => ({...prev, sortBy: e.target.value, page: 0}))}
                >
                  <option value="NEWEST">Newest First</option>
                  <option value="PRICE_ASC">Price: Low to High</option>
                  <option value="PRICE_DESC">Price: High to Low</option>
                  <option value="CONDITION">Condition</option>
                </select>
              </div>

              {isAuthenticated && (
                <div className="flex items-center gap-2 pt-2">
                  <input
                    type="checkbox"
                    id="myCampusOnly"
                    checked={searchParams.myCampusOnly}
                    onChange={(e) => setSearchParams(prev => ({...prev, myCampusOnly: e.target.checked, page: 0}))}
                    className="rounded border-slate-300 text-primary focus:ring-primary dark:border-slate-600 dark:bg-slate-700"
                  />
                  <label htmlFor="myCampusOnly" className="text-sm font-medium text-slate-700 dark:text-slate-300 cursor-pointer">
                    Show from my college & branch
                  </label>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Results Grid */}
        <div className="flex-1">
          {isLoading ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 animate-pulse">
              {[...Array(6)].map((_, i) => (
                <div key={i} className="bg-slate-200 dark:bg-slate-800 h-80 rounded-xl"></div>
              ))}
            </div>
          ) : error ? (
            <div className="text-center py-12 text-red-500">
              Failed to load books. Please try again later.
            </div>
          ) : data?.content?.length === 0 ? (
            <div className="text-center py-12 bg-white dark:bg-slate-800 rounded-xl border border-slate-200 dark:border-slate-700">
              <BookText className="mx-auto h-12 w-12 text-slate-400 mb-4" />
              <h3 className="text-lg font-medium text-slate-900 dark:text-white">No books found</h3>
              <p className="text-slate-500 dark:text-slate-400 mt-2">Try adjusting your filters or search term.</p>
            </div>
          ) : (
            <div className="space-y-8">
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {data?.content?.map((book) => (
                  <Link key={book.id} to={`/books/${book.id}`} className="group bg-white dark:bg-slate-800 rounded-xl border border-slate-200 dark:border-slate-700 overflow-hidden hover:shadow-md hover:border-primary/50 transition-all">
                    <div className="aspect-[4/3] bg-slate-100 dark:bg-slate-700 relative overflow-hidden">
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
                    <div className="p-4">
                      <h3 className="font-heading font-semibold text-lg text-slate-900 dark:text-white line-clamp-1 group-hover:text-primary transition-colors">
                        {book.title}
                      </h3>
                      <p className="text-sm text-slate-500 dark:text-slate-400 line-clamp-1 mb-3">
                        by {book.author}
                      </p>
                      
                      <div className="flex items-center gap-1 text-xs text-slate-500 dark:text-slate-400 mb-4 line-clamp-1">
                        <MapPin className="h-3.5 w-3.5" />
                        {book.seller?.collegeName || 'Verified Student'}
                      </div>

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

                      {isInCart(book.id) ? (
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
                          className="w-full mt-2 flex items-center justify-center gap-2 py-2.5 bg-primary/10 text-primary font-medium rounded-lg hover:bg-primary hover:text-white transition-colors disabled:opacity-50 disabled:hover:bg-primary/10 disabled:hover:text-primary"
                        >
                          {(addToCartMutation.isPending && addToCartMutation.variables === book.id) ? (
                            'Adding...'
                          ) : (
                            <>
                              <ShoppingCart className="h-4 w-4" /> Add to Cart
                            </>
                          )}
                        </button>
                      )}
                    </div>
                  </Link>
                ))}
              </div>

              {/* Pagination Controls */}
              {data?.totalPages > 1 && (
                <div className="flex items-center justify-between border-t border-slate-200 dark:border-slate-700 pt-6 mt-8">
                  <button
                    onClick={() => handlePageChange(data.number - 1)}
                    disabled={data.number === 0}
                    className="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed dark:bg-slate-800 dark:border-slate-600 dark:text-slate-300 dark:hover:bg-slate-700 transition-colors"
                  >
                    Previous
                  </button>
                  <span className="text-sm text-slate-600 dark:text-slate-400">
                    Page <span className="font-medium text-slate-900 dark:text-white">{data.number + 1}</span> of <span className="font-medium text-slate-900 dark:text-white">{data.totalPages}</span>
                  </span>
                  <button
                    onClick={() => handlePageChange(data.number + 1)}
                    disabled={data.number >= data.totalPages - 1}
                    className="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed dark:bg-slate-800 dark:border-slate-600 dark:text-slate-300 dark:hover:bg-slate-700 transition-colors"
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
  );
};

export default Home;
