const express = require('express');
const path = require('path');

const app = express();

const validationFormUrl = '/validator/validate';

app.use(validationFormUrl, express.static(path.join(__dirname, '..', 'client', 'validate', 'build')));
app.use(express.urlencoded({extended: true}));

app.post(validationFormUrl + '/submit-validation', (req, res) => {
  res.redirect(validationFormUrl);
});

app.listen(80, () => console.log('Listening on port 80'));
