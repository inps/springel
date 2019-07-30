package cn.inps.springel.expression;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.MethodResolver;

import java.util.List;

public class MyMethodResolver implements MethodResolver {
    public MethodExecutor resolve(EvaluationContext evaluationContext, Object o, String s, List<TypeDescriptor> list) throws AccessException {
        return null;
    }
}
