const express = require("express");
const path = require("path");
const {addTableValidationRequest, addDescriptorValidationRequest, addExpertValidationRequest, getValidationData} = require("./model");

const app = express();

const netPort = 80;

app.use(express.static(path.join(__dirname, "..", "client", "dist")));
app.use(express.urlencoded({extended: true}));

app.get("/validation-data/:id", async (req, res) => {
  const result = await getValidationData(req.params.id);
  res.json(result);
});

app.get("/*", (req, res) => {
  res.sendFile(path.join(__dirname, "..", "client", "dist", "index.html"));
});

app.post("/submit-table-validation", async (req, res) => {
  const id = await addTableValidationRequest(req.body.language, req.body.tableUrl, req.body.active);
  res.redirect("/result/" + id);
});

app.post("/submit-descriptor-validation", async (req, res) => {
  const id = await addDescriptorValidationRequest(req.body.language, req.body.descUrl, req.body.active);
  res.redirect("/result/" + id);
});

app.post("/submit-expert-validation", async (req, res) => {
  const id = await addExpertValidationRequest(req.body.language, req.body.style, req.body.passiveTables, req.body.activeTables, req.body.passiveDescriptors, req.body.activeDescriptors, req.body.description);
  res.redirect("/result/" + id);
});

app.listen(netPort, () => console.log("Listening on port " + netPort));
