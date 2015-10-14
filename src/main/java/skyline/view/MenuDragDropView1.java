package skyline.view;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.spark.domain.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import skyline.common.utils.MessageUtil;
import skyline.repository.model.Ptmenu;
import skyline.service.PlatformService;
import skyline.service.ToolsService;
import test.Node;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ManagedBean
@ViewScoped
public class MenuDragDropView1 implements Serializable {

    private TreeNode root1;
    private TreeNode selectedNode;
    private TreeNode pnode;
    private TreeNode node1;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<SelectItem> branchList;
    private String parentMenuId;
    private String menuLabel;
    private String menuAction;
    private String levelIdx;
    private String menudEsc;
    private String targetMachine;
    private Ptmenu ptmenuDel;
    private Ptmenu ptmenuUpd;
    private String strSubmitType;
    private String selectedNodeName ;


    //    目录树新增
    private String treeMenuId;
    private String treeMenuLabel;
    private Integer treeLevelIdx;
    private String treeMenudEsc;
    private String treeTargetMachine;
    private String treeMenuLabelAdd;
    private Integer treeLevelIdxAdd;
    private String treeMenudEscAdd;
    private String treeTargetMachineAdd;
    private String treeMenuIdDel;
    private String treeMenuLabelDel;
    private Integer treeLevelIdxDel;
    private String treeMenudEscDel;
    private String treeTargetMachineDel;
    String pTreeMenuIdDel ;
    String pTreeMenuNameDel ="";

    String sql ="";
    String sqlTree ="";
    String sql1 ="";
    String sql2 ="";
    String sqldel ="";
    String pTreeMenuId ;
    String pTreeMenuName ="";
    private List<Ptmenu> menuList;
    private List<Ptmenu> treeMenuList;
    private List<Ptmenu> ptreeMenuList;
    // private List<Ptmenu> filteredDetlList;
    List<Node> nodeList = new ArrayList<Node>();
//    private List<Theme> treeMenus;
    private Theme treeTheme;


    private String treeMenu;
    private String ptreeMenu;

    private String treeMenuAdd;
    private String treeMenuAddId;
    private String treeMenuUpd;
    private List<SelectItem> treeMenus;
    private List<SelectItem> treeMenuDefault ;
    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;
    @ManagedProperty(value = "#{platformService}")
    private PlatformService platformService;
    @ManagedProperty(value = "#{skylineJdbc}")
    private NamedParameterJdbcTemplate skylineJdbc;


    @PostConstruct
    public void init() {
        root1 = new DefaultTreeNode("Root", null);
        TreeNode node0 = new DefaultTreeNode("菜单管理", root1);

        try {
            sql = "SELECT J.MENUID id, J.MENULABEL nodeName, J.PARENTMENUID parentId,J.* FROM PTMENU J " +
                    " WHERE J.ISLEAF = '0' ORDER BY J.LEVELIDX ASC";
            nodeList = skylineJdbc.query(sql,new BeanPropertyRowMapper<Node>(Node.class));
            treeMenuList = skylineJdbc.query(sql,new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
            sqlTree ="SELECT * FROM PTMENU T ORDER BY T.LEVELIDX ASC";
            ptreeMenuList = skylineJdbc.query(sqlTree,new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
//        treeMenus = new ArrayList<Theme>();
        SelectItemGroup g1 = new SelectItemGroup("菜单管理");
        treeMenus = new ArrayList<SelectItem>();


        for(Node nodeAll : nodeList){

            treeMenus.add(new SelectItem(nodeAll.getId(), nodeAll.getNodeName()));
            if(nodeAll.getParentId()==0){
                pnode  = new DefaultTreeNode(nodeAll.getNodeName(), node0);
//                for(Node nodeAllChild : nodeList) {
//                    if (nodeAllChild.getParentId() == nodeAll.getId()) {
//                        pnode = new DefaultTreeNode(nodeAllChild.getNodeName(), pnode);
//                    }
//                }
                treeRecursion(nodeAll.getId());
            }
        }
//        for(Ptmenu menuTem : ptreeMenuList) {
//            if ("system".equals(menuTem.getTargetmachine())&&"1".equals(menuTem.getIsleaf().toString())) {
//                TreeNode node0 = new DefaultTreeNode(menuTem.getMenulabel(),root1);
//                if("菜单管理".equals(menuTem.getMenulabel())){
//                    for(Ptmenu menuChildTem : ptreeMenuList) {
//                        if("0".equals(menuChildTem.getIsleaf().toString())&&"0".equals(menuChildTem.getParentmenuid())) {
//                            node1 = new DefaultTreeNode(menuChildTem.getMenulabel(), node0);
//                            treeRecursion(menuChildTem.getMenuid());
//                        }
//                    }
//                }
//            }
//        }

        ptmenuUpd = new Ptmenu();
        ptmenuDel = new Ptmenu();
        strSubmitType = "";
    }
    public void treeRecursion(int treeid){
        for(Node nodeAllChild : nodeList) {
            if (nodeAllChild.getParentId() == treeid) {
                pnode = new DefaultTreeNode(nodeAllChild.getNodeName(), pnode);
                if(hasChild(nodeAllChild.getId())){
                    treeRecursion(nodeAllChild.getId());
                }
            }
        }
    }
//    判断主菜单是否有子节点
    public boolean hasChild(int treeid){
        boolean childFlag = false;
        for(Node nodeAllChild : nodeList) {
            if (nodeAllChild.getParentId() == treeid) {
               childFlag = true;
               break;
            }
        }
        return childFlag;
    }
//public void treeRecursion(String treeid){
//    for(Ptmenu nodeAll : ptreeMenuList) {
//        if (treeid.equals(nodeAll.getParentmenuid())&&"0".equals(nodeAll.getIsleaf().toString())) {
//            node1 = new DefaultTreeNode(nodeAll.getMenulabel(), node1);
//            if(hasChild(nodeAll.getMenuid())){
//                treeRecursion(nodeAll.getMenuid());
//            }
//        }
//    }
//}
//    //    判断主菜单是否有子节点
//    public boolean hasChild(String treeid){
//        boolean childFlag = false;
//        for(Ptmenu nodeAllChild : ptreeMenuList) {
//            if (treeid.equals(nodeAllChild.getParentmenuid())&&"0".equals(nodeAllChild.getIsleaf().toString())) {
//                childFlag = true;
//                break;
//            }
//        }
//        return childFlag;
//    }

    //    判断所有的菜单是否有子节点
public boolean hasChildAll(String treeid){
    boolean childFlag = false;
    for(Ptmenu nodeAllChild : ptreeMenuList) {
        if (treeid.equals(nodeAllChild.getParentmenuid())) {
            childFlag = true;
            break;
        }
    }
    return childFlag;
}

    public String onQuery() {
        try {
            if(!"".equals(this.selectedNode.toString())) {
                selectedNodeName = this.selectedNode.toString();
            }
        } catch (Exception e){
            selectedNodeName = selectedNodeName == null ? "菜单管理":selectedNodeName ;
        }
        try {
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("menuName", selectedNodeName);
            if("菜单管理".equals(selectedNodeName)){
                sql = "SELECT * FROM PTMENU T WHERE T.ISLEAF <>'0' ORDER BY T.LEVELIDX ASC";
            }else{
                sql = "SELECT * FROM PTMENU T WHERE T.ISLEAF <>'0' AND T.PARENTMENUID IN(SELECT MENUID FROM PTMENU WHERE MENULABEL in (:menuName)) ORDER BY T.LEVELIDX ASC";
            }

            menuList = skylineJdbc.query(sql,paramMap, new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
        return null;
    }

    public void selectRecordAction(String strSubmitTypePara,Ptmenu ptmenuSelectedPara){
        try {
            if("Del".equals(strSubmitTypePara)){
                ptmenuDel =(Ptmenu) BeanUtils.cloneBean(ptmenuSelectedPara);
            }else if("Upd".equals(strSubmitTypePara)){
                ptmenuUpd =(Ptmenu) BeanUtils.cloneBean(ptmenuSelectedPara);
            }
            strSubmitType = strSubmitTypePara;
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        pTreeMenuId="";
        pTreeMenuName="";
        treeMenu="";
        treeMenuDefault = new ArrayList<SelectItem>();
        treeMenuDefault.clear();
        for(Node pNodeAll : nodeList){
            if (ptmenuSelectedPara.getParentmenuid().equals(String.valueOf(pNodeAll.getId()))||ptmenuSelectedPara.getParentmenuid() == String.valueOf(pNodeAll.getId())){
                pTreeMenuId=String.valueOf(pNodeAll.getId());
                pTreeMenuName = pNodeAll.getNodeName();
                treeMenuDefault.add(new SelectItem(pNodeAll.getId(),pNodeAll.getNodeName()));
            }
        }
    }



    public List<SelectItem> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<SelectItem> branchList) {
        this.branchList = branchList;
    }


    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getMenuLabel() {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel) {
        this.menuLabel = menuLabel;
    }

    public String getMenuAction() {
        return menuAction;
    }

    public void setMenuAction(String menuAction) {
        this.menuAction = menuAction;
    }

    public String getLevelIdx() {
        return levelIdx;
    }

    public void setLevelIdx(String levelIdx) {
        this.levelIdx = levelIdx;
    }

    public String getMenudEsc() {
        return menudEsc;
    }

    public void setMenudEsc(String menudEsc) {
        this.menudEsc = menudEsc;
    }

    public String getTargetMachine() {
        return targetMachine;
    }

    public void setTargetMachine(String targetMachine) {
        this.targetMachine = targetMachine;
    }

    public Ptmenu getPtmenuDel() {
        return ptmenuDel;
    }

    public void setPtmenuDel(Ptmenu ptmenuDel) {
        this.ptmenuDel = ptmenuDel;
    }

    public Ptmenu getPtmenuUpd(){
        return ptmenuUpd;
    }
    public void setPtmenuUpd(Ptmenu ptmenuUpd){
        this.ptmenuUpd = ptmenuUpd;
    }
    public List<Ptmenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Ptmenu> menuList) {
        this.menuList = menuList;
    }
    public List<Ptmenu> getTreeMenuList() {
        return treeMenuList;
    }

    public void setTreeMenuList(List<Ptmenu> treeMenuList) {
        this.treeMenuList = treeMenuList;
    }
    public List<Ptmenu> getPtreeMenuList() {
        return ptreeMenuList;
    }

    public void setPtreeMenuList(List<Ptmenu> ptreeMenuList) {
        this.ptreeMenuList = ptreeMenuList;
    }

    public ToolsService getToolsService() {
        return toolsService;
    }

    public void setToolsService(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    public PlatformService getPlatformService() {
        return platformService;
    }

    public void setPlatformService(PlatformService platformService) {
        this.platformService = platformService;
    }

    public NamedParameterJdbcTemplate getSkylineJdbc() {
        return skylineJdbc;
    }

    public void setSkylineJdbc(NamedParameterJdbcTemplate skylineJdbc) {
        this.skylineJdbc = skylineJdbc;
    }

    public TreeNode getRoot1() {
        return root1;
    }
    public String getpTreeMenuId() {
        return pTreeMenuId;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
//       if(selectedNode != null&&!"".equals(selectedNode)) {
           this.selectedNode = selectedNode;
//       }
    }
//    public List<Theme> getTreeMenus() {
//        return treeMenus;
//    }
//
//    public Theme getTreeTheme() {
//        return treeTheme;
//    }
    public List<SelectItem> getTreeMenus() {
        return treeMenus;
    }

    public void setTreeMenus(List<SelectItem> treeMenus) {
        this.treeMenus = treeMenus;
    }
    public String getTreeMenu() {
        return treeMenu;
    }

    public void setTreeMenu(String treeMenu) {
        this.treeMenu = treeMenu;
    }

    public String getTreeMenuAdd() {
        return treeMenuAdd;
    }

    public void setTreeMenuAdd(String treeMenuAdd) {
        this.treeMenuAdd = treeMenuAdd;
    }

    public String getTreeMenuUpd() {
        return treeMenuUpd;
    }

    public void setTreeMenuUpd(String treeMenuUpd) {
        this.treeMenuUpd = treeMenuUpd;
    }

    public String getPtreeMenu() {
        return ptreeMenu;
    }

    public void setPtreeMenu(String ptreeMenu) {
        this.ptreeMenu = ptreeMenu;
    }

    public String getpTreeMenuName() {
        return pTreeMenuName;
    }

    public void setpTreeMenuName(String pTreeMenuName) {
        this.pTreeMenuName = pTreeMenuName;
    }

    public List<SelectItem> getTreeMenuDefault() {
        return treeMenuDefault;
    }

    public void setTreeMenuDefault(List<SelectItem> treeMenuDefault) {
        this.treeMenuDefault = treeMenuDefault;
    }
    public void setTreeTheme(Theme treeTheme) {
        this.treeTheme = treeTheme;
    }

    public String getTreeMenuId() {
        return treeMenuId;
    }

    public void setTreeMenuId(String treeMenuId) {
        this.treeMenuId = treeMenuId;
    }
    public String getTreeMenuLabel() {
        return treeMenuLabel;
    }
    public void setTreeMenuLabel(String treeMenuLabel) {
        this.treeMenuLabel = treeMenuLabel;
    }
    public Integer getTreeLevelIdx() {
        return treeLevelIdx;
    }
    public void setTreeLevelIdx(Integer treeLevelIdx) {
        this.treeLevelIdx = treeLevelIdx;
    }
    public String getTreeMenudEsc() {
        return treeMenudEsc;
    }
    public void setTreeMenudEsc(String treeMenudEsc) {
        this.treeMenudEsc = treeMenudEsc;
    }
    public String getTreeTargetMachine() {
        return treeTargetMachine;
    }
    public void setTreeTargetMachine(String treeTargetMachine) {
        this.treeTargetMachine = treeTargetMachine;
    }

    public String getTreeTargetMachineAdd() {
        return treeTargetMachineAdd;
    }

    public void setTreeTargetMachineAdd(String treeTargetMachineAdd) {
        this.treeTargetMachineAdd = treeTargetMachineAdd;
    }

    public String getTreeMenuLabelAdd() {
        return treeMenuLabelAdd;
    }

    public void setTreeMenuLabelAdd(String treeMenuLabelAdd) {
        this.treeMenuLabelAdd = treeMenuLabelAdd;
    }

    public Integer getTreeLevelIdxAdd() {
        return treeLevelIdxAdd;
    }

    public void setTreeLevelIdxAdd(Integer treeLevelIdxAdd) {
        this.treeLevelIdxAdd = treeLevelIdxAdd;
    }

    public String getTreeMenudEscAdd() {
        return treeMenudEscAdd;
    }

    public void setTreeMenudEscAdd(String treeMenudEscAdd) {
        this.treeMenudEscAdd = treeMenudEscAdd;
    }

    public String getTreeMenuIdDel() {
        return treeMenuIdDel;
    }

    public void setTreeMenuIdDel(String treeMenuIdDel) {
        this.treeMenuIdDel = treeMenuIdDel;
    }

    public String getTreeMenuLabelDel() {
        return treeMenuLabelDel;
    }

    public void setTreeMenuLabelDel(String treeMenuLabelDel) {
        this.treeMenuLabelDel = treeMenuLabelDel;
    }

    public Integer getTreeLevelIdxDel() {
        return treeLevelIdxDel;
    }

    public void setTreeLevelIdxDel(Integer treeLevelIdxDel) {
        this.treeLevelIdxDel = treeLevelIdxDel;
    }

    public String getTreeMenudEscDel() {
        return treeMenudEscDel;
    }

    public void setTreeMenudEscDel(String treeMenudEscDel) {
        this.treeMenudEscDel = treeMenudEscDel;
    }

    public String getTreeTargetMachineDel() {
        return treeTargetMachineDel;
    }

    public void setTreeTargetMachineDel(String treeTargetMachineDel) {
        this.treeTargetMachineDel = treeTargetMachineDel;
    }

    public String getpTreeMenuIdDel() {
        return pTreeMenuIdDel;
    }

    public void setpTreeMenuIdDel(String pTreeMenuIdDel) {
        this.pTreeMenuIdDel = pTreeMenuIdDel;
    }

    public String getpTreeMenuNameDel() {
        return pTreeMenuNameDel;
    }

    public void setpTreeMenuNameDel(String pTreeMenuNameDel) {
        this.pTreeMenuNameDel = pTreeMenuNameDel;
    }


    public void onDragDrop(TreeDragDropEvent event) {
        TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Dragged " + dragNode.getData(), "Dropped on " + dropNode.getData() + " at " + dropIndex);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeSelect(NodeSelectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"警告",summary);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public void submitThisRecordAction() {
        if ("Del".equals(strSubmitType)) {
            try {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("menuID", ptmenuDel.getMenuid());
                sql1 = "delete FROM PTMENU WHERE menuID =:menuID ";
                skylineJdbc.update(sql1, paramMap);
            } catch (Exception e) {
                logger.error("删除数据时出现错误。", e);
                MessageUtil.addWarn("删除数据时出现错误。" + e.getMessage());
            }
        } else if ("Upd".equals(strSubmitType)){
            try {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("menuID", ptmenuUpd.getMenuid());
                paramMap.put("parentmenuid", treeMenu);
                paramMap.put("menulabel", ptmenuUpd.getMenulabel());
                paramMap.put("menuaction", ptmenuUpd.getMenuaction());
                paramMap.put("levelidx", ptmenuUpd.getLevelidx());
                paramMap.put("menudesc", ptmenuUpd.getMenudesc());
                paramMap.put("targetmachine", ptmenuUpd.getTargetmachine());
                sql1 = "UPDATE PTMENU J SET j.parentmenuid = :parentmenuid, J.MENULABEL = :menulabel,J.MENUACTION = :menuaction,J.LEVELIDX= :levelidx,J.MENUDESC = :menudesc,J.TARGETMACHINE = :targetmachine WHERE MENUID = :menuID";
                skylineJdbc.update(sql1, paramMap);
            } catch (Exception e) {
                logger.error("编辑数据时出现错误。", e);
                MessageUtil.addWarn("编辑数据时出现错误。" + e.getMessage());
            }
        }

        onQuery();
    }
//    新增子菜单
    public void addThisRecordAction() {

        for (Ptmenu treeMenuTem : treeMenuList) {
            if (treeMenuTem.getMenulabel().equals(treeMenuAdd)) {
                treeMenuAddId = treeMenuTem.getMenuid();
            }
        }
        if ("".equals(treeMenuAdd)|| treeMenuAdd == null || "菜单管理".equals(treeMenuAdd)) {
            addMessage("上级菜单不能为空...");
        } else {
            try {
//                treeMenuAdd = selectedNode.getData().toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("parentmenuid", treeMenuAddId);
                paramMap.put("menulabel", this.getMenuLabel());
                paramMap.put("menuaction", this.getMenuAction());
                paramMap.put("levelidx", this.getLevelIdx());
                paramMap.put("menudesc", this.getMenudEsc());
                paramMap.put("targetmachine", this.getTargetMachine());
                sql1 = "INSERT INTO ptmenu t (t.menuid,t.menulevel,t.isleaf,t.parentmenuid,t.menulabel," +
                        "t.menuaction,t.levelidx,t.menudesc,t.targetmachine)" +
                        "VALUES(ptmenusequences.nextval,2,1,:parentmenuid,:menulabel,:menuaction,:levelidx,:menudesc,:targetmachine)";
                skylineJdbc.update(sql1, paramMap);
            } catch (Exception e) {
                logger.error("新增子菜单数据时出现错误。", e);
                MessageUtil.addWarn("新增子菜单数据时出现错误。" + e.getMessage());
            }
            treeMenu = "";
            parentMenuId = "";
            menuLabel = "";
            menuAction = "";
            levelIdx = "";
            menudEsc = "";
            targetMachine = "";
            onQuery();
        }
    }

//    新增主菜单
    public void treeMenuAction(String addFlag1){
        if("subadd".equals(addFlag1)){
            try {
                treeMenuAdd = this.selectedNode.getData().toString();
            }catch (Exception e){
            };
        }else if("update".equals(addFlag1)) {
            for (Ptmenu treeMenuTem : treeMenuList) {
                if (treeMenuTem.getMenulabel().equals(selectedNode.getData().toString())) {
                    treeMenuId = treeMenuTem.getMenuid();
                    treeMenuLabel = treeMenuTem.getMenulabel();
                    treeLevelIdx = treeMenuTem.getLevelidx();
                    treeMenudEsc = treeMenuTem.getMenudesc();
                    treeTargetMachine = treeMenuTem.getTargetmachine();

//                    查找对应的父节点
                    if ("0".equals(treeMenuTem.getParentmenuid())) {
                        pTreeMenuId = "0";
                        pTreeMenuName = "菜单管理";
                    } else {
                        for (Ptmenu pTreeMenuTem : treeMenuList) {
                            if (treeMenuTem.getParentmenuid().equals(pTreeMenuTem.getMenuid())) {
                                pTreeMenuId = pTreeMenuTem.getMenuid();
                                pTreeMenuName = pTreeMenuTem.getMenulabel();
                            }
                        }
                    }
                }

            }
        }else if ("delete".equals(addFlag1)){
            for (Ptmenu treeMenuTem : treeMenuList) {
                if (treeMenuTem.getMenulabel().equals(selectedNode.getData().toString())) {
                    treeMenuIdDel = treeMenuTem.getMenuid();
                    treeMenuLabelDel = treeMenuTem.getMenulabel();
                    treeLevelIdxDel = treeMenuTem.getLevelidx();
                    treeMenudEscDel = treeMenuTem.getMenudesc();
                    treeTargetMachineDel = treeMenuTem.getTargetmachine();

//                    查找对应的父节点
                    if ("0".equals(treeMenuTem.getParentmenuid())) {
                        pTreeMenuIdDel = "0";
                        pTreeMenuNameDel = "菜单管理";
                    } else {
                        for (Ptmenu pTreeMenuTem : treeMenuList) {
                            if (treeMenuTem.getParentmenuid().equals(pTreeMenuTem.getMenuid())) {
                                pTreeMenuIdDel = pTreeMenuTem.getMenuid();
                                pTreeMenuNameDel = pTreeMenuTem.getMenulabel();
                            }
                        }
                    }
                }

            }
        }
    }
    public void addTreeMenuAction (String addFlag) {
        if ("update".equals(addFlag)) {
            try {
                ptreeMenu = ptreeMenu == null ? "0" : ptreeMenu;
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("menuID", treeMenuId);
                paramMap.put("parentmenuid", ptreeMenu);
                paramMap.put("menulabel", treeMenuLabel);
                paramMap.put("levelidx", treeLevelIdx);
                paramMap.put("menudesc", treeMenudEsc);
                paramMap.put("targetmachine", treeTargetMachine);
                sql1 = "UPDATE PTMENU J  SET j.parentmenuid = :parentmenuid, J.MENULABEL = :menulabel,J.LEVELIDX= :levelidx,J.MENUDESC = :menudesc,J.TARGETMACHINE = :targetmachine WHERE MENUID = :menuID";
                skylineJdbc.update(sql1, paramMap);
            } catch (Exception e) {
                logger.error("编辑数据时出现错误。", e);
                MessageUtil.addWarn("编辑数据时出现错误。" + e.getMessage());
            }

        } else if ("add".equals(addFlag)){
            try {
                treeMenuUpd = treeMenuUpd == null ? "0" : treeMenuUpd;
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("parentmenuid", treeMenuUpd);
                paramMap.put("menulabel", treeMenuLabelAdd);
                paramMap.put("levelidx", treeLevelIdxAdd);
                paramMap.put("menudesc", treeMenudEscAdd);
                paramMap.put("targetmachine", treeTargetMachineAdd);
                sql2 = "INSERT INTO ptmenu t (t.menuid,t.menulevel,t.isleaf,t.parentmenuid,t.menulabel," +
                        "t.levelidx,t.menudesc,t.targetmachine)" +
                        "VALUES(ptmenusequences.nextval,2,0,:parentmenuid,:menulabel,:levelidx,:menudesc,:targetmachine)";
                skylineJdbc.update(sql2, paramMap);
            } catch (Exception e) {
                logger.error("新增主菜单数据时出现错误。", e);
                MessageUtil.addWarn("新增主菜单数据时出现错误。" + e.getMessage());
            }

        } else if ("delete".equals(addFlag)) {
            if (hasChildAll(treeMenuIdDel)) {
                addMessage("删除的主菜单下不能有子菜单，请删除子菜单后再操作...");
            } else {
                try {
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("menuID", treeMenuIdDel);
                    sqldel = "delete FROM PTMENU WHERE menuID =:menuID ";
                    skylineJdbc.update(sqldel, paramMap);
                } catch (Exception e) {
                    logger.error("删除数据时出现错误。", e);
                    MessageUtil.addWarn("删除数据时出现错误。" + e.getMessage());
                }
            }
        }
        init();
    }

}
