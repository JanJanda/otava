import React, {useContext} from "react";
import {Link} from "react-router-dom";
import {LocaleContext} from "./App";

export default function HomePage() {
  const locale = useContext(LocaleContext);

  return (
    <div className="mx-auto mt-4 mb-3 home-page">
      <Link to="/table-validation" className="val-link">{locale.tableValidation}</Link>
      <Link to="/descriptor-validation" className="val-link">{locale.descValidation}</Link>
      <Link to="/expert-validation" className="val-link">{locale.expertValidation}</Link>
    </div>
  );
}
