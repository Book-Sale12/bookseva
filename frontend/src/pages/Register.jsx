import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNavigate, Link } from 'react-router-dom';
import api from '../lib/api';
import { BookOpen, User, Mail, Lock, Phone, GraduationCap, BookMarked } from 'lucide-react';
import { COLLEGES } from '../data/colleges';

// ---------------------------------------------------------------------------
// Zod schema
// ---------------------------------------------------------------------------
const registerSchema = z.object({
  name: z
    .string()
    .min(2, 'Name must be at least 2 characters')
    .regex(/^[a-zA-Z\s]+$/, 'Name must not contain numbers or special characters'),
  email: z.string().regex(/^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/, 'Invalid email address'),
  password: z
    .string()
    .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/, 'Password must be at least 8 characters, with one uppercase, one lowercase, one number, and one special character'),
  phone: z
    .string()
    .regex(/^\d{10}$/, 'Phone number must be exactly 10 digits.'),
  // "Other" dropdown selection is resolved in onSubmit; we validate the
  // free-text entry separately via otherCollegeName below.
  collegeName: z.string().min(1, 'Please select a college'),
  otherCollegeName: z.string().optional(),
  courseBranch: z.string().min(2, 'Course/Branch is required'),
});

// Extra zod refinement applied when "Other" is selected
const isNotPurelyNumeric = (val) => !/^\d+$/.test(val ?? '');
const hasAtLeastOneLetter = (val) => /[a-zA-Z]/.test(val ?? '');

// ---------------------------------------------------------------------------
const Register = () => {
  const navigate = useNavigate();
  const [serverError, setServerError] = useState('');
  const [selectedCollege, setSelectedCollege] = useState('');
  const [otherCollegeError, setOtherCollegeError] = useState('');

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors, isSubmitting, isValid },
  } = useForm({
    resolver: zodResolver(registerSchema),
    mode: 'onChange',
    defaultValues: {
      collegeName: '',
      otherCollegeName: '',
    },
  });

  const watchOther = watch('otherCollegeName', '');
  const watchPhone = watch('phone', '');

  // Real-time validation for the "Other" text field
  useEffect(() => {
    if (selectedCollege !== 'Other') {
      setOtherCollegeError('');
      return;
    }
    const val = watchOther?.trim() ?? '';
    if (!val) {
      setOtherCollegeError('College name must not be empty.');
    } else if (val.length < 3) {
      setOtherCollegeError('College name must be at least 3 characters.');
    } else if (!hasAtLeastOneLetter(val)) {
      setOtherCollegeError('College name cannot be only numbers or symbols.');
    } else if (isNotPurelyNumeric(val) === false) {
      setOtherCollegeError('College name cannot be only numbers or symbols.');
    } else {
      setOtherCollegeError('');
    }
  }, [watchOther, selectedCollege]);

  const handleCollegeChange = (e) => {
    const val = e.target.value;
    setSelectedCollege(val);
    setValue('collegeName', val, { shouldValidate: true });
    if (val !== 'Other') {
      setValue('otherCollegeName', '', { shouldValidate: false });
      setOtherCollegeError('');
    }
  };

  // Block non-digit keypresses on the phone field
  const handlePhoneKeyDown = (e) => {
    const allowed = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab', 'Home', 'End'];
    if (allowed.includes(e.key)) return;
    if (!/^\d$/.test(e.key)) {
      e.preventDefault();
    }
  };

  // Prevent paste of non-digits
  const handlePhonePaste = (e) => {
    const pasted = e.clipboardData.getData('text');
    if (!/^\d+$/.test(pasted)) {
      e.preventDefault();
    }
  };

  const isFormReadyToSubmit = () => {
    if (selectedCollege === 'Other') {
      if (otherCollegeError || !watchOther?.trim()) return false;
    }
    return true;
  };

  const onSubmit = async (data) => {
    // Resolve the actual college name
    if (selectedCollege === 'Other') {
      const trimmed = data.otherCollegeName?.trim() ?? '';
      if (!trimmed || !hasAtLeastOneLetter(trimmed) || trimmed.length < 3) {
        setOtherCollegeError('Please enter a valid college name.');
        return;
      }
      data.collegeName = trimmed;
    }

    try {
      setServerError('');
      await api.post('/auth/register', {
        name: data.name,
        email: data.email,
        password: data.password,
        phone: data.phone,
        collegeName: data.collegeName,
        courseBranch: data.courseBranch,
      });
      navigate('/verify-otp', { state: { email: data.email } });
    } catch (err) {
      const respData = err.response?.data;
      if (respData?.errors) {
        setServerError(Object.values(respData.errors).join(' | '));
      } else {
        setServerError(respData?.error?.message || 'Registration failed. Please try again.');
      }
    }
  };

  return (
    <div className="min-h-[calc(100vh-4rem)] flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="flex justify-center">
          <BookOpen className="h-12 w-12 text-primary" />
        </div>
        <h2 className="mt-6 text-center text-3xl font-heading font-bold text-slate-900 dark:text-white">
          Create your account
        </h2>
        <p className="mt-2 text-center text-sm text-slate-600 dark:text-slate-400">
          Or{' '}
          <Link to="/login" className="font-medium text-primary hover:text-primary/80 transition-colors">
            log in to your existing account
          </Link>
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white dark:bg-slate-900 py-8 px-4 shadow-xl sm:rounded-2xl sm:px-10 border border-slate-100 dark:border-slate-800">
          <form className="space-y-6" onSubmit={handleSubmit(onSubmit)}>

            {serverError && (
              <div className="bg-red-50 dark:bg-red-900/30 border border-red-200 dark:border-red-800 text-red-600 dark:text-red-400 p-3 rounded-lg text-sm">
                {serverError}
              </div>
            )}

            {/* Full Name */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                Full Name
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <User className="h-5 w-5 text-slate-400" />
                </div>
                <input
                  type="text"
                  {...register('name')}
                  className={`focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm rounded-md bg-transparent dark:text-white py-2 border transition-colors ${
                    errors.name
                      ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
                      : 'border-slate-300 dark:border-slate-700'
                  }`}
                  placeholder="John Doe"
                />
              </div>
              {errors.name && <p className="mt-1 text-sm text-red-500">{errors.name.message}</p>}
            </div>

            {/* Email */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                Email address
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Mail className="h-5 w-5 text-slate-400" />
                </div>
                <input
                  type="email"
                  {...register('email')}
                  className={`focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm rounded-md bg-transparent dark:text-white py-2 border transition-colors ${
                    errors.email
                      ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
                      : 'border-slate-300 dark:border-slate-700'
                  }`}
                  placeholder="you@college.edu"
                />
              </div>
              {errors.email && <p className="mt-1 text-sm text-red-500">{errors.email.message}</p>}
            </div>

            {/* Password */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                Password
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Lock className="h-5 w-5 text-slate-400" />
                </div>
                <input
                  type="password"
                  {...register('password')}
                  className={`focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm rounded-md bg-transparent dark:text-white py-2 border transition-colors ${
                    errors.password
                      ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
                      : 'border-slate-300 dark:border-slate-700'
                  }`}
                  placeholder="••••••••"
                />
              </div>
              {errors.password && <p className="mt-1 text-sm text-red-500">{errors.password.message}</p>}
            </div>

            {/* Phone */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                Phone Number
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Phone className="h-5 w-5 text-slate-400" />
                </div>
                <input
                  type="tel"
                  {...register('phone')}
                  maxLength={10}
                  onKeyDown={handlePhoneKeyDown}
                  onPaste={handlePhonePaste}
                  className={`focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm rounded-md bg-transparent dark:text-white py-2 border transition-colors ${
                    errors.phone
                      ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
                      : 'border-slate-300 dark:border-slate-700'
                  }`}
                  placeholder="9876543210"
                  inputMode="numeric"
                />
              </div>
              {errors.phone && <p className="mt-1 text-sm text-red-500">{errors.phone.message}</p>}
              {!errors.phone && watchPhone && watchPhone.length > 0 && watchPhone.length < 10 && (
                <p className="mt-1 text-xs text-slate-400">{watchPhone.length}/10 digits</p>
              )}
            </div>

            {/* College Name — Dropdown */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                College / Institute Name
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <GraduationCap className="h-5 w-5 text-slate-400" />
                </div>
                <select
                  value={selectedCollege}
                  onChange={handleCollegeChange}
                  className={`focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm rounded-md bg-white dark:bg-slate-900 dark:text-white py-2 border transition-colors appearance-none ${
                    errors.collegeName && !selectedCollege
                      ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
                      : 'border-slate-300 dark:border-slate-700'
                  }`}
                >
                  <option value="">— Select your college —</option>
                  {COLLEGES.map((c) => (
                    <option key={c} value={c}>
                      {c}
                    </option>
                  ))}
                </select>
              </div>
              {errors.collegeName && !selectedCollege && (
                <p className="mt-1 text-sm text-red-500">{errors.collegeName.message}</p>
              )}
            </div>

            {/* "Other" free-text input */}
            {selectedCollege === 'Other' && (
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                  Enter your college name
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <GraduationCap className="h-5 w-5 text-slate-400" />
                  </div>
                  <input
                    type="text"
                    {...register('otherCollegeName')}
                    className={`focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm rounded-md bg-transparent dark:text-white py-2 border transition-colors ${
                      otherCollegeError
                        ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
                        : 'border-slate-300 dark:border-slate-700'
                    }`}
                    placeholder="Type your college/university name"
                  />
                </div>
                {otherCollegeError && (
                  <p className="mt-1 text-sm text-red-500">{otherCollegeError}</p>
                )}
              </div>
            )}

            {/* Course & Branch */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                Course &amp; Branch
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <BookMarked className="h-5 w-5 text-slate-400" />
                </div>
                <input
                  type="text"
                  {...register('courseBranch')}
                  className={`focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm rounded-md bg-transparent dark:text-white py-2 border transition-colors ${
                    errors.courseBranch
                      ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
                      : 'border-slate-300 dark:border-slate-700'
                  }`}
                  placeholder="B.Tech Computer Science"
                />
              </div>
              {errors.courseBranch && <p className="mt-1 text-sm text-red-500">{errors.courseBranch.message}</p>}
            </div>

            {/* Submit */}
            <div>
              <button
                type="submit"
                disabled={isSubmitting || !isFormReadyToSubmit()}
                className="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-primary hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-all disabled:opacity-70"
              >
                {isSubmitting ? 'Creating account...' : 'Create account'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;
