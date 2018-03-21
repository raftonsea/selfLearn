package classloader;

/**
 * @author: raftonsea
 * @createTime:2018/2/23 14:19
 * @description:
 */
public class StaticFinalDemo {
    public static void main(String[] args) {
//        System.err.println(Demo.x);
        testLoader();
    }


    static void testLoader() {
//        ClassLoader loader = StaticFinalDemo.class.getClassLoader();
//        System.err.println(loader + " parent " + loader.getParent() + " ,parent : " + loader.getParent().getParent());
//
//        System.err.println(int.class.getClassLoader());
//        System.err.println(String.class.getClassLoader());

        System.err.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
    }


}


class Demo {
    //若有final 则静态代码块不输出
    //若无final ， 则输出
    //原因是： final 声明了变量不可变，在编译时期直接确定了变量值
    static final int x = 6 / 3;

    static {
        System.err.println(" Demo invoke ");
    }
}


