package skyline.view;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import skyline.common.utils.MessageUtil;
import skyline.repository.model.Ptmenu;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihe
 * on 2015/8/27.15:55
 */

@ManagedBean
@ViewScoped
public class SystemManager {
    private TreeNode root1;
    private TreeNode selectedNode;
    private TreeNode node1;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String sql = "";

    public List<Ptmenu> menuList = new ArrayList<Ptmenu>();
    private List<SelectItem> systemMenu;
    @ManagedProperty(value = "#{skylineJdbc}")
    private NamedParameterJdbcTemplate skylineJdbc;


    @PostConstruct
    public void init() {
        root1 = new DefaultTreeNode("Root", null);
        try {
            sql = "SELECT J.* FROM PTMENU J  ORDER BY J.LEVELIDX ASC";
            menuList = skylineJdbc.query(sql,new BeanPropertyRowMapper<Ptmenu>(Ptmenu.class));
        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }

        for(Ptmenu menuTem : menuList) {
            if ("system".equals(menuTem.getTargetmachine())&&"1".equals(menuTem.getIsleaf().toString())) {
                TreeNode node0 = new DefaultTreeNode(menuTem.getMenulabel(),root1);
                if("菜单管理".equals(menuTem.getMenulabel())){
                    for(Ptmenu menuChildTem : menuList) {
                        if("0".equals(menuChildTem.getIsleaf().toString())&&"0".equals(menuChildTem.getParentmenuid())) {
                            node1 = new DefaultTreeNode(menuChildTem.getMenulabel(), node0);
                            treeRecursion(menuChildTem.getMenuid());
                        }
                    }
                }
            }
        }
    }

    public void treeRecursion(String treeid){
        for(Ptmenu nodeAll : menuList) {
            if (treeid.equals(nodeAll.getParentmenuid())&&"0".equals(nodeAll.getIsleaf().toString())) {
                node1 = new DefaultTreeNode(nodeAll.getMenulabel(), node1);
                if(hasChild(nodeAll.getMenuid())){
                    treeRecursion(nodeAll.getMenuid());
                }
            }
        }
    }
    //    判断主菜单是否有子节点
    public boolean hasChild(String treeid){
        boolean childFlag = false;
        for(Ptmenu nodeAllChild : menuList) {
            if (treeid.equals(nodeAllChild.getParentmenuid())&&"0".equals(nodeAllChild.getIsleaf().toString())) {
                childFlag = true;
                break;
            }
        }
        return childFlag;
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

}
