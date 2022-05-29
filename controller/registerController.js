const validator = require('validator');
const bcrypt = require('bcrypt');
const {nanoid} = require('nanoid');
const {createAvatar} = require('@dicebear/avatars');
const style = require('@dicebear/micah');

const db = require('../models');

const User = db.users;

const register = (req, res) => {
  let email = req.body.email;
  const user = User.findOne({where: {email: email}});

  res.send(User);

  // add userId
  // const id = nanoid();
  // const userId = 'user-' + id;
  // const emailValid = validator.isEmail(req.body.email);
  // const hash = bcrypt.hashSync(req.body.password, 15);

  // // Check whether the email has been used or not
  // if (user === null) {
  //   res.status(401);
  //   res.json({
  //     error: true,
  //     message: 'email has been created. Please fill with another email',
  //   });
  // }

  // // Check whether email is valid or not
  // else if (!emailValid) {
  //   res.status(401);
  //   res.json({
  //     error: true,
  //     message: 'Email is not valid',
  //   });
  // }

  // // push the new data to the database/object
  // else {
  //   let svg = createAvatar(style, {
  //     seed: 'custom-seed',
  //     size: 70,
  //   });
  //   User.create({
  //     user_id: userId,
  //     name: req.body.name,
  //     email: req.body.email,
  //     password: hash,
  //     avatar: svg,
  //   });
  //   res.status(201);
  //   res.json({
  //     error: false,
  //     message: 'User Created',
  //   });
  // }
};

module.exports = register;
