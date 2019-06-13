package org.elasticsearch.extra.query.bool;

import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class MatchPhraseQuery implements StringQuery {

	@Override
	public boolean accept(BoolQueryBuilder boolQueryBuilder, AttributeContext context, String value) {
		MatchPhraseQueryBuilder phraseQuery = QueryBuilders.matchPhraseQuery(context.completeField(), value)
				.boost(context.boost());
		boolQueryBuilder.must(phraseQuery);
		return true;
	}
}
