package cn.inps.springel.rule;

public class MyRule {

    public static  Person  execute(){
        Person person = new Person();
        person.setAge("age37");
        person.setName("name37");
        return person;
    }
    public static  Person  execute(String input){
        System.out.println("input:"+input);
        Person person = new Person();
        person.setAge("age38");
        person.setName("name38");
        return person;
    }
    public static  Person  execute(String input,String input1){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        Person person = new Person();
        person.setAge("age39");
        person.setName("name39");
        return person;
    }
    public static  Person  execute(String input,String input1,String input2){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        System.out.println("input2:"+input2);
        Person person = new Person();
        person.setAge("age40");
        person.setName("name40");
        return person;
    }
    public static  Person  execute(String input,String input1,String input2,String input3){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        System.out.println("input2:"+input2);
        System.out.println("input3:"+input3);
        Person person = new Person();
        person.setAge("age40");
        person.setName("name40");
        return person;
    }
}
