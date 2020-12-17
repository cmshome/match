package com.lxk.match.lucene;

import com.lxk.match.factory.QueryFilterParserFactory;
import com.lxk.match.util.JsonUtils;
import com.google.common.collect.Maps;
import org.junit.Before;

import java.util.Map;

/**
 * @author LiXuekai on 2020/10/28
 */
public class LuceneBaseTest {
    protected final Map<String, Object> map = Maps.newLinkedHashMap();

    @Before
    public void init() {
        map.put("amount", 1000);
        map.put("amount1", 1000.0);
        map.put("amountString", "1000.00");
        map.put("amountString2", "1000.00123");
        map.put("name", "lxk");
        map.put("text", "lxk");
        map.put("regex", "abcsdas+-*lxkasdh.,/");
        map.put("abc", "aaa");
        map.put("k1", "k1");
        map.put("k2", "k2");
        map.put("tom", "tom");
        map.put("jack", "tom and jack");

    }

    protected void match(Map<String, Object> map, String query) {
        System.out.println("map is \r\n" + JsonUtils.parseObjToFormatJson(map));

        QueryFilter parse;
        try {
            parse = QueryFilterParserFactory.parse(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        boolean match = parse.match(map);
        System.out.println("lucene 表达式是：" + query + "  匹配结果是：" + match);
    }
}
