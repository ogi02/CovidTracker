const { createConnection } = require('typeorm');

try {
    createConnection().then(res => console.log(res.options.entities)).catch(err => console.log(err));
} catch (error) {
    return logger.error(error.stack);
}