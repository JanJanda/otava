import React, {useContext} from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import {LocaleContext} from "./App";

export default function TableValidationPage() {
  const locale = useContext(LocaleContext);

  return (
    <div className="mx-5 mt-4 mb-5">
      <h1>{locale.tableValidation}</h1>
      <TableValidationForm disabled={false} descUrl="" active={false} />
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

        <Form.Group className="mb-3" controlId="desc-url">
          <Form.Label>{locale.enterTableUrl}</Form.Label>
          <Form.Control disabled={props.disabled} name="descUrl" defaultValue={props.descUrl} />
        </Form.Group>

        <Form.Check type="switch" name="active" label={locale.switchActiveTable} className="mb-3" disabled={props.disabled} defaultChecked={props.active} />

        {submitButton}
      </Form>
    </div>
  );
}
