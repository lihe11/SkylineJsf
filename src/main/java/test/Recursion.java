package test;

/**
 * Created by lihe
 * on 2015/8/5.9:25
 */
public class Recursion {





    public static void main(String[] args) {

        // TODO 自动生成方法存根

        int car=3;

        int i=1;

        System.out.print(recursion(car,i));

    }

    public static int recursion(int car,int i){

        car=2*(car-1);

        i++;

        if( i<4){

            car +=recursion(car,i);

        }

        return car;

    }





}

