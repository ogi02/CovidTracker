const { createConnection } = require('typeorm');

createConnection().then(function () {
    const d = require('./Routes/Report');
    console.log(d.report())
}).catch(err => console.log(err));
