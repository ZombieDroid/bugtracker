let newproject = function () {

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
        bodyPadding: 10
    });

    var desc = Ext.create('Ext.form.TextArea', {
        fieldLabel: 'Description',
        width: 400,
        bodyPadding: 10
    });

    var s1date = Ext.create('Ext.form.Date', {
        fieldLabel: 'S1 date',
        width: 400,
        value: new Date()
    });

    var s2date = Ext.create('Ext.form.Date', {
        fieldLabel: 'S2 date',
        width: 400,
        value: new Date()
    });

    var s3date = Ext.create('Ext.form.Date', {
        fieldLabel: 'S3 date',
        width: 400,
        value: new Date()
    });

    var createButton = Ext.create('Ext.Button', {
        text: "Create",
        style: {
            border: 1
        },
        handler: function () {
            Ext.Ajax.request({
                url: "/api/project/create",
                method: "POST",
                jsonData: getProject(),
                success: function (form, action) {
                    window.location.assign("/project/all");
                },
                failure: function (form, action) {
                    alert(form.responseText);
                }
            })
        }
    });

    var getProject = function(){
        return {
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
        width: 500,
        height:300,
        title: 'New project',
        modal: true,
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
            createButton
        ]
    }).show();

    Ext.Ajax.request({
        url: '/api/user/getAllApprover',
        method: 'GET',
        success: function (form, action) {
            var allUsers = JSON.parse(form.responseText);
            for(var i=0; i<allUsers.length; i++){
                userStore.add({id: allUsers[i].id, name: allUsers[i].name});
            }

            approver.setValue(allUsers[0].id);
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
            for(var i=0; i<allDev.length; i++){
                devStore.add({id: allDev[i].id, name: allDev[i].name});
            }

            devs.setValue(allDev[0].id);
        },
        failure: function (form, action) {
            alert(form.responseText);
        }
    });
};