import React from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

export default function App() {
  return (
    <div className="mx-5 mt-4">
      <ValidationForm />
    </div>
  );
}

function ValidationForm() {
  return (
    <Form method="post" action="submit-validation">

      <div className="mb-3">
        <Form.Label>Validation result language</Form.Label>
        <Form.Check type="radio" name="language" value="en" label="English" defaultChecked />
        <Form.Check type="radio" name="language" value="cs" label="Czech" />
      </div>

      <div className="mb-3">
        <Form.Label>Validation style</Form.Label>
        <Form.Check type="radio" name="style" value="full" label="Full validation" defaultChecked />
        <Form.Check type="radio" name="style" value="tables" label="Tables only validation" />
        <Form.Check type="radio" name="style" value="descs" label="Descriptors only validation" />
      </div>

      <Form.Group className="mb-3" controlId="passive-tables">
        <Form.Label>Passive tables</Form.Label>
        <Form.Control as="textarea" name="passiveTables" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="active-tables">
        <Form.Label>Active tables</Form.Label>
        <Form.Control as="textarea" name="activeTables" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="passive-descriptors">
        <Form.Label>Passive descriptors</Form.Label>
        <Form.Control as="textarea" name="passiveDescriptors" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="active-descriptors">
        <Form.Label>Active descriptors</Form.Label>
        <Form.Control as="textarea" name="activeDescriptors" />
      </Form.Group>

      <Form.Group className="mb-3" controlId="description">
        <Form.Label>Description</Form.Label>
        <Form.Control as="textarea" name="description" />
      </Form.Group>

      <Button variant="primary" type="submit">Submit validation request</Button>
    </Form>
  );
}
