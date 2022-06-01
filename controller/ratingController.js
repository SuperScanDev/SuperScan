const Rating = require('../models/ratingModel')
const User = require("../models/userModel")
const asyncHandler = require("express-async-handler")

const rating = asyncHandler(async (req, res) => {
    const {critic, suggestion} = req.body
    const user = await User.findById(req.user.id).select('-password')
    const checkUser = await Rating.findById(req.user.id).select('-password')
    if (checkUser) {
        res.status(401).json({
            error: true, 
            message: "User already fill the form"
        })
    }
    console.log("user: " + req.user)
    if (critic === "" && suggestion === ""){
        res.status(404).json({
            error: true,
            message: "Please fill in the form, don't leave it blank"
        })
    } else {
        let rating = new Rating({
            _id: user._id,
            user: user,
            name: user.name,
            email: user.email,
            critic,
            suggestion,
        })
        const giveRate = await rating.save()
        res.status(201).json({
            error: false,
            message: "Success filling the form!",
            result: {
                user: user,
                userId: user._id,
                userName: user.name,
                userEmail: user.email,
                critic: critic,
                suggestion: suggestion
            }
        })

    }

})

module.exports = {rating}