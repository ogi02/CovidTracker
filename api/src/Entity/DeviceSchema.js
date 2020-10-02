const EntitySchema = require("typeorm").EntitySchema;

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
        isSick: {
            type: 'int'
        }
    }
});
