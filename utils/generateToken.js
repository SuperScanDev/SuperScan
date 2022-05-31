const jwt = require('jsonwebtoken');

const generateToken = (id) =>{
    return jwt.sign({id}, process.env.TOKEN_SECRET_KEY, {
        expiresIn: '5h'
    })
}

module.exports = generateToken;