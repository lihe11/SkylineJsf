package test;

/**
 * Created by lihe
 * on 2015/8/5.9:58
 */
/**
 *<p>Title:Java�ݹ��㷨ʵ��</p>
 *<p>Description:���õݹ��㷨���Fibonacci���е�5������ֵ</p>
 ���Fibonacci���еĵ�10��λ�õ�ֵ��
 ��쳲��������У�Fibonacci Sequence�����ֳƻƽ�ָ����У�
 ָ��������һ�����У�1��1��2��3��5��8��13��21������
 ����ѧ�ϣ�쳲��������������±��Եݹ�ķ������壺
 F0=0��F1=1��Fn=F(n-1)+F(n-2)��n>=2��n��N*����
 */
public class Fibonacci
{
    /**
     *�������������Fibonacci���еĵݹ��㷨
     *���������int n
     *�������ͣ�int
     */
    public static int fun(int n)
    {
        if(1==n || 2==n)
        {
            return 1;
        }
        else
        {
            return (fun(n-1) + fun(n-2));
        }
    }

    /**
     *����������������
     *���������String[] args
     *�������ͣ�void
     */
    public static void main(String[] args)
    {
        System.out.println(fun(10));
    }
}
