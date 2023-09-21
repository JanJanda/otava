import React, {createContext, useState} from "react";
import {Route, Routes} from "react-router-dom";
import ValidatePage from "./ValidatePage";
import ResultPage from "./ResultPage";
import TopBar from "./TopBar";
import {englishLocale} from "./englishLocale";

export const LocaleContext = createContext(englishLocale);

export default function App() {
  const [locale, setLocale] = useState(englishLocale);

  return (
    <LocaleContext.Provider value={locale}>
      <TopBar setLocale={setLocale} />
      <Routes>
        <Route path="/validate" element={<ValidatePage />} />
        <Route path="/result/:id" element={<ResultPage />} />
      </Routes>
    </LocaleContext.Provider>
  );
}
