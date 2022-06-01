const Akun = require("../models/userModel");
const jwt = require("jsonwebtoken");
const asyncHandler = require("express-async-handler");

const generateToken = (id) => {
  return jwt.sign({
    id
  }, process.env.TOKEN_SECRET_KEY, {
    expiresIn: '5h'
  })
}

const verifyToken = asyncHandler(async (req, res, next) => {
  let token;
  if (
    req.headers.authorization &&
    req.headers.authorization.startsWith("Bearer")
  ) {
    try {
      token = req.headers.authorization.split(" ")[1];
      if (!token) {
        res.status(401).send("Unauthorized, can't verify empty token");
      }

      const user = jwt.verify(token, process.env.TOKEN_SECRET_KEY);

      req.user = await Akun.findById(user.id).select("-password");
      next();
    } catch (error) {
      console.error(error);
      res.status(401).send("Invalid Token");
    }

  }
});

module.exports = {
  generateToken,
  verifyToken
};