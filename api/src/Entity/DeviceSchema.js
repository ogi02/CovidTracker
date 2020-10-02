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
            type: 'int',
            default: 0
        }
    },
});
