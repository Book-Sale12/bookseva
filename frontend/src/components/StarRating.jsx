import React from 'react';
import { Star } from 'lucide-react';

const StarRating = ({ rating, size = 16, className = "" }) => {
  // Ensure rating is between 0 and 5
  const clampedRating = Math.max(0, Math.min(5, Number(rating) || 0));
  
  return (
    <div className={`flex items-center gap-0.5 ${className}`} title={`${clampedRating} out of 5`}>
      {[1, 2, 3, 4, 5].map((index) => {
        const fillPercentage = Math.max(0, Math.min(1, clampedRating - index + 1)) * 100;
        
        return (
          <div key={index} className="relative inline-block" style={{ width: size, height: size }}>
            {/* Background (Empty) Star */}
            <Star 
              className="absolute inset-0 text-slate-200 dark:text-slate-700" 
              size={size} 
              strokeWidth={2.5}
            />
            {/* Foreground (Filled) Star with Clip Path for partial fill */}
            <div 
              className="absolute inset-0 overflow-hidden" 
              style={{ width: `${fillPercentage}%` }}
            >
              <Star 
                className="text-amber-400 fill-amber-400" 
                size={size}
                strokeWidth={2.5}
              />
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default StarRating;
