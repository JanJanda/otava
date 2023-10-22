import React, {useContext} from "react";
import {LocaleContext} from "./App";

export default function AboutPage() {
  const locale = useContext(LocaleContext);

  return (
    <div className="mx-5 mt-4 mb-3">
      <h1>OTAVA ({locale.otava}) - {locale.webApp}</h1>
      <p className="intro-text">{locale.introText}</p>
      <h2>{locale.manual}</h2>
      <p className="intro-text">{locale.howToUse}</p>
      <Extras />
    </div>
  );
}

function Extras() {
  const locale = useContext(LocaleContext);

  return (
    <div className="mt-5">
      <hr />
      <a href="https://github.com/JanJanda/otava" target="_blank" rel="noreferrer" className="extra-link">{locale.projectGitHub}</a>
      <a href="https://www.w3.org/TR/2016/NOTE-tabular-data-primer-20160225/" target="_blank" rel="noreferrer" className="extra-link">CSV on the Web</a>
    </div>
  );
}
