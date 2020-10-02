import {createConnection, Connection} from "typeorm";

const connection = await createConnection({
    type: "postgresql",
    host: "localhost",
    port: 5432,
    username: "covid",
    password: "pass12345",
    database: "covid"
});