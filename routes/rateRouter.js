const express = require('express');
const {rating} = require("../controller/ratingController")
const {verifyToken} = require("../middleware/jsonwebtoken")
// const validator = require('validator');
const rateRouter = express.Router();

rateRouter.route('/rate').post(verifyToken, rating)

module.exports = rateRouter