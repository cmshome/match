package com.lxk.match.lucene;

import org.junit.Before;
import org.junit.Test;

/**
 * 测试字段存在值
 *
 * @author LiXuekai on 2020/10/28
 */
public class ExistQueryTest extends LuceneBaseTest {

    @Before
    public void init() {
        super.init();
    }

    @Test
    public void exist() {
        String query = "_exists_:name";
        match(map, query);

        query = "_exists_:amount";
        match(map, query);

        query = "_exists_:abc";
        match(map, query);

        query = "_exists_:lxk";
        match(map, query);
    }


    @Test
    public void test() {
        String query = "name:lxk";
        match(map, query);

        query = "aaa:lxk";
        match(map, query);

        query = "NOT aaa:lxk";
        match(map, query);

    }


}
