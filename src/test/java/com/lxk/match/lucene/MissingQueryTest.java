package com.lxk.match.lucene;

import org.junit.Test;

/**
 * @author LiXuekai on 2020/10/28
 */
public class MissingQueryTest extends LuceneBaseTest {

    @Test
    public void missing() {
        //map.put("tom", "aaa");
        String query = "_missing_:tom";
        match(map, query);

        query = "_missing_:abc";
        match(map, query);

        query = "_missing_:kafka";
        match(map, query);
    }
}
