Ext.define('umlsApp.controller.controller', {
	extend : 'Ext.app.Controller',
	config : {

		refs : {
			searchField : '#searchField',
			drugList : '#drugList',
			genericList : '#genericList',
			backToList : '#backToList'
		},

		control : {
			searchField : {
				keyup : 'onKeyUp'
			},
			genericList : {
				itemtap : 'onItemTap'
			},
			backToList : {
				tap : 'onBack'
			}
		}
	},

	onKeyUp : function(x, e, eOpts) {
		if (e.browserEvent.keyIdentifier === 'Enter') {
			this.sendQuery();
		}
	},

	suggest: function(x) {
		Ext.getCmp('searchField').setValue(x);
		this.sendQuery();
	},
	
	sendQuery : function() {
		var x = Ext.getCmp('searchField');
		console.log(x.getValue());
		var msg = "{searchRequest : { query:" + x.getValue() + ", features: [" 
		if(Ext.getCmp('age').getValue() != null && Ext.getCmp('age').getValue() != '' ) {
			msg = msg + "{name: age, value: '" + Ext.getCmp('age').getValue() + "'} ";
		}
		if(Ext.getCmp('state').getValue() != '') {
			msg = msg + "{name: state, value: '" + Ext.getCmp('state').getValue() + "'} ";
		}
		msg = msg + "]}}";
		ws.send(msg);
		Ext.getCmp('mainView').setMasked(true);
	},

	connect : function() {

		if ("WebSocket" in window) {
			console.log("WebSocket is supported by your Browser!");
			// Let us open a web socket
			ws = new WebSocket("ws://" + window.location.host
					+ "/RaxaMachineLearning/ml/umls");
			ws.onopen = function() {
				// Web Socket is connected, send data using send()
				console.log("connected");
			};
			ws.onmessage = function(evt) {
				Ext.getCmp('disDef').setHidden(true);
				Ext.getCmp('listPanel').setHidden(true);
				Ext.getCmp('suggestions').setHidden(true);
				var msg = JSON.parse(evt.data);
				// console.log("Message is received... \n" + msg);
				// this.printResults(msg);
				console.log(msg);
				if (msg.disDef != null) {
					Ext.getCmp('disDef').setValue(msg.disDef.defination);
					Ext.getCmp('disDef').setLabel(msg.disDef.name);
					Ext.getCmp('disDef').setHidden(false);
				} else if (msg.disease[0] != null) {
					var sug = '';
					for (var i = 1; i < msg.disease.length && i < 30; i++) {
						sug = sug + '<button class="tag" onclick="umlsApp.app.getController(\'controller\').suggest(\''
						+ msg.disease[i].value.name + '\')">'
						+ msg.disease[i].value.name + '</button>';;
					}
					Ext.getCmp('suggestions').setHtml(sug);
					Ext.getCmp('suggestions').setHidden(false);
				}

				items = [];
				for (var i = 0; i < msg.drugs.length; i++) {
					items[i] = new Object();
					items[i].text = msg.drugs[i].generic;
					var str = msg.drugs[i].mayTreat[0].name;
					for (var j = 1; j < msg.drugs[i].mayTreat.length; j++) {
						str = str + ", " + msg.drugs[i].mayTreat[j].name;
					}
					items[i].mayTreat = str;
					items[i].drugs = [];
					for (var j = 0; j < msg.drugs[i].drugs.length; j++) {
						var temp = {
							drug : msg.drugs[i].drugs[j].value.name,
							cui : msg.drugs[i].drugs[j].value.CUI,
							code : msg.drugs[i].drugs[j].value.code
						}
						items[i].drugs[j] = temp;
					}
				}
				;

				var treeStore = new Ext.data.Store({
					requires : 'umlsApp.model.Generic',
					model : 'umlsApp.model.Generic',
					data : items
				});
				//console.log(items);

				if (items.length == 0) {
					Ext.getCmp('listPanel').setTitle('Drugs: Not Found');
				} else {
					Ext.getCmp('listPanel').setTitle('Drugs');
				}
				// treeStore.setRoot(items);
				//console.log(treeStore.getData());
				Ext.getCmp('genericList').setStore(treeStore);
				Ext.getCmp('listPanel').setHidden(false);
				Ext.getCmp('mainView').setMasked(false);
			};
			ws.onclose = function() {
				// websocket is closed.
				console.log("Connection is closed...");
			};
		} else {
			// The browser doesn't support WebSocket
			alert("WebSocket NOT supported by your Browser!");
		}
	},

	onItemTap : function(item, idx, target, record, evt, eOpts) {
		console.log(record.getData());
		var temp = record.getData();
		var store = new Ext.data.Store({
			requires : 'umlsApp.model.drug',
			model : 'umlsApp.model.drug',
			data : temp.drugs
		});
		console.log(store.getData());
		Ext.getCmp('drugList').setStore(store);
		Ext.getCmp('genericTitle').setTitle(temp.text);
		Ext.getCmp('mainView').setActiveItem(1);
	},

	onBack : function() {
		Ext.getCmp('mainView').setActiveItem(0);
	},

	// on entry point for application, give control to Util.getViews()
	launch : function() {
		Ext.create('Ext.Container', {
			id : 'mainView',
			fullscreen : true,
			layout : 'card',
			items : [ {
				xclass : 'umlsApp.view.ViewPort'
			}, {
				xclass : 'umlsApp.view.DrugView'
			} ],
			masked : {
				xtype : 'loadmask',
				message : 'Results Loading'
			},
		});
		Ext.getCmp('mainView').setActiveItem(0);
		Ext.getCmp('mainView').setMasked(false);
		this.connect();
	}
})