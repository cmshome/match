package com.lxk.match.lucene;

import org.junit.Before;
import org.junit.Test;

/**
 * @author LiXuekai on 2020/11/5
 */
public class RegexQueryTest extends LuceneBaseTest {

    @Before
    public void init() {
        super.init();
    }

    @Test
    public void regex() {
        // 期望是以 .00 结尾的
        String query = "amountString:/(.*)\\.00/";
        match(map, query);

        // 包含字符 lxk 就行
        query = "regex:/(.*)lxk(.*)/";
        match(map, query);
    }

    @Test
    public void amount() {
        String query = "amountString:/(.*)\\.00/";
        match(map, query);
    }
}
