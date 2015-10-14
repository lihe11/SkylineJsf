package skyline.view;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pub.platform.security.OperatorManager;
import skyline.common.SystemService;
import skyline.common.utils.MessageUtil;
import skyline.repository.model.Ptmenu;
import skyline.service.PlatformService;
import skyline.service.ToolsService;

import javax.annotation.PostConstruct;
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
 * on 2015/7/16.17:51
 */

@ManagedBean
@ViewScoped
public class MenumanagerAction1 implements Serializable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<SelectItem> branchList;
    private String branchId;
    private String operId;
    private String menuName;
    private String menuid;
    private String endDate;

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
        OperatorManager om = SystemService.getOperatorManager();
        String operid = om.getOperatorId();
        String branchid = om.getOperator().getDeptid();

        this.branchList = toolsService.selectBranchList(branchid);

//        this.startDate = new DateTime().dayOfMonth().withMinimumValue().toString("yyyy-MM-dd");
        this.endDate = new DateTime().toString("yyyy-MM-dd");
        menuList = new ArrayList<>();

    }

    public String onQuery() {
        try {
//            Ptoplog oplog = new Ptoplog();
//            oplog.setActionId("Menu_onQuery");
//            oplog.setActionName("菜单管理查询");
//            oplog.setOpDataBranchid(this.branchId);
//            oplog.setOpDataStartdate(this.startDate);
//            oplog.setOpDataEnddate(this.endDate);
//            platformService.insertNewOperationLog(oplog);

            Map<String,Object> paramMap = new HashMap<>();
//            List<String> branchStrList = platformService.selectBranchLevelList(branchId);
//            paramMap.put("branchStrList", branchStrList);
            paramMap.put("menuName",this.menuid);
            paramMap.put("endDate", this.endDate);
            String sql = "SELECT *   FROM PTMENU  WHERE (ISLEAF = '1')";
            menuList = skylineJdbc.query(sql,paramMap, new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
        return null;
    }

    //=========================

    public List<SelectItem> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<SelectItem> branchList) {
        this.branchList = branchList;
    }

    public String getBranchId() {
        return branchId;
    }
    public String getMenuId() {
        return menuid;
    }
    public void setMenuId(String menuid) {
        this.menuid = menuid;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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


    /*public List<Ptoplog> getFilteredDetlList() {
        return filteredDetlList;
    }

    public void setFilteredDetlList(List<Ptoplog> filteredDetlList) {
        this.filteredDetlList = filteredDetlList;
    }*/

}

