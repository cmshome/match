package com.lxk.match.lucene;

import org.junit.Test;

/**
 * @author LiXuekai on 2020/10/28
 */
public class MultiQueryTest extends LuceneBaseTest {

    @Test
    public void and() {

        String query = "name:l* AND abc:aaa";
        match(map, query);

        query = "name:l AND abc:aaa";
        match(map, query);
    }

    @Test
    public void many() {
        String query = "(name:lxk) AND (abc:aaa) AND (NOT text:a) AND (amount:<2000)";
        match(map, query);
    }

    @Test
    public void many2() {
        String query = "(name:lxk) && (abc:aaa) && (NOT text:a) && (amount:<2000)";
        match(map, query);
    }

    @Test
    public void or() {
        String query = "name:l* abc:aaa";
        match(map, query);

        query = "name:ls name:lx name:lk";
        match(map, query);

        query = "name:ls name:lx name:lk name:lxk";
        match(map, query);

        query = "name:ls name:lx name:lk name:lxk*";
        match(map, query);

        query = "name:l* abc:aaaasd";
        match(map, query);

        query = "name:l* OR abc:aaa";
        match(map, query);

        query = "name:l OR abc:aaa";
        match(map, query);
    }

    @Test
    public void or2() {
        String query = "name:l* || abc:aaa";
        match(map, query);
    }
}
