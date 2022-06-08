const express = require("express");
const {verifyToken} = require("../middleware/jsonwebtoken");
const {order, getProduct} = require("../controller/orderController");
const orderRouter = express.Router();

orderRouter.get("/getProduct/:product_name", getProduct);
orderRouter.route("/order").post(verifyToken, order);

module.exports = orderRouter;
