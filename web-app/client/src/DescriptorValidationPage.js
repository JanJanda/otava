import React, {useContext} from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import {LocaleContext} from "./App";

export default function DescriptorValidationPage() {
  const locale = useContext(LocaleContext);

  return (
    <div className="mx-5 mt-4 mb-5">
      <h1>{locale.descValidation}</h1>
      <DescriptorValidationForm disabled={false} lang="en" descUrl="" active={false} />
    </div>
  );
}

export function DescriptorValidationForm(props) {
  const locale = useContext(LocaleContext);

  let submitButton = <Button variant="primary" type="submit">{locale.submit}</Button>;
  if (props.disabled) submitButton = <></>;

  return (
    <div style={{ maxWidth: "1000px" }}>
      <Form method="post" action={props.disabled ? "" : "/submit-descriptor-validation"}>

        <div className="mb-3">
          <Form.Label>{locale.setValLang}</Form.Label>
          <Form.Check disabled={props.disabled} type="radio" name="language" value="en" label={locale.english} defaultChecked={props.lang === "en"} />
          <Form.Check disabled={props.disabled} type="radio" name="language" value="cs" label={locale.czech} defaultChecked={props.lang === "cs"} />
        </div>

        <Form.Group className="mb-3" controlId="desc-url">
          <Form.Label>{locale.descUrl}</Form.Label>
          <Form.Control disabled={props.disabled} name="descUrl" defaultValue={props.descUrl} />
        </Form.Group>

        <Form.Check type="switch" name="active" label={locale.switchActiveDesc} className="mb-3" disabled={props.disabled} defaultChecked={props.active} />

        {submitButton}
      </Form>
    </div>
  );
}
