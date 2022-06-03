const express = require('express');
const showDocsRouter = express.Router();
const path = require('path');

showDocsRouter.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, '../index.html'));
});

module.exports = showDocsRouter;
