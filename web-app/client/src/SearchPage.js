import React, {useContext, useState} from "react";
import {useNavigate} from "react-router-dom";
import {LocaleContext} from "./App";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

export default function SearchPage() {
  const [searchId, setSearchId] = useState("");
  const navigate = useNavigate();
  const locale = useContext(LocaleContext);

  return (
    <div className="mt-5 mx-auto search-bar">
      <Form.Group controlId="result-search" style={{ float: "left" }}>
        <Form.Label style={{ marginRight: "10px", float: "left", marginTop: "5px" }}>{locale.searchById}:</Form.Label>
        <Form.Control value={searchId} onChange={e => setSearchId(e.target.value)} style={{ maxWidth: "100px", marginRight: "10px"}} />
      </Form.Group>
      <Button variant="secondary" onClick={() => navigate("/result/" + searchId.trim())} disabled={searchId.trim() === ""}>{locale.search}</Button>
    </div>
  );
}
