const express = require('express');
const bcrypt = require('bcrypt');
// const validator = require('validator');
const loginRouter = express.Router();

let users = [
  {
    userId: 'abc123',
    name: 'ikhsanagil',
    email: 'ikhsanagil@gmail.com',
    password: `12345`,
  },
];

loginRouter.post('/', (req, res) => {
  // const emailValid = validator.isEmail(req.body.email);
  const user = users.find((user) => user.email === req.body.email);

  // check whether the user email and password fit with the database/object
  if (user) {
    const pwvalid = bcrypt.compare(user.password, req.body.password);
    if (pwvalid === true) {
      res.status(201);
      res.json({
        error: false,
        message: 'success',
        loginResult: {
          userId: 'abc123',
          name: user.name,
          token: 'jlasdjfkldfkljasdlfjaklj456',
        },
      });
    } else {
      res.status(401);
      res.json({
        error: true,
        message: 'username or password incorrect',
      });
    }
  } else if (!user) {
    res.status(401);
    res.json({
      error: true,
      message: 'username or password incorrect',
    });
  } else {
    res.status(500);
    res.json({
      error: true,
      message: 'Error 500: Internal Server Error',
    });
  }
});

module.exports = loginRouter;
