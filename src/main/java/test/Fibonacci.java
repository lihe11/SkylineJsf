package test;

/**
 * Created by lihe
 * on 2015/8/5.9:58
 */
/**
 *<p>Title:Java递归算法实例</p>
 *<p>Description:利用递归算法求解Fibonacci数列第5个数的值</p>
 求解Fibonacci数列的第10个位置的值？
 （斐波纳契数列（Fibonacci Sequence），又称黄金分割数列，
 指的是这样一个数列：1、1、2、3、5、8、13、21、……
 在数学上，斐波纳契数列以如下被以递归的方法定义：
 F0=0，F1=1，Fn=F(n-1)+F(n-2)（n>=2，n∈N*））
 */
public class Fibonacci
{
    /**
     *方法描述：求解Fibonacci数列的递归算法
     *输入参数：int n
     *返回类型：int
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
     *方法描述：主方法
     *输入参数：String[] args
     *返回类型：void
     */
    public static void main(String[] args)
    {
        System.out.println(fun(10));
    }
}
