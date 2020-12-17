package com.lxk.match.lucene;

import org.junit.Test;

/**
 * @author LiXuekai on 2020/10/28
 */
public class PrefixQueryTest extends LuceneBaseTest {


    @Test
    public void prefix() {
        String query = "name:l*";
        match(map, query);

        query = "name:ABCl*";
        match(map, query);

        query = "name:lxk*";
        match(map, query);

        // a or b
        query = "(name:lx* AND (text:lxks OR abc:aaa)) OR (k1:k* AND k2:k2) NOT (tom:lxk)";
        match(map, query);
    }

}
