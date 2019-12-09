let users = {};

let userStore = Ext.create('Ext.data.Store', {
    fields : ['id', 'name', 'email', 'type'],
    proxy : {
        type : 'memory',
        reader : {
            type : 'json',
            rootProperty : 'items'
        }
    }
});


Ext.define('App.view.UserPanel', {
    extend: 'Ext.grid.Panel',
    margin: '15 0 20 0',
    resizable: true,
    title: 'Users',
    store: userStore,

    columns: [  {
        text: 'Id',
        flex: 10 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'id'
    },{
        text: 'Name',
        flex: 35 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'name'
    },{
        text: 'Email',
        flex: 35 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'email'
    },{
        text: 'Type',
        flex: 20 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'type'
    }],
    viewConfig : {
        listeners : {
            itemdblclick : function(view, cell, cellIndex, record, row, rowIndex, e) {
                localStorage.setItem("userId", cell.data.id);
                window.location.assign("/user/modify");
            }
        }
    }
});

let userPanel = Ext.create('App.view.UserPanel', {
});

let newUserButton = Ext.create('Ext.button.Button', {
    text: 'New user',
    handler: function(){
        window.location.assign("/user/new");
    }
});

let searchUserField = Ext.create('Ext.form.TextField', {
    fieldLabel: 'Search',
    margin: "0 15 0 0",
    listeners: {
        specialkey: function(f,e){
            if(e.getKey() == e.ENTER){
                userStore.removeAll();
                updateUserTable(searchUserField.value);
            }
        }
    }
});

let searchUserButton = Ext.create('Ext.button.Button', {
    text: 'Search',
    margin: "0 15 0 0",
    handler: function(){
        userStore.removeAll();
        updateUserTable(searchUserField.value);
    }
});

let searchUserResetButton = Ext.create('Ext.button.Button', {
    text: 'Reset',
    handler: function(){
        userStore.removeAll();
        searchUserField.setValue('');
        updateUserTable(searchUserField.value);
    }
});

let searchUserPanel = Ext.create('Ext.panel.Panel', {
    layout: {
        type: 'hbox',       // Arrange child items vertically
        align: 'stretch',    // Each takes up full width
        padding: 5
    },
    items: [
        searchUserField,
        searchUserButton,
        searchUserResetButton
    ]
});


let updateUserTable = function(searchText){
    Ext.Ajax.request({
        url: "/api/user/all/",
        method: "GET",
        success: function (form, action){
            users = JSON.parse(form.responseText);
            fillUserStore();
        },
        failure: function(form, action){
            alert(form.responseText);
        }
    });
};

let fillUserStore = function (){
    var typer = "";
    for(var i=0; i<users.length; i++){

        if(users[i].type != null){
            switch (users[i].type) {
                case 1: typer = "Developer"; break;
                case 2: typer = "User"; break;
                case 3: typer = "Approver"; break;
                case 0: typer = "Admin"; break;
            }
        }


        userStore.add({id: users[i].id, name: users[i].name, email: users[i].email,
            type: typer});
    }
};
