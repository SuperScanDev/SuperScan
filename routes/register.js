const express = require('express');
const validator = require('validator');
const bcrypt = require('bcrypt');
const {nanoid} = require('nanoid');
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

  // add userId
  const id = nanoid();
  const userId = 'user-' + id;

  const emailValid = validator.isEmail(req.body.email);
  const hash = bcrypt.hashSync(req.body.password, 15);

  // Check whether the email has been used or not
  if (user) {
    res.status(401);
    res.json({
      error: true,
      message: 'email has been created. Please fill with another email',
    });
  }

  // Check whether email is valid or not
  else if (!emailValid) {
    res.status(401);
    res.json({
      error: true,
      message: 'Email is not valid',
    });
  }

  // push the new data to the database/object
  else {
    const user = {
      userId,
      name: req.body.name,
      email: req.body.email,
      password: hash,
    };
    users.push(user);
    res.status(201);
    res.json({
      error: false,
      message: 'User Created',
    });
  }
});

module.exports = registerRouter;
