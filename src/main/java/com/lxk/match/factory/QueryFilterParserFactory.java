package com.lxk.match.factory;


import com.lxk.match.lucene.FilterParser;
import com.lxk.match.lucene.QueryFilter;
import com.lxk.match.lucene.queries.QueryFilterParser;

public class QueryFilterParserFactory {

    private static FilterParser parser;

    public static QueryFilter parse(String queryString) throws Exception {
        initParser();
        return parser.parse(queryString);
    }

    private static void initParser() {
        if (parser == null) {
            synchronized (QueryFilterParserFactory.class) {
                if (parser == null) {
                    parser = new QueryFilterParser();
                }
            }
        }
    }
}
