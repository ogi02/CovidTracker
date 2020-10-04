module.exports = {
    type: 'postgres',
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    username: process.env.DB_USERNAME,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_DATABASE,
    logging: process.env.DB_LOGGING === 'true',
    synchronize: process.env.DB_SYNCHRONIZE === 'true',
    autoSchemaSync: process.env.DB_AUTO_SCHEMA_SYNC === 'true',
    schema: process.env.DB_SCHEMA,
    entities: ['src/Entity/*.js'],
};
