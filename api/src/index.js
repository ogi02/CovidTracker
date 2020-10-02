const { createConnection } = require('typeorm');

try {
    createConnection();
} catch (error) {
    return logger.error(error.stack);
}