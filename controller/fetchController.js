const Product = require("../models/productModel");
const productFetching = require("../randomproduct.json");
// const productFetch = require("../models/productFetchModel");

const fetch = async (req, res) => {
  const product = await Product.findOne({
    product_name: productFetching.name,
  });
  if (!product) {
    res.status = 401;
    res.json({
      error: true,
      message: "Fetching product failed",
    });
  } else {
    res.status = 201;
    res.json({
      name: product.product_name,
      price: product.product_price,
    });
  }
};

module.exports = fetch;
