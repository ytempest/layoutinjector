package com.ytempest.layoutinjector.compiler;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * @author heqidu
 * @since 2020/7/3
 */
class RScanner extends TreeScanner {
    private final Map<Integer, Id> resourceIds = new LinkedHashMap<>();

    @Override
    public void visitIdent(JCTree.JCIdent jcIdent) {
        super.visitIdent(jcIdent);
        Symbol symbol = jcIdent.sym;
        if (symbol.type instanceof Type.JCPrimitiveType) {
            Id id = parseId(symbol);
            if (id != null) {
                resourceIds.put(id.getValue(), id);
            }
        }
    }

    @Override
    public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
        Symbol symbol = jcFieldAccess.sym;
        Id id = parseId(symbol);
        if (id != null) {
            resourceIds.put(id.getValue(), id);
        }
    }

    @Override
    public void visitLiteral(JCTree.JCLiteral jcLiteral) {
        try {
            int value = (Integer) jcLiteral.value;
            resourceIds.put(value, new Id(value));
        } catch (Exception ignored) {
        }
    }

    void reset() {
        resourceIds.clear();
    }

    @Nullable
    private Id parseId(Symbol symbol) {
        Id id = null;
        if (symbol.getEnclosingElement() != null
                && symbol.getEnclosingElement().getEnclosingElement() != null
                && symbol.getEnclosingElement().getEnclosingElement().enclClass() != null) {
            try {
                Object constantValue = ((Symbol.VarSymbol) symbol).getConstantValue();
                int value = (Integer) requireNonNull(constantValue);
                id = new Id(value, symbol);
            } catch (Exception ignored) {
            }
        }
        return id;
    }

    @Nullable
    Id getId() {
        if (resourceIds.isEmpty()) {
            return null;
        }
        return resourceIds.values().iterator().next();
    }

}
