package cn.inps.springel.expression;

import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;


public class MyBeanResolver implements BeanResolver {

    public Object resolve(EvaluationContext context, String beanName) throws AccessException {
        if (beanName.equals("foo") || beanName.equals("bar")) {
            return "MyBeanResolver";
        }
        if (beanName.equals("&foo")) {
            Inventor a   =  new Inventor();
            a.setAge("age18");
            a.setName("nameAa");
            return a;
        }
        throw new AccessException("not heard of " + beanName);
    }
}