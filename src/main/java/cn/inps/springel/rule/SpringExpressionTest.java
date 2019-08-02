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
//        ctx.setBeanResolver(new MyBeanResolverB());
//        ctx.setBeanResolver(new MyBeanResolver());
//        ClassPathXmlApplicationContext ctxpath = new ClassPathXmlApplicationContext();
//        ctxpath.refresh();
//        ctx.setBeanResolver(new BeanFactoryResolver(ctxpath));

        Map<String,Object> hm = new   HashMap<String ,Object>();
        Map<String, Object>  lhm =  new LinkedHashMap<String, Object>();
        hm.put("bb","bbbbb");
        hm.put("cc","ccccc");

        ctx.setVariable("aa","aaaaaa");
        String  a  = "xxxx";
        ctx.setRootObject(hm);





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
//        Person resultp  = parser.parseExpression("#execute1('dddd','xxx')").getValue(ctx,Person.class);
//        log.info("executeperson result:{}",resultp.getName());

        //执行方法2
//        Properties result1 = parser.parseExpression("@systemProperties").getValue(ctx, Properties.class);
//        log.info("ress:{}",result1.toString());

        //执行方法2
       Person result1 = parser.parseExpression("@resolv").getValue(ctx, Person.class);

      //  Person result2 = parser.parseExpression("@aa").getValue(ctx, Person.class);

       // log.info("ressxxx:{}",result2.getName());


    }

    @Test
    public void testResolver(){
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setBeanResolver(new MyBeanResolver());

        ctx.setVariable("key","value");
        Map<String,Object>  lhm =  new LinkedHashMap<String,Object>();
        lhm.put("bb","bbbb");
        lhm.put("cc","cccc");
        ctx.setRootObject(lhm);

    //    ParserContext ParserContext  = new ParserContext();

       parser.parseExpression("#key").setValue(ctx,"xxx");

        Person result2 = parser.parseExpression("@key").getValue(ctx, Person.class);

    }

    @Test
    public void testMethod(){
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext ctx = new StandardEvaluationContext();

        String className  = "cn.inps.springel.rule.MyBeanResolver";

        Method executeMethod = null;
        try {

            if(className!=null||!className.trim().equals("")){
            //  execute = MyBeanResolver.class.getDeclaredMethod("execute");
                try {
                    executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class);



                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        ctx.registerFunction("execute", executeMethod);

        Map<String,Object>  lhm =  new LinkedHashMap<String,Object>();
        lhm.put("当前人id","bbbb33");
        lhm.put("流程定义id","cccc33");
        lhm.put("流程实例id","dddd333");
        ctx.setVariables(lhm);


        String  elstr  = "获取当前处理人(当前人id,流程定义id)";
        String expression = getEL(elstr,lhm);

       // String expression ="#execute(#currentid,#processdefid)";


        //执行方法1
       // Person resultp  = parser.parseExpression("#execute('dddd','xxx')").getValue(ctx,Person.class);
       Person resultp  = parser.parseExpression(expression).getValue(ctx,Person.class);
        log.info("executeperson result:{}",resultp.getName());

    }
    public String  getEL(String el,Map<String,Object> hm){

         // el  = "获取当前处理人(当前人id,流程定义id)";
        String expression=  el.replace(" ", "");
        expression=  el.replaceFirst(el.substring(0,el.indexOf('(')),"#execute");
        String quStr=expression.substring(el.indexOf("(")+2,el.indexOf(")")+1);
        //String quStr = expression.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");

        if(quStr!=null|| !"".equals(quStr)){
            String[] ary = quStr.split(",");//使用字符串逗号 ,切割字符串
            for(String item: ary){
                String value = "'"+hm.get(item)+"'";
                expression = expression.replace(item,value);
            }
        }
        System.out.println(expression);

        //String expression ="#execute(#currentid,#processdefid)";
        return expression;
    }


}
