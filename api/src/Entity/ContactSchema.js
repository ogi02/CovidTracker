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
        },
        lastKnownTransport1: {
            type: 'varchar'
        },
        lastKnownTransport2: {
            type: 'varchar'
        },
        longitude: {
            type: 'float'
        },
        latitude: {
            type: 'float'
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