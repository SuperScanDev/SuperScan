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
  picture: {
    type: String,
    required: true,
  },
});

module.exports = mongoose.model("Product", Product, "Product");
