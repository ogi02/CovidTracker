const EntitySchema = require('typeorm').EntitySchema;
const Contact = require('../Model/Contact').Contact;

module.exports = new EntitySchema({
    name: 'Contact',
    target: Contact,
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
            type: 'many-to-one',
            eager: true
        },
        device2: {
            target: 'Device',
            type: 'many-to-one',
            eager: true
        }
    }
})