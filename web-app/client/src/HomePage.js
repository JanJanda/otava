import React, {useContext} from "react";
import {LocaleContext} from "./App";

export default function HomePage() {
  const locale = useContext(LocaleContext);

  return (
    <div className="mx-5 mt-4">
      <h1>OTAVA ({locale.otava}) - {locale.webApp}</h1>
      <p className="intro-text">{locale.introText}</p>
      <h2>{locale.manual}</h2>
      <p className="intro-text">{locale.howToUse}</p>
      <Extras />
    </div>
  );
}

function Extras() {
  return (
    <div className="mt-5 mb-2">
      <hr />
      <a href="https://github.com/JanJanda/otava" target="_blank" rel="noreferrer" className="extra-link">The project on GitHub</a>
      <a href="https://www.w3.org/TR/2016/NOTE-tabular-data-primer-20160225/" target="_blank" rel="noreferrer" className="extra-link">CSV on the Web</a>
    </div>
  );
}
