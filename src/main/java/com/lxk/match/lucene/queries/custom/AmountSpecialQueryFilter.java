package com.lxk.match.lucene.queries.custom;

import com.lxk.match.lucene.QueryFilter;
import com.google.common.base.Strings;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * @author LiXuekai on 2020/11/10
 */
public class AmountSpecialQueryFilter extends QueryFilter {
    private static final String RELATION_1 = ":";
    private static final int TWO = 2;

    private String query;

    public AmountSpecialQueryFilter() {
    }

    public AmountSpecialQueryFilter(String query) {
        this.query = query;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean matchQuery(Object obj) {
        if (Strings.isNullOrEmpty(query)) {
            return false;
        }
        Map<String, Object> map = (Map<String, Object>) obj;
        String[] split = query.split(RELATION_1);
        if (split.length == TWO) {
            try {
                boolean isInteger = amountIsInteger(split, map);
                boolean b = Boolean.parseBoolean(split[1]);
                return b == isInteger;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean amountIsInteger(String[] split, Map<String, Object> map) {
        String amountMetric = String.valueOf(split[0]);
        if (amountMetric == null) {
            return false;
        }

        Object amount = map.get(amountMetric);
        if (amount == null) {
            return false;
        }
        double parseDouble = Double.parseDouble(amount.toString());
        DecimalFormat format = new DecimalFormat("0.00");
        String formatString = format.format(parseDouble);
        String toString = amount.toString();
        boolean b = formatString.endsWith(".00");
        if (!b) {
            return false;
        }
        if (toString.length() > formatString.length()) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean findQuery(Object obj) {
        return matchQuery(obj);
    }
}
