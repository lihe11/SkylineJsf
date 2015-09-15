package skyline.view;


import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import skyline.common.utils.MessageUtil;
import skyline.repository.model.Ptmenu;
import skyline.service.PlatformService;
import skyline.service.ToolsService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class TestForMenu implements Serializable {

    private TreeNode root1;
    private TreeNode selectedNode1;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<SelectItem> branchList;
    private String menuName;
    private String menuid;
    String sql ="";
    String sql1 ="";
    private List<Ptmenu> menuList;
    // private List<Ptmenu> filteredDetlList;

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
        TreeNode node00 = new DefaultTreeNode("个人业绩评价信息管理", node0);

        TreeNode node01 = new DefaultTreeNode("网点业绩评价信息管理", node0);
        TreeNode node02 = new DefaultTreeNode("个人产品套餐信息管理", node0);
        TreeNode node03 = new DefaultTreeNode("网点产品套餐信息管理", node0);
        TreeNode node04 = new DefaultTreeNode("业绩评价汇总查询", node0);
        TreeNode node05 = new DefaultTreeNode("产品套餐汇总查询", node0);
        TreeNode node06 = new DefaultTreeNode("产品套餐考核管理", node0);
        TreeNode node07 = new DefaultTreeNode("客户名单制报表", node0);
        TreeNode node08 = new DefaultTreeNode("客户营销管理", node0);
        TreeNode node09 = new DefaultTreeNode("客户营销数据检核", node0);
        TreeNode node10 = new DefaultTreeNode("短信营销平台", node0);
        TreeNode node11 = new DefaultTreeNode("大额资金流向监控", node0);
        TreeNode node12 = new DefaultTreeNode("有效客户拓展提升目标名单", node0);
        TreeNode node13 = new DefaultTreeNode("综合数据报表", node0);
        TreeNode node14 = new DefaultTreeNode("个人资金流向全景视图", node0);
        TreeNode node15 = new DefaultTreeNode("客户资产信息综合查询", node0);
        TreeNode node16 = new DefaultTreeNode("电子银行业务综合查询", node0);
        TreeNode node17 = new DefaultTreeNode("数据维护", node0);
        TreeNode node18 = new DefaultTreeNode("系统管理", node0);
    }


    public String onQuery() {
        try {
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("menuName", this.selectedNode1.toString());
            if("菜单管理".equals(this.selectedNode1.toString())){
                sql = "SELECT * FROM PTMENU T WHERE T.PARENTMENUID <>'0' ";
            }else{
                sql = "SELECT * FROM PTMENU  WHERE PARENTMENUID IN(SELECT MENUID FROM PTMENU WHERE MENULABEL in (:menuName)) ";
            }
            menuList = skylineJdbc.query(sql,paramMap, new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
        return null;
    }

    public List<SelectItem> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<SelectItem> branchList) {
        this.branchList = branchList;
    }


    public String getMenuName() {
        return menuName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getMenuId() {
        return menuid;
    }
    public void setMenuId(String menuid) {
        this.menuid = menuid;
    }

    public List<Ptmenu> getmenuList() {
        return menuList;
    }

    public void setmenuList(List<Ptmenu> menuList) {
        this.menuList = menuList;
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

    public TreeNode getSelectedNode1() {
        return selectedNode1;
    }

    public void setSelectedNode1(TreeNode selectedNode1) {
        this.selectedNode1 = selectedNode1;
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
    public String delete(){
        try{
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("menuID",menuid);
            sql1 = "delete FROM PTMENU WHERE menuID ='1102'";
            skylineJdbc.update(sql1,paramMap);
        } catch (Exception e) {
            logger.error("删除数据时出现错误。", e);
            MessageUtil.addWarn("删除数据时出现错误。" + e.getMessage());
        }
        return null;
    }
}
