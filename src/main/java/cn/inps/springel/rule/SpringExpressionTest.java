package cn.inps.springel.rule;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpringExpressionTest {
    public  static Logger log  =  LoggerFactory.getLogger(SpringExpressionTest.class);

    @Test
    public void testEL(){
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext ctx = new StandardEvaluationContext();


        Method execute = null;
        try {
          //  execute = MyBeanResolver.class.getDeclaredMethod("execute");
           execute = MyBeanResolver.class.getDeclaredMethod("execute", String.class,String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        ctx.registerFunction("execute1", execute);

        ctx.setVariable("execute2", execute);


        // 从bean中获取
//        BeanFactory beanFactory = SpringBeanUtils.getBeanFactory();
//        ctx.setBeanResolver(new BeanFactoryResolver(beanFactory));
        ctx.setBeanResolver(new MyBeanResolver());


        Map<String,Object> hm = new   HashMap<String ,Object>();
        Map<String, Object>  lhm =  new LinkedHashMap<String, Object>();
        hm.put("bb","bbbbb");
        hm.put("cc","ccccc");

        ctx.setVariable("aa","aaaaaa");
        String  a  = "xxxx";
        ctx.setRootObject(hm);


//        ClassPathXmlApplicationContext ctxpath = new ClassPathXmlApplicationContext();
//        ctxpath.refresh();
//        ctx.setBeanResolver(new BeanFactoryResolver(ctxpath));


//        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
//        ctx.refresh();
//        ExpressionParser parser = new SpelExpressionParser();
//        StandardEvaluationContext context = new StandardEvaluationContext();
//        context.setBeanResolver(new BeanFactoryResolver(ctx));
//        Properties result1 = parser.parseExpression("@systemProperties").getValue(context, Properties.class);


        EvaluationContext ctx2 = new StandardEvaluationContext();
        Person person = new Person();
        person.setAge("age18");
        person.setName("name18");
        ctx2.setVariable("p", person);
        //执行表达式1
        String exp  = parser.parseExpression("name").getValue(person,String.class);
        log.info("exp result:{}",exp);
        //执行表达式2
        String executeperson  = parser.parseExpression("#p.name").getValue(ctx2).toString();
        log.info("executeperson result:{}",executeperson);


        //执行方法1
        Person resultp  = parser.parseExpression("#execute1('dddd','xxx')").getValue(ctx,Person.class);
        log.info("executeperson result:{}",resultp.getName());

        //执行方法2
//        Properties result1 = parser.parseExpression("@systemProperties").getValue(ctx, Properties.class);
//        log.info("ress:{}",result1.toString());

        //执行方法2
      //  Person result1 = parser.parseExpression("@resolver").getValue(ctx, Person.class);

        Person result2 = parser.parseExpression("@aa").getValue(ctx, Person.class);

        log.info("ressxxx:{}",result2.getName());


    }

    @Test
    public void testRule(){
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setBeanResolver(new MyBeanResolver());

        ctx.setVariable("key","value");
        Map<String,Object>  lhm =  new LinkedHashMap<String,Object>();
        lhm.put("bb","bbbb");
        lhm.put("cc","cccc");
        ctx.setRootObject(lhm);

    //    ParserContext ParserContext  = new ParserContext();

      //  String resultxx = parser.parseExpression("#key").setValue(ctx,"xxx");

        Person result2 = parser.parseExpression("#key").getValue(ctx, Person.class);

    }}
