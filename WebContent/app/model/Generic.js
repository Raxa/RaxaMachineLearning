Ext.define('umlsApp.model.Generic', {
	extend : 'Ext.data.Model',
	config: {
        fields: [{
            name: 'text',
            type: 'string'
        }, {
        	name: 'mayTreat',
        	type: 'string'
        }, {
        	name: 'drugs',
        	type: 'umlsApp.model.drug'
        }]
    }
});