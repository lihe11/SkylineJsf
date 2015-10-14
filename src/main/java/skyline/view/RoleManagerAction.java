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
import skyline.repository.model.Ptrole;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class RoleManagerAction implements Serializable {

    private String sql ;
    private String sqlD ;
    private String sqlT ;
    private String sqlTree ;
    private String strTreeSubmitType ;
    private String strRecordSubmitType ;
    private String roleId;
    private String roleName;

    private TreeNode root1;
    private TreeNode root2;
    private TreeNode pnode;
    private TreeNode cnode;

    private TreeNode selectedNode;
    private TreeNode[] selectedNode2;
    private TreeNode[] selectedNode3;
    private List<Ptrole> roleList;
    private Ptrole ptroleUpd;
    private Ptrole ptroleDel;
    private Ptrole ptroleAdd;

    private Ptmenu ptroleResDel;
    private List<Ptmenu> ptroleResList;
    private List<Ptmenu> roleAddList;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ManagedProperty(value = "#{skylineJdbc}")
    private NamedParameterJdbcTemplate skylineJdbc;

    @PostConstruct
    public void init() {
        root1 = new DefaultTreeNode("Root", null);
//        TreeNode node0 = new DefaultTreeNode("角色管理", root1);
        try {
            sql = "SELECT * FROM PTROLE T WHERE T.STATUS = '1' ORDER BY T.ROLEID ASC";
            roleList = skylineJdbc.query(sql,new BeanPropertyRowMapper<Ptrole>(Ptrole.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
        for(Ptrole ptroleT:roleList){
            pnode = new DefaultTreeNode(ptroleT.getRoleDesc(), root1);
        }
        ptroleUpd = new Ptrole();
        ptroleDel = new Ptrole();
        ptroleAdd = new Ptrole();
    }

    //  角色授权查询；
    public void onQuery(){
        try {
            for(Ptrole ptroleT:roleList){
                if(ptroleT.getRoleDesc().equals(this.selectedNode.getData().toString())){
                    roleId = ptroleT.getRoleId();
                    roleName = ptroleT.getRoleDesc();
                }
            }
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("roleId", roleId);
            sql = "SELECT M.* FROM PTROLERES R, PTMENU M WHERE R.RESID = TRIM(M.MENUID) AND R.ROLEID =:roleId ORDER BY R.RESID ";
            ptroleResList = skylineJdbc.query(sql,paramMap, new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
    }

    //  角色授权删除数据复制、新增列表展示
    public void selectRecordAction(String strSubmitTypePara,Ptmenu ptroleResSelectedPara){
        try {
            if ("Del".equals(strSubmitTypePara)) {
                ptroleResDel = (Ptmenu) BeanUtils.cloneBean(ptroleResSelectedPara);
            }else if("Add".equals(strSubmitTypePara)){
                onQuery();
                root2 = new DefaultTreeNode("Root", null);
                try {
                    sqlTree ="SELECT * FROM PTMENU T ORDER BY T.LEVELIDX ASC ";
                    roleAddList = skylineJdbc.query(sqlTree,new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
                } catch (Exception e) {
                    logger.error("查询数据时出现错误。", e);
                    MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
                }
                for(Ptmenu p:roleAddList){
                    if("0".equals(p.getParentmenuid())){
                        pnode  = new DefaultTreeNode(p.getMenuid()+"_"+p.getMenulabel(), root2);
                        for(Ptmenu ptroleResT:ptroleResList) {
                            if (ptroleResT.getMenuid().equals(p.getMenuid())) {
                                pnode.setSelected(true);
                            }
                        }
                        treeRecursion(p.getMenuid(),pnode);
                    }
                }
            }
            strRecordSubmitType = strSubmitTypePara;
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
        }
    }

    public void treeRecursion(String treeid,TreeNode parentNode){
        for(Ptmenu pr:roleAddList) {
            if (treeid.equals(pr.getParentmenuid())) {
//待优化
                TreeNode cnode = new DefaultTreeNode(pr.getMenuid()+"_"+pr.getMenulabel(), parentNode);
                for(Ptmenu ptroleResT:ptroleResList) {
                    if (ptroleResT.getMenuid().equals(pr.getMenuid())) {
                        cnode.setSelected(true);
                    }
                }
                if(hasChild(pr.getMenuid())){
                    treeRecursion(pr.getMenuid(),cnode);
                }
            }
        }
    }
    //    判断主菜单是否有子节点
    public boolean hasChild(String treeid){
        boolean childFlag = false;
        for(Ptmenu pre:roleAddList) {
            if (treeid.equals(pre.getParentmenuid())) {
                childFlag = true;
                break;
            }
        }
        return childFlag;
    }

    //角色授权删除、新增处理
    public void submitThisRecordAction(TreeNode[] nodes){
        try {
            if ("Del".equals(strRecordSubmitType)) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("roleId", roleId);
                paramMap.put("resId", ptroleResDel.getMenuid());
                sqlD = "delete PTROLERES j WHERE j.roleid =:roleId and j.resid = :resId";
                skylineJdbc.update(sqlD, paramMap);
            } else if("Add".equals(strRecordSubmitType)){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("roleId", roleId);
                sqlD = "delete PTROLERES j WHERE j.roleid =:roleId ";
                skylineJdbc.update(sqlD, paramMap);
                if(nodes != null && nodes.length > 0) {
                    for (TreeNode node : nodes) {
                        Map<String,Object> paramMapT = new HashMap<>();
                        int subP = node.getData().toString().indexOf("_");
                        paramMapT.put("roleId",roleId);
                        paramMapT.put("resId",node.getData().toString().substring(0,subP));
                        sqlD = "INSERT INTO PTROLERES J (J.RESID, J.ROLEID) VALUES (:resId, :roleId) ";
                        skylineJdbc.update(sqlD, paramMapT);
                    }
                }

            }
        }catch (Exception e){
            logger.error("编辑数据时出现错误。", e);
            MessageUtil.addWarn("编辑数据时出现错误。" + e.getMessage());
        }
        onQuery();
    }

//角色管理增、删、改；
    public void selectTreeAction(String strSubmitTypePara){
        try {
            if("Upd".equals(strSubmitTypePara)){
                for(Ptrole ptroleT:roleList){
                    if(ptroleT.getRoleDesc().equals(this.selectedNode.getData().toString())){
                        ptroleUpd.setRoleId(ptroleT.getRoleId());
                        ptroleUpd.setRoleDesc(ptroleT.getRoleDesc());
                        break;
                    }
                }
            }else if("Del".equals(strSubmitTypePara)){
                for(Ptrole ptroleT:roleList){
                    if(ptroleT.getRoleDesc().equals(this.selectedNode.getData().toString())){
                        ptroleDel.setRoleId(ptroleT.getRoleId());
                        ptroleDel.setRoleDesc(ptroleT.getRoleDesc());
                        break;
                    }
                }
            }
            strTreeSubmitType = strSubmitTypePara;
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public void submitThisTreeAction(){
        try {
            if("Upd".equals(strTreeSubmitType)) {
                if(hasEquals("",ptroleUpd.getRoleDesc())) {
                    addMessage("角色描述已存在，请确认后再次更新...");
                }else if("".equals(ptroleUpd.getRoleDesc())){
                    addMessage("角色描述不能为空，请确认后再次更新...");
                }else {
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("roleId", ptroleUpd.getRoleId());
                    paramMap.put("roleDesc", ptroleUpd.getRoleDesc());
                    sqlD = "UPDATE ptrole j SET j.roledesc =:roleDesc WHERE j.roleid =:roleId";
                    skylineJdbc.update(sqlD, paramMap);
                }
            }else if ("Del".equals(strTreeSubmitType)){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("roleId", ptroleDel.getRoleId());
//                删除角色
                sqlD = "delete ptrole j WHERE j.roleid =:roleId";
                skylineJdbc.update(sqlD, paramMap);
//                删除角色授权
                sqlT = "delete PTROLERES j  WHERE j.roleid =:roleId";
                skylineJdbc.update(sqlT, paramMap);
            }else if ("Add".equals(strTreeSubmitType)){
                if(hasEquals(ptroleAdd.getRoleId(),ptroleAdd.getRoleDesc())){
                    addMessage("新增角色ID或角色描述已存在，请确认后再次添加...");
                }else if("".equals(ptroleAdd.getRoleId())||"".equals(ptroleAdd.getRoleDesc())){
                    addMessage("新增角色ID或角色描述不能为空，请确认后再次添加...");
                }else{
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("roleId", ptroleAdd.getRoleId());
                    paramMap.put("roleDesc", ptroleAdd.getRoleDesc());
                    sqlD = "insert into ptrole j (j.roleid,j.roledesc,j.status) values (:roleId,:roleDesc,'1')";
                    skylineJdbc.update(sqlD, paramMap);
                }
            }
        }catch (Exception e){
            logger.error("编辑数据时出现错误。", e);
            MessageUtil.addWarn("编辑数据时出现错误。" + e.getMessage());
        }
        init();
    }
//
    public boolean hasEquals(String objectId,String objectName){
        boolean objectFlag = false;
        for(Ptrole ptroleE:roleList){
            if(objectId.equals(ptroleE.getRoleId())||objectName.equals(ptroleE.getRoleDesc())){
                objectFlag = true;
                break;
            }
        }
        return objectFlag;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"警告",summary);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public List<Ptrole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Ptrole> roleList) {
        this.roleList = roleList;
    }

    public List<Ptmenu> getPtroleResList() {
        return ptroleResList;
    }

    public void setPtroleResList(List<Ptmenu> ptroleResList) {
        this.ptroleResList = ptroleResList;
    }

    public NamedParameterJdbcTemplate getSkylineJdbc() {
        return skylineJdbc;
    }

    public void setSkylineJdbc(NamedParameterJdbcTemplate skylineJdbc) {
        this.skylineJdbc = skylineJdbc;
    }

    public Ptrole getPtroleUpd() {
        return ptroleUpd;
    }

    public void setPtroleUpd(Ptrole ptroleUpd) {
        this.ptroleUpd = ptroleUpd;
    }

    public Ptrole getPtroleDel() {
        return ptroleDel;
    }

    public void setPtroleDel(Ptrole ptroleDel) {
        this.ptroleDel = ptroleDel;
    }

    public Ptrole getPtroleAdd() {
        return ptroleAdd;
    }

    public void setPtroleAdd(Ptrole ptroleAdd) {
        this.ptroleAdd = ptroleAdd;
    }

    public Ptmenu getPtroleResDel() {
        return ptroleResDel;
    }

    public void setPtroleResDel(Ptmenu ptroleResDel) {
        this.ptroleResDel = ptroleResDel;
    }

    public TreeNode getRoot1() {
        return root1;
    }

    public void setRoot1(TreeNode root1) {
        this.root1 = root1;
    }

    public TreeNode getRoot2() {
        return root2;
    }

    public void setRoot2(TreeNode root2) {
        this.root2 = root2;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode[] getSelectedNode2() {
        return selectedNode2;
    }

    public void setSelectedNode2(TreeNode[] selectedNode2) {
        this.selectedNode2 = selectedNode2;
    }

}
