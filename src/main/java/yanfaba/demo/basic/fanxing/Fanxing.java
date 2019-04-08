package yanfaba.demo.basic.fanxing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * 什么是泛型？为什么要使用泛型？
 *
 * 泛型，即“参数化类型”。一提到参数，最熟悉的就是定义方法时有形参，然后调用此方法时传递实参。那么参数化类型怎么理解呢？
 * 顾名思义，就是将类型由原来的具体的类型参数化，类似于方法中的变量参数，此时类型也定义成参数形式（可以称之为类型形参），然后在使用/调用时传入具体的类型（类型实参）。
 * 泛型的本质是为了参数化类型（在不创建新的类型的情况下，通过泛型指定的不同类型来控制形参具体限制的类型）。
 * 也就是说在泛型使用过程中，操作的数据类型被指定为一个参数，这种参数类型可以用在类、接口和方法中，分别被称为泛型类、泛型接口、泛型方法。
 */
public class Fanxing {

    @Autowired
    GenericFruit genericFruit;

    private Logger logger = LoggerFactory.getLogger(Fanxing.class);


    /**
     * ArrayList可以存放任意类型，例子中添加了一个String类型，添加了一个Integer类型，再使用时都以String的方式使用，
     * 因此程序崩溃了。为了解决类似这样的问题（在编译阶段就可以解决），泛型应运而生。
     */
  //  @Test
    public void demo1(){

        //arrayList.add(100); 在编译阶段，编译器就会报错
    //    List<String> arrayList = new ArrayList<String>();

        List arrayList = new ArrayList();
        arrayList.add("aaaa");
        arrayList.add(100);

        for(int i = 0; i< arrayList.size();i++){
            String item = (String)arrayList.get(i);
            logger.info("泛型测试","item = " + item);
        }

    }

    /**
     * 泛型只在编译阶段有效
     * 通过上面的例子可以证明，在编译之后程序会采取去泛型化的措施。也就是说Java中的泛型，只在编译阶段有效。
     * 在编译过程中，正确检验泛型结果后，会将泛型的相关信息擦出，并且在对象进入和离开方法的边界处添加类型检查和类型转换的方法。
     * 也就是说，泛型信息不会进入到运行时阶段。
     * 泛型类型在逻辑上看以看成是多个不同的类型，实际上都是相同的基本类型。
     */

   // @Test
    public void demo2(){
        List<String> stringArrayList = new ArrayList<String>();
        List<Integer> integerArrayList = new ArrayList<Integer>();

        Class classStringArrayList = stringArrayList.getClass();
        Class classIntegerArrayList = integerArrayList.getClass();

        if(classStringArrayList.equals(classIntegerArrayList)){
            logger.info("泛型测试","类型相同");
        }
    }

   // @Test
    public void demo3(){
        //泛型的类型参数只能是类类型（包括自定义类），不能是简单类型
        //传入的实参类型需与泛型的类型参数类型相同，即为Integer.
        Generic<Integer> genericInteger = new Generic<>(12345);
        //传入的实参类型需与泛型的类型参数类型相同，即为String.
        Generic<String> genericString = new Generic<>("keystring");
        logger.info(String.valueOf(genericInteger.getKey()));
        logger.info(genericString.getKey());

    }

    /**
     * 定义的泛型类，就一定要传入泛型类型实参么？
     * 并不是这样，在使用泛型的时候如果传入泛型实参，则会根据传入的泛型实参做相应的限制，此时泛型才会起到本应起到的限制作用。
     * 如果不传入泛型类型实参的话，在泛型类中使用泛型的方法或成员变量定义的类型可以为任何的类型。
     */
    //@Test
    public void demo4(){
        Generic generic1 = new Generic(11111);
        Generic generic2 = new Generic("string");
        Generic generic3 = new Generic(false);
        Generic generic4 = new Generic(55.55);

        logger.info(String.valueOf(generic1.getKey()));
        logger.info(String.valueOf(generic2.getKey()));
        logger.info(String.valueOf(generic3.getKey()));
        logger.info(String.valueOf(generic4.getKey()));
    }

    //泛型的类型参数只能是类类型，不能是简单类型。
    // 不能对确切的泛型类型使用instanceof操作。如下面的操作是非法的，编译时会出错。
    //    if(ex_num instanceof Generic<Number>){
    // }


    /**
     * 泛型通配符 ？
     *
     * @param obj
     */
    public void showKeyValue(Generic<Number> obj){

       logger.info(String.valueOf(obj.getKey()));
    }

    /**
     * 类型通配符一般是使用？代替具体的类型实参，注意了，此处’？’是类型实参，而不是类型形参 。
     * 重要说三遍！此处’？’是类型实参，而不是类型形参 ！ 此处’？’是类型实参，而不是类型形参 ！
     * 再直白点的意思就是，此处的？和Number、String、Integer一样都是一种实际的类型，可以把？看成所有类型的父类。是一种真实的类型。
     * 可以解决当具体类型不确定的时候，这个通配符就是 ?  ；当操作类型时，不需要使用类型的具体功能时，只使用Object类中的功能。
     * 那么可以用 ? 通配符来表未知类型。
     * @param obj
     */
    public void showKeyValue1(Generic<?> obj){

        logger.info(String.valueOf(obj.getKey()));
    }

    /**
     * 通过提示信息我们可以看到Generic<Integer>不能被看作为`Generic<Number>的子类。由此可以看出:同一种泛型可以对应多个版本（因为参数类型是不确定的），不同版本的泛型类实例是不兼容的。
     * 回到上面的例子，如何解决上面的问题？总不能为了定义一个新的方法来处理Generic<Integer>类型的类，这显然与java中的多台理念相违背。
     * 因此我们需要一个在逻辑上可以表示同时是Generic<Integer>和Generic<Number>父类的引用类型。由此类型通配符应运而生。
     */
    //@Test
    public void demo6(){

        Generic<Integer> gInteger = new Generic<Integer>(123);
        Generic<Number> gNumber = new Generic<Number>(456);

        showKeyValue(gNumber);
        // showKeyValue这个方法编译器会为我们报错：Generic<java.lang.Integer>
        // cannot be applied to Generic<java.lang.Number>
        //showKeyValue(gInteger);

        showKeyValue1(gInteger);
        showKeyValue1(gNumber);

    }

    /**
     * 泛型方法与可变参数
     *
     * @param args
     * @param <T>
     */
    public <T> void printMsg( T... args){
        for(T t : args){
            logger.info(String.valueOf(t));
        }
    }

   // @Test
    public void demo8(){
        printMsg("111",222,"aaaa","2323.4",55.55);
    }


    public void showKeyValue2(Generic<? extends Number> obj){
        logger.info(String.valueOf(obj.getKey()));
    }

    public void showKeyValue3(GenericNumber<?> obj){
        logger.info(String.valueOf(obj.getKey()));
    }


    /**
     * 在使用泛型的时候，我们还可以为传入的泛型类型实参进行上下边界的限制，如：类型实参只准传入某种类型的父类或某种类型的子类。
     * 为泛型添加上边界，即传入的类型实参必须是指定类型的子类型
     *
     */

    //@Test
    public void demo9(){

        Generic<String> generic1 = new Generic<String>("11111");
        Generic<Integer> generic2 = new Generic<Integer>(2222);
        Generic<Float> generic3 = new Generic<Float>(2.4f);
        Generic<Double> generic4 = new Generic<Double>(2.56);

        //这一行代码也会报错，因为String不是Number的子类
       // GenericNumber<String> generic5 = new GenericNumber<String>("1111");

        //这一行代码编译器会提示错误，因为String类型并不是Number类型的子类
        //showKeyValue2(generic1);

        showKeyValue2(generic2);
        showKeyValue2(generic3);
        showKeyValue2(generic4);

    }






    /**
     *  通过两个例子可以看出 : 泛型的上下边界添加，必须与泛型的声明在一起 。
     * @param container
     * @param <T>
     * @return
     */
    //在泛型方法中添加上下边界限制的时候，必须在权限声明与返回值之间的<T>上添加上下边界，即在泛型声明的时候添加
 //   public <T> T showKeyName(Generic<T extends Number> container){    // 编译器会报错："Unexpected bound"
    public <T extends Number> T showKeyName(Generic<T> container){
        System.out.println("container key :" + container.getKey());
        T test = container.getKey();
        return test;
    }

    /**
     * 泛型数组
     */
    public void demo10(){

        //在java中是”不能创建一个确切的泛型类型的数组”的。
       // List<String>[] ls = new ArrayList<String>[10];

        //而使用通配符创建泛型数组是可以的
        List<?>[] ls = new ArrayList<?>[10];

        List<String>[] ls1 = new ArrayList[10];

       // 下面采用通配符的方式是被允许的:数组的类型不可以是类型变量，除非是采用通配符的方式，因为对于通配符的方式，最后取出数据是要做显式的类型转换的。

        List<?>[] lsa = new List<?>[10]; // OK, array of unbounded wildcard type.
        Object o = lsa;
        Object[] oa = (Object[]) o;
        List<Integer> li = new ArrayList<Integer>();
        li.add(new Integer(3));
        oa[1] = li; // Correct.
        Integer i = (Integer) lsa[1].get(0); // OK

    }

}
