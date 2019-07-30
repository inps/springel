package cn.inps.springel.expression;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.ConstructorExecutor;
import org.springframework.expression.ConstructorResolver;
import org.springframework.expression.EvaluationContext;

import java.util.List;

public class MyConstructorResolvers implements ConstructorResolver {

    public ConstructorExecutor resolve(EvaluationContext evaluationContext, String s, List<TypeDescriptor> list) throws AccessException {


        return null;
    }
}
