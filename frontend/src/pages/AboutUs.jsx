import { Link } from 'react-router-dom';
import { ShoppingCart, Tag, Search, GraduationCap, Globe, Users, ArrowRight, ArrowDownToLine, TrendingUp } from 'lucide-react';

const AboutUs = () => {
  return (
    <div className="bg-slate-50 dark:bg-slate-900 min-h-screen text-slate-800 dark:text-slate-200 font-sans selection:bg-primary/20 selection:text-primary">
      
      {/* Section 1 — Hero/Intro */}
      <section className="relative overflow-hidden bg-white dark:bg-slate-950 py-24 sm:py-32 flex flex-col items-center justify-center text-center px-4 sm:px-6 lg:px-8 border-b border-slate-100 dark:border-slate-800">
        <div className="absolute inset-0 bg-grid-slate-100/[0.05] dark:bg-grid-slate-700/[0.05] bg-[center_top_-1px] border-b border-slate-100 dark:border-slate-800"></div>
        <div className="relative max-w-4xl mx-auto space-y-8">
          <h1 className="text-4xl sm:text-5xl md:text-7xl font-heading font-extrabold tracking-tight text-slate-900 dark:text-white leading-[1.1]">
            Books Should Open Doors, <span className="text-primary bg-clip-text text-transparent bg-gradient-to-r from-primary to-purple-500">Not Close Them</span>
          </h1>
          <p className="text-lg sm:text-2xl text-slate-600 dark:text-slate-400 max-w-3xl mx-auto leading-relaxed font-medium">
            BookSeva: A peer-to-peer marketplace making quality education affordable for every Indian student.
          </p>
        </div>
      </section>

      {/* Section 2 — The Real Cost of Education in India */}
      <section className="py-20 sm:py-28 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
        <div className="mb-12 text-center">
          <h2 className="text-sm font-semibold tracking-wide uppercase text-primary mb-3">The Reality</h2>
          <h3 className="text-3xl sm:text-4xl font-heading font-bold text-slate-900 dark:text-white">The Real Cost of Education in India</h3>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="bg-white dark:bg-slate-800 p-8 rounded-3xl shadow-xl shadow-slate-200/50 dark:shadow-none border border-slate-100 dark:border-slate-700 hover:-translate-y-1 transition-transform duration-300">
            <div className="text-5xl font-black text-slate-900 dark:text-white mb-4">97%</div>
            <h4 className="text-xl font-bold mb-2">Self-Funded</h4>
            <p className="text-slate-600 dark:text-slate-400">Of students are funded entirely by their own households, with no scholarships or government aid.</p>
          </div>
          <div className="bg-white dark:bg-slate-800 p-8 rounded-3xl shadow-xl shadow-slate-200/50 dark:shadow-none border border-slate-100 dark:border-slate-700 hover:-translate-y-1 transition-transform duration-300">
            <div className="text-5xl font-black text-slate-900 dark:text-white mb-4">₹2,002</div>
            <h4 className="text-xl font-bold mb-2">Per Student/Year</h4>
            <p className="text-slate-600 dark:text-slate-400">Spent purely on books & stationery at the school level across the country.</p>
          </div>
          <div className="bg-white dark:bg-slate-800 p-8 rounded-3xl shadow-xl shadow-slate-200/50 dark:shadow-none border border-slate-100 dark:border-slate-700 hover:-translate-y-1 transition-transform duration-300">
            <div className="text-5xl font-black text-slate-900 dark:text-white mb-4">8x</div>
            <h4 className="text-xl font-bold mb-2">The Gap</h4>
            <p className="text-slate-600 dark:text-slate-400">Private school students spend 8 times more than government school students on books.</p>
          </div>
        </div>
      </section>

      {/* Section 3 — The Book Problem Nobody Talks About */}
      <section className="py-20 sm:py-28 bg-white dark:bg-slate-950 border-y border-slate-100 dark:border-slate-800 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="mb-16">
            <h2 className="text-sm font-semibold tracking-wide uppercase text-primary mb-3">The Problem</h2>
            <h3 className="text-3xl sm:text-4xl font-heading font-bold text-slate-900 dark:text-white max-w-2xl">The Book Problem Nobody Talks About</h3>
          </div>

          <div className="space-y-12">
            <div className="flex flex-col md:flex-row gap-8 items-center bg-slate-50 dark:bg-slate-900 p-8 rounded-3xl border border-slate-100 dark:border-slate-800">
              <div className="flex-shrink-0 h-24 w-24 bg-red-100 dark:bg-red-900/30 text-red-600 dark:text-red-400 rounded-2xl flex items-center justify-center">
                <ArrowDownToLine className="w-10 h-10" />
              </div>
              <div>
                <h4 className="text-2xl font-bold text-slate-900 dark:text-white mb-2">JEE/NEET Aspirants</h4>
                <p className="text-lg text-slate-600 dark:text-slate-400">Spend ₹10,000–12,000/year on prep books. These books are used heavily for one exam cycle, then abandoned.</p>
              </div>
            </div>

            <div className="flex flex-col md:flex-row-reverse gap-8 items-center bg-slate-50 dark:bg-slate-900 p-8 rounded-3xl border border-slate-100 dark:border-slate-800">
              <div className="flex-shrink-0 h-24 w-24 bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 rounded-2xl flex items-center justify-center">
                <TrendingUp className="w-10 h-10" />
              </div>
              <div className="md:text-right">
                <h4 className="text-2xl font-bold text-slate-900 dark:text-white mb-2">Engineering Students</h4>
                <p className="text-lg text-slate-600 dark:text-slate-400">Spend ₹500–2,000 on books bought repeatedly every semester as editions change slightly each year.</p>
              </div>
            </div>

            <div className="flex flex-col md:flex-row gap-8 items-center bg-slate-50 dark:bg-slate-900 p-8 rounded-3xl border border-slate-100 dark:border-slate-800">
              <div className="flex-shrink-0 h-24 w-24 bg-purple-100 dark:bg-purple-900/30 text-purple-600 dark:text-purple-400 rounded-2xl flex items-center justify-center">
                <Users className="w-10 h-10" />
              </div>
              <div>
                <h4 className="text-2xl font-bold text-slate-900 dark:text-white mb-2">The Trust Gap</h4>
                <p className="text-lg text-slate-600 dark:text-slate-400">Used books carry the exact same value at a fraction of the price, yet buying & selling remains unorganised, localized, and hard to trust.</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Section 4 — What BookSeva Does */}
      <section className="py-20 sm:py-28 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
        <div className="mb-16 text-center">
          <h2 className="text-sm font-semibold tracking-wide uppercase text-primary mb-3">The Solution</h2>
          <h3 className="text-3xl sm:text-4xl font-heading font-bold text-slate-900 dark:text-white">What BookSeva Does</h3>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="bg-white dark:bg-slate-800 p-10 rounded-[2rem] shadow-xl shadow-slate-200/50 dark:shadow-none border border-slate-100 dark:border-slate-700 flex flex-col items-center text-center">
            <div className="w-20 h-20 bg-primary/10 text-primary rounded-full flex items-center justify-center mb-6">
              <ShoppingCart className="w-8 h-8" />
            </div>
            <h4 className="text-2xl font-bold text-slate-900 dark:text-white mb-4">Buy</h4>
            <p className="text-slate-600 dark:text-slate-400">Used textbooks at 50–75% lower prices. Filter easily by class, board, stream, or specific course.</p>
          </div>
          
          <div className="bg-white dark:bg-slate-800 p-10 rounded-[2rem] shadow-xl shadow-slate-200/50 dark:shadow-none border border-slate-100 dark:border-slate-700 flex flex-col items-center text-center">
            <div className="w-20 h-20 bg-primary/10 text-primary rounded-full flex items-center justify-center mb-6">
              <Tag className="w-8 h-8" />
            </div>
            <h4 className="text-2xl font-bold text-slate-900 dark:text-white mb-4">Sell</h4>
            <p className="text-slate-600 dark:text-slate-400">Pass on outgrown books instead of letting them gather dust on a shelf, and earn back what was spent.</p>
          </div>

          <div className="bg-white dark:bg-slate-800 p-10 rounded-[2rem] shadow-xl shadow-slate-200/50 dark:shadow-none border border-slate-100 dark:border-slate-700 flex flex-col items-center text-center">
            <div className="w-20 h-20 bg-primary/10 text-primary rounded-full flex items-center justify-center mb-6">
              <Search className="w-8 h-8" />
            </div>
            <h4 className="text-2xl font-bold text-slate-900 dark:text-white mb-4">Find</h4>
            <p className="text-slate-600 dark:text-slate-400">Exactly what's needed, when it's needed. No irrelevant listings, no scams, and no wasted time.</p>
          </div>
        </div>
      </section>

      {/* Section 5 — Real Savings for Real Students */}
      <section className="py-20 sm:py-28 bg-slate-900 dark:bg-black text-white px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="mb-16 md:text-center">
            <h2 className="text-sm font-semibold tracking-wide uppercase text-primary mb-3">The Impact</h2>
            <h3 className="text-3xl sm:text-4xl font-heading font-bold">Real Savings for Real Students</h3>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-slate-800/50 border border-slate-700 p-8 rounded-3xl backdrop-blur-sm">
              <h4 className="text-lg font-medium text-slate-400 mb-6">Class 10 CBSE</h4>
              <div className="space-y-4">
                <div>
                  <div className="flex justify-between text-sm mb-1 text-slate-400"><span>Original Cost</span> <span>₹3,000</span></div>
                  <div className="h-2 w-full bg-slate-700 rounded-full overflow-hidden"><div className="h-full bg-slate-500 w-full"></div></div>
                </div>
                <div>
                  <div className="flex justify-between text-sm mb-1 text-white font-medium"><span>BookSeva Price</span> <span>₹1,200</span></div>
                  <div className="h-2 w-full bg-slate-700 rounded-full overflow-hidden"><div className="h-full bg-primary w-[40%]"></div></div>
                </div>
              </div>
              <div className="mt-6 pt-6 border-t border-slate-700">
                <span className="text-green-400 font-bold text-xl">Save ₹1,800</span>
              </div>
            </div>

            <div className="bg-slate-800/50 border border-slate-700 p-8 rounded-3xl backdrop-blur-sm relative overflow-hidden">
              <div className="absolute top-0 right-0 bg-primary text-white text-xs font-bold px-3 py-1 rounded-bl-lg">Highest Impact</div>
              <h4 className="text-lg font-medium text-slate-400 mb-6">JEE Aspirant</h4>
              <div className="space-y-4">
                <div>
                  <div className="flex justify-between text-sm mb-1 text-slate-400"><span>Original Cost</span> <span>₹12,000</span></div>
                  <div className="h-2 w-full bg-slate-700 rounded-full overflow-hidden"><div className="h-full bg-slate-500 w-full"></div></div>
                </div>
                <div>
                  <div className="flex justify-between text-sm mb-1 text-white font-medium"><span>BookSeva Price</span> <span>₹4,500</span></div>
                  <div className="h-2 w-full bg-slate-700 rounded-full overflow-hidden"><div className="h-full bg-primary w-[37.5%]"></div></div>
                </div>
              </div>
              <div className="mt-6 pt-6 border-t border-slate-700">
                <span className="text-green-400 font-bold text-xl">Save ₹7,500</span>
              </div>
            </div>

            <div className="bg-slate-800/50 border border-slate-700 p-8 rounded-3xl backdrop-blur-sm">
              <h4 className="text-lg font-medium text-slate-400 mb-6">Engineering Student</h4>
              <div className="space-y-4">
                <div>
                  <div className="flex justify-between text-sm mb-1 text-slate-400"><span>Original Cost</span> <span>₹8,000</span></div>
                  <div className="h-2 w-full bg-slate-700 rounded-full overflow-hidden"><div className="h-full bg-slate-500 w-full"></div></div>
                </div>
                <div>
                  <div className="flex justify-between text-sm mb-1 text-white font-medium"><span>BookSeva Price</span> <span>₹3,000</span></div>
                  <div className="h-2 w-full bg-slate-700 rounded-full overflow-hidden"><div className="h-full bg-primary w-[37.5%]"></div></div>
                </div>
              </div>
              <div className="mt-6 pt-6 border-t border-slate-700">
                <span className="text-green-400 font-bold text-xl">Save ₹5,000</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Section 6 — Why This Matters Beyond Money */}
      <section className="py-20 sm:py-28 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
        <div className="mb-16 text-center">
          <h2 className="text-sm font-semibold tracking-wide uppercase text-primary mb-3">The Big Picture</h2>
          <h3 className="text-3xl sm:text-4xl font-heading font-bold text-slate-900 dark:text-white">Why This Matters Beyond Money</h3>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="flex flex-col gap-4">
            <div className="w-16 h-16 bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 rounded-2xl flex items-center justify-center">
              <GraduationCap className="w-8 h-8" />
            </div>
            <h4 className="text-xl font-bold text-slate-900 dark:text-white">Keeps Students in School</h4>
            <p className="text-slate-600 dark:text-slate-400">Lower book costs mean families don't have to choose between subjects or siblings. Access to materials directly impacts completion rates.</p>
          </div>
          
          <div className="flex flex-col gap-4">
            <div className="w-16 h-16 bg-green-100 dark:bg-green-900/30 text-green-600 dark:text-green-400 rounded-2xl flex items-center justify-center">
              <Globe className="w-8 h-8" />
            </div>
            <h4 className="text-xl font-bold text-slate-900 dark:text-white">Better for the Planet</h4>
            <p className="text-slate-600 dark:text-slate-400">Every reused book means one less tree cut for paper production and one less book thrown into a landfill at the end of a semester.</p>
          </div>

          <div className="flex flex-col gap-4">
            <div className="w-16 h-16 bg-orange-100 dark:bg-orange-900/30 text-orange-600 dark:text-orange-400 rounded-2xl flex items-center justify-center">
              <Users className="w-8 h-8" />
            </div>
            <h4 className="text-xl font-bold text-slate-900 dark:text-white">Builds Community</h4>
            <p className="text-slate-600 dark:text-slate-400">It's seniors helping juniors. It's an organised and trustworthy ecosystem where knowledge is literally passed forward to the next generation.</p>
          </div>
        </div>
      </section>

      {/* Section 7 — Our Mission */}
      <section className="py-24 sm:py-32 px-4 sm:px-6 lg:px-8 bg-gradient-to-br from-primary/10 via-transparent to-purple-500/10 dark:from-primary/20 dark:to-purple-900/20">
        <div className="max-w-4xl mx-auto text-center space-y-8">
          <h2 className="text-3xl sm:text-5xl md:text-6xl font-heading font-extrabold text-slate-900 dark:text-white leading-tight">
            "No student in India ever skips a book, a subject, or a year of study because of what it costs."
          </h2>
          <p className="text-lg sm:text-2xl font-medium text-slate-600 dark:text-slate-400">
            That is the promise behind every listing, every transaction, and every connection made on BookSeva.
          </p>
        </div>
      </section>

      {/* Section 8 — Join the Movement / CTA */}
      <section className="py-20 sm:py-28 px-4 sm:px-6 lg:px-8 bg-white dark:bg-slate-950 border-t border-slate-100 dark:border-slate-800">
        <div className="max-w-4xl mx-auto text-center space-y-12">
          <div>
            <h2 className="text-4xl sm:text-5xl font-heading font-bold text-slate-900 dark:text-white mb-4">Join the Movement</h2>
            <p className="text-xl text-slate-600 dark:text-slate-400">Buy smarter. Sell what you've outgrown. Help a junior stay in school.</p>
            <p className="mt-4 inline-block px-4 py-2 bg-slate-100 dark:bg-slate-800 rounded-full text-sm font-semibold text-primary">Every book on BookSeva teaches twice, three times, or more.</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 text-left">
            <div className="p-6 bg-slate-50 dark:bg-slate-900 rounded-2xl border border-slate-100 dark:border-slate-800">
              <h4 className="text-lg font-bold text-slate-900 dark:text-white mb-2"><span className="text-primary mr-2">1.</span>Buy a Book</h4>
              <p className="text-sm text-slate-600 dark:text-slate-400">Save up to 75% on the books you need today.</p>
            </div>
            <div className="p-6 bg-slate-50 dark:bg-slate-900 rounded-2xl border border-slate-100 dark:border-slate-800">
              <h4 className="text-lg font-bold text-slate-900 dark:text-white mb-2"><span className="text-primary mr-2">2.</span>Sell a Book</h4>
              <p className="text-sm text-slate-600 dark:text-slate-400">Turn your outgrown books into someone else's opportunity.</p>
            </div>
            <div className="p-6 bg-slate-50 dark:bg-slate-900 rounded-2xl border border-slate-100 dark:border-slate-800">
              <h4 className="text-lg font-bold text-slate-900 dark:text-white mb-2"><span className="text-primary mr-2">3.</span>Grow Together</h4>
              <p className="text-sm text-slate-600 dark:text-slate-400">Be part of a community that keeps students in school.</p>
            </div>
          </div>

          <div className="flex flex-col sm:flex-row gap-4 justify-center pt-8">
            <Link to="/" className="inline-flex items-center justify-center px-8 py-4 text-base font-semibold text-white bg-primary hover:bg-primary/90 rounded-xl transition-colors shadow-lg shadow-primary/20">
              Browse Books <ArrowRight className="ml-2 w-5 h-5" />
            </Link>
            <Link to="/sell" className="inline-flex items-center justify-center px-8 py-4 text-base font-semibold text-slate-700 dark:text-slate-200 bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 rounded-xl transition-colors">
              Sell a Book
            </Link>
          </div>
        </div>
      </section>

    </div>
  );
};

export default AboutUs;
