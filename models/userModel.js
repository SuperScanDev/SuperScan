const mongoose = require('mongoose');

const User = new mongoose.Schema({
  name: {
    type: String,
    required: true,
    min: 6,
  },
  email: {
    type: String,
    required: true,
    min: 6
  },
  password: {
    type: String,
    required: true,
    min: 6,
  },
  avatar: {
    type: String,
  },
  createdAt: {
    type: String,
    default: Date,
  }
});

module.exports = mongoose.model("User", User, "User");


