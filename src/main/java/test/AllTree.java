package test;

/**
 * Created by lihe
 * on 2015/8/5.14:18
 */

//树对象
public class AllTree implements Comparable<AllTree> {

    private int  id;

    private String name;

    private int pid;

    @Override
    public String toString() {
        return "AllTree [id=" + id + ", name=" + name + ", pid=" + pid + "]";
    }
    //重写equals方法，过滤重复的父类时使用
    //在使用list.contains()方法会调用此方法比较
    @Override
    public boolean equals(Object obj) {
        AllTree at = (AllTree) obj;

        //业务需要：当前的pid等于传过来的id时，即说明父类对象已经add到了list中
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
