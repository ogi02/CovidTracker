const EntitySchema = require('typeorm').EntitySchema;
const Meeting = require('../Model/Meeting').Meeting;

module.exports = new EntitySchema({
    name: 'Meeting',
    target: Meeting,
    columns: {
        id: {
            primary: true,
            type: 'int',
            generated: true
        },
        timestamp: {
            type: 'timestamp'
        }
    },
    relations: {
        device1: {
            target: 'Device',
            type: 'many-to-one'
        },
        device2: {
            target: 'Device',
            type: 'many-to-one'
        }
    }
})