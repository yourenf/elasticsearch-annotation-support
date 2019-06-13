package org.elasticsearch.extra.query.attribute;

import org.elasticsearch.extra.query.annotation.Highlight;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

public class HighlightAttribute implements SearchSourceBuilderAttribute {

	private Highlight highlight;

	@Override
	public void initialize(Class<?> type) {
		highlight = type.getAnnotation(Highlight.class);
	}

	@Override
	public void accept(SearchSourceBuilder source) {
		if (highlight == null) {
			return;
		}
		HighlightBuilder highlightBuilder = new HighlightBuilder()
				.preTags(highlight.preTags())
				.postTags(highlight.postTags());
		String[] fields = highlight.field();
		for (String field : fields) {
			highlightBuilder.field(field);
		}
		source.highlighter(highlightBuilder);
	}
}
