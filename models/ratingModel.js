const mongoose = require('mongoose');

const Rating = new mongoose.Schema({
  user: {
    type: mongoose.Schema.Types.ObjectId,
    required: true,
    ref: 'User',
  },
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
  critic: {
    type: String,
    min: 6,
  },
  suggestion: {
    type: String,
    min: 6,
  },
  createdAt: {
    type: String,
    default: Date,
  }
});

module.exports = mongoose.model("Rating", Rating, "Rating");


