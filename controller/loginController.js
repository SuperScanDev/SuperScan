const bcrypt = require('bcrypt');
const User = require('../models/userModel');

const login = async (req, res) => {
  // const emailValid = validator.isEmail(req.body.email);
  const userFind = await User.findOne({email: req.body.email});

  // check whether the user email and password fit with the database/object
  if (userFind === null) {
    res.status(401);
    res.json({
      error: true,
      message: 'username or password incorrect',
    });
  } else if (userFind !== null) {
    const pwvalid = bcrypt.compare(userFind.password, req.body.password);
    if (pwvalid === false) {
      res.status(401);
      res.json({
        error: true,
        message: 'username or password incorrect',
      });
    } else {
      res.status(201);
      res.json({
        error: false,
        message: 'success',
        loginResult: {
          userId: userFind._id,
          name: userFind.name,
          token: 'jlasdjfkldfkljasdlfjaklj456',
        },
      });
    }
  } else {
    res.status(500);
    res.json({
      error: true,
      message: 'Error 500: Internal Server Error',
    });
  }
};

module.exports = login;
