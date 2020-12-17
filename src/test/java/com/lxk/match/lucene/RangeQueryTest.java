package com.lxk.match.lucene;

import org.junit.Test;

/**
 * @author LiXuekai on 2020/10/28
 */
public class RangeQueryTest extends LuceneBaseTest {

    /**
     * {}尖括号表示不包含最小值和最大值，可以单独使用
     * []方括号表示包含最小值和最大值，可以单独使用
     */
    @Test
    public void range() {

        String query = "amount:{10 TO 100}";
        match(map, query);

        query = "amount:{1000 TO 1000}";
        match(map, query);

        query = "amount:[1000 TO 1001}";
        match(map, query);

        query = "amount:{100 TO 1000]";
        match(map, query);

        query = "amount:[1000 TO 1000]";
        match(map, query);

        query = "amount:>=90";
        match(map, query);

        query = "amount:[10 TO 1001] AND name:lxk";
        match(map, query);


    }
}
