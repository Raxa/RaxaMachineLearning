Ext.define('umlsApp.view.ViewPort', {
	extend : 'Ext.Panel',
	config : {
		layout : {
			type : 'vbox',
			align : 'center'
		},
		
		height: '100%',
		fullscreen : true,
		items : [ {
			xtype : 'toolbar',
			height : 44,
			docked : 'top',
			layout : {
				type : 'hbox'
			},
			items : [ {
				xtype : 'searchfield',
				id : 'searchField',
				width : '70%',
				centered: true,
				placeHolder: 'Disease/Syndrom'
			}, {
				xtype : 'numberfield',
				id : 'age',
				width : '7%',
				placeHolder: 'Age'
			}, {
				xtype: 'selectfield',
				placeHolder: 'State',
				id: 'state',
				width: '9%',
                options: [
                    {text: 'state', value: ''},
                    {text: 'AK',  value: 'AK'},
                    {text: 'AL', value: 'AL'},
                    {text: 'AZ',  value: 'AZ'},
                    {text: 'CA',  value: 'CA'},
                    {text: 'CO',  value: 'CO'},
                    {text: 'CT', value: 'CT'},
                    {text: 'DC',  value: 'DC'},
                    {text: 'FL',  value: 'FL'},
                    {text: 'GH', value: 'GH'},
                    {text: 'HI',  value: 'HI'},
                    {text: 'IA',  value: 'IA'},
                    {text: 'ID', value: 'ID'},
                    {text: 'IL',  value: 'IL'},
                    {text: 'IN',  value: 'IN'},
                    {text: 'KS', value: 'KS'},
                    {text: 'KY',  value: 'KY'},
                    {text: 'LA',  value: 'LA'},
                    {text: 'MA', value: 'MA'},
                    {text: 'MD',  value: 'MD'},
                    {text: 'MI',  value: 'MI'},
                    {text: 'NC', value: 'NC'},
                    {text: 'NJ',  value: 'NJ'},
                    {text: 'NY',  value: 'NY'},
                    {text: 'OH', value: 'OH'},
                    {text: 'OK',  value: 'OK'},
                    {text: 'PA',  value: 'PA'},
                    {text: 'PR',  value: 'PR'},
                    {text: 'TN', value: 'TN'},
                    {text: 'TX',  value: 'TX'},
                ]
			} ]
		}, {
			xtype : 'textareafield',
			readOnly : true,
			border : 1,
			width : '95%',
			label : '',
			id : 'disDef',
			clearIcon : false,
			hidden: true
		}, {
			xtype : 'container',
			hidden: true,
			width : '95%',
			height : 150,
			border : 1,
			id : 'suggestions',
		
		}, {
			xtype : 'fieldset',
			title : 'Drugs',
			id : 'listPanel',
			margin : 0,
			hidden : true,
			width : '95%',
			flex: 1,
			items : [ {
				xtype : 'list',
				id : 'genericList',
				emptyText: 'No Drugs Found',
				deferEmptyText: false,
				title : 'Drugs',
				itemTpl : [ 'Generic: {text}<br><font size="2">May Treat: {mayTreat} </font>' ],
			} ]
		} ]
	}
});