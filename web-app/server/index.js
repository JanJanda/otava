const express = require('express');
const path = require('path');

const app = express();

app.use('/validator/validate', express.static(path.join(__dirname, '..', 'client', 'validate', 'build')));
app.use(express.urlencoded({ extended: false }))

app.post('/validator/validate/submit-validation', (req, res) => {
  console.log(req.body.mailik);
  res.send(req.body.mailik);
});

app.listen(80, () => console.log('Listening on port 80'));
