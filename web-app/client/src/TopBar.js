import React, {useContext} from "react";
import Container from "react-bootstrap/Container";
import Navbar from "react-bootstrap/Navbar";
import FormSelect from "react-bootstrap/FormSelect";
import {Link} from "react-router-dom";
import {englishLocale} from "./englishLocale";
import {czechLocale} from "./czechLocale";
import {LocaleContext} from "./App";

export default function TopBar(props) {
  const locale = useContext(LocaleContext);

  return (
    <Navbar className="bg-body-tertiary" data-bs-theme="dark">
      <Container>
        <Link to="/" style={{ textDecoration: "none" }}>
          <Navbar.Brand>OTAVA - WebApp</Navbar.Brand>
        </Link>
        <Link to="/search" className="topbar-link">
          {locale.searchValidation}
        </Link>
        <Link to="/about" className="topbar-link">
          {locale.about}
        </Link>
        <LangSelect setLocale={props.setLocale} />
      </Container>
    </Navbar>
  );
}

function LangSelect(props) {
  return (
    <FormSelect style={{ maxWidth: "200px" }} onChange={e => props.setLocale(selectLocale(e.target.value))}>
      <option value="en">English</option>
      <option value="cs">Česky</option>
    </FormSelect>
  );
}

function selectLocale(langTag) {
  if (langTag === "en") return englishLocale;
  if (langTag === "cs") return czechLocale;
  return englishLocale;
}
