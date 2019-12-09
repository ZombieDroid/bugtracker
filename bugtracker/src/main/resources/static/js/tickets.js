let ticketdetails = function() {
    var ticket = {};

    Ext.Ajax.request({
        url: '/api/ticket/'+localStorage.getItem("ticketId"),
        method: 'GET',
        async: false,
        success: function (form, action) {
            ticket = JSON.parse(form.responseText);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    var name = Ext.create('Ext.form.TextField',{
        value: ticket.name,
        fieldLabel: "Name",
        allowBlank: false
    });

    var desc = Ext.create('Ext.form.TextArea', {
        value: ticket.description,
        fieldLabel: "Description"
    });

    var projectStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var reporterStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var priorityStore = Ext.create('Ext.data.Store', {
        fields: ['priority'],
        data : [
            {"value":0, "name":"S1"},
            {"value":1, "name":"S2"},
            {"value":2, "name":"S3"}
        ]
    });

    var typeStore = Ext.create('Ext.data.Store', {
        fields: ['type'],
        data : [
            {"value":0, "name":"Feature"},
            {"value":1, "name":"Bug"}
        ]
    });

    var projects = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Project',
        store: projectStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
    });

    var reporters = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Reporter',
        store: reporterStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
    });

    var ticketPrio = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Priority',
        valueField: 'value',
        store: priorityStore,
        displayField: 'name',
        queryMode: 'local'
    });

    var ticketType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Type',
        valueField: 'value',
        store: typeStore,
        displayField: 'name',
        queryMode: 'local'
    });

    ticketPrio.setValue(ticket.priority);
    ticketType.setValue(ticket.type);

    Ext.Ajax.request({
        url: '/api/project/getAll',
        method: 'GET',
        success: function (form, action) {
            allProjects = JSON.parse(form.responseText);
            for(var i=0; i<allProjects.length; i++){
                projectStore.add({id: allProjects[i].id, name: allProjects[i].name});
            }
            projects.setValue(ticket.projectId);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    Ext.Ajax.request({
        url: '/api/user/getAllUser',
        method: 'GET',
        success: function (form, action) {
            allUser = JSON.parse(form.responseText);
            for(var i=0; i<allUser.length; i++){
                reporterStore.add({id: allUser[i].id, name: allUser[i].name});
            }
            reporters.setValue(ticket.reporterId);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    var updateButton = Ext.create('Ext.button.Button', {
        text: 'Update',
        handler: function(){
            Ext.Ajax.request({
                url: '/api/ticket/update',
                method: 'POST',
                jsonData: getTicket(),
                success: function (form, action) {
                    ticketdetailswindow.close();
                    updateTicketTable('');
                },
                failure: function (form, action) {
                    alert(form.responseText);
                }
            });
        }
    });

    var getTicket = function(){
        return {
            id: ticket.id,
            name: name.getValue(),
            description: desc.getValue(),
            projectId: projects.getValue(),
            priority: ticketPrio.getValue(),
            type: ticketType.getValue(),
            reporterId: reporters.getValue()
        }
    };

    var statusStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var statuses = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Status',
        store: statusStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
    });

    Ext.Ajax.request({
        url: '/api/ticket/validStatuses/' + ticket.id,
        method: 'GET',
        success: function (form, action) {
            allStatuses = JSON.parse(form.responseText);
            for(var i=0; i<allStatuses.length; i++){
                statusStore.add({id: allStatuses[i].id, name: allStatuses[i].name});
            }

            statuses.setValue(ticket.statusId);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    var ticketdetailswindow = Ext.create('Ext.Window', {
        width: 1000,
        height: 500,
        padding: 15,
        modal: true,
        layout: {
            type: 'vbox',
            padding: 5
        },
        items: [
            name,
            desc,
            projects,
            reporters,
            statuses,
            ticketPrio,
            ticketType
        ],
        buttons: [
            updateButton
        ]
    }).show();
};


let newticket = function () {

    var projectStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var priorityStore = Ext.create('Ext.data.Store', {
        fields: ['priority'],
        data: [
            {"value": 0, "name": "S1"},
            {"value": 1, "name": "S2"},
            {"value": 2, "name": "S3"}
        ]
    });

    var typeStore = Ext.create('Ext.data.Store', {
        fields: ['type'],
        data: [
            {"value": 0, "name": "Feature"},
            {"value": 1, "name": "Bug"}
        ]
    });

    var projects = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Project',
        store: projectStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
    });

    var ticketPrio = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Priority',
        valueField: 'value',
        store: priorityStore,
        displayField: 'name',
        queryMode: 'local'
    });

    var ticketType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Type',
        valueField: 'value',
        store: typeStore,
        displayField: 'name',
        queryMode: 'local'
    });

    var name = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Name',
        width: 400,
        bodyPadding: 10
    });

    var desc = Ext.create('Ext.form.TextArea', {
        fieldLabel: 'Description',
        width: 400,
        bodyPadding: 10
    });

    ticketPrio.setValue(0);
    ticketType.setValue(0);

    var createButton = Ext.create('Ext.Button', {
        text: "Create",
        style: {
            border: 1
        },
        handler: function () {
            Ext.Ajax.request({
                url: "/api/ticket/create",
                method: "POST",
                jsonData: getTicket(),
                success: function (form, action) {
                    newticketwindow.close();
                    updateTicketTable('');
                },
                failure: function (form, action) {
                    alert(form.responseText);
                }
            })
        }
    });

    var getTicket = function () {
        return {
            name: name.value,
            description: desc.value,
            priority: ticketPrio.getValue(),
            projectId: projects.getValue(),
            type: ticketType.getValue()
        }
    };

    var reporterStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var reporters = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Reporter',
        store: reporterStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
    });

    Ext.Ajax.request({
        url: '/api/user/getAllUser',
        method: 'GET',
        success: function (form, action) {
            allUser = JSON.parse(form.responseText);
            for (var i = 0; i < allUser.length; i++) {
                reporterStore.add({id: allUser[i].id, name: allUser[i].name});
            }
            reporters.setValue(allUser[0]);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    var newticketwindow = Ext.create('Ext.Window', {
        width: 1000,
        height: 500,
        modal: true,
        title: "New ticket",
        layout: {
            type: 'vbox',
            padding: 5
        },
        items: [
            name,
            desc,
            projects,
            reporters,
            ticketPrio,
            ticketType
        ],
        buttons: [
            createButton
        ]
    }).show();

    Ext.Ajax.request({
        url: '/api/project/getAll',
        method: 'GET',
        success: function (form, action) {
            allProjects = JSON.parse(form.responseText);
            for (var i = 0; i < allProjects.length; i++) {
                projectStore.add({id: allProjects[i].id, name: allProjects[i].name});
            }

            projects.setValue(allProjects[0].id);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });
};

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
        ticketStore.removeAll();
        updateTicketTable(searchTicketField.value);
    }
});

let searchTicketResetButton = Ext.create('Ext.button.Button', {
    text: 'Reset',
    handler: function(){
        ticketStore.removeAll();
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
                ticketdetails();
            }
        }
    }
});

let ticketPanel = Ext.create('App.view.TicketPanel', {
});

let newTicketButton = Ext.create('Ext.button.Button', {
    text: 'New ticket',
    handler: newticket
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