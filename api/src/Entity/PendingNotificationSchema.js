const EntitySchema = require('typeorm').EntitySchema;
const PendingNotification = require('../Model/PendingNotification').PendingNotification;

module.exports = new EntitySchema({
    name: 'PendingNotification',
    target: PendingNotification,
    columns: {
        id: {
            primary: true,
            type: 'int',
            generated: true
        },
        timestamp: {
            type: 'varchar'
        },
        lastKnownTransport1: {
            type: 'varchar'
        },
        lastKnownTransport2: {
            type: 'varchar'
        }
    },
    relations: {
        infected: {
            target: 'Device',
            type: 'many-to-one',
            eager: true
        },
        contacted: {
            target: 'Device',
            type: 'many-to-one',
            eager: true
        }
    }
})