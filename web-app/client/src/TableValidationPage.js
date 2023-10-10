import React, {useContext} from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import {LocaleContext} from "./App";

export default function TableValidationPage() {
  const locale = useContext(LocaleContext);

  return (
    <div className="mx-5 mt-4 mb-5">
      <h1>{locale.tableValidation}</h1>
      <TableValidationForm disabled={false} lang="en" tableUrl="" active={true} />
    </div>
  );
}

export function TableValidationForm(props) {
  const locale = useContext(LocaleContext);

  let submitButton = <Button variant="primary" type="submit">{locale.submit}</Button>;
  if (props.disabled) submitButton = <></>;

  return (
    <div style={{ maxWidth: "1000px" }}>
      <Form method="post" action={props.disabled ? "" : "/submit-table-validation"}>

        <Form.Group className="mb-3" controlId="table-url">
          <Form.Label>{locale.tableUrl}</Form.Label>
          <Form.Control disabled={props.disabled} name="tableUrl" defaultValue={props.tableUrl} />
        </Form.Group>

        <Form.Check type="switch" name="active" label={locale.switchActiveTable} className="mb-3" disabled={props.disabled} defaultChecked={props.active} />

        <div className="mb-3">
          <Form.Label>{locale.setValLang}</Form.Label>
          <Form.Check disabled={props.disabled} type="radio" name="language" value="en" label={locale.english} defaultChecked={props.lang === "en"} />
          <Form.Check disabled={props.disabled} type="radio" name="language" value="cs" label={locale.czech} defaultChecked={props.lang === "cs"} />
        </div>

        {submitButton}
      </Form>
    </div>
  );
}
