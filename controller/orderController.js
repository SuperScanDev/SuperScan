const User = require('../models/userModel');
const Product = require("../models/productModel");
const Transaction = require("../models/transactionModel")
const asyncHandler = require("express-async-handler")
const uuid = require("uuid")

const getProduct = asyncHandler(async (req, res) => {
    const product = await Product.findOne({
        product_name: req.params.product_name.replace(/-/g, " "),
    })

    if (product) {
        res.status(200).json(product)
    } else {
        res.status(404).json("Not Found")
    }
})

const order = asyncHandler(async (req, res) => {
    const {product, totalBills } = req.body

    const paymentCode = `SS${uuid.v4().replace(/-/g, '').toUpperCase()}`
    const customer = await User.findById(req.user._id).select('-password')

    if (product && product.length === 0){
        res.status(404).json({
            error: true,
            message: 'Empty Product'
        })
    } else {
        try {
            const createOrder = new Transaction({
                product,
                customer,
                custName: customer.name,
                totalBills,
                paymentCode,
            })
            const saveOrder = createOrder.save()
            res.status(201).json(createOrder)
        } catch (error) {
            res.status(400).json(error)
        }
    }
    
})

const history = asyncHandler(async (req, res) => {
    const userHistory = await Transaction.find({customer: req.user._id})

    if (userHistory) {
        res.status(200).json({
            message: "Success!",
            history: userHistory
        })
    } else {
        res.status(404).json({
            message: "History Not Found!"
        })
    }
    
})

module.exports = {order, getProduct, history}