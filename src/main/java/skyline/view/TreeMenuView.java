package skyline.view;

import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 * Created by lihe
 * on 2015/8/6.11:17
 */
@ManagedBean
public class TreeMenuView {
    private TreeNode root1;
    private TreeNode selectedNode;
    @PostConstruct
    public void init() {

    }
    public TreeNode getRoot1() {
        return root1;
    }
    public TreeNode getSelectedNode() {
        return selectedNode;
    }
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
}
