package cn.inps.springel.rule;

import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;


//@Service("resolver")
public class MyBeanResolver implements BeanResolver {

    public Person resolve(EvaluationContext evaluationContext, String s) throws AccessException {
        System.out.println("test:"+s);
        System.out.println("te:"+evaluationContext.getRootObject().getValue().toString());

        Person person = new Person();
        person.setAge("age34");
        person.setName("name34");
        return person;
    }



    public static  Person  execute(){
        Person person = new Person();
        person.setAge("age38");
        person.setName("name38");
        return person;
    }
}
