package org.elasticsearch.extra.query.plugin.strategy;

public class EndWithStrategy implements Strategy {

	private String rule;

	@Override
	public void setRule(String strategy) {
		this.rule = strategy.trim();
	}

	@Override
	public boolean match(String value) {
		return value.endsWith(rule);
	}

	@Override
	public String cleanValue(String value) {
		int i = value.length() - rule.length();
		return value.substring(0, i);
	}

}