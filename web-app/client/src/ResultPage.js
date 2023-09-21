import React, {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import Alert from "react-bootstrap/Alert";
import Spinner from "react-bootstrap/Spinner";
import {LocaleContext} from "./App";
import {ValidationForm} from "./ValidatePage";

export default function ResultPage() {
  const resultId = useParams().id;

  const [data, setData] = useState(null);

  useEffect(() => {
    fetch("/validation-data/" + resultId)
      .then(response => response.json())
      .then(parsed => setData(parsed), () => setData("missing"));
  }, [resultId]);

  let content;
  if (data === null) content = <Loading />;
  else if (data === "missing") content = <Missing />;
  else if (data.state === "queueing") content = <Queueing reqData={data} />;
  else if (data.state === "working") content = <Working reqData={data} />;
  else if (data.state === "finished") content = <Finished reqData={data} />;

  const locale = useContext(LocaleContext);

  return (
    <div className="mx-5 mt-4">
      <h1>{locale.validation} {resultId}</h1>
      {content}
    </div>
  );
}

function Loading() {
  return (
    <div className="center-spin">
      <Spinner animation="border" variant="dark" />
    </div>
  );
}

function Missing() {
  const locale = useContext(LocaleContext);

  return <Alert variant="danger">{locale.noValidation}</Alert>;
}

function Queueing(props) {
  const locale = useContext(LocaleContext);

  return (
    <>
      <Alert variant="primary">{locale.queueing}<Spinner animation="border" variant="primary" style={{ float: "right" }} /></Alert>
      <RequestData reqData={props.reqData} />
    </>
  );
}

function Working(props) {
  const locale = useContext(LocaleContext);

  return (
    <>
      <Alert variant="warning">{locale.working}<Spinner animation="grow" variant="warning" style={{ float: "right" }} /></Alert>
      <RequestData reqData={props.reqData} />
    </>
  );
}

function Finished(props) {
  const locale = useContext(LocaleContext);

  return (
    <>
      <Alert variant="success">{locale.finished}</Alert>
      <RequestData reqData={props.reqData} />
    </>
  );
}


function RequestData(props) {
  const locale = useContext(LocaleContext);
  const data = props.reqData;

  return (
    <>
      <h2>{locale.reqDetail}</h2>
      <div className="mb-3">
        {locale.reqTime}: {new Date(data["request-time"]).toLocaleString(locale.langTag)}
      </div>
      <ValidationForm disabled={true} lang={data.language} valStyle={data.style} passiveTables={data["passive-tables"]} activeTables={data["active-tables"]} passiveDescriptors={data["passive-descriptors"]} activeDescriptors={data["active-descriptors"]} description={data.description} />
    </>
  );
}

function ResultData(props) {
  
}
