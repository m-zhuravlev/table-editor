package tableeditor.expression.dso;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static tableeditor.expression.Constants.MATH_CONTEXT;

/**
 * Domain specific operations
 */
public class DSO {

    public static final Map<String, UnaryOperation> unaryOp = Map.of(
            "+", new UnaryOperation('+',(l) -> l.get(0)),
            "-", new UnaryOperation('-',(l) -> l.get(0).negate()));
    public static final Map<String, MathOperation> mathOp = Map.of(
            "+", new MathOperation('+', 2, (l) -> l.get(0).add(l.get(1), MATH_CONTEXT)),
            "-", new MathOperation('-', 2, (l) -> l.get(0).subtract(l.get(1), MATH_CONTEXT)),
            "*", new MathOperation('*', 3, (l) -> l.get(0).multiply(l.get(1), MATH_CONTEXT)),
            "/", new MathOperation('/', 3, (l) -> l.get(0).divide(l.get(1), MATH_CONTEXT)),
            "%", new MathOperation('%', 3, (l) -> l.get(0).remainder(l.get(1), MATH_CONTEXT)));

    public static final Map<String, CompareOperation> compareOp = Map.of(
            ">", new CompareOperation(">", (l) -> l.get(0).compareTo(l.get(1)) > 0),
            "<", new CompareOperation("<", (l) -> l.get(0).compareTo(l.get(1)) < 0),
            "==", new CompareOperation("==", (l) -> l.get(0).compareTo(l.get(1)) == 0),
            "!=", new CompareOperation("!=", (l) -> l.get(0).compareTo(l.get(1)) != 0),
            ">=", new CompareOperation(">=", (l) -> l.get(0).compareTo(l.get(1)) >= 0),
            "<=", new CompareOperation("<=", (l) -> l.get(0).compareTo(l.get(1)) <= 0));

    public static final Map<String, NamedOperation> namedOp = Map.of(
            "pow", new NamedOperation("pow", List.of(BigDecimal.class, BigDecimal.class), BigDecimal.class, (l) -> ((BigDecimal) l.get(0)).pow(((BigDecimal) l.get(1)).intValue(), MATH_CONTEXT)),
            "sqrt", new NamedOperation("sqrt", List.of(BigDecimal.class), BigDecimal.class, (l) -> ((BigDecimal) l.get(0)).sqrt(MATH_CONTEXT)),
            "abs", new NamedOperation("abs", List.of(BigDecimal.class), BigDecimal.class, (l) -> ((BigDecimal) l.get(0)).abs(MATH_CONTEXT)));
}
