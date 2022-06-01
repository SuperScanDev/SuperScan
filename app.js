const express = require('express');
const mongoose = require('mongoose');
const dotenv = require('dotenv');
const dbconnect = require('./config/dbconnect');
const authRouter = require('./routes/authRouter.js');
const rateRouter = require('./routes/rateRouter.js');
const passport = require('passport');
const session = require('express-session');
const MongoStore = require('connect-mongo');
const morgan = require('morgan');

//retrieve and load .env file
dotenv.config({path: './config/config.env'});

//connect to MongoDB
dbconnect();

const app = express();

//body parser
app.use(express.urlencoded({extended: false}));
app.use(express.json());

//logging
app.use(morgan('dev'));

//initialize session
app.use(
  session({
    secret: 'keyboard cat',
    resave: false,
    saveUninitialized: false,
    store: MongoStore.create({mongoUrl: process.env.MONGODB_URI}),
  })
);
app.use(passport.initialize());
app.use(passport.session());

app.use((error, req, res, next) => {
  const statusCode = 200 ? 500 : req.statusCode;
  res.status(statusCode);
  res.json({message: error.message});
});

//routes
app.use('/', authRouter);
app.use('/', rateRouter);

//For security, please write port number in .env file
const port = process.env.PORT || process.env.PORT_ALTERNATIVE;
app.listen(
  port,
  console.log(`Server is running on Port: http://localhost:${port}`)
);
