import React, {useContext} from "react";
import {LocaleContext} from "./App";

export default function HomePage() {
  const locale = useContext(LocaleContext);

  return (
    <div className="mx-5 mt-4">
      <h1>OTAVA ({locale.otava}) - {locale.webApp}</h1>
      <p className="intro-text mt-3">{locale.introText}</p>
    </div>
  );
}
