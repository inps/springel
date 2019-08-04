package cn.inps.springel.rule;

import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

import java.util.HashMap;
import java.util.Map;


//@Service("resolver")
public class MyBeanResolver implements BeanResolver {

    public Person resolve(EvaluationContext evaluationContext, String s) throws AccessException {
        System.out.println("test:"+s);
        System.out.println("te:"+evaluationContext.getRootObject().getValue().toString());

        Map<String,Object> hm = new HashMap<String ,Object>();
        hm =(Map<String, Object>) evaluationContext.getRootObject().getValue();
        System.out.println("cc:"+hm.get("cc"));
        System.out.println("bb:"+hm.get("bb"));


        Person person = new Person();
        person.setAge("age34");
        person.setName("name34");
        return person;
    }



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
        Person person = new Person();
        person.setAge("age40");
        person.setName("name40");
        return person;
    }


}
