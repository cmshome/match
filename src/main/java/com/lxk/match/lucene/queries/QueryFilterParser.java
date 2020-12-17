package com.lxk.match.lucene.queries;

import com.lxk.match.exceptions.QueryFilterNotSupportedException;
import com.lxk.match.lucene.FilterParser;
import com.lxk.match.lucene.QueryFilter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryFilterParser implements FilterParser {

    private static Pattern EXISTS_PATTERN = Pattern.compile("[^\\\\]\"");
    private static Pattern REPLACE_PATTERN = Pattern.compile("[^\\\\]\"(([^\"])|(\\\\\"))*[^\\\\]\"");
    private static Pattern RANGE_PATTERN = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    private static final String RKEY = "RK";

    /**
     * 通过查询语句，解析为待使用的过滤条件
     * <br>中间使用lucene的原生query解析，因为效率问题，解析为我们使用的query
     *
     * @param queryString
     * @return
     * @throws ParseException
     * @throws QueryFilterNotSupportedException
     */
    @Override
    public QueryFilter parse(String queryString) throws QueryFilterNotSupportedException {
        // 首先检查语句，由于我们不分词，双引号中间的数据作为原始的term，不进行解析
        Map<String, String> replaceValues = Maps.newHashMap();
        queryString = getReplaceQuery(queryString, replaceValues);
        QueryParser contentsParser = new QueryParser("message",
                new WhitespaceAnalyzer());
        contentsParser.setAllowLeadingWildcard(true);
        contentsParser.setLowercaseExpandedTerms(false);
        Query query;
        try {
            query = contentsParser.parse(queryString);
        } catch (ParseException e) {
            throw new QueryFilterNotSupportedException(e);
        }
        QueryFilter qf = parse(query, replaceValues);
        if (!replaceValues.isEmpty()) {
            throw new QueryFilterNotSupportedException();
        }
        return qf;
    }

    private static QueryFilter parse(Query query, Map<String, String> replaceValues)
            throws QueryFilterNotSupportedException {
        if (query instanceof BooleanQuery) {
            BooleanQuery bquery = (BooleanQuery) query;
            List<BooleanClause> clauseList = Lists.newArrayList();
            int shouldTermCount = 0;
            Set<String> fieldList = Sets.newHashSet();
            Set<String> values = Sets.newHashSet();
            for (org.apache.lucene.search.BooleanClause bc : bquery.clauses()) {
                BooleanClause clause = new BooleanClause();
                clause.setOccur(bc.getOccur());
                clause.setQuery(parse(bc.getQuery(), replaceValues));
                clauseList.add(clause);

                //TODO 暂时只对TermQueryFilter并且类型是should的
                if (clause.getQuery() instanceof TermQueryFilter) {
                    TermQueryFilter termQueryFilter = (TermQueryFilter) clause.getQuery();
                    //统计规则中为should的数量
                    if (clause.getOccur() == org.apache.lucene.search.BooleanClause.Occur.SHOULD) {
                            fieldList.add(termQueryFilter.getKey());
                        values.add(termQueryFilter.getTerm().getValue());
                        shouldTermCount++;
                    }
                }
            }

            //当所有规则都为should并且名字都相同的时候，对这类做一些优化
            if (shouldTermCount == clauseList.size() && fieldList.size() == 1) {
                //TODO 目前只对一种特殊情况优化，先满足中信的场景，以后再想方案对所有情况都做优化
                BooleanQueryFilter4Map queryFilter = new BooleanQueryFilter4Map(String.valueOf(fieldList.toArray()[0]), values);
                queryFilter.setClauseList(clauseList);
                return queryFilter;
            } else {
                BooleanQueryFilter queryFilter = new BooleanQueryFilter();
                queryFilter.setClauseList(clauseList);
                return queryFilter;
            }
        } else if (query instanceof TermQuery) {
            TermQuery tquery = (TermQuery) query;

            if (tquery.getTerm().text().startsWith(">") ||
                    tquery.getTerm().text().startsWith("<")) {
                boolean canEqual = tquery.getTerm().text().charAt(1) == '=';
                String text = tquery.getTerm().text().substring(canEqual ? 2 : 1);
                if (RANGE_PATTERN.matcher(text).matches()) {
                    TermRangeQueryFilter queryFilter = new TermRangeQueryFilter();
                    queryFilter.setKey(tquery.getTerm().field());
                    if (tquery.getTerm().text().startsWith(">")) {
                        queryFilter.setLowerTerm(new TermValue(text, replaceValues));
                        queryFilter.setUpperTerm(new TermValue("*", replaceValues));
                        queryFilter.setIncludeLower(canEqual);
                        queryFilter.setIncludeUpper(false);
                    } else {
                        queryFilter.setLowerTerm(new TermValue("*", replaceValues));
                        queryFilter.setUpperTerm(new TermValue(text, replaceValues));
                        queryFilter.setIncludeLower(false);
                        queryFilter.setIncludeUpper(canEqual);
                    }
                    return queryFilter;
                }
            } else if (tquery.getTerm().field().endsWith("_missing_")) {
                MissingQueryFilter missingQF = new MissingQueryFilter();
                missingQF.setKey(tquery.getTerm().text());
                return missingQF;
            } else if (tquery.getTerm().field().endsWith("_exists_")) {
                ExistsQueryFilter existsQF = new ExistsQueryFilter();
                existsQF.setKey(tquery.getTerm().text());
                return existsQF;
            }

            TermQueryFilter queryFilter = new TermQueryFilter();
            queryFilter.setKey(tquery.getTerm().field());
            queryFilter.setTerm(new TermValue(tquery.getTerm().text(), replaceValues));
            return queryFilter;
        } else if (query instanceof WildcardQuery) {
            WildcardQuery wquery = (WildcardQuery) query;
            RegexpQueryFilter queryFilter = new RegexpQueryFilter();
            queryFilter.setKey(wquery.getTerm().field());
            queryFilter.setValue(wquery.getTerm().text().replace("*", ".*").replace("?", ".{1}"));
            return queryFilter;
        } else if (query instanceof PrefixQuery) {
            PrefixQuery pquery = (PrefixQuery) query;
            PrefixQueryFilter queryFilter = new PrefixQueryFilter();
            queryFilter.setKey(pquery.getField());
            queryFilter.setPrefix(pquery.getPrefix().text());
            return queryFilter;
        } else if (query instanceof RegexpQuery) {
            RegexpQuery rquery = (RegexpQuery) query;
            RegexpQueryFilter queryFilter = new RegexpQueryFilter();
            queryFilter.setKey(rquery.getField());
            String text = rquery.toString(rquery.getField());
            queryFilter.setValue(text.substring(text.indexOf("/") + 1, text.lastIndexOf("/")));
            return queryFilter;
        } else if (query instanceof TermRangeQuery) {
            TermRangeQuery rquery = (TermRangeQuery) query;
            TermRangeQueryFilter queryFilter = new TermRangeQueryFilter();
            queryFilter.setKey(rquery.getField());
            queryFilter.setLowerTerm(new TermValue(Term.toString(rquery.getLowerTerm()), replaceValues));
            queryFilter.setUpperTerm(new TermValue(Term.toString(rquery.getUpperTerm()), replaceValues));
            queryFilter.setIncludeLower(rquery.includesLower());
            queryFilter.setIncludeUpper(rquery.includesUpper());
            return queryFilter;
        }
//		else if (query instanceof NumericRangeQuery) {
//			NumericRangeQuery<?> rquery = (NumericRangeQuery<?>)query;
//			TermRangeQueryFilter queryFilter = new TermRangeQueryFilter();
//			queryFilter.setKey(rquery.getField());
//			queryFilter.setLowerTerm(new TermValue(rquery.getMin().toString(), replaceValues));
//			queryFilter.setUpperTerm(new TermValue(rquery.getMax().toString(), replaceValues));
//			queryFilter.setIncludeLower(rquery.includesMin());
//			queryFilter.setIncludeUpper(rquery.includesMax());
//			return queryFilter;
//		}
//		else if (query instanceof PhraseQuery) {
//			PhraseQuery pq = (PhraseQuery)query;
//			if (pq.getTerms() != null && pq.getTerms().length > 0) {
//				String field = pq.getTerms()[0].field();
//				String s = pq.toString(field);
//				if (s.startsWith("\"") && s.endsWith("\"")) {
//					TermQueryFilter queryFilter = new TermQueryFilter();
//					queryFilter.setKey(pq.getTerms()[0].field());
//					queryFilter.setTerm(new TermValue(s));
//					return queryFilter;
//				}
//			}
//			throw new QueryFilterNotSupportedException("Not supported:" + query.getClass());
//		}
        else {
            // 不分词，不支持
            throw new QueryFilterNotSupportedException("Not supported:" + query.getClass());
        }
    }

    private static int getReplaceKey(String query, String key, int index) {
        String rKey;
        do {
            rKey = key + ++index;
        }
        while (query.indexOf(rKey) != -1);
        return index;
    }

    private static String getReplaceQuery(String queryString,
                                          Map<String, String> replaceValues) {
        if (EXISTS_PATTERN.matcher(queryString).find()) {
            // 生成替换符
            int rIndex = 0, sIndex = 0;
            Matcher mr = REPLACE_PATTERN.matcher(queryString);
            StringBuffer newQueryStr = new StringBuffer();
            while (sIndex < queryString.length() && mr.find(sIndex)) {
                int start = mr.start() + 1;
                int end = mr.end();
                rIndex = getReplaceKey(queryString, RKEY, rIndex);
                replaceValues.put(RKEY + rIndex, queryString.substring(start + 1, end - 1));
                newQueryStr.append(queryString.substring(sIndex, start)).append(RKEY + rIndex);
                sIndex = end;
            }
            newQueryStr.append(queryString.substring(sIndex));
            queryString = newQueryStr.toString();
//			System.err.println("-----" + queryString + "--" + replaceValues);
        }
        return queryString;
    }
}
