package skyline.view;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pub.auth.MD5Helper;
import skyline.common.utils.MessageUtil;
import skyline.repository.model.Ptdept;
import skyline.repository.model.Ptoper;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihe
 * on 2015/9/10.18:18
 */
@ManagedBean
@ViewScoped
public class DeptManagerAction {

    private String sql;
    private String sqlD;
    private String deptId;
    private String deptName;
    private String strSubmitType;
    private String strRecordType;

    private TreeNode root1;
    private TreeNode pnode;
    private TreeNode selectedNode;

    private List<Ptdept> deptList;
    private List<Ptoper> operList;
    private Ptdept ptdeptAdd;
    private Ptdept ptdeptUpd;
    private Ptdept ptdeptDel;
    private Ptoper ptoperAdd;
    private Ptoper ptoperUpd;
    private Ptoper ptoperDel;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ManagedProperty(value = "#{skylineJdbc}")
    private NamedParameterJdbcTemplate skylineJdbc;

    @PostConstruct
    public void init(){
        try {
            sql = "SELECT * FROM PTDEPT T ORDER BY T.DEPTID ASC";
            deptList = skylineJdbc.query(sql,new BeanPropertyRowMapper<Ptdept>(Ptdept.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
        root1 = new DefaultTreeNode("Root", null);
        for(Ptdept ptdept:deptList){
            if("0".equals(ptdept.getParentdeptid())){
                pnode = new DefaultTreeNode(ptdept.getDeptname()+"("+ptdept.getDeptid()+")",root1);
                if(deptHasChildren(ptdept.getDeptid())){
                    deptTreeRecursion(ptdept.getDeptid(),pnode);
                }
            }
        }
        ptdeptAdd = new Ptdept();
        ptdeptUpd = new Ptdept();
        ptdeptDel = new Ptdept();
        ptoperAdd = new Ptoper();
        ptoperUpd = new Ptoper();
        ptoperDel = new Ptoper();
    }

    public void deptTreeRecursion(String treeId,TreeNode parentNode){
        for(Ptdept ptdeptR:deptList){
            if(ptdeptR.getParentdeptid().equals(treeId)){
                TreeNode rnode = new DefaultTreeNode(ptdeptR.getDeptname()+"("+ptdeptR.getDeptid()+")",parentNode);
                if(deptHasChildren(ptdeptR.getDeptid())){
                    deptTreeRecursion(ptdeptR.getDeptid(),rnode);
                }
            }
        }
    }

    public boolean deptHasChildren(String treeId1){
        boolean childFlag = false;
        for(Ptdept ptdeptC:deptList){
            if(ptdeptC.getParentdeptid().equals(treeId1)){
                childFlag = true;
                break;
            }
        }
        return childFlag;
    }

    //    用户列表查询
    public void onQuery(){
        try {
            deptId = selectedNode.getData().toString();
            deptId = deptId.substring(deptId.lastIndexOf("(")+1,deptId.lastIndexOf(")"));
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("deptId",deptId);
            sql = "SELECT * FROM PTOPER T WHERE T.DEPTID = :deptId ORDER BY T.OPERID ASC ";
            operList = skylineJdbc.query(sql,paramMap, new BeanPropertyRowMapper<Ptoper>(Ptoper.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
    }

    //    机构操作
    public void selectTreeAction(String strSubmitTypePara){
        try{
            deptId = selectedNode.getData().toString();
            deptId = deptId.substring(deptId.lastIndexOf("(")+1,deptId.lastIndexOf(")"));
            for (Ptdept ptdeptT : deptList) {
                if (ptdeptT.getDeptid().equals(deptId)) {
                    if("deptUpd".equals(strSubmitTypePara)) {
                        ptdeptUpd = (Ptdept) BeanUtils.cloneBean(ptdeptT);
                    }else if("deptDel".equals(strSubmitTypePara)){
                        ptdeptDel = (Ptdept) BeanUtils.cloneBean(ptdeptT);
                    }
                    break;
                }
            }
        } catch (Exception e){
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
        strSubmitType = strSubmitTypePara;
    }

    public void submitThisTreeAction(){
        if("deptAdd".equals(strSubmitType)){
            if(hasEquals(ptdeptAdd.getDeptid(),ptdeptAdd.getDeptname())){
                addMessage("新增机构代码或机构名称已存在，请确认后再次添加...");
            }else if("".equals(ptdeptAdd.getDeptid())||"".equals(ptdeptAdd.getDeptname())){
                addMessage("新增机构代码或机构名称不能为空，请确认后再次添加...");
            }else{
                deptId = selectedNode.getData().toString();
                deptId = deptId.substring(deptId.lastIndexOf("(")+1,deptId.lastIndexOf(")"));
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("parentdeptid", deptId);
                paramMap.put("deptname", ptdeptAdd.getDeptname());
                paramMap.put("deptid", ptdeptAdd.getDeptid());
                paramMap.put("fillstr10", ptdeptAdd.getFillstr10());
                paramMap.put("deptdesc", ptdeptAdd.getDeptdesc());
                sqlD = "insert into ptdept t (t.parentdeptid,t.deptid,t.deptname,t.fillstr10,t.deptdesc) " +
                        "values (:parentdeptid,:deptid,:deptname,:fillstr10,:deptdesc)";
                skylineJdbc.update(sqlD, paramMap);
            }
        } else if("deptUpd".equals(strSubmitType)){
            deptName = this.selectedNode.getData().toString();
            deptName = deptName.substring(0,deptName.lastIndexOf("("));
            if(!ptdeptUpd.getDeptname().equals(deptName)){
                if(hasEquals("",ptdeptUpd.getDeptname())) {
                    addMessage("机构名称已存在，请确认后再次添加...");
                }
            }else if("".equals(ptdeptUpd.getDeptname())){
                addMessage("机构名称不能为空，请确认后再次添加...");
            }else{
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("deptid", ptdeptUpd.getDeptid());
                paramMap.put("deptname", ptdeptUpd.getDeptname());
                paramMap.put("fillstr10", ptdeptUpd.getFillstr10());
                paramMap.put("deptdesc", ptdeptUpd.getDeptdesc());
                sqlD = "update ptdept t set t.deptname = :deptname,t.fillstr10 = :fillstr10,t.deptdesc = :deptdesc where t.deptid = :deptid";
                skylineJdbc.update(sqlD, paramMap);
            }
        }else if("deptDel".equals(strSubmitType)){
            if(deptHasChildren(ptdeptDel.getDeptid())){
                addMessage("该机构下含有子机构，请删除子机构后再操作...");
            }else {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("deptid", ptdeptDel.getDeptid());
//                删除机构
                sql = "delete ptdept j WHERE j.deptid =:deptid";
                skylineJdbc.update(sql, paramMap);
//                删除用户
                sqlD = "delete ptoper j  WHERE j.deptid =:deptid";
                skylineJdbc.update(sqlD, paramMap);
            }

        }
        init();
    }

    //    判断是否有已存在相同名字或代码的机构
    public boolean hasEquals(String compareId,String compareName){
        boolean compareFlag = false;
        for(Ptdept ptdept:deptList){
            if(compareId.equals(ptdept.getDeptid())||compareName.equals(ptdept.getDeptname())){
                compareFlag = true;
                break;
            }
        }
        return compareFlag;
    }

    public void selectRecordAction(String strRecordTypePara,Ptoper ptoperSelectedPara){
        deptId = selectedNode.getData().toString();
        deptName = deptId.substring(0,deptId.lastIndexOf("("));
        deptId = deptId.substring(deptId.lastIndexOf("(")+1,deptId.lastIndexOf(")"));


        strRecordType = strRecordTypePara;

    }

    public  void  submitThisRecordAction(String rrrr){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("deptid", deptId);
        paramMap.put("operid", ptoperAdd.getOperid());
        paramMap.put("opername", ptoperAdd.getOpername());
        paramMap.put("sex", ptoperAdd.getSex());
        paramMap.put("issuper", ptoperAdd.getIssuper());//是否领导
        paramMap.put("operphone", ptoperAdd.getOperphone());
        paramMap.put("mobilephone", ptoperAdd.getMobilephone());
        paramMap.put("otherphone", ptoperAdd.getOtherphone());
        paramMap.put("operenabled", ptoperAdd.getOperenabled());//账号是否可用
        paramMap.put("opertype", ptoperAdd.getOpertype());//类别
        paramMap.put("email", ptoperAdd.getEmail());
        paramMap.put("operpasswd", MD5Helper.getMD5String(ptoperAdd.getOperpasswd()));
//PTOPERSEQUENCES.NEXTVAL

    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"警告",summary);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<Ptdept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<Ptdept> deptList) {
        this.deptList = deptList;
    }

    public List<Ptoper> getOperList() {
        return operList;
    }

    public void setOperList(List<Ptoper> operList) {
        this.operList = operList;
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

    public NamedParameterJdbcTemplate getSkylineJdbc() {
        return skylineJdbc;
    }

    public void setSkylineJdbc(NamedParameterJdbcTemplate skylineJdbc) {
        this.skylineJdbc = skylineJdbc;
    }

    public Ptdept getPtdeptAdd() {
        return ptdeptAdd;
    }

    public void setPtdeptAdd(Ptdept ptdeptAdd) {
        this.ptdeptAdd = ptdeptAdd;
    }

    public Ptdept getPtdeptUpd() {
        return ptdeptUpd;
    }

    public void setPtdeptUpd(Ptdept ptdeptUpd) {
        this.ptdeptUpd = ptdeptUpd;
    }

    public Ptdept getPtdeptDel() {
        return ptdeptDel;
    }

    public void setPtdeptDel(Ptdept ptdeptDel) {
        this.ptdeptDel = ptdeptDel;
    }

    public Ptoper getPtoperAdd() {
        return ptoperAdd;
    }

    public void setPtoperAdd(Ptoper ptoperAdd) {
        this.ptoperAdd = ptoperAdd;
    }

    public Ptoper getPtoperUpd() {
        return ptoperUpd;
    }

    public void setPtoperUpd(Ptoper ptoperUpd) {
        this.ptoperUpd = ptoperUpd;
    }

    public Ptoper getPtoperDel() {
        return ptoperDel;
    }

    public void setPtoperDel(Ptoper ptoperDel) {
        this.ptoperDel = ptoperDel;
    }
}
