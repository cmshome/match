package com.lxk.match.lucene.queries;


import com.lxk.match.lucene.QueryFilter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 目前只处理所有都为should并且所有字段名字都相同的情况，其他的场景后续再想方案优化，如果有包含非should的规则，都当做should处理
 */
public class BooleanQueryFilter4Map extends QueryFilter implements Serializable {
    private String field;
    private QueryProperty queryProperty;
    private List<BooleanClause> clauseList;
    private Set<String> values;

    public BooleanQueryFilter4Map(String field, Set<String> values) {
        this.field = field;
        this.values = values;
        this.queryProperty = new QueryProperty();
        this.queryProperty.setKey(this.field);
    }

    public List<BooleanClause> getClauseList() {
        return clauseList;
    }

    public void setClauseList(List<BooleanClause> clauseList) {
        this.clauseList = clauseList;
    }


    @Override
    protected boolean matchQuery(Object obj) {
        if (values == null) {
            return true;
        }
        Object fieldValue;
        try {
            fieldValue = this.queryProperty.getValue(obj);
        } catch (Exception e) {
            return false;
        }
        if (fieldValue == null) {
            return false;
        }
        String stringValue;
        if (fieldValue instanceof String) {
            stringValue = String.valueOf(fieldValue);
        } else {
            stringValue = fieldValue + "";
        }
        return values.contains(stringValue);

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
                if (subQuery instanceof BooleanQueryFilter4Map) {
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

        return matchQuery(obj);
    }
}
