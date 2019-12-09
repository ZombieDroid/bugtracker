let tickets = {};

let ticketStore = Ext.create('Ext.data.Store', {
    fields : ['id', 'name', 'priority', 'description',
        'reporter', 'status', 'project', 'spentTime', 'type'],
    proxy : {
        type : 'memory',
        reader : {
            type : 'json',
            rootProperty : 'items'
        }
    }
});

let searchTicketField = Ext.create('Ext.form.TextField', {
    fieldLabel: 'Search',
    margin: "0 15 0 0",
    listeners: {
        specialkey: function(f,e){
            if(e.getKey() == e.ENTER){
                projectStore.removeAll();
                updateTicketTable(searchTicketField.value);
            }
        }
    }
});

let searchTicketButton = Ext.create('Ext.button.Button', {
    text: 'Search',
    margin: "0 15 0 0",
    handler: function(){
        projectStore.removeAll();
        updateTicketTable(searchTicketField.value);
    }
});

let searchTicketResetButton = Ext.create('Ext.button.Button', {
    text: 'Reset',
    handler: function(){
        projectStore.removeAll();
        searchTicketField.setValue('');
        updateTicketTable(searchTicketField.value);
    }
});

let searchTicketPanel = Ext.create('Ext.panel.Panel', {
    layout: {
        type: 'hbox',       // Arrange child items vertically
        align: 'stretch',    // Each takes up full width
        padding: 5
    },
    items: [
        searchTicketField,
        searchTicketButton,
        searchTicketResetButton
    ]
});

Ext.define('App.view.TicketPanel', {
    extend: 'Ext.grid.Panel',
    title: 'Tickets',
    store: ticketStore,
    margin: '15 0 20 0',
    resizable: true,

    columns: [  {
        text: 'Id',
        flex: 5 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'id'
    },{
        text: 'Name',
        flex: 10 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'name'
    },{
        text: 'Priority',
        flex: 5 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'priority'
    },{
        text: 'Description',
        flex: 20 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'description'
    },{
        text: 'Reporter',
        flex: 15 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'reporter'
    },{
        text: 'Status',
        flex: 10 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'status'
    },{
        text: 'Project',
        flex: 15 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'project'
    },{
        text: 'Spent Time',
        flex: 10 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'spentTime'
    },{
        text: 'Type',
        flex: 10 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'type'
    }],
    viewConfig : {
        listeners : {
            itemdblclick : function(view, cell, cellIndex, record, row, rowIndex, e) {
                localStorage.setItem("ticketId", cell.data.id);
                window.location.assign("/ticket");
            }
        }
    }
});

let ticketPanel = Ext.create('App.view.TicketPanel', {
});

let newTicketButton = Ext.create('Ext.button.Button', {
    text: 'New ticket',
    handler: function(){
        window.location.assign("/ticket/new");
    }
});

let updateTicketTable = function(searchText){
    Ext.Ajax.request({
        url: "/api/ticket/searchTickets/" + searchText,
        method: "GET",
        success: function (form, action){
            tickets = JSON.parse(form.responseText);
            fillTicketStore();
        },
        failure: function(form, action){
            alert(form.responseText);
        }
    });
};

let fillTicketStore = function (){
    for(var i=0; i<tickets.length; i++){
        var reporter = "";
        var project = "";
        var status = "";
        var responseType = "";

        if(tickets[i].reporterId != null){
            Ext.Ajax.request({
                url: "/api/ticket/getReporter/" + tickets[i].reporterId,
                method: "GET",
                async: false,
                success: function (form, action){
                    reporter = form.responseText;
                },
                failure: function(form, action){
                    alert(form.responseText);
                }
            });
        }

        if(tickets[i].projectId != null){
            Ext.Ajax.request({
                url: "/api/ticket/getProject/" + tickets[i].projectId,
                method: "GET",
                async: false,
                success: function (form, action){
                    project = form.responseText;
                },
                failure: function(form, action){
                    alert(form.responseText);
                }
            });
        }

        if(tickets[i].statusId != null){
            Ext.Ajax.request({
                url: "/api/status/" + tickets[i].statusId,
                method: "GET",
                async: false,
                success: function (form, action){
                    console.log(form.responseText);
                    status = JSON.parse(form.responseText).name;
                },
                failure: function(form, action){
                    alert(form.responseText);
                }
            });
        }

        if(tickets[i].type != null){
            Ext.Ajax.request({
                url: "/api/ticket/getType/" + tickets[i].type,
                method: "GET",
                async: false,
                success: function (form, action){
                    responseType = form.responseText;
                },
                failure: function(form, action){
                    alert(form.responseText);
                }
            });
        }

        ticketStore.add({id: tickets[i].id, name: tickets[i].name, priority: tickets[i].priority,
            description: tickets[i].description, reporter: reporter, status: status,
            project: project, spentTime: tickets[i].spentTime, type: responseType });
    }
};