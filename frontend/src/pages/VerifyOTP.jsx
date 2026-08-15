import { useState } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import api from '../lib/api';
import { BookOpen, KeyRound } from 'lucide-react';

const VerifyOTP = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { t } = useTranslation();
  const email = location.state?.email || '';

  const [otp, setOtp] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (otp.length !== 6) {
      setError(t('auth.otpMustBe6'));
      return;
    }

    try {
      setIsSubmitting(true);
      setError('');
      
      await api.post('/auth/verify-otp', {
        email,
        otp,
      });
      
      setSuccess(t('auth.accountVerified'));
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.error?.message || t('auth.verificationFailed'));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleResend = async () => {
    try {
      setError('');
      setSuccess('');
      await api.post('/auth/resend-otp', { email });
      setSuccess('A new OTP has been sent to your email.');
    } catch (err) {
      setError(err.response?.data?.error?.message || t('errors.somethingWentWrong'));
    }
  };

  if (!email) {
    return (
      <div className="min-h-[calc(100vh-4rem)] flex items-center justify-center p-8 text-center">
        <div>
          <h2 className="text-2xl font-bold text-slate-800 dark:text-white">{t('auth.missingInfo')}</h2>
          <p className="mt-2 text-slate-600 dark:text-slate-400">{t('auth.missingInfoDesc')}</p>
          <Link to="/register" className="mt-4 inline-block text-primary font-medium hover:underline">
            {t('auth.goToRegistration')}
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-[calc(100vh-4rem)] flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="flex justify-center">
          <BookOpen className="h-12 w-12 text-primary" />
        </div>
        <h2 className="mt-6 text-center text-3xl font-heading font-bold text-slate-900 dark:text-white">
          {t('auth.verifyEmailTitle')}
        </h2>
        <p className="mt-2 text-center text-sm text-slate-600 dark:text-slate-400">
          {t('auth.verifyEmailSubtitle')} <span className="font-medium text-slate-900 dark:text-slate-200">{email}</span>
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white dark:bg-slate-900 py-8 px-4 shadow-xl sm:rounded-2xl sm:px-10 border border-slate-100 dark:border-slate-800">
          <form className="space-y-6" onSubmit={handleSubmit}>
            
            {error && (
              <div className="bg-red-50 dark:bg-red-900/30 border border-red-200 dark:border-red-800 text-red-600 dark:text-red-400 p-3 rounded-lg text-sm">
                {error}
              </div>
            )}

            {success && (
              <div className="bg-green-50 dark:bg-green-900/30 border border-green-200 dark:border-green-800 text-green-700 dark:text-green-400 p-3 rounded-lg text-sm">
                {success}
              </div>
            )}

            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
                {t('auth.verificationCode')}
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
                  className="focus:ring-primary focus:border-primary block w-full pl-10 sm:text-lg tracking-widest text-center border-slate-300 dark:border-slate-700 rounded-md bg-transparent dark:text-white py-3 border font-mono"
                  placeholder="000000"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                disabled={isSubmitting || otp.length !== 6}
                className="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-primary hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-all disabled:opacity-70"
              >
                {isSubmitting ? t('auth.verifying') : t('auth.verifyEmail')}
              </button>
            </div>
            
            <div className="text-center text-sm">
              <span className="text-slate-600 dark:text-slate-400">{t('auth.didNotReceive')} </span>
              <button
                type="button"
                onClick={handleResend}
                className="font-medium text-primary hover:text-primary/80 transition-colors"
              >
                {t('auth.resendOtp')}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default VerifyOTP;
