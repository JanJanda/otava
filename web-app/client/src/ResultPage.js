import React, {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import Alert from "react-bootstrap/Alert";
import Spinner from "react-bootstrap/Spinner";
import {LocaleContext} from "./App";

export default function ResultPage() {
  const resultId = useParams().id;

  const [data, setData] = useState(null);

  useEffect(() => {
    fetch("/validation-data/" + resultId)
      .then(response => response.json())
      .then(parsed => setData(parsed), () => setData(""));
  }, [resultId]);

  if (data === null) return <Waiting />;
  if (data === "") return <MissingData />;
  if (data.state === "waiting") return <WaitingPage />;
}

function Waiting() {
  return (
    <div className="mt-5 center-spin">
      <Spinner animation="border" variant="dark" />
    </div>
  );
}

function MissingData() {
  const locale = useContext(LocaleContext);

  return <Alert variant="danger" className="mx-5 mt-5">{locale.noValidation}</Alert>;
}

function WaitingPage() {
  const locale = useContext(LocaleContext);

  return (
    <Alert variant="primary" className="mx-5 mt-5">{locale.waiting}</Alert>
  );
}
