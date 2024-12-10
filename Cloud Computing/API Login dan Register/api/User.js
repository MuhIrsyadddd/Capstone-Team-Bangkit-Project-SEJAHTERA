const express = require('express');
const router = express.Router();
const User = require('./../models/User');  // Pastikan path ini benar
const bcrypt = require('bcrypt');

// Definisikan rute POST untuk signup
router.post('/signup', (req, res) => {
    let { name, email, password, dateOfBirth } = req.body;
    name = name.trim();
    email = email.trim();
    password = password.trim();
    dateOfBirth = dateOfBirth.trim();

    if (name === "" || email === "" || password === "" || dateOfBirth === "") {
        return res.json({
            status: "Failed",
            message: "Empty input fields"
        });
    } else if (!/^[a-zA-Z ]*$/.test(name)) {
        return res.json({
            status: "Failed",
            message: "Invalid name entered"
        })
    } else if (!/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(email)) {
        return res.json({
            status: "Failed",
            message: "Invalid email entered"
        })
    } else if (!new Date(dateOfBirth).getTime()) {
        return res.json({
            status: "Failed",
            message: "Invalid date of Birth entered"
        })
    } else if (password.length < 8) {
        return res.json({
            status: "Failed",
            message: "Password is too short"
        })
    } else {
        User.find({ email }).then(result => {
            if (result.length) {
                res.json({
                    status: "Failed",
                    message: "User with the provided email already exists"
                })
            } else {
                const saltRounds = 10;
                bcrypt.hash(password, saltRounds).then(hashedPassword => {
                    const newUser = new User({
                        name,
                        email,
                        password: hashedPassword,
                        dateOfBirth
                    });
                    newUser.save().then(result => {
                        res.json({
                            status: "Success",
                            message: "Signup successful",
                            data: result
                        })
                    }).catch(err => {
                        res.json({
                            status: "Failed",
                            message: "An error occurred while saving user account"
                        })
                    })
                }).catch(err => {
                    res.json({
                        status: "Failed",
                        message: "An error occurred while hashing password"
                    })
                });
            }
        }).catch(err => {
            console.log(err)
            res.json({
                status: "Failed",
                message: "An error occurred while checking for existing user"
            })
        })
    }
})

module.exports = router;
