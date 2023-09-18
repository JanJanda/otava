const express = require('express');
const path = require('path');

const app = express();

app.use('/validator/validate', express.static(path.join(__dirname, '..', 'client', 'validate', 'build')));

app.listen(80, () => console.log('Listening on port 80'));
