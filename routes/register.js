const express = require('express');
const validator = require('validator');
// const bcrypt = require('bcrypt');
const registerRouter = express.Router();

let users = [
  {
    id: 'abc123',
    name: 'ikhsanagil',
    email: 'ikhsanagil@gmail.com',
    password: '12345',
  },
];

registerRouter.get('/', (req, res) => {
  res.send(users);
});

registerRouter.post('/', (req, res) => {
  const user = users.find((user) => user.email === req.body.email);
  // res.send(datum);
  const emailValid = validator.isEmail(req.body.email);

  //cek apakah usernamenya sama
  if (user) {
    res.status(401);
    res.json({
      error: true,
      message: 'Username is created. Please fill with another username',
    });
  }

  //cek apakah email valid
  else if (!emailValid) {
    res.status(401);
    res.json({
      error: true,
      message: 'Email is not valid',
    });
  } else {
    users.push(req.body);
    res.status(201);
    res.json({
      error: false,
      message: 'User Created',
    });
  }
});

module.exports = registerRouter;
