var dhxLayout;
function doOnLoad() {

    changeheigth();
    onUserDocumentLoad();

    dhxLayout = new dhtmlXLayoutObject("parentId", "2U");
    dhxLayout.cells("a").setText("菜单列表");
    dhxLayout.cells("b").setText("菜单明细");

    //var rightHight = document.body.offsetHeight;
    dhxLayout.cells("a").setWidth(240);
    dhxLayout.cells("a").attachObject("menuId");
    dhxLayout.cells("b").attachObject("gridId");
}

function changeheigth() {
    //画面有效高度：553-92=461
    // alert(document.body.offsetHeight);
    document.all("rootUl").style.height = document.body.offsetHeight - 75;
}

var menu;
function onUserDocumentLoad() {

    var menuText = "添加主菜单,,,,/images/frame/insert.gif,add;-,0,,,,;修改主菜单,,,,/images/frame/property.gif,edit;";
    menuText = menuText + "-,0,,,,;删除主菜单,,,,/images/frame/delete.gif,delete;";


    var domdoc = createDomDocument();

    var retStr = createselectArr("parentid", "text", "0", "sm0027");

    if (retStr != false) {

        domdoc.loadXML(retStr);

        var Root = domdoc.documentElement;

        treeInit();
        Nodes_clear();


        menu = new Menu(menuText);

        var nRoot = vNodes.add("", "", "0", "菜单管理", "/images/frame/domain.gif");

        nRoot.xData.xNode = new Object();
        nRoot.xData.xNode.appID = "0";

        nRoot.xData.xNode.childCount = 0;
        nRoot.xData.waitforLoad = false;

        if (Root.childNodes.length > 0) {
            showApplications(Root.firstChild, nRoot);
        }
        nRoot.setExpanded(true);

    }
    domdoc.free;

}

function showApplications(eleRoot, nFather, bManagedApp) {
    var node = eleRoot;
    var count = 0;

    while (node) {
        var nApp = nFather.add("tvwChild", decode(getAttrValue(node.childNodes[0], "value")), decode(getAttrValue(node.childNodes[1], "value")), "/images/frame/application.gif");

        nApp.xData.xNode = new Object();
        nApp.xData.xNode.appID = "1";

        nApp.span.parentid = "0";

        nApp.span.title = decode(getAttrValue(node.childNodes[2], "value"));
        nApp.span.childcount = decode(getAttrValue(node.childNodes[3], "value"));
        nApp.span.childindex = decode(getAttrValue(node.childNodes[4], "value"));

        nApp.xData.codeName = decode(getAttrValue(node.childNodes[1], "value"));
        nApp.xData.targetmachine = decode(getAttrValue(node.childNodes[5], "value"));

        if (decode(getAttrValue(node.childNodes[3], "value")) != "0") {
            nApp.xData.waitforLoad = true;

            nApp.add("tvwChild", "", "载入中...");
        }
        node = node.nextSibling;
        count = count + 1;


    }


}

//////////////显示右边菜单

function menuTreeBeforePopup(el) {
    var tNode = document.lastNode;

    var xNode = tNode.xData.xNode;
    var type = xNode.type;

    switch (el.menuData) {

        case "add":    el.disableItem = xNode.appID == "30";
            break;
        case "edit":    el.disableItem = xNode.appID == "0";
            break;
        case "delete":    el.disableItem = xNode.appID == "0";
            break;

    }
}

function menuTreeClick(el) {
    var tNode = document.lastNode;
    var xNode = tNode.xData.xNode;
    var type = xNode.type;

    //try
    //{
    if (tNode) {
        var xNode = tNode.xData.xNode;

        switch (el.menuData) {
            case "add":    addSubNode(tNode);
                break;
            case "edit":    editSubNode(tNode);
                break;
            case "delete":    deleteSubNode(tNode);
                break;

        }
    }
    //}
    //catch(e)
    //{
    //	showError(e);
    //}
}


function tvNodeRightClick(node) {

    var tNode = node;

    if (tNode) {
        document.lastNode = tNode;

        show(event.x, event.y);
    }

}


function tvNodeExpand() {
    try {
        var n = document.node;

        if (n.xData.waitforLoad)
            loadChildren(n);
    }
    catch(e) {

    }
}
///装载树形子节点
function loadChildren(n) {
    try {
        var retStr = createselectArr("parentid", "text", n.key, "sm0027");
        if (retStr != "false") {

            var domdoc = createDomDocument();
            domdoc.loadXML(retStr);

            var Root = domdoc.documentElement;
            var node = Root.firstChild;

            n.xData.waitforLoad = false;
            n.removeChildren();
            while (node) {
                var nApp = n.add("tvwChild", decode(getAttrValue(node.childNodes[0], "value")), decode(getAttrValue(node.childNodes[1], "value")), "/images/frame/application.gif");

                nApp.xData.xNode = new Object();
                nApp.xData.xNode.appID = "1";

                nApp.span.parentid = n.key;
                nApp.span.title = decode(getAttrValue(node.childNodes[2], "value"));
                nApp.span.childcount = decode(getAttrValue(node.childNodes[3], "value"));
                nApp.span.childindex = decode(getAttrValue(node.childNodes[4], "value"));
                nApp.xData.targetmachine = decode(getAttrValue(node.childNodes[5], "value"));
                nApp.xData.codeName = decode(getAttrValue(node.childNodes[1], "value"));


                if (decode(getAttrValue(node.childNodes[3], "value")) != "0") {
                    nApp.xData.waitforLoad = true;

                    nApp.add("tvwChild", "", "载入中...");
                }
                node = node.nextSibling;


            }
            domdoc.free;
        }
    }
    catch(e) {
    }

}
function tvNodeSelected() {

    document.all("paramValue").value = document.node.key;

    //if (document.node.key !="0")
    //{

    innerDocTD.innerHTML = "<iframe id='frmContainer' src='MenuJsp.jsp' style='WIDTH:98%;HEIGHT:98%' frameborder='0' scrolling='auto'></iframe>";

    //}
    imgClick();
}

function addSubNode(node) {
    //try{


    var sfeature = "dialogwidth:300px; Dialogheight:250px;center:yes;help:no;resizable:yes;scroll:no;status:no";

    var spath = "editmainmenu.jsp";
    var arg = new Object();
    arg.type = "insert";
    arg.parentid = node.key;


    var goupstr = window.showModalDialog(spath, arg, sfeature);

    onUserDocumentLoad();


    //}catch(e){
    //}

}

function editSubNode(node) {
    //try{
    var sfeature = "dialogwidth:300px; Dialogheight:250px;center:yes;help:no;resizable:yes;scroll:no;status:no";

    var spath = "editmainmenu.jsp";
    var arg = new Object();
    arg.type = "update";
    arg.parentid = node.span.parentid;
    arg.id = node.key;
    arg.title = node.span.title;
    arg.caption = node.xData.codeName;
    arg.index = node.span.childindex;
    if (node.xData.targetmachine == "null"){
        arg.targetmachine = "default";
    }else{
        arg.targetmachine = node.xData.targetmachine;
    }
    var goupstr = window.showModalDialog(spath, arg, sfeature);

    onUserDocumentLoad();


    //}catch(e){
    //}

}

function deleteSubNode(node) {

    if (confirm("您确定要删除当前记录吗？")) {
        var whereArr = new Array("menuid&text&" + node.key);
        if (createDelArray(whereArr, "sm0026") + "" == "true") {
            onUserDocumentLoad();

        }

    }


}
