import React, {useContext} from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import {LocaleContext} from "./App";

export default function ValidatePage() {
  return (
    <div className="mx-5 mt-4">
      <ValidationForm disabled={false} lang="en" valStyle="full" passiveTables="" activeTables="" passiveDescriptors="" activeDescriptors="" description="" />
    </div>
  );
}

export function ValidationForm(props) {
  const locale = useContext(LocaleContext);

  let submitButton = <Button variant="primary" type="submit">{locale.submit}</Button>;
  if (props.disabled) submitButton = <></>;

  return (
    <Form method="post" action={props.disabled ? "" : "/submit-validation"}>

      <div className="mb-3">
        <Form.Label>{locale.setValLang}</Form.Label>
        <Form.Check disabled={props.disabled} type="radio" name="language" value="en" label={locale.english} defaultChecked={props.lang === "en"} />
        <Form.Check disabled={props.disabled} type="radio" name="language" value="cs" label={locale.czech} defaultChecked={props.lang === "cs"} />
      </div>

      <div className="mb-3">
        <Form.Label>{locale.valStyle}</Form.Label>
        <Form.Check disabled={props.disabled} type="radio" name="style" value="full" label={locale.fullVal} defaultChecked={props.valStyle === "full"} />
        <Form.Check disabled={props.disabled} type="radio" name="style" value="tables" label={locale.tablesVal} defaultChecked={props.valStyle === "tables"} />
        <Form.Check disabled={props.disabled} type="radio" name="style" value="descs" label={locale.descVal} defaultChecked={props.valStyle === "descs"} />
      </div>

      <Form.Group className="mb-3" controlId="passive-tables">
        <Form.Label>{locale.passTables}</Form.Label>
        <Form.Control disabled={props.disabled} as="textarea" name="passiveTables" defaultValue={props.passiveTables} />
      </Form.Group>

      <Form.Group className="mb-3" controlId="active-tables">
        <Form.Label>{locale.activeTables}</Form.Label>
        <Form.Control disabled={props.disabled} as="textarea" name="activeTables" defaultValue={props.activeTables} />
      </Form.Group>

      <Form.Group className="mb-3" controlId="passive-descriptors">
        <Form.Label>{locale.passDescs}</Form.Label>
        <Form.Control disabled={props.disabled} as="textarea" name="passiveDescriptors" defaultValue={props.passiveDescriptors} />
      </Form.Group>

      <Form.Group className="mb-3" controlId="active-descriptors">
        <Form.Label>{locale.activeDescs}</Form.Label>
        <Form.Control disabled={props.disabled} as="textarea" name="activeDescriptors" defaultValue={props.activeDescriptors} />
      </Form.Group>

      <Form.Group className="mb-3" controlId="description">
        <Form.Label>{locale.description}</Form.Label>
        <Form.Control disabled={props.disabled} as="textarea" name="description" defaultValue={props.description} />
      </Form.Group>

      {submitButton}
    </Form>
  );
}