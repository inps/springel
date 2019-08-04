package cn.inps.springel.rule;

import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

import java.util.HashMap;
import java.util.Map;


//@Service("resolver")
public class MyBeanResolver implements BeanResolver {

    public Participant resolve(EvaluationContext evaluationContext, String s) throws AccessException {
        System.out.println("test:"+s);
        System.out.println("te:"+evaluationContext.getRootObject().getValue().toString());

        Map<String,Object> hm = new HashMap<String ,Object>();
        hm =(Map<String, Object>) evaluationContext.getRootObject().getValue();
        System.out.println("cc:"+hm.get("cc"));
        System.out.println("bb:"+hm.get("bb"));


        Participant participant = new Participant();
        participant.setAge("age34");
        participant.setName("name34");
        return participant;
    }



    public static Participant execute(){
        Participant participant = new Participant();
        participant.setAge("age37");
        participant.setName("name37");
        return participant;
    }
    public static Participant execute(String input){
        System.out.println("input:"+input);
        Participant participant = new Participant();
        participant.setAge("age38");
        participant.setName("name38");
        return participant;
    }
    public static Participant execute(String input, String input1){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        Participant participant = new Participant();
        participant.setAge("age39");
        participant.setName("name39");
        return participant;
    }
    public static Participant execute(String input, String input1, String input2){
        System.out.println("input:"+input);
        System.out.println("input1:"+input1);
        Participant participant = new Participant();
        participant.setAge("age40");
        participant.setName("name40");
        return participant;
    }


}
