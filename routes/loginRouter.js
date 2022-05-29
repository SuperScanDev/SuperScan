const express = require('express');
const login = require('../controller/loginController');
// const validator = require('validator');
const loginRouter = express.Router();

loginRouter.post('/', login);

module.exports = loginRouter;
