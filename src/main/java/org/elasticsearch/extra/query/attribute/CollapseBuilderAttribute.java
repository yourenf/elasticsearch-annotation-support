package org.elasticsearch.extra.query.attribute;

import org.elasticsearch.extra.query.annotation.Collapse;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;

public class CollapseBuilderAttribute implements SearchSourceBuilderAttribute {

	private Collapse collapse;

	@Override
	public void initialize(Class<?> type) {
		collapse = type.getAnnotation(Collapse.class);
	}

	@Override
	public void accept(SearchSourceBuilder source) {
		if (collapse == null) {
			return;
		}
		CollapseBuilder collapseBuilder = new CollapseBuilder(collapse.field());
		InnerHitBuilder innerHits = new InnerHitBuilder(collapse.innerHitsName());
		innerHits.setFrom(collapse.innerHitsForm())
				.setSize(collapse.innerHitSize());
		collapseBuilder.setInnerHits(innerHits);
		source.collapse(collapseBuilder);
	}
}
