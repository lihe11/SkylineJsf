package test;

/**
 * Created by lihe
 * on 2015/8/5.10:08
 */
/**
 * ���޼��ڵ�ģ��
 */
public class Node {
    /**
     * �ڵ�id
     */
    private int id;
    /**
     * �ڵ�����
     */
    private String nodeName;
    /**
     * ���ڵ�id
     */
    private int parentId;
    public Node() {
    }
    Node(int id, int parentId) {
        this.id = id;
        this.parentId = parentId;
    }
    public Node(int id, String nodeName, int parentId) {
        this.id = id;
        this.nodeName = nodeName;
        this.parentId = parentId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}