const express = require('express');
const loginRouter = express.Router();

let users = [
  {
    id: 'abc123',
    name: 'ikhsanagil',
    email: 'ikhsanagil@gmail.com',
    password: '12345',
  },
];

loginRouter.post('/', (req, res) => {
  const user = users.find((user) => user.email === req.body.email);
  if (req.body.email === user.email && req.body.password === user.password) {
    res.status(201);
    res.json({
      error: false,
      message: 'success',
      loginResult: {
        userId: 'abc123',
        name: req.body.name,
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
});

module.exports = loginRouter;
