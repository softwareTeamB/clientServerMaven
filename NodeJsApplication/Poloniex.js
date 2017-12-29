//load modules
var express = require('express');
var fs = require('fs');
var Poloniex = require("Poloniex.js");

var router = express.Router();

//laat config file
var config = JSON.parse(fs.readFileSync("config.json"));

//laat de keys
var apiKey = config.poloniex.apiKey;
var apiSecretKey = config.poloniex.apiSecretKey;

//constructor
var poloniex = new Poloniex(apiKey, apiSecretKey);

//router balance
router.get('/getBalance', function (req, res) {



    poloniex.returnCompleteBalances(function (err, data) {
        if (err) {
            // handle error

            res.send(err);
        }
        
        res.send(data);
    });
});


module.exports = router;