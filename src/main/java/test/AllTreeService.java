package test;

/**
 * Created by lihe
 * on 2015/8/5.14:19
 */

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllTreeService {
    private List<AllTree> at ;

    @Before
    public void listTree(){
        at= new ArrayList<AllTree>();
        at.add(new AllTree(1,"A父栏目",0));
        at.add(new AllTree(2,"B父栏目",0));
        at.add(new AllTree(4,"A子栏目",1));
        at.add(new AllTree(5,"A子栏目",1));
        at.add(new AllTree(7,"B子栏目",2));
        at.add(new AllTree(8,"B子栏目",2));
        at.add(new AllTree(9,"B子栏目",2));
        at.add(new AllTree(3,"A子栏目",1));
        at.add(new AllTree(6,"A子栏目",1));
        at.add(new AllTree(11,"B9子栏目",9));
        at.add(new AllTree(12,"B9子栏目",9));
        at.add(new AllTree(10,"B9子栏目",9));
        at.add(new AllTree(13,"B2子栏目",2));
        at.add(new AllTree(14,"B2子栏目",2));
        at.add(new AllTree(15,"B2子栏目",2));
        at.add(new AllTree(16,"B2子栏目",2));
    }
    /**
     *
     * @param parentTree 父节点树
     * @param ats  所有的树
     * @param pids  所有树父类 ID
     * @param tree  已经保存的树结构，初始传入时为null
     */
    public List<AllTree> listChildren(AllTree parentTree ,List<AllTree> ats ,List<Integer> pids,List<AllTree> tree){
        if(tree==null){
            tree  = new ArrayList<AllTree>();
        }
        for(AllTree atr :ats){
            if(atr.getPid()!=0){
                if(atr.getPid()==(parentTree.getId())){
                    tree.add(atr);
                    //如果父类树列表中包含当前树节点
                    if(pids.contains(atr.getPid())){
                        List<Integer> pbs = pids;
                        pbs.remove(Integer.valueOf(atr.getId()));
                        //递归再进行处理
                        listChildren(atr , ats ,pbs,tree );
                    }
                }
            }
        }

        return tree;

    }

    /**
     * 获取所有父类
     * @param ats   所有分类 父和子
     * @return
     */
    public List<AllTree>  listParent(List<AllTree> ats ){
        List<AllTree> ptrees = new ArrayList<AllTree>();
        for(AllTree at :ats){
            if(!ptrees.contains(at)){
                ptrees.add(at);
            }
        }

        return ptrees;

    }

    /**
     * 获取所有父节点的ID值
     * @param ats
     * @return
     */
    private List<Integer> getParentids(List<AllTree> ats) {
        List<Integer> pids = new ArrayList<Integer>();
        for(AllTree a : ats){
            pids.add(a.getId());
        }
        return pids;
    }

    @Test
    public void getTrees(){
        List<AllTree> atree = new ArrayList<AllTree>();
        List<AllTree> ats= listParent( at );

        Iterator<AllTree> atsiter = ats.iterator();
        while (atsiter.hasNext()){
            AllTree aa= atsiter.next();
            List<Integer> pids = getParentids(ats);
            List<AllTree> tree = null;

            tree =  listChildren(aa,at,pids,null);
            if(tree.size()>1){
                tree.add(0,aa);
            }

            atree.addAll(tree);
        }

        System.out.println(atree);

    }


}
