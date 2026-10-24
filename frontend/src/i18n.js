import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

import enTranslation from './locales/en/translation.json';
import hiTranslation from './locales/hi/translation.json';
import mrTranslation from './locales/mr/translation.json';

const SUPPORTED_LANGUAGES = ['en', 'hi', 'mr'];

i18n
  // Detects browser language and reads/writes to localStorage
  .use(LanguageDetector)
  // Passes i18n instance to react-i18next
  .use(initReactI18next)
  .init({
    // Bundled resources — no async fetch needed
    resources: {
      en: { translation: enTranslation },
      hi: { translation: hiTranslation },
      mr: { translation: mrTranslation },
    },

    // Language detection configuration
    detection: {
      // Check localStorage first (persisted manual selection), then browser language
      order: ['localStorage', 'navigator'],
      // Key used to store the selected language in localStorage
      lookupLocalStorage: 'bookseva_language',
      // Cache the detected/selected language back to localStorage
      caches: ['localStorage'],
    },

    // Fall back to English if browser language is unsupported (e.g. French, German)
    supportedLngs: SUPPORTED_LANGUAGES,
    fallbackLng: 'en',
    // Don't try 'en-IN', 'hi-IN' etc. — match the base language code only
    load: 'languageOnly',

    interpolation: {
      // React already escapes values, no need for i18next to escape
      escapeValue: false,
    },
  });

export default i18n;
