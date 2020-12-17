package com.lxk.match.lucene.queries;


/**
 */
public class TermRangeQueryFilter extends KeyQueryFilter {

	private TermValue lowerTerm;
	private TermValue upperTerm;
	private boolean includeLower;
	private boolean includeUpper;

	@Override
	protected boolean matchQuery(Object obj) {
		try {
			if (obj == null) {
				return false;
			}
			Object object = this.getValue(obj);
			int a = "*".equals(lowerTerm.getValue()) ? -1 : lowerTerm.compareTo(object);
			if (a > 0 || (a == 0 && !includeLower)) {
				return false;
			}
			int b = "*".equals(upperTerm.getValue()) ? 1 : upperTerm.compareTo(object);
			return (b > 0 || (b == 0 && includeUpper));
		} catch (Exception e) {
			return false;
		}
	}

	public String getKey() {
		return this.queryProperty.getKey();
	}

	public void setKey(String key) {
		this.queryProperty.setKey(key);
	}

	public TermValue getLowerTerm() {
		return lowerTerm;
	}

	public void setLowerTerm(TermValue lowerTerm) {
		this.lowerTerm = lowerTerm;
	}

	public TermValue getUpperTerm() {
		return upperTerm;
	}

	public void setUpperTerm(TermValue upperTerm) {
		this.upperTerm = upperTerm;
	}

	public boolean isIncludeLower() {
		return includeLower;
	}

	public void setIncludeLower(boolean includeLower) {
		this.includeLower = includeLower;
	}

	public boolean isIncludeUpper() {
		return includeUpper;
	}

	public void setIncludeUpper(boolean includeUpper) {
		this.includeUpper = includeUpper;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(getKey());
		buffer.append(":");
		buffer.append(includeLower ? '[' : '{');
		buffer.append(lowerTerm != null ? ("*".equals(lowerTerm.getValue()) ? "\\*"
				: lowerTerm.getValue())
				: "*");
		buffer.append(" TO ");
		buffer.append(upperTerm != null ? ("*".equals(upperTerm.getValue()) ? "\\*"
				: upperTerm.getValue())
				: "*");
		buffer.append(includeUpper ? ']' : '}');
		return buffer.toString();
	}

	@Override
	protected boolean findQuery(Object obj) {
		return matchQuery(obj);
	}
}
