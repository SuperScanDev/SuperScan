const mongoose = require("mongoose");

const Transaction = new mongoose.Schema({
  product: [{
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
    quantity: {
      type: Number,
      required: true,
    }
  }],
  customer: {
    type: mongoose.Schema.Types.ObjectId,
    required: true,
    ref: 'User'
  },
  custName: {
    type: String,
    required: true,
  },
  totalBills: {
    type: Number,
    required: true,
  },
  paymentCode: {
    type: String,
    required: true,
    unique: true,
  },
  orderAt: {
    type: String,
    default: Date
  }

});

module.exports = mongoose.model("Transaction", Transaction, "Transaction");