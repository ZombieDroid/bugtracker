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
    // height: 600,
    // width: 1000,
    title: 'Projects',
    store: projectStore,

    columns: [  {
        text: 'Id',
        // columnWidth: 0.5,
        sortable: false,
        hideable: false,
        dataIndex: 'id'
    },{
        text: 'Name',
        // width: 100,
        sortable: false,
        hideable: false,
        dataIndex: 'name'
    },{
        text: 'Description',
        // width: 100,
        sortable: false,
        hideable: false,
        dataIndex: 'description'
    },{
        text: 'Approver',
        // width: 100,
        sortable: false,
        hideable: false,
        dataIndex: 'approver'
    },{
        text: 'Developer',
        // width: 100,
        sortable: false,
        hideable: false,
        dataIndex: 'assignee'
    },{
        text: 'S1',
        // width: 100,
        sortable: false,
        hideable: false,
        dataIndex: 's1'
    },{
        text: 'S2',
        // width: 100,
        sortable: false,
        hideable: false,
        dataIndex: 's2'
    },{
        text: 'S3',
        // width: 100,
        sortable: false,
        hideable: false,
        dataIndex: 's3'
    }],
    viewConfig : {
        listeners : {
            itemdblclick : function(view, cell, cellIndex, record, row, rowIndex, e) {
                localStorage.setItem("projectId", cell.data.id);
                window.location.assign("/project");
            }
        }
    }
});

let projectPanel = Ext.create('BugtrackerApp.view.ProjectPanel', {
    // renderTo: "projects"
});

let newButton = Ext.create('Ext.button.Button', {
    text: 'New',
    handler: function(){
        window.location.assign("/project/new");
    }
});

// Ext.create('Ext.panel.Panel', {
//     renderTo: Ext.getBody(),
//     width: 1000,
//     height: 600,
//     layout: {
//         type: 'vbox',
//         padding: 5
//     },
//     items: [
//         searchField,
//         searchButton,
//         projectPanel
//     ],
//     buttons: [
//         newButton
//     ]
// });

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

// updateProjectTable("");

