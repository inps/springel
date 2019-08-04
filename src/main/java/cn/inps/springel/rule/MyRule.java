package cn.inps.springel.rule;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则说明：
 * 1. 如果使用springel进行解析，需要使用static对方法进行修饰
 * 2. 参与者规则返回值类型必须是 List<Participant>
 * 3. 方法的参数类型必须是String  ，  默认方法名称execute
 */

public class MyRule {

    public   List<Participant>  dd(){
        Participant person = new Participant();
        person.setAge("age37");
        person.setName("name37");
        List<Participant> pp= new ArrayList<>();
        pp.add(person);
        return pp;
    }
    public static  Participant  cc(String input){
        System.out.println("input:"+input);
        Participant person = new Participant();
        person.setAge("age38");
        person.setName("name38");
        return person;
    }
    public static  Participant  bb(String input,String input1){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        Participant person = new Participant();
        person.setAge("age39");
        person.setName("name39");
        return person;
    }
    public  List<Participant> execute(String input, String input1, String input2){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        System.out.println("input2:"+input2);
        Participant person = new Participant();
        person.setAge("age40");
        person.setName("name40");

        List<Participant> pp= new ArrayList<>();
        pp.add(person);
        return pp;
    }

    public   List<Participant>  ee(String input,int input1,String input2,String input3){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        System.out.println("input2:"+input2);
        System.out.println("input3:"+input3);
        Participant person = new Participant();
        person.setAge("age40");
        person.setName("name40");
        List<Participant> pp= new ArrayList<>();
        pp.add(person);
        return pp;
    }

    public static  List<Participant>  xxxaa(String input,String input1,String input2,String input3){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        System.out.println("input2:"+input2);
        System.out.println("input3:"+input3);
        Participant person = new Participant();
        person.setAge("age40");
        person.setName("name40");
        List<Participant> pp= new ArrayList<>();
        pp.add(person);
        return pp;
    }
}
