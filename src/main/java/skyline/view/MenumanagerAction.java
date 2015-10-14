package skyline.view;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import skyline.common.utils.MessageUtil;
import skyline.repository.model.Ptmenu;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihe
 * on 2015/10/12.21:28
 */

@ManagedBean
@ViewScoped
public class MenuManagerAction implements Serializable {
    private TreeNode root1;
    private TreeNode selectedNode;
    private TreeNode pnode;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<Ptmenu> menuList;
    private List<Ptmenu> treeMenuList;
//    treeMenus 修改菜单时上级菜单数据源
    private List<SelectItem> treeMenus;
    private Ptmenu ptmenuDel;
    private Ptmenu ptmenuUpd;
    private Ptmenu ptmenuAdd;
    private Ptmenu pttreeMenuDel;
    private Ptmenu pttreeMenuUpd;
    private Ptmenu pttreeMenuAdd;
    String sql = "";
    String sqlTree = "";
    String menuId = "";
    private String strSubmitType = "";

    @ManagedProperty(value = "#{skylineJdbc}")
    private NamedParameterJdbcTemplate skylineJdbc;

    @PostConstruct
    public void init() {
        root1 = new DefaultTreeNode("Root", null);
        TreeNode node0 = new DefaultTreeNode("菜单管理(0000)", root1);
        treeMenus = new ArrayList<SelectItem>();
        try {
            sql = "SELECT * FROM PTMENU J ORDER BY J.LEVELIDX ASC";
            treeMenuList = skylineJdbc.query(sql, new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }

        for (Ptmenu ptmenuTem : treeMenuList) {
            if ("0".equals(ptmenuTem.getIsleaf().toString())) {
                treeMenus.add(new SelectItem(ptmenuTem.getMenuid(), ptmenuTem.getMenulabel()));
                if ("0".equals(ptmenuTem.getParentmenuid())){
                    pnode = new DefaultTreeNode(ptmenuTem.getMenulabel()+"("+ptmenuTem.getMenuid()+")", node0);
                    treeRecursion(ptmenuTem.getMenuid(),pnode);
                }
            }
        }
        ptmenuAdd = new Ptmenu();
        ptmenuDel = new Ptmenu();
        ptmenuUpd = new Ptmenu();
        pttreeMenuUpd = new Ptmenu();
        pttreeMenuDel = new Ptmenu();
        pttreeMenuAdd = new Ptmenu();
    }
    public void treeRecursion(String treeid,TreeNode parentNode) {
        for (Ptmenu ptmenuTem2 : treeMenuList) {
            if ("0".equals(ptmenuTem2.getIsleaf().toString()) && treeid.equals(ptmenuTem2.getParentmenuid())) {
                TreeNode cnode = new DefaultTreeNode(ptmenuTem2.getMenulabel()+"("+ptmenuTem2.getMenuid()+")", parentNode);
                if (hasChild(ptmenuTem2.getMenuid())) {
                    treeRecursion(ptmenuTem2.getMenuid(),cnode);
                }
            }
        }
    }

    //    判断主菜单是否有子节点
    public boolean hasChild(String treeid) {
        boolean childFlag = false;
        for (Ptmenu ptmenuTem3 : treeMenuList) {
            if ("0".equals(ptmenuTem3.getIsleaf().toString()) && treeid.equals(ptmenuTem3.getParentmenuid())) {
                childFlag = true;
                break;
            }
        }
        return childFlag;
    }
//查询
    public String onQuery(){
        try {
            Map<String,Object> paramMap = new HashMap<>();
            menuId = selectedNode.getData().toString();
            menuId = menuId.substring(menuId.lastIndexOf("(")+1,menuId.lastIndexOf(")"));
            paramMap.put("menuId", menuId);
            if("0000".equals(menuId)){
                sql = "SELECT * FROM PTMENU T WHERE T.ISLEAF <>'0' ORDER BY T.LEVELIDX ASC";
            }else{
                sql = "SELECT * FROM PTMENU T WHERE T.ISLEAF <>'0' AND T.PARENTMENUID =:menuId ORDER BY T.LEVELIDX ASC";
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
            if("menuDel".equals(strSubmitTypePara)){
                ptmenuDel =(Ptmenu) BeanUtils.cloneBean(ptmenuSelectedPara);
            }else if("menuUpd".equals(strSubmitTypePara)){
                ptmenuUpd =(Ptmenu) BeanUtils.cloneBean(ptmenuSelectedPara);
            }
            strSubmitType = strSubmitTypePara;
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
//    子菜单提交修改
    public void submitThisRecordAction(){
        if ("menuDel".equals(strSubmitType)) {
            try {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("menuID", ptmenuDel.getMenuid());
                sql = "delete FROM PTMENU WHERE menuID =:menuID ";
                skylineJdbc.update(sql, paramMap);
            } catch (Exception e) {
                logger.error("删除数据时出现错误。", e);
                MessageUtil.addWarn("删除数据时出现错误。" + e.getMessage());
            }
        } else if ("menuUpd".equals(strSubmitType)){
            try {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("menuID", ptmenuUpd.getMenuid());
                paramMap.put("parentmenuid", ptmenuUpd.getParentmenuid());
                paramMap.put("menulabel", ptmenuUpd.getMenulabel());
                paramMap.put("menuaction", ptmenuUpd.getMenuaction());
                paramMap.put("levelidx", ptmenuUpd.getLevelidx());
                paramMap.put("menudesc", ptmenuUpd.getMenudesc());
                paramMap.put("targetmachine", ptmenuUpd.getTargetmachine());
                sql = "UPDATE PTMENU J SET j.parentmenuid = :parentmenuid, J.MENULABEL = :menulabel,J.MENUACTION = :menuaction,J.LEVELIDX= :levelidx,J.MENUDESC = :menudesc,J.TARGETMACHINE = :targetmachine WHERE MENUID = :menuID";
                skylineJdbc.update(sql, paramMap);
            } catch (Exception e) {
                logger.error("编辑数据时出现错误。", e);
                MessageUtil.addWarn("编辑数据时出现错误。" + e.getMessage());
            }
        } else if("menuAdd".equals(strSubmitType)){
            try {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("parentmenuid", ptmenuAdd.getParentmenuid());
                paramMap.put("menulabel", ptmenuAdd.getMenulabel());
                paramMap.put("menuaction", ptmenuAdd.getMenuaction());
                paramMap.put("levelidx", ptmenuAdd.getLevelidx());
                paramMap.put("menudesc", ptmenuAdd.getMenudesc());
                paramMap.put("targetmachine", ptmenuAdd.getTargetmachine());
                sql = "INSERT INTO ptmenu t (t.menuid,t.menulevel,t.isleaf,t.parentmenuid,t.menulabel," +
                        "t.menuaction,t.levelidx,t.menudesc,t.targetmachine)" +
                        "VALUES(ptmenusequences.nextval,2,1,:parentmenuid,:menulabel,:menuaction,:levelidx,:menudesc,:targetmachine)";
                skylineJdbc.update(sql, paramMap);
            } catch (Exception e) {
                logger.error("新增子菜单数据时出现错误。", e);
                MessageUtil.addWarn("新增子菜单数据时出现错误。" + e.getMessage());
            }
        }
        onQuery();
    }

    //  操作主菜单前处理
    public void treeMenuAction(String addFlag1){
        menuId = selectedNode.getData().toString();
        menuId = menuId.substring(menuId.lastIndexOf("(")+1,menuId.lastIndexOf(")"));
        if("treeUpd".equals(addFlag1)) {
            pttreeMenuUpd = new Ptmenu();
            for (Ptmenu treeMenuTem : treeMenuList) {
                if (treeMenuTem.getMenuid().equals(menuId)) {
                    pttreeMenuUpd =treeMenuTem;
                    break;
                }
            }
        }else if("treeAdd".equals(addFlag1)) {
            pttreeMenuAdd = new Ptmenu();
//            pttreeMenuAdd.setParentmenuid(menuId);
        }else if ("treeDel".equals(addFlag1)){
            pttreeMenuDel = new Ptmenu();
            for (Ptmenu treeMenuTem : treeMenuList) {
                if (treeMenuTem.getMenuid().equals(menuId)) {
                    pttreeMenuDel =treeMenuTem;
                    break;
                }
            }
        }else if("menuAdd".equals(addFlag1)){
            strSubmitType = "menuAdd";
            ptmenuAdd = new Ptmenu();
            ptmenuAdd.setParentmenuid(menuId);
        }
    }
    //  操作主菜单
    public void addTreeMenuAction (String addFlag) {
        if ("treeUpd".equals(addFlag)) {
            try {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("menuID", pttreeMenuUpd.getMenuid());
                paramMap.put("parentmenuid", pttreeMenuUpd.getParentmenuid());
                paramMap.put("menulabel", pttreeMenuUpd.getMenulabel());
                paramMap.put("levelidx", pttreeMenuUpd.getLevelidx());
                paramMap.put("menudesc", pttreeMenuUpd.getMenudesc());
                paramMap.put("targetmachine", pttreeMenuUpd.getTargetmachine());
                sql = "UPDATE PTMENU J SET j.parentmenuid = :parentmenuid, J.MENULABEL = :menulabel,J.LEVELIDX= :levelidx,J.MENUDESC = :menudesc,J.TARGETMACHINE = :targetmachine WHERE MENUID = :menuID";
                skylineJdbc.update(sql, paramMap);
            } catch (Exception e) {
                logger.error("编辑数据时出现错误。", e);
                MessageUtil.addWarn("编辑数据时出现错误。" + e.getMessage());
            }

        } else if ("treeAdd".equals(addFlag)){
            try {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("parentmenuid", pttreeMenuAdd.getParentmenuid());
                paramMap.put("menulabel", pttreeMenuAdd.getMenulabel());
                paramMap.put("levelidx", pttreeMenuAdd.getLevelidx());
                paramMap.put("menudesc", pttreeMenuAdd.getMenudesc());
                paramMap.put("targetmachine", pttreeMenuAdd.getTargetmachine());
                sql = "INSERT INTO ptmenu t (t.menuid,t.menulevel,t.isleaf,t.parentmenuid,t.menulabel," +
                        "t.levelidx,t.menudesc,t.targetmachine)" +
                        "VALUES(ptmenusequences.nextval,2,0,:parentmenuid,:menulabel,:levelidx,:menudesc,:targetmachine)";
                skylineJdbc.update(sql, paramMap);
            } catch (Exception e) {
                logger.error("新增主菜单数据时出现错误。", e);
                MessageUtil.addWarn("新增主菜单数据时出现错误。" + e.getMessage());
            }

        } else if ("treeDel".equals(addFlag)) {
            if (hasTreeChild(menuId)) {
                addMessage("删除的主菜单下不能有子菜单，请删除子菜单后再操作...");
            } else {
                try {
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("menuID", menuId);
                    sql = "delete FROM PTMENU WHERE menuID =:menuID ";
                    skylineJdbc.update(sql, paramMap);
                } catch (Exception e) {
                    logger.error("删除数据时出现错误。", e);
                    MessageUtil.addWarn("删除数据时出现错误。" + e.getMessage());
                }
            }
        }
        init();
    }

    //    删除主菜单时用----判断主菜单是否有子节点
    public boolean hasTreeChild(String treeid) {
        boolean childFlag = false;
        for (Ptmenu ptmenuTem4 : treeMenuList) {
            if (treeid.equals(ptmenuTem4.getParentmenuid())) {
                childFlag = true;
                break;
            }
        }
        return childFlag;
    }
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"警告",summary);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public TreeNode getRoot1() {
        return root1;
    }

    public void setRoot1(TreeNode root1) {
        this.root1 = root1;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<Ptmenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Ptmenu> menuList) {
        this.menuList = menuList;
    }

    public NamedParameterJdbcTemplate getSkylineJdbc() {
        return skylineJdbc;
    }

    public void setSkylineJdbc(NamedParameterJdbcTemplate skylineJdbc) {
        this.skylineJdbc = skylineJdbc;
    }

    public List<SelectItem> getTreeMenus() {
        return treeMenus;
    }

    public void setTreeMenus(List<SelectItem> treeMenus) {
        this.treeMenus = treeMenus;
    }

    public Ptmenu getPtmenuDel() {
        return ptmenuDel;
    }

    public void setPtmenuDel(Ptmenu ptmenuDel) {
        this.ptmenuDel = ptmenuDel;
    }

    public Ptmenu getPttreeMenuDel() {
        return pttreeMenuDel;
    }

    public void setPttreeMenuDel(Ptmenu pttreeMenuDel) {
        this.pttreeMenuDel = pttreeMenuDel;
    }

    public Ptmenu getPttreeMenuUpd() {
        return pttreeMenuUpd;
    }

    public void setPttreeMenuUpd(Ptmenu pttreeMenuUpd) {
        this.pttreeMenuUpd = pttreeMenuUpd;
    }

    public Ptmenu getPtmenuUpd() {
        return ptmenuUpd;
    }

    public void setPtmenuUpd(Ptmenu ptmenuUpd) {
        this.ptmenuUpd = ptmenuUpd;
    }

    public Ptmenu getPtmenuAdd() {
        return ptmenuAdd;
    }

    public void setPtmenuAdd(Ptmenu ptmenuAdd) {
        this.ptmenuAdd = ptmenuAdd;
    }

    public Ptmenu getPttreeMenuAdd() {
        return pttreeMenuAdd;
    }

    public void setPttreeMenuAdd(Ptmenu pttreeMenuAdd) {
        this.pttreeMenuAdd = pttreeMenuAdd;
    }
}
