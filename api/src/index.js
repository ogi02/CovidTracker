const { createConnection } = require('typeorm');

createConnection().then().catch(err => console.log(err));
