const {Sequelize, DataTypes} = require('sequelize');
const dbconnect = require('../config/dbconnect');

const sequelize = new Sequelize(
  dbconnect.DATABASE,
  dbconnect.USERNAME,
  dbconnect.PASSWORD,
  {
    host: dbconnect.HOST,
    dialect: dbconnect.DIALECT,
  }
);

sequelize
  .authenticate()
  .then(() => {
    console.log('connected');
  })
  .catch((error) => console.log('Error: ' + error));

let db = {};

db.Sequelize = Sequelize;
db.sequelize = sequelize;

db.users = require('./userModel.js')(sequelize, DataTypes);

db.sequelize.sync().then(() => {
  console.log('re-sync done!');
});

module.exports = db;
