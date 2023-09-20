const express = require("express");
const path = require("path");
const {addValidationRequest} = require("./model");

const app = express();

const netPort = 80;
const validationFormUrl = "/validator/validate";

app.use(validationFormUrl, express.static(path.join(__dirname, "..", "client", "validate", "build")));
app.use(express.urlencoded({extended: true}));

app.post(validationFormUrl + "/submit-validation", async (req, res) => {
  const id = await addValidationRequest(req.body.language, req.body.style, req.body.passiveTables, req.body.activeTables, req.body.passiveDescriptors, req.body.activeDescriptors, req.body.description);
  res.send(id.toString());
});

app.listen(netPort, () => console.log("Listening on port " + netPort));
