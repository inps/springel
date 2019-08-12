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
import java.util.*;
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
        Participant participant = new Participant();
        participant.setAge("age18");
        participant.setName("name18");
        ctx2.setVariable("p", participant);
        //执行表达式1
        String exp  = parser.parseExpression("name").getValue(participant,String.class);
        log.info("exp result:{}",exp);
        //执行表达式2
        String executeperson  = parser.parseExpression("#p.name").getValue(ctx2).toString();
        log.info("executeperson result:{}",executeperson);


        //执行方法1
//        Participant resultp  = parser.parseExpression("#execute1('dddd','xxx')").getValue(ctx,Participant.class);
//        log.info("executeperson result:{}",resultp.getName());

        //执行方法2
//        Properties result1 = parser.parseExpression("@systemProperties").getValue(ctx, Properties.class);
//        log.info("ress:{}",result1.toString());

        //执行方法2
       Participant result1 = parser.parseExpression("@resolv").getValue(ctx, Participant.class);

      //  Participant result2 = parser.parseExpression("@aa").getValue(ctx, Participant.class);

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

        Participant result2 = parser.parseExpression("@key").getValue(ctx, Participant.class);

    }

    /**
     * 执行方法规则
     */
    @Test
    public void testMethod(){
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext ctx = new StandardEvaluationContext();

        //输入参数
        String className  = "cn.inps.springel.rule.MyRule";
        String  el  = "获取当(流程实例id,流程定义id,dd,xxxx)";
        //参数数量
        int paramCount = 0;


        Map<String,Object>  lhm =  new LinkedHashMap<String,Object>();
        lhm.put("当前人id","bbbb33");
        lhm.put("流程定义id","cccc33");
        lhm.put("流程实例id","dddd333");


        //替换规则表达式中所有的空格和'符合
        String expression=  el.replace(" ", "");
        expression=  expression.replace("'", "");


        // 通过正则表达式获取小括号中的内容，表达式变为   #execute（'当前人id的值','流程定义id的值'）
        Pattern pattern = Pattern.compile("\\([^)]+\\)");
        Matcher matcher = pattern.matcher(expression);
        String[] params =null;

        if (matcher.find())
        {
            // System.out.println(matcher.group());
            String quStr  = matcher.group(0);
            quStr = matcher.group().substring(1, matcher.group().length()-1);
            String[] ary = quStr.split(",");//使用字符串逗号 ,切割字符串
            paramCount = ary.length;
            params = new String[paramCount];

            // 现将数据替换成#execute（{0},{1},{2}）, 如果出现了一个key的value 和后面要替换的key相同，可能会出现替换错误
            for (int i = 0; i < paramCount; i++) {
                String aryStr  = String.valueOf(ary[i]);
                if(!aryStr.equals("")) {
                    // 使用replaceFirst防止参数名称部分相同，导致全部被替换
                    try {
                        expression = expression.replaceFirst(aryStr, "{" + i + "}");
                    } catch (Exception e) {
                        log.info("表达式参数不正确，包含未识别字符串。");
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 0; i < paramCount; i++) {
                // 忽略map中的key大小写
                String  aryValue  = String.valueOf(lhm.get(ary[i]));
                for(String k:lhm.keySet()) {
                    if (k.equalsIgnoreCase(ary[i])) {
                       aryValue = String.valueOf(lhm.get(k));
                    }
                }
                // 只为invoke赋值
                params[i] =aryValue;
                //替换字符串{0}{1}中的对应的值
                expression = expression.replaceFirst(String.format("\\{%d\\}", i), "'"+aryValue+"'");
            }


//            //直接替换字符串，如果出现了一个key的value 和后面要替换的key相同，可能会出现替换错误
//            for(String item: ary){
//                String value = "'"+lhm.get(item)+"'";
//                expression = expression.replace(item,value);
//            }
        }

        //执行规则对应的方法， 没有在规则中使用 BeanResolver resolve， 由于规则编写过程中不知道map<string ,object>中的参数含义，对规则编写不友好。
        Object objInst =null;
        Method[] methods =null;
        Method executeMethod = null;
        Method defaultMethod =null;
        String executeMethodName ="";


        if(className!=null||!className.trim().equals("")){
                try {
                    // 动态方法名称
                    Class<?> Clazz = Class.forName(className);
                    objInst = Clazz.newInstance();//实例化
                    methods = Clazz.getDeclaredMethods();
                    for (Method method:methods) {

//                        String [] s = new String[count];
//                        for(int i = 0; i < count; i++) {
//                            s[i] = String.valueOf("java.lang.String");
//                        }
                        // 对默认方法进行赋值
                        defaultMethod  =methods[0];

                        Class[]  ptypes = method.getParameterTypes();
                        //判断有任意一个类型不是String都不能执行
                        boolean pTypeFlag =true;
                        for (Class pt: ptypes) {
                            if(pt.getName()=="java.lang.String"){
                                pTypeFlag = pTypeFlag && true;
                            }else {
                                pTypeFlag = false;
                            }
                            log.info("参数名称:{},参数类型{},所有参数类型是否为String:{}",method.getName(),pt.getName(),pTypeFlag);
                        }

                        String typeName = method.getGenericReturnType().getTypeName();
                        log.info("方法类型：{}",typeName);
                        log.info("参数数量：{}",method.getParameterCount());

                        if(typeName.contains("Participant") && typeName.contains("List")  && method.getParameterCount()==paramCount&&pTypeFlag){
                            executeMethod=method;
                            executeMethodName = method.getName();
                        }
                        if(method.getParameterCount()==0) {
                            defaultMethod = method;
                            log.info("为默认执行方法赋值。");
                        }
                    }


//                    if(count==0){
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute");
//                    }else if(count==1) {
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class);
//                    }else if(count==2){
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class);
//                    }else if(count==3){
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class);
//                    }else if(count==4){
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class);
//                    }else if(count==5){
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class);
//                    }else if(count==6){
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class,String.class);
//                    }else if(count==7){
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class,String.class,String.class);
//                    }else if(count==8){
//                        executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class);
//                    } else{
//                        log.info("规则方法参数数量不正确，规则参数大于8个！");
//                    };


//                    switch (count){
//                        case 0:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute");
//                            break;
//                        case 1:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class);
//                            break;
//                        case 2:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class);
//                            break;
//                        case 3:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class);
//                            break;
//                        case 4:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class);
//                            break;
//                        case 5:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class);
//                            break;
//                        case 6:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class,String.class);
//                            break;
//                        case 7:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class,String.class,String.class);
//                            break;
//                        case 8:
//                            executeMethod = Class.forName(className).getDeclaredMethod("execute", String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class);
//                            break;
//                         default:
//                            log.info("规则方法参数数量不正确，规则参数大于8个！");
//                            break;
//                    }


                } catch (ClassNotFoundException e) {
                    log.info("没有找到规则对应的类！");
                    e.printStackTrace();
                }catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                log.info("规则定义所引用的类名为空！");
            }
//        } catch (NoSuchMethodException e) {
//            log.info("没有找到规则对应的方法");
//            e.printStackTrace();
//        }
        if(expression.contains("(")) {
            expression = expression.replaceFirst(el.substring(0, el.indexOf('(')), "#" + executeMethodName);
            log.info("需要执行的表单式:{}", expression);
        }else{
            executeMethod=defaultMethod;
            log.info("表达式中不包含()执行了默认方法");
        }
        //注册方法
        ctx.registerFunction(executeMethodName, executeMethod);

        //增加变量, 由于不支持中文变量， 直接将值传入表达式  #execute（'当前人id的值','流程定义id的值'），没有采用以下方式
        //ctx.setVariables(lhm);
        // String expression ="#execute(#currentid,#processdefid)";
        // String expression = convertExpression(elstr,lhm);

        //执行方法1
       // Participant resultp  = parser.parseExpression("#execute('dddd','xxx')").getValue(ctx,Participant.class);
        List<Participant> resultp  = null;
        try {
            //resultp = (List<Participant>) parser.parseExpression(expression).getValue(ctx);
            resultp = (List<Participant>) executeMethod.invoke(objInst,params);
        } catch (EvaluationException e) {
            log.info("表达式格式异常,确认所有参数类型是否String");
            e.printStackTrace();
        } catch (ParseException e) {
            log.info("解析表达式异常,确认是否有规则对应的方法和返回值类型");
            e.printStackTrace();
        } catch (Exception e) {
            log.info("解析表达式格式异常，未找到对应的方法，参数不能为空值。");
            e.printStackTrace();
        }
        log.info("executeperson result:{}",resultp.get(0).getName());

    }

    /**
     * RuleUtils 规则执行的方法
     * @param el
     * @param ruleParam
     * @param className
     * @return
     */
    public static Object execute1(String el, Map<String,Object> lhm, String className) {
        //输入参数
        //参数数量
        int paramCount = 0;
        //Map<String,Object> lhm =  ruleParam.getEnv();   // RuleParameters ruleParam

        //替换规则表达式中所有的空格和'符合
        String expression=  el.replace(" ", "");
        expression=  expression.replace("'", "");

        // 通过正则表达式获取小括号中的内容，表达式变为   #execute（'当前人id的值','流程定义id的值'）
        Pattern pattern = Pattern.compile("\\([^)]+\\)");
        Matcher matcher = pattern.matcher(expression);
        String[] params =null;

        if (matcher.find())
        {
            // System.out.println(matcher.group());
            String quStr  = matcher.group(0);
            quStr = matcher.group().substring(1, matcher.group().length()-1);
            String[] ary = quStr.split(",");//使用字符串逗号 ,切割字符串
            paramCount = ary.length;
            params = new String[paramCount];

            for (int i = 0; i < paramCount; i++) {
                // 忽略map中的key大小写
                String  aryValue  = String.valueOf(lhm.get(ary[i]));
                for(String k:lhm.keySet()) {
                    if (k.equalsIgnoreCase(ary[i])) {
                        aryValue = String.valueOf(lhm.get(k));
                    }
                }
                // 只为invoke赋值
                params[i] =aryValue;
                //替换字符串{0}{1}中的对应的值
                //expression = expression.replaceFirst(String.format("\\{%d\\}", i), "'"+aryValue+"'");
            }
        }
        //执行规则对应的方法， 没有在规则中使用 BeanResolver resolve， 由于规则编写过程中不知道map<string ,object>中的参数含义，对规则编写不友好。
        Object objInst =null;
        Method[] methods =null;
        Method executeMethod = null;
        Method defaultMethod =null;
        //判断类名是否为空
        if(className!=null||!className.trim().equals("")){
            try {
                // 动态方法名称
                Class<?> Clazz = Class.forName(className);
                objInst = Clazz.newInstance();//实例化
                methods = Clazz.getDeclaredMethods();
                for (Method method:methods) {

                    // 对默认方法进行赋值
                    defaultMethod  =methods[0];

                    Class[]  ptypes = method.getParameterTypes();
                    //判断有任意一个类型不是String都不能执行
                    boolean pTypeFlag =true;
                    for (Class pt: ptypes) {
                        if(pt.getName()=="java.lang.String"){
                            pTypeFlag = pTypeFlag && true;
                        }else {
                            pTypeFlag = false;
                        }
                        log.info("参数名称:{},参数类型{},所有参数类型是否为String:{}",method.getName(),pt.getName(),pTypeFlag);
                    }

                    String typeName = method.getGenericReturnType().getTypeName();
                    log.info("方法类型：{}",typeName);
                    log.info("参数数量：{}",method.getParameterCount());

                    if(method.getParameterCount()==paramCount&&pTypeFlag){
                        executeMethod=method;
                    }
                    if(method.getParameterCount()==0) {
                        defaultMethod = method;
                        log.info("为默认执行方法赋值。");
                    }
                }

            } catch (ClassNotFoundException e) {
                log.info("没有找到规则对应的类！");
                e.printStackTrace();
            }catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            log.info("规则定义所引用的类名为空！");
        }
//        } catch (NoSuchMethodException e) {
//            log.info("没有找到规则对应的方法");
//            e.printStackTrace();
//        }
        if(!expression.contains("(")) {
            executeMethod=defaultMethod;
            log.info("表达式中不包含()执行了默认方法");
        }

        //执行方法
        Object resultObject  = null;
        try {
            resultObject = executeMethod.invoke(objInst,params);
        }catch (Exception e) {
            log.info("解析表达式格式异常，未找到对应的方法，参数不能为空值。");
            e.printStackTrace();
        }
        //log.info("executeperson result:{}",obj.get(0).getName());
        return  resultObject;
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
