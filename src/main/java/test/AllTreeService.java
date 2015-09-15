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
        at.add(new AllTree(1,"A����Ŀ",0));
        at.add(new AllTree(2,"B����Ŀ",0));
        at.add(new AllTree(4,"A����Ŀ",1));
        at.add(new AllTree(5,"A����Ŀ",1));
        at.add(new AllTree(7,"B����Ŀ",2));
        at.add(new AllTree(8,"B����Ŀ",2));
        at.add(new AllTree(9,"B����Ŀ",2));
        at.add(new AllTree(3,"A����Ŀ",1));
        at.add(new AllTree(6,"A����Ŀ",1));
        at.add(new AllTree(11,"B9����Ŀ",9));
        at.add(new AllTree(12,"B9����Ŀ",9));
        at.add(new AllTree(10,"B9����Ŀ",9));
        at.add(new AllTree(13,"B2����Ŀ",2));
        at.add(new AllTree(14,"B2����Ŀ",2));
        at.add(new AllTree(15,"B2����Ŀ",2));
        at.add(new AllTree(16,"B2����Ŀ",2));
    }
    /**
     *
     * @param parentTree ���ڵ���
     * @param ats  ���е���
     * @param pids  ���������� ID
     * @param tree  �Ѿ���������ṹ����ʼ����ʱΪnull
     */
    public List<AllTree> listChildren(AllTree parentTree ,List<AllTree> ats ,List<Integer> pids,List<AllTree> tree){
        if(tree==null){
            tree  = new ArrayList<AllTree>();
        }
        for(AllTree atr :ats){
            if(atr.getPid()!=0){
                if(atr.getPid()==(parentTree.getId())){
                    tree.add(atr);
                    //����������б��а�����ǰ���ڵ�
                    if(pids.contains(atr.getPid())){
                        List<Integer> pbs = pids;
                        pbs.remove(Integer.valueOf(atr.getId()));
                        //�ݹ��ٽ��д���
                        listChildren(atr , ats ,pbs,tree );
                    }
                }
            }
        }

        return tree;

    }

    /**
     * ��ȡ���и���
     * @param ats   ���з��� ������
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
     * ��ȡ���и��ڵ��IDֵ
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
