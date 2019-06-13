package org.elasticsearch.extra.query.support;

import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.query.BoolQueryAttributeContext;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;

import java.util.Optional;
import java.util.function.Supplier;

public interface AnnotationSupport extends Supplier<Optional<BoolQueryHandler>> {

	void initialize(Property property, BoolQueryAttributeContext context);

	default void setPath(String path) {

	}

	Optional<BoolQueryHandler> get();
}