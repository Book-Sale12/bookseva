import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useNavigate, useParams } from "react-router-dom";
import api from "../lib/api";
import { useAuth } from "../context/AuthContext";
import { ImagePlus, Info, X } from "lucide-react";

const MAX_FILE_SIZE = 5000000;
const ACCEPTED_IMAGE_TYPES = [
  "image/jpeg",
  "image/jpg",
  "image/png",
  "image/webp",
];

const editListingSchema = z.object({
  title: z
    .string()
    .min(3, "Title must be at least 3 characters")
    .max(150, "Title cannot exceed 150 characters"),
  author: z.string().min(2, "Author is required"),
  isbn: z.string().optional().nullable(),
  category: z.string().min(1, "Category is required"),
  condition: z.string().min(1, "Condition is required"),
  mrp: z.number().min(1, "MRP must be greater than 0"),
  price: z.number().min(1, "Price must be greater than 0"),
  description: z
    .string()
    .min(20, "Description must be at least 20 characters")
    .max(2000, "Description too long"),
  images: z
    .any()
    .optional()
    .refine(
      (files) =>
        !files || Array.from(files).every((file) => file.size <= MAX_FILE_SIZE),
      `Max file size is 5MB.`,
    )
    .refine(
      (files) =>
        !files || Array.from(files).every((file) =>
          ACCEPTED_IMAGE_TYPES.includes(file.type),
        ),
      "Only .jpg, .jpeg, .png and .webp formats are supported.",
    ),
});

const EditListing = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();
  const [serverError, setServerError] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  
  const [existingImages, setExistingImages] = useState([]);
  const [newImagePreviews, setNewImagePreviews] = useState([]);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    watch,
    reset,
  } = useForm({
    resolver: zodResolver(editListingSchema),
  });

  useEffect(() => {
    const fetchListing = async () => {
      try {
        const res = await api.get(`/books/${id}`);
        const book = res.data.data;
        
        if (user && book.seller.id !== user.id) {
          navigate("/"); // unauthorized
          return;
        }

        reset({
          title: book.title,
          author: book.author,
          isbn: book.isbn || "",
          category: book.category,
          condition: book.condition,
          mrp: book.mrp,
          price: book.price,
          description: book.description,
        });

        if (book.images) {
          setExistingImages(book.images.map(img => img.url));
        }

        setIsLoading(false);
      } catch (err) {
        setServerError("Failed to load listing details.");
        setIsLoading(false);
      }
    };
    
    if (isAuthenticated) {
      fetchListing();
    }
  }, [id, isAuthenticated, reset, navigate, user]);

  const watchImages = watch("images");

  useEffect(() => {
    if (watchImages && watchImages.length > 0) {
      const newPreviews = Array.from(watchImages).map((file) =>
        URL.createObjectURL(file),
      );
      setNewImagePreviews(newPreviews);

      return () => {
        newPreviews.forEach((url) => URL.revokeObjectURL(url));
      };
    } else {
      setNewImagePreviews([]);
    }
  }, [watchImages]);

  const watchCondition = watch("condition");
  const watchMrp = watch("mrp");

  const getSuggestedPrice = (condition, mrp) => {
    if (!condition || !mrp) return null;
    switch (condition) {
      case "EXCELLENT": return { min: mrp * 0.6, max: mrp * 0.75 };
      case "GOOD": return { min: mrp * 0.4, max: mrp * 0.6 };
      case "FAIR": return { min: mrp * 0.2, max: mrp * 0.4 };
      case "POOR": return { min: mrp * 0.05, max: mrp * 0.15 };
      case "DONATE": return { min: 0, max: 0 };
      default: return null;
    }
  };

  const suggestedPrice = getSuggestedPrice(watchCondition, watchMrp);

  const removeExistingImage = (indexToRemove) => {
    setExistingImages(prev => prev.filter((_, idx) => idx !== indexToRemove));
  };

  const onSubmit = async (data) => {
    try {
      setServerError("");
      
      const totalImages = existingImages.length + (data.images ? data.images.length : 0);
      if (totalImages < 1 || totalImages > 5) {
        setServerError("You must have between 1 and 5 images total.");
        return;
      }

      const formData = new FormData();

      if (data.images && data.images.length > 0) {
        Array.from(data.images).forEach((file) => {
          formData.append("images", file);
        });
      }

      const requestData = {
        title: data.title,
        author: data.author,
        isbn: data.isbn,
        category: data.category,
        condition: data.condition,
        mrp: data.mrp,
        price: data.price,
        description: data.description,
        keptImageUrls: existingImages,
      };

      formData.append(
        "data",
        new Blob([JSON.stringify(requestData)], { type: "application/json" })
      );

      await api.put(`/books/${id}`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      navigate(`/books/${id}`);
    } catch (err) {
      setServerError(
        err.response?.data?.error?.message ||
        err.response?.data?.message ||
        "Failed to update listing. Please try again."
      );
    }
  };

  if (!isAuthenticated) {
    navigate("/login");
    return null;
  }

  if (isLoading) {
    return <div className="text-center py-12">Loading listing details...</div>;
  }

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

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="mb-8">
        <h1 className="text-3xl font-heading font-bold text-slate-900 dark:text-white">
          Edit Listing
        </h1>
        <p className="mt-2 text-slate-600 dark:text-slate-400">
          Update the details or photos for your book.
        </p>
      </div>

      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-8 bg-white dark:bg-slate-900 shadow-sm rounded-2xl border border-slate-200 dark:border-slate-800 p-8"
      >
        {serverError && (
          <div className="bg-red-50 text-red-600 p-4 rounded-xl text-sm border border-red-200">
            {serverError}
          </div>
        )}

        <div className="space-y-6">
          <h2 className="text-xl font-heading font-semibold text-slate-900 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">
            Book Details
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Book Title *
              </label>
              <input
                type="text"
                {...register("title")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
              {errors.title && <p className="mt-1 text-sm text-red-500">{errors.title.message}</p>}
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Author *
              </label>
              <input
                type="text"
                {...register("author")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
              {errors.author && <p className="mt-1 text-sm text-red-500">{errors.author.message}</p>}
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                ISBN (Optional)
              </label>
              <input
                type="text"
                {...register("isbn")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Category *
              </label>
              <select
                {...register("category")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              >
                <option value="">Select Category</option>
                {categories.map((category) => (
                  <option key={category.value} value={category.value}>
                    {category.label}
                  </option>
                ))}
              </select>
              {errors.category && <p className="mt-1 text-sm text-red-500">{errors.category.message}</p>}
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Condition *
              </label>
              <select
                {...register("condition")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              >
                <option value="">Select Condition</option>
                <option value="EXCELLENT">Excellent (Like new, no marks)</option>
                <option value="GOOD">Good (Minor wear, few highlights)</option>
                <option value="FAIR">Fair (Visible wear, some torn pages)</option>
                <option value="POOR">Poor (Heavy damage, for parts)</option>
                <option value="DONATE">Donate (Free)</option>
              </select>
              {errors.condition && <p className="mt-1 text-sm text-red-500">{errors.condition.message}</p>}
            </div>
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Description *
              </label>
              <textarea
                {...register("description")}
                rows="4"
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary resize-none"
              ></textarea>
              {errors.description && <p className="mt-1 text-sm text-red-500">{errors.description.message}</p>}
            </div>
          </div>
        </div>

        <div className="space-y-6 pt-6 border-t border-slate-100 dark:border-slate-800">
          <h2 className="text-xl font-heading font-semibold text-slate-900 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">
            Pricing
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Original MRP (₹) *
              </label>
              <input
                type="number"
                {...register("mrp", { valueAsNumber: true })}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
              {errors.mrp && <p className="mt-1 text-sm text-red-500">{errors.mrp.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Your Price (₹) *
              </label>
              <input
                type="number"
                {...register("price", { valueAsNumber: true })}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
              {errors.price && <p className="mt-1 text-sm text-red-500">{errors.price.message}</p>}
            </div>
          </div>

          {suggestedPrice && suggestedPrice.max > 0 && (
            <>
              <div className="bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 p-4 rounded-xl flex gap-3">
                <Info className="h-5 w-5 text-blue-500 shrink-0 mt-0.5" />
                <div>
                  <p className="text-sm text-blue-700 dark:text-blue-300 font-medium">
                    Suggested Price Range: ₹{Math.round(suggestedPrice.min)} - ₹{Math.round(suggestedPrice.max)}
                  </p>
                  <p className="text-xs text-blue-600/80 dark:text-blue-400 mt-1">
                    Based on the book's MRP and condition.
                  </p>
                </div>
              </div>
            </>
          )}
        </div>

        <div className="space-y-6 pt-6 border-t border-slate-100 dark:border-slate-800">
          <h2 className="text-xl font-heading font-semibold text-slate-900 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">
            Photos
          </h2>
          <p className="text-sm text-slate-500 dark:text-slate-400">
            Keep existing photos or add new ones. Total images must be between 1 and 5.
          </p>

          {existingImages.length > 0 && (
            <div className="mb-4">
              <h3 className="text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Existing Photos</h3>
              <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-4">
                {existingImages.map((url, i) => (
                  <div key={`existing-${i}`} className="relative aspect-square rounded-xl overflow-hidden border border-slate-200 shadow-sm">
                    <img src={url} alt={`Existing ${i + 1}`} className="w-full h-full object-cover" />
                    <button
                      type="button"
                      onClick={() => removeExistingImage(i)}
                      className="absolute top-1 right-1 bg-red-500 text-white p-1 rounded-full hover:bg-red-600 transition-colors"
                    >
                      <X className="h-4 w-4" />
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}

          <div className="mt-4">
             <h3 className="text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Add New Photos</h3>
            <div className="flex items-center justify-center w-full">
              <label className="flex flex-col items-center justify-center w-full h-32 border-2 border-slate-300 dark:border-slate-700 border-dashed rounded-xl cursor-pointer bg-slate-50 dark:bg-slate-800/50 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors">
                <div className="flex flex-col items-center justify-center pt-5 pb-6">
                  <ImagePlus className="w-8 h-8 text-slate-400 mb-2" />
                  <p className="mb-1 text-sm text-slate-500 dark:text-slate-400">
                    <span className="font-semibold">Click to add new images</span>
                  </p>
                </div>
                <input
                  type="file"
                  multiple
                  accept="image/jpeg,image/png,image/webp"
                  className="hidden"
                  {...register("images")}
                />
              </label>
            </div>
            {errors.images && <p className="mt-2 text-sm text-red-500">{errors.images.message}</p>}
          </div>

          {newImagePreviews.length > 0 && (
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-4 mt-4">
              {newImagePreviews.map((url, i) => (
                <div key={`new-${i}`} className="relative aspect-square rounded-xl overflow-hidden border border-blue-200 shadow-sm">
                  <img src={url} alt={`Preview ${i + 1}`} className="w-full h-full object-cover" />
                  <div className="absolute bottom-0 left-0 right-0 bg-blue-500/80 text-white text-xs text-center py-1">New</div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="pt-6">
          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full py-4 bg-primary text-white font-medium rounded-xl hover:bg-primary/90 transition-colors shadow-md shadow-primary/20 hover:shadow-lg disabled:opacity-70 text-lg"
          >
            {isSubmitting ? "Saving Changes..." : "Save Changes"}
          </button>
        </div>
      </form>
    </div>
  );
};

export default EditListing;
