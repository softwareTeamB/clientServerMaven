//load modules
var express = require('express');
var fs = require('fs');

var app = express();

//app.use
app.use('/poloniex', require("./Poloniex.js"));




//start server
app.listen(9091, function(){
    console.log("Client server");
});