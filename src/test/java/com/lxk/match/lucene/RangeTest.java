package com.lxk.match.lucene;

import com.lxk.match.factory.QueryFilterParserFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author LiXuekai on 2020/9/29
 */
public class RangeTest {

    private final List<String> list1 = Lists.newArrayList();
    private final List<Integer> list2 = Lists.newArrayList();

    @Before
    public void init() {
        // 不等于
        list1.add("NOT amount:90");
        // or
        list1.add("amount:90 amount:10 amount:100");
        // 大于等于
        list1.add("amount:>=90");
        //
        list1.add("amount:{10 TO 100}");
        list1.add("amount:{10 TO 100]");
        // and
        list1.add("amount:[10 TO 100] AND name:lxk");

        list2.add(100);
        list2.add(10);
        list2.add(90);
    }

    /**
     * [] 包含双边
     * {} 不包含双边
     */
    @Test
    public void test() {
        Map<String, Object> additionalData = Maps.newHashMap();
        list2.forEach(amount -> {
            additionalData.put("amount", amount);
            additionalData.put("name", "lxk");

            list1.forEach(s -> {
                QueryFilter parse;
                try {
                    parse = QueryFilterParserFactory.parse(s);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                boolean match = parse.match(additionalData);
                System.out.println("map is " + additionalData.toString() + " 判断条件："+ s + " 结果：" + match);
            });
            System.out.println();
        });


    }
}
