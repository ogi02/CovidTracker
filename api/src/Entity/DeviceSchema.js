const EntitySchema = require('typeorm').EntitySchema;
const Device = require('../Model/Device').Device;

module.exports = new EntitySchema({
    name: 'Device',
    target: Device,
    columns: {
        id: {
            primary: true,
            type: 'int',
            generated: true
        },
        name: {
            type: 'varchar'
        },
        isInfected: {
<<<<<<< HEAD
            type: 'int'
=======
            type: 'int',
            default: 0
>>>>>>> 64875695a0d9f626685d8713e94ee67c28e09ee0
        }
    },
});
