const express = require('express');
const register = require('../controller/registerController');
const registerRouter = express.Router();

// registerRouter.get('/', (req, res) => {
//   res.send(svg);
// });

registerRouter.post('/', register);

module.exports = registerRouter;
