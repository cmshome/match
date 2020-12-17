## lucene规则匹配底层jar包实现 

### 使用的map测试数据  
```
{
	"amount":1000,
	"amount1":1000.0,
	"amountString":"1000.00",
	"amountString2":"1000.00123",
	"name":"lxk",
	"text":"lxk",
	"regex":"abcsdas+-*lxkasdh.,/",
	"abc":"aaa",
	"k1":"k1",
	"k2":"k2",
	"tom":"tom",
	"is_responsed":1,
	"ret_code_probse_st":"noresponse",
    "jack":"tom and jack"
}
```

* 存在  
```
lucene 表达式是：_exists_:name  匹配结果是：true
lucene 表达式是：_exists_:amount  匹配结果是：true
lucene 表达式是：_exists_:abc  匹配结果是：true
lucene 表达式是：_exists_:lxk  匹配结果是：false
```
* 不存在
```
lucene 表达式是：_missing_:tom  匹配结果是：false
lucene 表达式是：_missing_:abc  匹配结果是：false
lucene 表达式是：_missing_:kafka  匹配结果是：true
```
* term
```
lucene 表达式是：tom:(as l lx lxk abc a b c tom)  匹配结果是：true
lucene 表达式是：tom:(as l lx lxk abc a b c t?m)  匹配结果是：true
lucene 表达式是：tom:as k1:l k2:lx k3:lxk k4:abc tom:a tom:b tom:c tom:tom  匹配结果是：true
lucene 表达式是：tom:as k1:l k2:lx k3:lxk k4:abc tom:a tom:b tom:c tom:t*m  匹配结果是：true
lucene 表达式是：tom:(a b c d to*) k1:(k ka k2)  匹配结果是：true
lucene 表达式是：jack:(a b c d \"tom and jack\") k1:(k ka k2)  匹配结果是：true
```
* 前缀
```
lucene 表达式是：name:l*  匹配结果是：true
lucene 表达式是：name:ABCl*  匹配结果是：false
lucene 表达式是：name:lxk*  匹配结果是：true
lucene 表达式是：(name:lx* AND (text:lxks OR abc:aaa)) OR (k1:k* AND k2:k2) NOT (tom:lxk)  匹配结果是：true
```
* NOT
```
lucene 表达式是：NOT name:lxk  匹配结果是：false
lucene 表达式是：NOT (name:lxk AND abc:aaa)  匹配结果是：false
lucene 表达式是：NOT (name:a AND abc:s)  匹配结果是：true
lucene 表达式是：NOT (name:a AND abc:aaa)  匹配结果是：true
lucene 表达式是：NOT name:a AND abc:s  匹配结果是：false
lucene 表达式是：NOT name:a AND abc:aaa  匹配结果是：true
lucene 表达式是：NOT name:lxk NOT abc:aaa  匹配结果是：false
lucene 表达式是：((_exists_:ret_code_probe_st) AND (NOT ret_code_probe_st:noresponse)) OR (is_responsed:1)  匹配结果是：true
lucene 表达式是：NOT bu_cun_zai_de_key:noresponse  匹配结果是：true
```
* 范围
```
lucene 表达式是：amount:{10 TO 100}  匹配结果是：false
lucene 表达式是：amount:{1000 TO 1000}  匹配结果是：false
lucene 表达式是：amount:[1000 TO 1001}  匹配结果是：true
lucene 表达式是：amount:{100 TO 1000]  匹配结果是：true
lucene 表达式是：amount:[1000 TO 1000]  匹配结果是：true
lucene 表达式是：amount:>=90  匹配结果是：true
lucene 表达式是：amount:[10 TO 1001] AND name:lxk  匹配结果是：true
```
* regex
```
lucene 表达式是：amountString:/(.*)\.00/  匹配结果是：true
lucene 表达式是：regex:/(.*)lxk(.*)/  匹配结果是：true
lucene 表达式是：amountString:/(.*)\.00/  匹配结果是：true
```
* 多个条件匹配
```
lucene 表达式是：name:l* AND abc:aaa  匹配结果是：true
lucene 表达式是：name:l AND abc:aaa  匹配结果是：false
lucene 表达式是：(name:lxk) AND (abc:aaa) AND (NOT text:a) AND (amount:<2000)  匹配结果是：true
lucene 表达式是：(name:lxk) && (abc:aaa) && (NOT text:a) && (amount:<2000)  匹配结果是：true
lucene 表达式是：name:l* abc:aaa  匹配结果是：true
lucene 表达式是：name:ls name:lx name:lk  匹配结果是：false
lucene 表达式是：name:ls name:lx name:lk name:lxk  匹配结果是：true
lucene 表达式是：name:ls name:lx name:lk name:lxk*  匹配结果是：true
lucene 表达式是：name:l* abc:aaaasd  匹配结果是：true
lucene 表达式是：name:l* OR abc:aaa  匹配结果是：true
lucene 表达式是：name:l OR abc:aaa  匹配结果是：true
lucene 表达式是：name:l* || abc:aaa  匹配结果是：true
```
* 特殊字符 需要转义
```
由于Lucene中支持很多的符号，如
+ - && || ! ( ) { } [ ] ^ " ~ * ? : \
因此如果需要搜索 (1+1):2 需要对改串进行转换，使用字符\。
\(1\+1\)\:2
```