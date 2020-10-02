const typeorm = require('typeorm');

typeorm.createConnection({
    type: "postgres",
    host: "localhost",
    port: 5432,
    username: "covid",
    password: "pass12345",
    database: "covid",
    synchronized: true,
    entities: [
        require("./Entity/DeviceSchema"),
        require("./Entity/MeetingSchema")
    ]
});