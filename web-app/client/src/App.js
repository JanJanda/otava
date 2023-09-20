import React from "react";
import {Route, Routes} from "react-router-dom";
import ValidatePage from "./ValidatePage";
import ResultPage from "./ResultPage";

export default function App() {
  return (
    <Routes>
      <Route path="/validate" element={<ValidatePage />} />
      <Route path="/result/:id" element={<ResultPage />} />
    </Routes>
  );
}
