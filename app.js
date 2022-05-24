const express = require('express');
const bodyParser = require('body-parser');
const registerRouter = require('./routes/register.js');
const loginRouter = require('./routes/login.js');
const app = express();

const port = 3000;

app.use(bodyParser.json());
app.use('/register', registerRouter);
app.use('/login', loginRouter);

app.listen(port, () => {
  console.log(`Server is running on: http://localhost:3000`);
});
