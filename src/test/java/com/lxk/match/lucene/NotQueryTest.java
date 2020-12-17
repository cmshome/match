package com.lxk.match.lucene;

import org.junit.Test;

/**
 * @author LiXuekai on 2020/10/28
 */
public class NotQueryTest extends LuceneBaseTest {

    @Test
    public void not() {
        String query = "NOT name:lxk";
        match(map, query);

        query = "NOT (name:lxk AND abc:aaa)";
        match(map, query);

        query = "NOT (name:a AND abc:s)";
        match(map, query);

        query = "NOT (name:a AND abc:aaa)";
        match(map, query);

        query = "NOT name:a AND abc:s";
        match(map, query);

        query = "NOT name:a AND abc:aaa";
        match(map, query);

        query = "NOT name:lxk NOT abc:aaa";
        match(map, query);
    }

    @Test
    public void filter1() {
        // 这个字段只有在配置了是否响应的预处理之后才会有的
        map.put("is_responsed", 1);
        map.put("ret_code_probse_st", "noresponse");

        String query = "((_exists_:ret_code_probe_st) AND (NOT ret_code_probe_st:noresponse)) OR (is_responsed:1)";
        match(map, query);
    }

    @Test
    public void filter2() {
        String query = "NOT bu_cun_zai_de_key:noresponse";
        match(map, query);
    }

}
