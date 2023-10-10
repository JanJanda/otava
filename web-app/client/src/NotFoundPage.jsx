import React, {useContext} from "react";
import {LocaleContext} from "./App";

export default function NotFoundPage() {
  const locale = useContext(LocaleContext);

  return (
    <div className="center-msg mt-5 mb-5">
      <h1>{locale.notFound}</h1>
    </div>
  );
}
