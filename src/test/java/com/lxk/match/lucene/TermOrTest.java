package com.lxk.match.lucene;

import org.junit.Test;

/**
 * @author LiXuekai on 2020/12/15
 */
public class TermOrTest extends LuceneBaseTest {

    /**
     * 通配符 ?匹配单个字符
     */
    @Test
    public void or() {
        String query = "tom:(as l lx lxk abc a b c t?m)";
        match(map, query);
    }

    /**
     * 通配符 *匹配0个或多个字符
     */
    @Test
    public void or2() {
        String query = "tom:as k1:l k2:lx k3:lxk k4:abc tom:a tom:b tom:c tom:t*m";
        match(map, query);
    }

    @Test
    public void or3() {
        String query = "tom:(a b c d to*) k1:(k ka k2)";
        match(map, query);
    }

    /**
     * 匹配句子，需要把整体引号引起来，因为引号是特殊字符，需要转义。
     */
    @Test
    public void or4() {
        String query = "jack:(a b c d \"tom and jack\") k1:(k ka k2)";
        match(map, query);
    }


}
