import React, {createContext, useState} from "react";
import {Route, Routes} from "react-router-dom";
import ExpertValidationPage from "./ExpertValidationPage";
import ResultPage from "./ResultPage";
import TopBar from "./TopBar";
import {englishLocale} from "./englishLocale";
import HomePage from "./HomePage";
import NotFoundPage from "./NotFoundPage";
import SearchPage from "./SearchPage";
import AboutPage from "./AboutPage";

export const LocaleContext = createContext(englishLocale);

export default function App() {
  const [locale, setLocale] = useState(englishLocale);

  return (
    <LocaleContext.Provider value={locale}>
      <TopBar setLocale={setLocale} />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/expert-validation" element={<ExpertValidationPage />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/result/:id" element={<ResultPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </LocaleContext.Provider>
  );
}
