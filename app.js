const express = require('express');
const bodyParser = require('body-parser');
const registerRouter = require('./routes/registerRouter.js');
const loginRouter = require('./routes/loginRouter.js');

require('./config/dbconnect');

const app = express();

const port = 3000;

app.use(bodyParser.json());
app.use('/register', registerRouter);
app.use('/login', loginRouter);

app.listen(port, () => {
  console.log(`Server is running on: http://localhost:3000`);
});
