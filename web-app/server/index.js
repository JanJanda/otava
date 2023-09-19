const express = require('express');
const path = require('path');
const {addValidationRequest} = require('./model');

const app = express();

const netPort = 80;
const validationFormUrl = '/validator/validate';

app.use(validationFormUrl, express.static(path.join(__dirname, '..', 'client', 'validate', 'build')));
app.use(express.urlencoded({extended: true}));

app.post(validationFormUrl + '/submit-validation', (req, res) => {
  res.redirect(validationFormUrl);
});

app.listen(netPort, () => console.log('Listening on port ' + netPort));
