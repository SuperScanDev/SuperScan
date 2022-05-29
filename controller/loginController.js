const bcrypt = require('bcrypt');
const db = require('../models');

const User = db.users;

const login = (req, res) => {
  // const emailValid = validator.isEmail(req.body.email);
  const user = User.findOne({where: {email: req.body.email}});

  res.send(user);
  // check whether the user email and password fit with the database/object
  // if (user !== null) {
  //   const pwvalid = bcrypt.compare(user.password, req.body.password);
  //   if (pwvalid === true) {
  //     res.status(201);
  //     res.json({
  //       error: false,
  //       message: 'success',
  //       loginResult: {
  //         userId: 'abc123',
  //         name: user.name,
  //         token: 'jlasdjfkldfkljasdlfjaklj456',
  //       },
  //     });
  //   } else {
  //     res.status(401);
  //     res.json({
  //       error: true,
  //       message: 'username or password incorrect',
  //     });
  //   }
  // } else if (user === null) {
  //   res.status(401);
  //   res.json({
  //     error: true,
  //     message: 'username or password incorrect',
  //   });
  // } else {
  //   res.status(500);
  //   res.json({
  //     error: true,
  //     message: 'Error 500: Internal Server Error',
  //   });
  // }
};

module.exports = login;
