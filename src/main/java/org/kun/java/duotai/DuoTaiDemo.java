package org.kun.java.duotai;

/**
 * 所谓多态，就是指一个引用（类型）在不同的情况下的多种状态。也可以理解为，多态是指通过指向父类的指针，来调用在不同子类中实现的方法。。
 * 
 * 场景假设： 一个主人养了猫和狗，猫和狗都有自己爱吃的东西，主人在喂它们的时候，如果既要判断是猫还是狗，再判断他们分别爱吃什么，就显得很麻烦。
 * 
 * 如果主人养了很多种动物，这样的重复判断，就会浪费很多时间。有什么办法，能让主人拿到一种食物就知道这是哪种动物的，就好了。
 * 
 * @author kun
 *
 */

public class DuoTaiDemo {

    public static void main(String[] args) {
        Master master = new Master();
        master.feed(new Dog(), new Bone());
        master.feed(new Cat(), new Fish());
    }

}
