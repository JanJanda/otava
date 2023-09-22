import React, {createContext, useState} from "react";
import {Route, Routes} from "react-router-dom";
import ValidatePage from "./ValidatePage";
import ResultPage from "./ResultPage";
import TopBar from "./TopBar";
import {englishLocale} from "./englishLocale";
import HomePage from "./HomePage";
import NotFoundPage from "./NotFoundPage";

export const LocaleContext = createContext(englishLocale);

export default function App() {
  const [locale, setLocale] = useState(englishLocale);

  return (
    <LocaleContext.Provider value={locale}>
      <TopBar setLocale={setLocale} />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/validate" element={<ValidatePage />} />
        <Route path="/result/:id" element={<ResultPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </LocaleContext.Provider>
  );
}
