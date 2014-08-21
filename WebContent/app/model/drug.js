Ext.define('umlsApp.model.drug', {
	extend : 'Ext.data.Model',
	config: {
        fields: [{
            name: 'drug',
            type: 'string'
        }, {
        	name: 'cui',
        	type: 'string'
        }, {
        	name: 'code',
        	type: 'string'
        }]
    }
});