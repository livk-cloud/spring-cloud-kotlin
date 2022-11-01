package com.livk.spring;

import com.livk.util.StreamUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Iterator;

/**
 * <p>
 * AbstractImportSelector
 * </p>
 *
 * @author livk
 * @date 2022/9/20
 */
public abstract class AbstractImportSelector<T> implements DeferredImportSelector, Ordered {

    @SuppressWarnings("unchecked")
    private final Class<T> annotationClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractImportSelector.class);

    @NotNull
    @Override
    public String[] selectImports(@Nullable AnnotationMetadata importingClassMetadata) {
        if (!isEnabled()) {
            return new String[0];
        }
        Assert.notNull(annotationClass, "annotation Class not be null");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Iterator<String> iterator = ImportCandidates.load(annotationClass, classLoader).iterator();
        return StreamUtils.of(iterator).toArray(String[]::new);
    }

    protected Class<T> getAnnotationClass() {
        return this.annotationClass;
    }

    protected boolean isEnabled() {
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
