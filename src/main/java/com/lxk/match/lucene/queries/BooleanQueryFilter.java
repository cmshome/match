package com.lxk.match.lucene.queries;

import com.lxk.match.lucene.QueryFilter;
import org.apache.lucene.search.BooleanClause.Occur;

import java.util.List;

/**
 */
public class BooleanQueryFilter extends QueryFilter {
	private List<BooleanClause> clauseList;

	public List<BooleanClause> getClauseList() {
		return clauseList;
	}

	public void setClauseList(List<BooleanClause> clauseList) {
		this.clauseList = clauseList;
	}

	@Override
	protected boolean matchQuery(Object obj) {
		if (clauseList.isEmpty()) {
            return true;
        }
		boolean result = clauseList.get(0).getQuery().match(obj);
		if (clauseList.get(0).getOccur() == Occur.MUST_NOT) {
            result = !result;
        }
		int size = clauseList.size();
		for (int i = 1; i < size; i++) {
			BooleanClause bc = clauseList.get(i);
			switch (bc.getOccur()) {
				case MUST:
					// 如果本来就是非，无论非与上什么都是非，所以就不用计算了
					if (result) {
						result &= bc.getQuery().match(obj);
					}
					break;
				case SHOULD:
					// 如果本来就是是，无论是或上什么都是是，所以就不用计算了
					if (!result) {
						result |= bc.getQuery().match(obj);
					}
					break;
				case MUST_NOT:
					// 如果本来就是非，无论非与上什么都是非，所以就不用计算了
					if (result) {
						result &= !bc.getQuery().match(obj);
					}
					break;
				default:
					break;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		int size = clauseList.size();
		for (int i = 0; i < size; i++) {
			BooleanClause c = clauseList.get(i);
			if (c.isProhibited()) {
				buffer.append("-");
			} else if (c.isRequired()) {
				buffer.append("+");
			}

			QueryFilter subQuery = c.getQuery();
			if (subQuery != null) {
				if (subQuery instanceof BooleanQueryFilter) {
					buffer.append("(");
					buffer.append(subQuery.toString());
					buffer.append(")");
				} else {
					buffer.append(subQuery.toString());
				}
			} else {
				buffer.append("null");
			}

			if (i != size - 1) {
				buffer.append(" ");
			}
		}

		return buffer.toString();
	}

	@Override
	protected boolean findQuery(Object obj) {
		if (clauseList.isEmpty()) {
            return true;
        }
		boolean result = clauseList.get(0).getQuery().find(obj);
		if (clauseList.get(0).getOccur() == Occur.MUST_NOT) {
            result = !result;
        }
		int size = clauseList.size();
		for (int i = 1; i < size; i++) {
			BooleanClause bc = clauseList.get(i);
			switch (bc.getOccur()) {
				case MUST:
					// 如果本来就是非，无论非与上什么都是非，所以就不用计算了
					if (result) {
						result &= bc.getQuery().find(obj);
					}
					break;
				case SHOULD:
					// 如果本来就是是，无论是或上什么都是是，所以就不用计算了
					if (!result) {
						result |= bc.getQuery().find(obj);
					}
					break;
				case MUST_NOT:
					// 如果本来就是非，无论非与上什么都是非，所以就不用计算了
					if (result) {
						result &= !bc.getQuery().find(obj);
					}
					break;
				default:
					break;
			}
		}
		return result;
	}
}
