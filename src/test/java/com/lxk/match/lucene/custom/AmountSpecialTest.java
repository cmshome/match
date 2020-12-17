package com.lxk.match.lucene.custom;

import com.lxk.match.lucene.LuceneBaseTest;
import com.lxk.match.lucene.QueryFilter;
import com.lxk.match.lucene.queries.custom.AmountSpecialQueryFilter;
import org.junit.Before;
import org.junit.Test;

/**
 * @author LiXuekai on 2020/11/10
 */
public class AmountSpecialTest extends LuceneBaseTest {

    @Before
    public void init() {
        super.init();
    }


    @Test
    public void test() {
        String query = "amount:true";
        QueryFilter filter = new AmountSpecialQueryFilter(query);
        boolean match = filter.match(map);
        System.out.println(match);

        query = "amount1:true";
        filter = new AmountSpecialQueryFilter(query);
        match = filter.match(map);
        System.out.println(match);

        query = "amountString:true";
        filter = new AmountSpecialQueryFilter(query);
        match = filter.match(map);
        System.out.println(match);

        query = "amountString2:true";
        filter = new AmountSpecialQueryFilter(query);
        match = filter.match(map);
        System.out.println(match);

        query = "amountString2:false";
        filter = new AmountSpecialQueryFilter(query);
        match = filter.match(map);
        System.out.println(match);
    }
}
