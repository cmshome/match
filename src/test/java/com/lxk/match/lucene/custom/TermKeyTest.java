package com.lxk.match.lucene.custom;

import com.lxk.match.lucene.LuceneBaseTest;
import com.lxk.match.lucene.QueryFilter;
import com.lxk.match.lucene.queries.custom.TermKeyQueryFilter;
import org.junit.Before;
import org.junit.Test;

/**
 * @author LiXuekai on 2020/11/10
 */
public class TermKeyTest extends LuceneBaseTest {

    @Before
    public void init() {
        super.init();
    }


    @Test
    public void test() {
        String query = "name!=text";
        QueryFilter filter = new TermKeyQueryFilter(query);
        boolean match = filter.match(map);
        System.out.println(match);
    }
}
