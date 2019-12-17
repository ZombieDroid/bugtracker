let fetchUser = function(userId) {
    Ext.Ajax.request({
        url: '/api/user/'+userId,
        method: 'GET',
        success: function (form, action) {
            return JSON.parse(form.responseText)
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });
};

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

    var ownerStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var commentStore = Ext.create('Ext.data.Store', {
        fields: ['commentText', 'commentTime', 'commentUser']
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

    var owners = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Owner',
        store: ownerStore,
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

    Ext.Ajax.request({
        url: '/api/user/getAllOwner',
        method: 'GET',
        success: function (form, action) {
            allUser = JSON.parse(form.responseText);
            for(var i=0; i<allUser.length; i++){
                ownerStore.add({id: allUser[i].id, name: allUser[i].name});
            }
            owners.setValue(ticket.ownerId);
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
            reporterId: reporters.getValue(),
            ownerId: owners.getValue(),
            statusId: statuses.getValue()
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

    var logTimeButton = Ext.create('Ext.button.Button', {
        text: 'Log Time',
        handler: logTime
    });

      var showHistoryButton = Ext.create('Ext.button.Button', {
        text: 'Show history',
        handler: showHistory

    });

  var ticketdetailspanel = Ext.create('Ext.panel.Panel',{
        width: 400,
        layout: {
            type: 'vbox',
            // padding: 5
        },
        items: [
            name,
            desc,
            projects,
            reporters,
            owners,
            statuses,
            ticketPrio,
            ticketType
        ],
        buttons: [
            logTimeButton,
            showHistoryButton,
            updateButton
        ]
    });


    Ext.Ajax.request({
        url: '/api/comment/all/'+localStorage.getItem("ticketId"),
        method: 'GET',
        async: false,
        success: function (form, action) {
            let comments = JSON.parse(form.responseText);
            for(let i=0; i<comments.length; i++){
                let d = comments[i].commentTime
                console.log(d)
                commentStore.add({
                    commentText: comments[i].commentText,
                    commentTime: new Date(Date(d[0], d[1], d[2], d[3], d[4], d[5])),
                    commentUser: comments[i].userName
                });
            }
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    let sendComment = function() {
        if (commentTextInput.getValue()) {
            Ext.Ajax.request({
                url: '/api/user/current/',
                method: 'GET',
                success: function (form, action) {
                    let currentuser = JSON.parse(form.responseText);
                    let newcomment = {
                        commentText: commentTextInput.getValue(),
                        commentTime: new Date(),
                        commentUser: currentuser.name
                    };
                    createComment(currentuser, newcomment);
                    commentStore.add(newcomment);
                    commentslist.getScrollable().scrollTo(Infinity, Infinity, true);
                    commentTextInput.setValue('');
                },
                failure: function (form, action) {
                    alert("Cannot fetch current user")
                }
            });
        }
    };

    var commentTextInput = Ext.create('Ext.form.TextArea', {
        margin: 10,
        docked: 'bottom',
        maxLength: 250,
        enforceMaxLength: true,
        enableKeyEvents:true,
        listeners: {
            keypress: function(field, evt, eOpts) {
                if (evt.getKey() == evt.ENTER) {
                    evt.preventDefault();
                    sendComment();
                }
            }
        }
    });

    var commentslist = Ext.create('Ext.DataView', {
        flex: 1,
        store: commentStore,
        disableSelection: true,
        cls: 'comments',
        scrollable: true,
        emptyText: 'There are no comments',
        autoScroll: true,
        height: '100%',
        margin: 10,
        itemTpl: [
            '<div class="header"><span class="created">{commentTime:date("Y-m-d H:i")}</span> - <span class="user">{commentUser}</span></div>',
            '<div class="content">{commentText}</div>'
        ]
    });

    let createComment = function(user, comment) {
        Ext.Ajax.request({
            url: '/api/comment/create',
            method: 'POST',
            jsonData: {
                ticketId: localStorage.getItem("ticketId"),
                userId: user.id,
                commentText: comment.commentText,
                commentTime: comment.commentTime,
                userName: user.name
            },
            success: function(form, action) {},
            failure: function (form, action) {
                alert("Cannot save comment")
            }
        });
    };

    var commentButton = Ext.create('Ext.button.Button', {
        text: 'Send',
        handler: sendComment
    });

    var commentspanel = Ext.create('Ext.panel.Panel',{
        width: 580,
        layout: {
            type: 'vbox',
            align: 'stretch',
            padding: 10
        },
        items: [
            {
                xtype: 'component',
                html: '<h2>Comments</h2>'
            },
            commentslist,
            commentTextInput
        ],
        buttons: [
            commentButton
        ]
    });

    var ticketdetailswindow = Ext.create('Ext.Window', {
        width: 1030,
        height: 500,
        padding: 15,
        modal: true,
        title: "Ticket details",
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        items: [
            ticketdetailspanel,
            {
                xtype: 'splitter',
            },
            commentspanel
        ]
    }).show();
};

var showHistory = function () {
    updateTicketHistoryTable();

    var historiesPanel = Ext.define('App.view.HistoryPanel', {
        extend: 'Ext.grid.Panel',
        title: 'History',
        store: historyStore,
        margin: '15 0 20 0',
        resizable: true,

        columns: [  {
            text: 'Date',
            flex: 5 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 'createdAt'
        },{
            text: 'Event description',
            flex: 10 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 'eventDescription'
        },{
            text: 'Free text',
            flex: 5 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 'freeText'
        }]
    });

    var historyPanel = Ext.create('App.view.HistoryPanel', {
        renderTo: Ext.getBody()
    });


    var historesWindow = Ext.create('Ext.Window', {
        width: 1000,
        height: 500,
        padding: 15,
        title:'History',
        modal: true,
        items: [
            historyPanel
        ]
    }).show();
};

var updateTicketHistoryTable = function(){
    var ticketId = localStorage.getItem('ticketId');
    console.log('update ticket: ' + ticketId);

    Ext.Ajax.request({
        url: "/api/history/all/objectid/" + ticketId,
        method: "GET",
        success: function (form, action){
            histories = JSON.parse(form.responseText);
            fillHistoryStore(histories);
            //updateTicketHistoryTable();
        },
        failure: function(form, action){
            alert(form.responseText);
        }
    });
};

var fillHistoryStore = function (histories){
    var emptyArray = [];
    historyStore = emptyArray;
    for(var i=0; i<histories.length; i++){
        historyStore.push({createdAt: histories[i].createdAt, eventDescription: histories[i].eventDescription,
            freeText: histories[i].freeText});
    }
};

var historyStore = Ext.create('Ext.data.Store', {
    fields : ['createdAt', 'eventDescription', 'freeText'],
    proxy : {
        type : 'memory',
        reader : {
            type : 'json',
            rootProperty : 'items'
        }
    }
});

let logTime = function () {

    var time = Ext.create('Ext.form.field.Number', {
        fieldLabel: 'Time',
        width: 400,
        minValue: 0,
        value: 0
    });

    var desc = Ext.create('Ext.form.TextArea', {
        fieldLabel: 'Description',
        width: 400,
        bodyPadding: 10
    });

    var logTimeButton = Ext.create('Ext.button.Button', {
        text: 'Log Time',
        handler: function() {
            Ext.Ajax.request({
                url: '/api/timelog/log',
                method: 'POST',
                jsonData: getTimeLog(),
                success: function (form, action) {
                    logTimeWindow.close();
                },
                failure: function (form, action) {
                    alert(form.responseText);
                }
            })
        }
    });

    var getTimeLog = function(){
        return {
            ticketId: localStorage.getItem("ticketId"),
            time: time.value,
            description: desc.value
        };
    };

    var logTimeWindow = Ext.create('Ext.Window', {
        width: 500,
        height: 300,
        modal: true,
        title: "Log time",
        layout: {
            type: 'vbox',
            padding: 5
        },
        items: [
            time,
            desc
        ],
        buttons: [
            logTimeButton
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
            type: ticketType.getValue(),
            ownerId: owners.getValue(),
            reporterId: reporters.getValue()
        }
    };

    var reporterStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var ownerStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var reporters = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Reporter',
        store: reporterStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
    });



    var owners = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Owner',
        store: ownerStore,
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
            reporters.setValue(allUser[0].id);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });


    Ext.Ajax.request({
        url: '/api/user/getAllOwner',
        method: 'GET',
        success: function (form, action) {
            allUser = JSON.parse(form.responseText);
            for (var i = 0; i < allUser.length; i++) {
                ownerStore.add({id: allUser[i].id, name: allUser[i].name});
            }
            owners.setValue(allUser[0].id);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });





    var newticketwindow = Ext.create('Ext.Window', {
        width: 500,
        height: 370,
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
            owners,
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
        'reporter', 'owner', 'status', 'project', 'spentTime', 'type'],
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
    },
    {
        text: 'Owner',
        flex: 15 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'owner'
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
        var owner = "";
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

        if(tickets[i].ownerId != null){
            Ext.Ajax.request({
                url: "/api/ticket/getOwner/" + tickets[i].ownerId,
                method: "GET",
                async: false,
                success: function (form, action){
                    owner = form.responseText;
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
            description: tickets[i].description, reporter: reporter,  owner: owner, status: status,
            project: project, spentTime: tickets[i].spentTime, type: responseType });
    }
};
