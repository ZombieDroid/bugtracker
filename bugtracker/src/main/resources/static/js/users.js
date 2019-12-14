let userdetails = function () {
    var user = {};

    Ext.Ajax.request({
        url: '/api/user/'+localStorage.getItem("userId"),
        method: 'GET',
        async: false,
        success: function (form, action) {
            user = JSON.parse(form.responseText);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    var typeStore = Ext.create('Ext.data.Store', {
        fields: ['type'],
        data : [
            {"value":0, "name":"Admin"},
            {"value":1, "name":"Developer"},
            {"value":2, "name":"User"},
            {"value":3, "name":"Approver"}  // TODO: jovahagyo?
        ]
    });

    var type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Type',
        store: typeStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'value'
    });
    type.select(type.store.getAt(user.type));


    var name = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Name',
        width: 400,
        bodyPadding: 10,
        value: user.name
    });

    var email = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Email',
        width: 400,
        bodyPadding: 10,
        value: user.email,
        vtype: 'email'
    });

    var password = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Password',
        fieldType: 'password',
        inputType: 'password',
        width: 400,
        bodyPadding: 10,
        value: ''
    });



    var updateButton = Ext.create('Ext.Button', {
        text: "Update",
        style: {
            border: 1
        },
        handler: function () {
            Ext.Ajax.request({
                url: "/api/user/modify",
                method: "POST",
                jsonData: getUser(),
                success: function (form, action) {
                    userdetailswindow.close();
                    updateUserTable('');
                },
                failure: function (form, action) {
                    alert(form.responseText);
                }
            })
        }
    });

    var deleteUserButton = Ext.create('Ext.Button', {
        text: 'Remove user',
        handler: function() {
            Ext.Ajax.request({
                url: "/api/user/delete",
                method: "POST",
                jsonData: getUser(),
                success: function (form, action) {
                    userdetailswindow.close();
                    updateUserTable('');
                },
                failure: function (form, action) {
                    alert(form.responseText);
                }
            });
        }
    });

    var undeleteUserButton = Ext.create('Ext.Button', {
        text: 'Restore user',
        handler: function() {
            Ext.Ajax.request({
                url: "/api/user/undelete",
                method: "POST",
                jsonData: getUser(),
                success: function (form, action) {
                    userdetailswindow.close();
                    updateUserTable('');
                },
                failure: function (form, action) {
                    alert(form.responseText);
                }
            });
        }
    });

    var getUser = function(){
        return {
            id: user.id,
            name: name.value,
            password: password.value,
            email: email.value,
            type: type.value
        }
    };

    var userdetailswindow = Ext.create('Ext.Window', {
        width: 600,
        height: 300,
        padding: 15,
        title: "Modify user",
        modal: true,
        layout: {
            type: 'vbox',
            padding: 5
        },
        items: [
            name,
            password,
            email,
            type
        ],
        buttons: (function() {
            if (user.deletedTs) {
                return [
                    undeleteUserButton,
                    updateButton
                ];
            }
            else {
                return [
                    deleteUserButton,
                    updateButton
                ]
            }
        })()
    }).show();
};

let newuser = function () {
    var typeStore = Ext.create('Ext.data.Store', {
        fields: ['type'],
        data : [
            {"value":0, "name":"Admin"},
            {"value":1, "name":"Developer"},
            {"value":2, "name":"User"},
            {"value":3, "name":"Approver"}  // TODO: jovahagyo?
        ]
    });

    var userType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Type',
        store: typeStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'value'
    });

    var name = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Name',
        width: 400,
        bodyPadding: 10
    });

    var psw = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Password',
        inputType: 'password',
        width: 400,
        bodyPadding: 10
    });

    var email = Ext.create('Ext.form.field.Text', {
        fieldLabel: 'Email',
        width: 400,
        bodyPadding: 10,
        vtype: 'email'
    });

    userType.setValue(0);

    var createButton = Ext.create('Ext.Button', {
        text: "Create",
        style: {
            border: 1
        },
        handler: function () {
            Ext.Ajax.request({
                url: "/api/user/create",
                method: "POST",
                jsonData: getUser(),
                success: function (form, action) {
                    newuserwindow.close();
                    updateUserTable('');
                },
                failure: function (form, action) {
                    alert(form.responseText);
                }
            })
        }
    });

    var getUser = function(){
        return {
            name: name.value,
            password: psw.value,
            email: email.value,
            type: userType.getValue()
        }
    };

    var newuserwindow = Ext.create('Ext.Window', {
        width: 1000,
        height: 500,
        padding: 15,
        modal: true,
        title: "New user",
        layout: {
            type: 'vbox',
            padding: 5
        },
        items: [
            name,
            psw,
            email,
            userType
        ],
        buttons: [
            createButton
        ]
    }).show();
};

let users = {};

let userStore = Ext.create('Ext.data.Store', {
    fields : [
        'id',
        'name',
        'email',
        'type',
        'deletedTs'
    ],
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
        flex: 25 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'name'
    },{
        text: 'Email',
        flex: 25 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'email'
    },{
        text: 'Type',
        flex: 20 / 100,
        sortable: false,
        hideable: false,
        dataIndex: 'type'
    },{
        text: "Delete time",
        flex: 20/100,
        sortable: false,
        hideable: false,
        dataIndex: 'deletedTs',
        renderer: function (t, meta, record) {
            var dt = record.getData().deletedTs;
            if (dt) {
                return "".concat(dt[0], "/", dt[1], "/", dt[2], " ", dt[3], ":", dt[4], ":", dt[5]);
            }
            else {
                return '';
            }
        }
    }],
    viewConfig : {
        listeners : {
            itemdblclick : function(view, cell, cellIndex, record, row, rowIndex, e) {
                localStorage.setItem("userId", cell.data.id);
                userdetails();
            }
        },
        stripeRows: false,
        getRowClass: function(record) {
            return record.get('deletedTs') ? 'deleted-user' : '';
        }
    }
});

let userPanel = Ext.create('App.view.UserPanel', {
});

let newUserButton = Ext.create('Ext.button.Button', {
    text: 'New user',
    handler: newuser
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
        url: "/api/user/searchUsers/" + searchText,
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


        userStore.add({
            id: users[i].id,
            name: users[i].name,
            email: users[i].email,
            type: typer,
            deletedTs: users[i].deletedTs
        });
    }
};
