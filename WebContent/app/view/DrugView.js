Ext.define('umlsApp.view.DrugView', {
	extend : 'Ext.Container',
	config : {
		layout : {
			align : 'center',
			type : 'vbox'
		},
		scrollable : false,
		items : [ {
			xtype : 'titlebar',

			height : 44,
			docked : 'top',
			id : 'genericTitle',
			items : [ {
				xtype : 'button',
				id : 'backToList',
				ui : 'back',
				text : 'Back'
			} ]
		}, {
			xtype : 'list',
			id : 'drugList',
			width: '95%',
			flex: 1,
			itemTpl : [ '{drug}' ],
		} ]
	}
});