package cn.inps.springel.rule;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //输入参数
        String className  = "cn.inps.springel.rule.MyRule";
        String  el  = "获取当(流程实例id,,'dd)";
        int count = 0;


        Map<String,Object>  lhm =  new LinkedHashMap<String,Object>();
        lhm.put("当前人id","bbbb33");
        lhm.put("流程定义id","cccc33");
        lhm.put("流程实例id","dddd333");


        //替换规则表达式中所有的空格和'符合
        String expression=  el.replace(" ", "");
        expression=  expression.replace("'", "");
        expression=  expression.replaceFirst(el.substring(0,el.indexOf('(')),"#execute");

        // 通过正则表达式获取小括号中的内容，表达式变为   #execute（'当前人id的值','流程定义id的值'）
        Pattern pattern = Pattern.compile("\\([^)]+\\)");
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find())
        {
            // System.out.println(matcher.group());
            String quStr  = matcher.group(0);
            quStr = matcher.group().substring(1, matcher.group().length()-1);

            String[] ary = quStr.split(",");//使用字符串逗号 ,切割字符串
            count = ary.length;
            // 现将数据替换成#execute（{0},{1},{2}）, 如果出现了一个key的value 和后面要替换的key相同，可能会出现替换错误
            for (int i = 0; i < count; i++) {
                if(!String.valueOf(ary[i]).equals("")) {
                    expression = expression.replace(String.valueOf(ary[i]), "{" + i + "}");
                }
            }
            for (int i = 0; i < count; i++) {
                expression = expression.replaceAll(String.format("\\{%d\\}", i), "'"+String.valueOf(lhm.get(ary[i]))+"'");
            }
            log.info("需要执行的表单式:{}",expression);

//            for(String item: ary){
//                //如果出现了一个key的value 和后面要替换的key相同，可能会出现替换错误
//                String value = "'"+lhm.get(item)+"'";
//                expression = expression.replace(item,value);
//            }
        }

        //执行规则对应的方法， 没有在规则中使用 BeanResolver resolve， 由于规则编写过程中不知道map<string ,object>中的参数含义，对规则编写不友好。
        Method executeMethod = null;
        try {

            if(className!=null||!className.trim().equals("")){
            //  execute = MyBeanResolver.class.getDeclaredMethod("execute");
                try {
                    if(count==0){
                        executeMethod = Class.forName(className).getDeclaredMethod("execute");
                    }else if(count==1) {
                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class);
                    }else if(count==2){
                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class);
                    }else if(count==3){
                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class);
                    }else if(count==4){
                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class);
                    }else if(count==5){
                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class);
                    }else if(count==6){
                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class,String.class);
                    }else if(count==7){
                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class,String.class,String.class);
                    }else{
                        log.info("规则方法参数数量不正确，规则参数大于7个！");
                    };
                } catch (ClassNotFoundException e) {
                    log.info("没有找到规则对应的类");
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            log.info("没有找到规则对应的方法");
            e.printStackTrace();
        }

        //注册方法
        ctx.registerFunction("execute", executeMethod);

        //增加变量, 由于不支持中文变量， 直接将值传入表达式  #execute（'当前人id的值','流程定义id的值'），没有采用以下方式
        //ctx.setVariables(lhm);
        // String expression ="#execute(#currentid,#processdefid)";
        // String expression = convertExpression(elstr,lhm);



        //执行方法1
       // Person resultp  = parser.parseExpression("#execute('dddd','xxx')").getValue(ctx,Person.class);
        Person resultp  = null;
        try {
            resultp = parser.parseExpression(expression).getValue(ctx, Person.class);
        } catch (EvaluationException e) {
            log.info("表达式格式异常");
            e.printStackTrace();
        } catch (ParseException e) {
            log.info("解析表达式异常");
            e.printStackTrace();
        } catch (Exception e) {
            log.info("解析表达式格式异常，参数不能为空值。");
            e.printStackTrace();
        }
        log.info("executeperson result:{}",resultp.getName());

    }


    public String  convertExpression(String el,Map<String,Object> hm){

         // el  = "获取当前处理人(当前人id,流程定义id)";
        String expression=  el.replace(" ", "");
        expression=  el.replaceFirst(el.substring(0,el.indexOf('(')),"#execute");

        // 通过正则表达式获取小括号中的内容
        Pattern pattern = Pattern.compile("\\([^)]+\\)");
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find())
        {
           // System.out.println(matcher.group());
            String quStr  = matcher.group(0);
            quStr = matcher.group().substring(1, matcher.group().length()-1);

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
    public int getParametesCount(String elstr){
        int count =0;
        String expression=  elstr.replace(" ", "");
        // 通过正则表达式获取小括号中的内容
        Pattern pattern = Pattern.compile("\\([^)]+\\)");
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find())
        {
            // System.out.println(matcher.group());
            String quStr  = matcher.group(0);
            quStr = matcher.group().substring(1, matcher.group().length()-1);
            String[] ary = quStr.split(",");//使用字符串逗号 ,切割字符串
            count = ary.length;
        }

        return count;
    }

}
