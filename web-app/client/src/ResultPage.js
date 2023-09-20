import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import Alert from "react-bootstrap/Alert";

export default function ResultPage() {
  const resultId = useParams().id;

  const [data, setData] = useState(null);

  useEffect(() => {
    fetch("/validation-data/" + resultId)
      .then(response => response.json())
      .then(parsed => setData(parsed), () => setData(null));
  }, [resultId]);

  if (data === null) return <MissingData />;
  if (data.state === "waiting") return <WaitingPage />;
}

function MissingData() {
  return <Alert variant="danger" className="mx-5 mt-5">The validation request does not exist.</Alert>;
}

function WaitingPage() {
  return (
    <Alert variant="primary" className="mx-5 mt-5">The validation request is waiting in the queue.</Alert>
  )
}
