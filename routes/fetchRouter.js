const express = require("express");
const {verifyToken} = require("../middleware/jsonwebtoken");
const fetch = require("../controller/fetchController");
const fetchRouter = express.Router();

fetchRouter.post("/fetch", fetch);

module.exports = fetchRouter;
