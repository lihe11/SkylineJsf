package test;

/**
 * Created by lihe
 * on 2015/8/5.14:18
 */

//������
public class AllTree implements Comparable<AllTree> {

    private int  id;

    private String name;

    private int pid;

    @Override
    public String toString() {
        return "AllTree [id=" + id + ", name=" + name + ", pid=" + pid + "]";
    }
    //��дequals�����������ظ��ĸ���ʱʹ��
    //��ʹ��list.contains()��������ô˷����Ƚ�
    @Override
    public boolean equals(Object obj) {
        AllTree at = (AllTree) obj;

        //ҵ����Ҫ����ǰ��pid���ڴ�������idʱ����˵����������Ѿ�add����list��
        if(this.pid==at.id){
            return true;
        }
        return false;
    }

    public AllTree() {
        super();
        // TODO Auto-generated constructor stub
    }

    public AllTree(int id, String name, int pid) {
        super();
        this.id = id;
        this.name = name;
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public int compareTo(AllTree at) {
        return (this.id>at.id)?1:((this.id<at.id)?-1:0);
    }

}
