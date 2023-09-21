import React, {useContext} from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import {LocaleContext} from "./App";

export default function ValidatePage() {
  return (
    <div className="mx-5 mt-4">
      <ValidationForm />
    </div>
  );
}

function ValidationForm() {
  const locale = useContext(LocaleContext);

  return (
    <Form method="post" action="/submit-validation">

      <div className="mb-3">
        <Form.Label>{locale.setValLang}</Form.Label>
        <Form.Check type="radio" name="language" value="en" label={locale.english} defaultChecked />
        <Form.Check type="radio" name="language" value="cs" label={locale.czech} />
      </div>

      <div className="mb-3">
        <Form.Label>{locale.valStyle}</Form.Label>
        <Form.Check type="radio" name="style" value="full" label={locale.fullVal} defaultChecked />
        <Form.Check type="radio" name="style" value="tables" label={locale.tablesVal} />
        <Form.Check type="radio" name="style" value="descs" label={locale.descVal} />
      </div>

      <Form.Group className="mb-3" controlId="passive-tables">
        <Form.Label>{locale.passTables}</Form.Label>
        <Form.Control as="textarea" name="passiveTables" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="active-tables">
        <Form.Label>{locale.activeTables}</Form.Label>
        <Form.Control as="textarea" name="activeTables" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="passive-descriptors">
        <Form.Label>{locale.passDescs}</Form.Label>
        <Form.Control as="textarea" name="passiveDescriptors" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="active-descriptors">
        <Form.Label>{locale.activeDescs}</Form.Label>
        <Form.Control as="textarea" name="activeDescriptors" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="description">
        <Form.Label>{locale.description}</Form.Label>
        <Form.Control as="textarea" name="description" />
      </Form.Group>

      <Button variant="primary" type="submit">{locale.submit}</Button>
    </Form>
  );
}
