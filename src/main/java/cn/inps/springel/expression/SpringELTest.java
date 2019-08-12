package cn.inps.springel.expression;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringELTest {


    @Test
    public void test() {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new MyBeanResolver());
       // List<Inventor> list = (List<Inventor>) parser.parseExpression("&foo").getValue(context);
       Inventor inv = (Inventor) parser.parseExpression("&foo").getValue(context);

        //Object bean = parser.parseExpression("&foo").getValue(context);
        System.out.println(inv.getAge());
    }


    @Test
    public void testEL() {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("aid","1");
        ctx.setVariable("bid","1");
        Expression exp = parser.parseExpression("#aid=='zhoudl'||#bid=='222'");
        Boolean message = (Boolean) exp.getValue(ctx);
        System.out.println(message);
    }



//    public void testMethod() {
//        StandardEvaluationContext context = new StandardEvaluationContext();
//        context.setVariable("o", "xxxx");
//        List result = new ArrayList();
//        ExpressionParser parser = new SpelExpressionParser();
//        try {
//            rules(rule -> {
//                Expression preExpression = parser.parseExpression(rule.getPrerequisite());
//                if (preExpression.getValue(context, Boolean.class)) {
//                    Expression postExpression = parser.parseExpression(rule.getRequirement());
//                    if (!postExpression.getValue(context, Boolean.class)) {
//                        result.add(rule.getValidationMsg().getError());
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//    }


}
