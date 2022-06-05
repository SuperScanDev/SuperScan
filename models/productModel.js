const mongoose = require("mongoose");

const Product = new mongoose.Schema({
  product_name: {
    type: String,
    required: true,
  },
  product_price: {
    type: Number,
    required: true,
  },
  product_training_photos: {
    type: String,
    required: true,
  },
  product_testing_photos: {
    type: String,
    required: true,
  },
});

module.exports = mongoose.model("Product", Product, "Product");
