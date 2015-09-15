package test.test1;

/**
 * Created by lihe
 * on 2015/8/5.17:37
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * The persistent class for the tree database table.
 *
 */
@Entity
@Table(name = "TREE")
public class Tree implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NODE_ID")
    private int nodeId;

    @Column(name = "NODE_DATA")
    private String nodeData;

    @Column(name = "PARENT_ID")
    private int parentId;

    @Column(name = "NODE_TYPE")
    private String nodeType;

    public Tree() {
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeData() {
        return this.nodeData;
    }

    public void setNodeData(String nodeData) {
        this.nodeData = nodeData;
    }

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getNodeType() {
        return nodeType;
    }
}