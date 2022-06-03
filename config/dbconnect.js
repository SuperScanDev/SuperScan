const mongoose = require("mongoose");

const dbconnect = async () => {
  try {
    //for security reason, please write mongodb uri in .env file
    const connect = await mongoose.connect(process.env.MONGODB_URI, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    });

    console.log(`MongoDB Connected at Host: ${connect.connection.host}`);
  } catch (error) {
    console.log(error);
    process.exit(1);
  }
};

module.exports = dbconnect;
