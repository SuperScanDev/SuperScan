const express = require('express');
const dotenv = require('dotenv');
const dbconnect = require('./config/dbconnect');
const authRouter = require('./routes/authRouter.js');
const morgan = require('morgan');

//retrieve and load .env file
dotenv.config({path: './config/config.env'});

//connect to MongoDB
dbconnect();

const app = express();

//body parser
app.use(express.json());
app.use(express.urlencoded({extended: false}));

//logging
app.use(morgan('dev'));

app.use((error, req, res, next) => {
  const statusCode = 200 ? 500 : req.statusCode;
  res.status(statusCode);
  res.json({message: error.message});
});

//routes
app.use('/', authRouter);

// for deployment testing, please delete after testing is success.
app.get('/', (req, res) => {
  res.send('SuperScan Server is running');
});

//For security, please write port number in .env file
const port = process.env.PORT || process.env.PORT_ALTERNATIVE;
app.listen(
  port,
  console.log(`Server is running on Port: http://localhost:${port}`)
);
