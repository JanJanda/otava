import React, {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import Alert from "react-bootstrap/Alert";
import Spinner from "react-bootstrap/Spinner";
import {LocaleContext} from "./App";
import {ExpertValidationForm} from "./ExpertValidationPage";
import Accordion from "react-bootstrap/Accordion";

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
  else if (data.state === "queueing") content = <Queueing dbData={data} />;
  else if (data.state === "working") content = <Working dbData={data} />;
  else if (data.state === "finished") content = <Finished dbData={data} />;

  const locale = useContext(LocaleContext);

  return (
    <div className="mx-5 mt-4 mb-5">
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
    <div>
      <Alert variant="primary">{locale.queueing}<Spinner animation="border" variant="primary" style={{ float: "right" }} /></Alert>
      <RequestData dbData={props.dbData} />
    </div>
  );
}

function Working(props) {
  const locale = useContext(LocaleContext);

  return (
    <div>
      <Alert variant="warning">{locale.working}<Spinner animation="grow" variant="warning" style={{ float: "right" }} /></Alert>
      <RequestData dbData={props.dbData} />
    </div>
  );
}

function Finished(props) {
  const locale = useContext(LocaleContext);

  return (
    <div>
      <Alert variant="success">{locale.finished}</Alert>
      <RequestData dbData={props.dbData} />
      <Report dbData={props.dbData} />
    </div>
  );
}

function RequestData(props) {
  const locale = useContext(LocaleContext);
  const data = props.dbData;

  return (
    <div>
      <h2>{locale.reqDetail}</h2>
      <div className="mb-3">
        {locale.reqTime}: {new Date(data["request-time"]).toLocaleString(locale.langTag)}
      </div>
      <ExpertValidationForm disabled={true} lang={data.language} valStyle={data.style} passiveTables={data["passive-tables"]} activeTables={data["active-tables"]} passiveDescriptors={data["passive-descriptors"]} activeDescriptors={data["active-descriptors"]} description={data.description} />
    </div>
  );
}

function Report(props) {
  const locale = useContext(LocaleContext);
  const data = props.dbData;

  return (
    <div>
      <h2>{locale.report}</h2>
      <div className="mb-3">
        {locale.finishTime}: {new Date(data["finish-time"]).toLocaleString(locale.langTag)}
      </div>
      <ReportFormats dbData={props.dbData} />
    </div>
  );
}

function ReportFormats(props) {
  const data = props.dbData;

  return (
    <Accordion defaultActiveKey={["0"]} alwaysOpen>
      <Accordion.Item eventKey="0">
        <Accordion.Header>Text</Accordion.Header>
        <Accordion.Body>
          <div className="keep-lines">{data["outcome-text"]}</div>
        </Accordion.Body>
      </Accordion.Item>
      <Accordion.Item eventKey="1">
        <Accordion.Header>JSON</Accordion.Header>
        <Accordion.Body>
          <div className="keep-lines">{data["outcome-json"]}</div>
        </Accordion.Body>
      </Accordion.Item>
      <Accordion.Item eventKey="2">
        <Accordion.Header>Turtle</Accordion.Header>
        <Accordion.Body>
          <div className="keep-lines">{data["outcome-turtle"]}</div>
        </Accordion.Body>
      </Accordion.Item>
    </Accordion>
  );
}
