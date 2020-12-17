package com.lxk.match.lucene.queries.custom;

import com.lxk.match.lucene.QueryFilter;
import com.google.common.base.Strings;

import java.util.Map;
import java.util.Objects;

/**
 * key1的值等于key2的值。。。
 *
 * @author LiXuekai on 2020/11/10
 */
public class TermKeyQueryFilter extends QueryFilter {
    private static final String RELATION_1 = "==";
    private static final String RELATION_2 = "!=";
    private static final int TWO = 2;

    private String query;

    public TermKeyQueryFilter() {
    }

    public TermKeyQueryFilter(String query) {
        this.query = query;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean matchQuery(Object obj) {
        if (Strings.isNullOrEmpty(query)) {
            return false;
        }
        String[] split1 = query.split(RELATION_1);
        String[] split2 = query.split(RELATION_2);
        Map<String, Object> map = (Map<String, Object>) obj;
        if (split1.length == TWO) {
            Object value0 = map.get(split1[0]);
            Object value1 = map.get(split1[1]);
            if (value0 == null && value1 == null) {
                return false;
            }
            return Objects.equals(value0, value1);
        }

        if (split2.length == TWO) {
            Object value0 = map.get(split2[0]);
            Object value1 = map.get(split2[1]);
            if (value0 == null && value1 == null) {
                return false;
            }
            return !Objects.equals(value0, value1);
        }

        return false;
    }

    @Override
    protected boolean findQuery(Object obj) {
        return matchQuery(obj);
    }
}
