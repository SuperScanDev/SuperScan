const bcrypt = require('bcryptjs');
const User = require('../models/userModel');
const {createAvatar} = require('@dicebear/avatars');
const style = require('@dicebear/micah');
const validator = require('validator');
const generateToken = require('../utils/generateToken');

// Login process
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
  } else if (userFind) {
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
          token: generateToken(userFind._id),
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

// Register process
const register = async (req, res) => {
  let email = req.body.email;
  const hashPassword = bcrypt.hashSync(req.body.password, 15);

  const userFind = await User.findOne({email: email});
  const emailValid = validator.isEmail(req.body.email);
  // Check whether the email has been used or not
  if (userFind) {
    res.status(401);
    res.json({
      error: true,
      message: 'Email has been used. Please use another email',
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
    const user = await User.create({
      name: req.body.name,
      email: req.body.email,
      password: hashPassword,
      avatar: `https://avatars.dicebear.com/4.6/api/micah/${req.body.name}.svg`,
    });
    if (user) {
      res.status(201);
      res.json({
        _id: user._id,
        error: false,
        message: 'User Created',
      });
    }
  }
};

module.exports = {
  login,
  register,
};
