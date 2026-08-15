import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import api from '../lib/api';
import { BookOpen, Mail, KeyRound, Lock } from 'lucide-react';

const ForgotPassword = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [step, setStep] = useState(1);
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleRequestOtp = async (e) => {
    e.preventDefault();
    if (!email) {
      setError(t('auth.pleaseEnterEmail'));
      return;
    }
    const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    if (!emailRegex.test(email)) {
      setError("Please enter a valid email address.");
      return;
    }
    
    try {
      setIsSubmitting(true);
      setError('');
      setSuccess('');
      
      const res = await api.post('/auth/forgot-password', { email });
      setSuccess(res.data.message || t('auth.otpSent'));
      setTimeout(() => {
        setSuccess('');
        setStep(2);
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.error?.message || t('auth.resetFailed'));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    if (otp.length !== 6) {
      setError(t('auth.otpMustBe6'));
      return;
    }
    if (newPassword.length < 8) {
      setError(t('auth.passwordMin8'));
      return;
    }
    if (newPassword !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    try {
      setIsSubmitting(true);
      setError('');
      setSuccess('');
      
      const res = await api.post('/auth/reset-password', {
        email,
        otp,
        newPassword
      });
      
      setSuccess(res.data.message || t('auth.passwordResetSuccess'));
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.error?.message || t('auth.resetFailed'));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="min-h-[calc(100vh-4rem)] flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="flex justify-center">
          <BookOpen className="h-12 w-12 text-primary" />
        </div>
        <h2 className="mt-6 text-center text-3xl font-heading font-bold text-slate-900 dark:text-white">
          {t('auth.resetTitle')}
        </h2>
        <p className="mt-2 text-center text-sm text-slate-600 dark:text-slate-400">
          {step === 1 ? t('auth.resetStep1Subtitle') : `${t('auth.resetStep2Subtitle')} ${email}`}
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white dark:bg-slate-900 py-8 px-4 shadow-xl sm:rounded-2xl sm:px-10 border border-slate-100 dark:border-slate-800">
          
          {error && (
            <div className="mb-6 bg-red-50 dark:bg-red-900/30 border border-red-200 dark:border-red-800 text-red-600 dark:text-red-400 p-3 rounded-lg text-sm">
              {error}
            </div>
          )}

          {success && (
            <div className="mb-6 bg-green-50 dark:bg-green-900/30 border border-green-200 dark:border-green-800 text-green-700 dark:text-green-400 p-3 rounded-lg text-sm">
              {success}
            </div>
          )}

          {step === 1 ? (
            <form className="space-y-6" onSubmit={handleRequestOtp}>
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                  {t('auth.emailLabel')}
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Mail className="h-5 w-5 text-slate-400" />
                  </div>
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    className="focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm border-slate-300 dark:border-slate-700 rounded-md bg-transparent dark:text-white py-2 border"
                    placeholder="you@college.edu"
                  />
                </div>
              </div>

              <div>
                <button
                  type="submit"
                  disabled={isSubmitting || !email}
                  className="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-primary hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-all disabled:opacity-70"
                >
                  {isSubmitting ? t('auth.sending') : t('auth.sendResetCode')}
                </button>
              </div>
              
              <div className="text-center text-sm">
                <Link to="/login" className="font-medium text-primary hover:text-primary/80 transition-colors">
                  {t('auth.backToLogin')}
                </Link>
              </div>
            </form>
          ) : (
            <form className="space-y-6" onSubmit={handleResetPassword}>
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                  {t('auth.resetCode')}
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <KeyRound className="h-5 w-5 text-slate-400" />
                  </div>
                  <input
                    type="text"
                    maxLength={6}
                    value={otp}
                    onChange={(e) => setOtp(e.target.value.replace(/[^0-9]/g, ''))}
                    required
                    className="focus:ring-primary focus:border-primary block w-full pl-10 sm:text-lg tracking-widest text-center border-slate-300 dark:border-slate-700 rounded-md bg-transparent dark:text-white py-3 border font-mono"
                    placeholder="000000"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                  {t('auth.newPassword')}
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Lock className="h-5 w-5 text-slate-400" />
                  </div>
                  <input
                    type="password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                    className="focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm border-slate-300 dark:border-slate-700 rounded-md bg-transparent dark:text-white py-2 border"
                    placeholder="••••••••"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                  Confirm New Password
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Lock className="h-5 w-5 text-slate-400" />
                  </div>
                  <input
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                    className="focus:ring-primary focus:border-primary block w-full pl-10 sm:text-sm border-slate-300 dark:border-slate-700 rounded-md bg-transparent dark:text-white py-2 border"
                    placeholder="••••••••"
                  />
                </div>
              </div>

              <div>
                <button
                  type="submit"
                  disabled={isSubmitting || otp.length !== 6 || newPassword.length < 8 || newPassword !== confirmPassword}
                  className="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-primary hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-all disabled:opacity-70"
                >
                  {isSubmitting ? t('auth.resetting') : t('auth.resetPassword')}
                </button>
              </div>
              
              <div className="text-center text-sm flex justify-between">
                <button
                  type="button"
                  onClick={() => setStep(1)}
                  className="font-medium text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white transition-colors"
                >
                  {t('auth.changeEmail')}
                </button>
                <button
                  type="button"
                  onClick={handleRequestOtp}
                  className="font-medium text-primary hover:text-primary/80 transition-colors"
                >
                  {t('auth.resendOtp')}
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;
