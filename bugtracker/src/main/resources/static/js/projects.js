let projectdetails = function() {
    var project = {};

    Ext.Ajax.request({
        url: '/api/project/' + localStorage.getItem("projectId"),
        method: 'GET',
        async: false,
        success: function (form, action) {
            project = JSON.parse(form.responseText);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    var getDate = function (date) {
        if (date == null || date < 3) {
            return '01 01 0000';
        }
        return date[0] + ' ' + date[1] + ' ' + date[2];
    };

    var userStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var devStore = Ext.create('Ext.data.Store', {
        fields: ['id', 'name']
    });

    var approver = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Approver',
        store: userStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
    });

    var devs = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'Developer',
        store: devStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
    });

    var name = Ext.create('Ext.form.TextField', {
        fieldLabel: 'Name',
        width: 400,
        bodyPadding: 10,
        value: project.name
    });

    var desc = Ext.create('Ext.form.TextArea', {
        fieldLabel: 'Description',
        width: 400,
        bodyPadding: 10,
        value: project.description
    });

    var s1date = Ext.create('Ext.form.Date', {
        fieldLabel: 'S1 date',
        width: 400,
        format: 'Y m d',
        value: getDate(project.s1Time)
    });

    var s2date = Ext.create('Ext.form.Date', {
        fieldLabel: 'S2 date',
        width: 400,
        format: 'Y m d',
        value: getDate(project.s2Time)
    });

    var s3date = Ext.create('Ext.form.Date', {
        fieldLabel: 'S3 date',
        width: 400,
        format: 'Y m d',
        value: getDate(project.s3Time)
    });

    Ext.Ajax.request({
        url: '/api/user/getAllApprover',
        method: 'GET',
        success: function (form, action) {
            var allUsers = JSON.parse(form.responseText);
            for (var i = 0; i < allUsers.length; i++) {
                userStore.add({id: allUsers[i].id, name: allUsers[i].name});
            }

            approver.setValue(project.defaultApproverId);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    Ext.Ajax.request({
        url: '/api/user/getAllDeveloper',
        method: 'GET',
        success: function (form, action) {
            var allDev = JSON.parse(form.responseText);
            for (var i = 0; i < allDev.length; i++) {
                devStore.add({id: allDev[i].id, name: allDev[i].name});
            }

            devs.setValue(project.defaultDeveloperId);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });

    var updateButton = Ext.create('Ext.button.Button', {
        text: 'Update',
        handler: function () {
            Ext.Ajax.request({
                url: '/api/project/update',
                method: 'POST',
                jsonData: getProject(),
                success: function (form, action) {
                    this.close();
                    // window.location.assign("/project/all");
                },
                failure: function (form, action) {
                    this.close();
                    // alert(form.responseText);
                }
            });
        }
    });

    var getProject = function () {
        return {
            id: project.id,
            name: name.value,
            description: desc.value,
            defaultApproverId: approver.value,
            defaultDeveloperId: devs.value,
            s1Time: s1date.value,
            s2Time: s2date.value,
            s3Time: s3date.value
        }
    };

    Ext.create('Ext.Window', {
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
            approver,
            devs,
            s1date,
            s2date,
            s3date
        ],
        buttons: [
            updateButton
        ]
    }).show();
};

let projects = {};

let projectStore = Ext.create('Ext.data.Store', {
    fields : ['id', 'name', 'description', 'approver', 'assignee', 's1', 's2', 's3'],
    proxy : {
        type : 'memory',
        reader : {
            type : 'json',
            rootProperty : 'items'
        }
    }
});

let searchField = Ext.create('Ext.form.TextField', {
    fieldLabel: 'Search'
});

let searchButton = Ext.create('Ext.button.Button', {
    text: 'Search',
    handler: function(){
        projectStore.removeAll();
        updateProjectTable(searchField.value);
    }
});

Ext.define('BugtrackerApp.view.ProjectPanel', {
    extend: 'Ext.grid.Panel',
    title: 'Projects',
    store: projectStore,
    resizable: true,

    columns: {
        items: [{
            text: 'Id',
            flex: 5 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 'id'
        }, {
            text: 'Name',
            flex: 10 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 'name'
        }, {
            text: 'Description',
            flex: 35 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 'description'
        }, {
            text: 'Approver',
            flex: 10 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 'approver'
        }, {
            text: 'Developer',
            flex: 10 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 'assignee'
        }, {
            text: 'S1',
            flex: 10 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 's1'
        }, {
            text: 'S2',
            flex: 10 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 's2'
        }, {
            text: 'S3',
            flex: 10 / 100,
            sortable: false,
            hideable: false,
            dataIndex: 's3'
        }]
    },
    viewConfig : {
        listeners : {
            itemdblclick : function(view, cell, cellIndex, record, row, rowIndex, e) {
                localStorage.setItem("projectId", cell.data.id);
                projectdetails();
            }
        }
    },
    removeAll : function(destroy){
        var c;
        while(c=this.items.first()){
            this.remove(c, destroy);
        }
    }
});

let projectPanel = Ext.create('BugtrackerApp.view.ProjectPanel', {
});

let newButton = Ext.create('Ext.button.Button', {
    text: 'New',
    handler: newproject
});

let updateProjectTable = function(searchText){
    Ext.Ajax.request({
        url: "/api/project/searchProjects/" + searchText,
        method: "GET",
        success: function (form, action){
            projects = JSON.parse(form.responseText);
            fillProjectStore();
        },
        failure: function(form, action){
            alert(form.responseText);
        }
    });
};

let fillProjectStore = function (){
    for(var i=0; i<projects.length; i++){
        var approver = "";
        var assignee = "";

        if(projects[i].defaultApproverId != null){
            Ext.Ajax.request({
                url: "/api/user/" + projects[i].defaultApproverId,
                method: "GET",
                async: false,
                success: function (form, action){
                    approver = JSON.parse(form.responseText).name;
                },
                failure: function(form, action){
                    alert(form.responseText);
                }
            });
        }

        if(projects[i].defaultDeveloperId != null){
            Ext.Ajax.request({
                url: "/api/user/" + projects[i].defaultDeveloperId,
                method: "GET",
                async: false,
                success: function (form, action){
                    assignee = JSON.parse(form.responseText).name;
                },
                failure: function(form, action){
                    alert(form.responseText);
                }
            });
        }

        projectStore.add({id: projects[i].id, name: projects[i].name, description: projects[i].description,
            approver: approver, assignee: assignee, s1: getTime(projects[i].s1Time),
            s2: getTime(projects[i].s2Time), s3: getTime(projects[i].s3Time) });
    }
};

let getTime = function(timeArr){
    if(timeArr == null || timeArr.length < 3){
        return "-";
    }
    return timeArr[0] + "-" + timeArr[1] + "-" + timeArr[2];
};


