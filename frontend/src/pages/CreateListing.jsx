import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import api from "../lib/api";
import { useAuth } from "../context/AuthContext";
import { AlertCircle, ImagePlus, Info, X, WifiOff } from "lucide-react";

const MAX_FILE_SIZE = 5000000;
const ACCEPTED_IMAGE_TYPES = [
  "image/jpeg",
  "image/jpg",
  "image/png",
  "image/webp",
];

const DESCRIPTION_MAX = 150;

const listingSchema = z.object({
  title: z
    .string()
    .min(2, "Book name must be at least 2 characters")
    .max(150, "Book name cannot exceed 150 characters")
    .regex(/^(?!\d+$).+$/, "Book name cannot be purely numeric"),
  author: z
    .string()
    .min(2, "Author name must be at least 2 characters")
    .regex(/^(?!\d+$).+$/, "Author cannot be purely numeric"),
  isbn: z.string().optional(),
  category: z.string().min(1, "Category is required"),
  condition: z.string().min(1, "Condition is required"),
  mrp: z.number().min(1, "MRP must be greater than 0"),
  price: z.number().min(1, "Price must be greater than 0"),
  description: z
    .string()
    .min(10, "Description must be at least 10 characters")
    .max(DESCRIPTION_MAX, `Description cannot exceed ${DESCRIPTION_MAX} characters`)
    .superRefine((val, ctx) => {
      if (/^\d+$/.test(val)) {
        ctx.addIssue({ code: z.ZodIssueCode.custom, message: "Description cannot consist of only numbers." });
        return;
      }
      if (/^[^a-zA-Z0-9\s]+$/.test(val)) {
        ctx.addIssue({ code: z.ZodIssueCode.custom, message: "Description cannot consist of only special characters." });
        return;
      }
      if (/(\d)\1{2,}/.test(val)) {
        ctx.addIssue({ code: z.ZodIssueCode.custom, message: "Description cannot contain repeated digits (e.g., \"111\", \"999\")." });
        return;
      }
      if (/([^a-zA-Z0-9\s])\1{2,}/.test(val)) {
        ctx.addIssue({ code: z.ZodIssueCode.custom, message: "Description cannot contain repeated special characters (e.g., \"...\", \"!!!\")." });
      }
    }),
  quantity: z.number().min(1).default(1),
  images: z
    .any()
    .refine((files) => files?.length >= 1, "At least 1 image is required.")
    .refine((files) => files?.length <= 5, "Maximum 5 images allowed.")
    .refine(
      (files) =>
        Array.from(files || []).every((file) => file.size <= MAX_FILE_SIZE),
      `Max file size is 5MB.`,
    )
    .refine(
      (files) =>
        Array.from(files || []).every((file) =>
          ACCEPTED_IMAGE_TYPES.includes(file.type),
        ),
      "Only .jpg, .jpeg, .png and .webp formats are supported.",
    ),
});

const CreateListing = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const { t } = useTranslation();
  // serverError holds { stage, message, errors } so we can show stage-specific UI.
  // We never clear the form on error — only update the error state.
  const [serverError, setServerError] = useState(null);
  const [isSubmittingForm, setIsSubmittingForm] = useState(false);
  const [photos, setPhotos] = useState([]);

  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    watch,
    setValue,
    setError,
  } = useForm({
    resolver: zodResolver(listingSchema),
    mode: "onChange",
    defaultValues: {
      quantity: 1,
    },
  });

  useEffect(() => {
    register("images");
  }, [register]);

  const handlePhotoChange = (e) => {
    const files = Array.from(e.target.files);
    if (photos.length + files.length > 5) {
      alert(t('sell.maxPhotos'));
      return;
    }
    setPhotos((prev) => {
      const newPhotos = [...prev, ...files];
      setValue("images", newPhotos, { shouldValidate: true });
      return newPhotos;
    });
    e.target.value = null;
  };

  const removePhoto = (index) => {
    setPhotos((prev) => {
      const newPhotos = prev.filter((_, i) => i !== index);
      setValue("images", newPhotos, { shouldValidate: true });
      return newPhotos;
    });
  };

  const watchCondition = watch("condition");
  const watchMrp = watch("mrp");
  const watchDescription = watch("description", "");

  const getSuggestedPrice = (condition, mrp) => {
    if (!condition || !mrp) return null;
    switch (condition) {
      case "EXCELLENT":
        return { min: mrp * 0.6, max: mrp * 0.75 };
      case "GOOD":
        return { min: mrp * 0.4, max: mrp * 0.6 };
      case "FAIR":
        return { min: mrp * 0.2, max: mrp * 0.4 };
      case "POOR":
        return { min: mrp * 0.05, max: mrp * 0.15 };
      // TODO: Donate feature disabled temporarily - not working, needs fix.
      // case "DONATE":
      //   return { min: 0, max: 0 };
      default:
        return null;
    }
  };

  const suggestedPrice = getSuggestedPrice(watchCondition, watchMrp);

  const onSubmit = async (data) => {
    setServerError(null);
    setIsSubmittingForm(true);
    try {
      const formData = new FormData();

      // Append files
      photos.forEach((file) => {
        formData.append("images", file);
      });

      // Append text data as JSON string
      const requestData = {
        title: data.title,
        author: data.author,
        isbn: data.isbn,
        category: data.category,
        condition: data.condition,
        mrp: data.mrp,
        price: data.price,
        description: data.description,
        quantity: data.quantity,
      };

      formData.append(
        "data",
        new Blob([JSON.stringify(requestData)], {
          type: "application/json",
        }),
      );

      const res = await api.post("/books", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      navigate(`/books/${res.data.data.id}`);
    } catch (err) {
      // --- Form data is NEVER reset on error. The user can simply fix the issue and retry. ---
      const responseData = err.response?.data;
      const stage = responseData?.stage;

      if (!err.response) {
        // Pure network error (no response received at all — connection dropped, timeout, etc.)
        setServerError({
          stage: "IMAGE_UPLOAD_FAILED",
          message: t('errors.networkError')
        });
      } else if (stage === "VALIDATION_FAILED" && responseData?.errors) {
        // Map each server-side field error into RHF so it shows inline under the relevant field.
        // This is a defense layer for Postman/API bypass; the Zod schema should catch most of these first.
        Object.entries(responseData.errors).forEach(([field, message]) => {
          if (field === "photos") {
            // "photos" is managed by the images RHF field
            setError("images", { type: "server", message });
          } else {
            setError(field, { type: "server", message });
          }
        });
        setServerError({
          stage,
          message: t('errors.fixHighlightedFields'),
        });
      } else if (stage === "IMAGE_UPLOAD_FAILED") {
        setServerError({
          stage,
          message:
            responseData?.message ||
            t('errors.uploadFailedMessage')
        });
      } else if (stage === "LISTING_SAVE_FAILED") {
        setServerError({
          stage,
          message:
            responseData?.message ||
            t('errors.saveFailedMessage')
        });
      } else {
        // Generic fallback (e.g. 401, 403, unexpected server error)
        setServerError({
          stage: "UNKNOWN",
          message:
            responseData?.error?.message ||
            responseData?.message ||
            t('errors.somethingWentWrong')
        });
      }
    } finally {
      setIsSubmittingForm(false);
    }
  };

  if (!isAuthenticated) {
    navigate("/login");
    return null;
  }

  // const categories = ["Engineering", "Medical", "Science", "Commerce", "Arts & Humanities", "Diploma", "Law", "Management (MBA/BBA)", "Computer Applications (BCA/MCA)", "Other"];

  // updated code added bcz of bug
  const categories = [
    { label: "Engineering", value: "ENGINEERING" },
    { label: "Medical", value: "MEDICAL" },
    { label: "Science", value: "SCIENCE" },
    { label: "Commerce", value: "COMMERCE" },
    { label: "Arts & Humanities", value: "ARTS_HUMANITIES" },
    { label: "Diploma", value: "DIPLOMA" },
    { label: "Law", value: "LAW" },
    { label: "Management (MBA/BBA)", value: "MANAGEMENT" },
    {
      label: "Computer Applications (BCA/MCA)",
      value: "COMPUTER_APPLICATIONS",
    },
    { label: "Other", value: "OTHER" },
  ];

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="mb-8">
        <h1 className="text-3xl font-heading font-bold text-slate-900 dark:text-white">
          {t('sell.pageTitle')}
        </h1>
        <p className="mt-2 text-slate-600 dark:text-slate-400">
          {t('sell.pageSubtitle')}
        </p>
      </div>

      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-8 bg-white dark:bg-slate-900 shadow-sm rounded-2xl border border-slate-200 dark:border-slate-800 p-8"
      >
        {/* Error banners — shown above the form; form data is always preserved */}
        {serverError && serverError.stage === "IMAGE_UPLOAD_FAILED" && (
          <div
            id="error-network"
            className="flex items-start gap-3 bg-orange-50 dark:bg-orange-900/20 text-orange-700 dark:text-orange-300 p-4 rounded-xl text-sm border border-orange-200 dark:border-orange-800"
          >
            <WifiOff className="h-5 w-5 shrink-0 mt-0.5" />
            <div>
              <p className="font-semibold">{t('errors.uploadFailed')}</p>
              <p className="mt-0.5">{serverError.message}</p>
            </div>
          </div>
        )}
        {serverError && serverError.stage === "LISTING_SAVE_FAILED" && (
          <div
            id="error-save"
            className="flex items-start gap-3 bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-300 p-4 rounded-xl text-sm border border-red-200 dark:border-red-800"
          >
            <AlertCircle className="h-5 w-5 shrink-0 mt-0.5" />
            <div>
              <p className="font-semibold">{t('errors.saveFailed')}</p>
              <p className="mt-0.5">{serverError.message}</p>
            </div>
          </div>
        )}
        {serverError && (serverError.stage === "VALIDATION_FAILED" || serverError.stage === "UNKNOWN") && (
          <div
            id="error-validation"
            className="flex items-start gap-3 bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-300 p-4 rounded-xl text-sm border border-red-200 dark:border-red-800"
          >
            <AlertCircle className="h-5 w-5 shrink-0 mt-0.5" />
            <p>{serverError.message}</p>
          </div>
        )}

        <div className="space-y-6">
          <h2 className="text-xl font-heading font-semibold text-slate-900 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">
            {t('sell.bookDetails')}
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                {t('sell.bookTitleLabel')} *
              </label>
              <input
                type="text"
                {...register("title")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
                placeholder="e.g. Introduction to Algorithms"
              />
              {errors.title && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.title.message}
                </p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                {t('sell.author')} *
              </label>
              <input
                type="text"
                {...register("author")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
              {errors.author && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.author.message}
                </p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                {t('sell.isbn')}
              </label>
              <input
                type="text"
                {...register("isbn")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
            </div>
            {/* <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Category *
              </label>
              <select
                {...register("category")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              >
                <option value="">Select Category</option>
                {categories.map((c) => (
                  <option key={c} value={c}>
                    {c}
                  </option>
                ))}
              </select>
              {errors.category && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.category.message}
                </p>
              )}
            </div> */}
            {/* // ! updated bcz of bux fix */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                {t('sell.category')} *
              </label>

              <select
                {...register("category")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              >
                <option value="">{t('sell.selectCategory')}</option>

                {categories.map((category) => (
                  <option key={category.value} value={category.value}>
                    {category.label}
                  </option>
                ))}
              </select>

              {errors.category && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.category.message}
                </p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                {t('sell.condition')} *
              </label>
              <select
                {...register("condition")}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              >
                <option value="">{t('sell.selectCondition')}</option>
                <option value="EXCELLENT">
                  {t('sell.conditionExcellent')}
                </option>
                <option value="GOOD">{t('sell.conditionGood')}</option>
                <option value="FAIR">
                  {t('sell.conditionFair')}
                </option>
                <option value="POOR">{t('sell.conditionPoor')}</option>
                {/* <option value="DONATE">Donate (Free)</option> */}
              </select>
              {errors.condition && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.condition.message}
                </p>
              )}
            </div>
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                {t('sell.description')} *
              </label>
              <textarea
                {...register("description")}
                rows="4"
                maxLength={DESCRIPTION_MAX}
                className={`w-full rounded-lg border bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary resize-none transition-colors ${errors.description ? 'border-red-500 focus:border-red-500' : 'border-slate-300 dark:border-slate-700'}`}
                placeholder={t('sell.descriptionPlaceholder')}
              ></textarea>
              <div className="flex justify-between items-start mt-1">
                <div>
                  {errors.description && (
                    <p className="text-sm text-red-500">{errors.description.message}</p>
                  )}
                </div>
                <span className={`text-xs tabular-nums shrink-0 ml-2 ${(watchDescription?.length ?? 0) >= DESCRIPTION_MAX ? 'text-red-500 font-semibold' : 'text-slate-400'}`}>
                  {watchDescription?.length ?? 0}/{DESCRIPTION_MAX}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="space-y-6 pt-6 border-t border-slate-100 dark:border-slate-800">
          <h2 className="text-xl font-heading font-semibold text-slate-900 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">
            {t('sell.pricing')}
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                {t('sell.originalMrp')} *
              </label>
              <input
                type="number"
                {...register("mrp", { valueAsNumber: true })}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
              {errors.mrp && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.mrp.message}
                </p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                {t('sell.yourPrice')} *
              </label>
              <input
                type="number"
                {...register("price", { valueAsNumber: true })}
                className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 py-2 text-slate-900 dark:text-white focus:ring-primary focus:border-primary"
              />
              {errors.price && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.price.message}
                  {/* {"hello"} */}
                </p>
              )}
            </div>
          </div>

          {suggestedPrice && suggestedPrice.max > 0 && (
            <>
              <div className="bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 p-4 rounded-xl flex gap-3">
                <Info className="h-5 w-5 text-blue-500 shrink-0 mt-0.5" />
                <div>
                  <p className="text-sm text-blue-700 dark:text-blue-300 font-medium">
                    {t('sell.suggestedRange')}: ₹{Math.round(suggestedPrice.min)} - ₹
                    {Math.round(suggestedPrice.max)}
                  </p>
                  <p className="text-xs text-blue-600/80 dark:text-blue-400 mt-1">
                    {t('sell.suggestedRangeHint')}
                  </p>
                </div>
              </div>
              {watch("price") &&
                (watch("price") < suggestedPrice.min ||
                  watch("price") > suggestedPrice.max) && (
                  <div className="bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-800 p-4 rounded-xl flex gap-3 mt-4">
                    <Info className="h-5 w-5 text-amber-500 shrink-0 mt-0.5" />
                    <div>
                      <p className="text-sm text-amber-700 dark:text-amber-300 font-medium">
                        {t('sell.priceOutsideRange')}
                      </p>
                      <p className="text-xs text-amber-600/80 dark:text-amber-400 mt-1">
                        {t('sell.priceOutsideRangeHint')}
                      </p>
                    </div>
                  </div>
                )}
            </>
          )}
        </div>

        <div className="space-y-6 pt-6 border-t border-slate-100 dark:border-slate-800">
          <h2 className="text-xl font-heading font-semibold text-slate-900 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">
            {t('sell.photos')}
          </h2>
          <p className="text-sm text-slate-500 dark:text-slate-400">
            {t('sell.photosHint')}
          </p>

          <div className="mt-2">
            {photos.length < 5 && (
              <div className="flex items-center justify-center w-full">
                <label className="flex flex-col items-center justify-center w-full h-48 border-2 border-slate-300 dark:border-slate-700 border-dashed rounded-xl cursor-pointer bg-slate-50 dark:bg-slate-800/50 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors">
                  <div className="flex flex-col items-center justify-center pt-5 pb-6">
                    <ImagePlus className="w-10 h-10 text-slate-400 mb-3" />
                    <p className="mb-2 text-sm text-slate-500 dark:text-slate-400">
                      <span className="font-semibold">{t('sell.clickToUpload')}</span> {t('sell.dragAndDrop')}
                    </p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">
                      {t('sell.photoFileHint')}
                    </p>
                  </div>
                  <input
                    type="file"
                    multiple
                    accept="image/jpeg,image/png,image/webp"
                    className="hidden"
                    onChange={handlePhotoChange}
                  />
                </label>
              </div>
            )}
            {photos.length >= 5 && (
              <p className="text-sm text-amber-500 font-medium">{t('sell.maxPhotos')}</p>
            )}
            {errors.images && (
              <p className="mt-2 text-sm text-red-500">
                {errors.images.message}
              </p>
            )}
          </div>

          {photos.length > 0 && (
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-4 mt-4">
              {photos.map((file, i) => (
                <div
                  key={i}
                  className="relative aspect-square rounded-xl overflow-hidden border border-slate-200 shadow-sm group"
                >
                  <img
                    src={URL.createObjectURL(file)}
                    alt={`Preview ${i + 1}`}
                    className="w-full h-full object-cover"
                  />
                  <button
                    type="button"
                    onClick={() => removePhoto(i)}
                    className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1 opacity-0 group-hover:opacity-100 transition-opacity z-10"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="pt-6">
          <button
            type="submit"
            disabled={isSubmittingForm || !isValid || photos.length === 0}
            className="w-full py-4 bg-primary text-white font-medium rounded-xl hover:bg-primary/90 transition-colors shadow-md shadow-primary/20 hover:shadow-lg disabled:opacity-70 text-lg"
          >
            {isSubmittingForm ? t('sell.publishing') : t('sell.publishListing')}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateListing;
