package com.example.demo.service.neo4j.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Neo4jCypherConstructor implements Neo4jCypher {
	
	private static Logger logger = LoggerFactory.getLogger(Neo4jCypherConstructor.class);
	private int skip;
	private int limit;

    public Neo4jCypherConstructor(){}
	public Neo4jCypherConstructor(int skip, int limit){
	    this.skip = skip;
	    this.limit = limit;
    }

    public String cypherTemplate(int templateNum, Map<String, Object> args, String ...labels){
        String cypher = "";
        if(templateNum == 0){
            cypher = template0(args, labels[0]);
        }else if(templateNum == 1){
            cypher = template1(args, labels);
        }else if(templateNum == 2){
            cypher = template2(args, labels);
        }
        return cypher;
    }

    /**
     * 支持labels个数 1
     * @param args
     * @param label
     * @return
     */
    private String template0(Map<String, Object> args, String label){
        String match = doMatch(label);
        String where = doWhere(args);
        String ret = doReturn(label, null);
        return match + where + ret + " skip " + skip + " limit " + limit;
    }

    /**
     * 支持labels个数 [2,3]
     * @param args
     * @param labels
     * @return
     */
    private String template1(Map<String, Object> args, String ...labels){
        String cypher = "";
        String ret = "";
        if(labels.length==2){
            String match  = String.format(" (%s:%s)-[r]-(%s:%s) ", labels[0].toLowerCase(), labels[0],
                    labels[1].toLowerCase(), labels[1]);
            cypher = "MATCH" + match;
            ret = doReturn(labels[0], new ArrayList(){{add(labels[0].toLowerCase());add("r");add(labels[1].toLowerCase());}});
        }else if(labels.length==3){
            String match  = String.format(" (%s:%s)-[%s:%s]-(%s:%s) ", labels[0].toLowerCase(), labels[0], labels[1].toLowerCase(),
                    labels[1], labels[2].toLowerCase(), labels[2]);
            cypher = "MATCH" + match;
            ret = doReturn(labels[0], new ArrayList(){{add(labels[0].toLowerCase());add(labels[1].toLowerCase());
                add(labels[2].toLowerCase());}});
        }
        String where = doWhere(args);
        return cypher + where + ret + " skip " + skip + " limit " + limit;
    }

    /**
     * 支持labels个数 [3,5]
     * @param args
     * @param labels
     * @return
     */
    private String template2(Map<String, Object> args, String ...labels){
        String cypher = "";
        String ret = "";
        List<String> lowerCaseLabels = Arrays.asList(labels).stream().map(lable -> lable.toLowerCase()).collect(Collectors.toList());
        lowerCaseLabels.add("r1");
        lowerCaseLabels.add("r2");
        if(labels.length==3){
            String match  = String.format(" (%s:%s)-[r1]-(%s:%s)-[r2]-(%s:%s) ", labels[0].toLowerCase(), labels[0],
                    labels[1].toLowerCase(), labels[1], labels[2].toLowerCase(), labels[2]);
            cypher = "MATCH" + match;
            ret = doReturn(labels[0], lowerCaseLabels);
        }
        String where = doWhere(args);
        return cypher + where + ret + " skip " + skip + " limit " + limit;
    }

	public String doCount(String startLabel, String midLabel, String endLabel, Map<String, Object> args) {
		String match = doMatch(startLabel);
		String optionalMatch = doOptionalMatch(startLabel, midLabel, endLabel);
		String where = doWhere(args);
		List<String> list = new ArrayList<>();
		if (StringUtils.isNotBlank(endLabel)) {
			list.add("r1");
			list.add(endLabel.toLowerCase());
		}
		if (StringUtils.isNotBlank(midLabel)) {
			list.add("r2");
			list.add(midLabel.toLowerCase());
		}
		return match + optionalMatch + where + "RETURN count(*) as total";
	}

	public String makeCypher(String startLabel, String midLabel, String endLabel, Map<String, Object> args, int skip,
			int limit) {
		String match = doMatch(startLabel);
		String optionalMatch = doOptionalMatch(startLabel, midLabel, endLabel);
		String where = doWhere(args);
		List<String> list = new ArrayList<>();
		if (StringUtils.isNotBlank(endLabel)) {
			list.add("r1");
			list.add(endLabel.toLowerCase());
		}
		if (StringUtils.isNotBlank(midLabel)) {
			list.add("r2");
			list.add(midLabel.toLowerCase());
		}
		String ret = doReturn(startLabel, list);
		return match + optionalMatch + where + ret + " skip " + skip + " limit " + limit;
	}

	/**
	 * 根据前端传入的条件，拼接Cypher检索语句
	 * 
	 * @param label
	 *            检索实体类型
	 * @param args
	 *            条件数组
	 * @param formula
	 *            条件数组表达式
	 * @param skip
	 *            分页开始位置
	 * @param limit
	 *            分页大小
	 * @return cypherSQL
	 */
	public String makeCypher(String label, List<Map<String, Object>> args, String formula, int skip, int limit) {
		String match = this.doMatch(label);
		String optionMatch = this.doOptionalMatch(label, args);
		String where = this.doWhere(label, args, formula);
		List<String> list = new ArrayList<String>();
		
		list.add(label.toLowerCase());
		
		for (Map<String, Object> map : args) {
			if (map.containsKey("label")) {
				if (label.equals(map.get("label")) || list.contains(map.get("label").toString().toLowerCase())) {
					continue;
				} else {
					list.add(map.get("label").toString().toLowerCase());
				}
			}
		}
		String _return = this.doReturn(label.toLowerCase(), list);
		String page = this.doPage(skip, limit);
		return match + optionMatch + where + _return + page;
	}

	public String doMatch(String label) {
		String match = "MATCH (%s:%s)";
		return String.format(match, label.toLowerCase(), label);
	}

	/**
	 * 数据表格cypherSQL语句拼接
	 * 
	 * @param label
	 *            实体类型
	 * @param args
	 *            条件数组
	 * @return cypherSql
	 */
	public String doOptionalMatch(String label, List<Map<String, Object>> args) {
		if (args == null || args.size() <= 0)
			return "";
		List<String> labels = new ArrayList<String>();
		String sql = "";
		for (Map<String, Object> map : args) {
			String type = map.containsKey("type") ? (String) map.get("type").toString().toLowerCase() : "";
			String _label = map.containsKey("label") ? (String) map.get("label").toString() : "";
			String cypher = "";
			if (!labels.contains(_label)) {
				if (StringUtils.isBlank(_label) || label.toLowerCase().equals(_label)) {
					// 此种情况说明条件中的实体类型和检索的开始节点的类型一致
					continue;
				} else {
					if ("rel".equals(type)) {// 关系
						cypher = " MATCH (%s) - [%s:%s] - () ";
					} else {// 该字段默认标记为节点(Node)
						cypher = " MATCH (%s) - [] - (%s:%s) ";
					}
				}
				sql += String.format(cypher, label.toLowerCase(), _label.toLowerCase(), _label);
				labels.add(_label);
			}
		}
		return sql;
	}

	/**
	 * 数据表格cypherSql语句条件拼接
	 * 
	 * @param label
	 *            实体类型
	 * @param args
	 *            条件数组
	 * @return 条件字符串
	 */
	public String doWhere(String label, List<Map<String, Object>> args, String formula) {
//		String sql = " WHERE 1=1 ";
//		String cypher = " AND %s.%s %s '%s'";
//
//		for (Map<String, Object> map : args) {
//			String _label = map.containsKey("label") ? map.get("label").toString().toLowerCase() : "";
//			String field = map.containsKey("field") ? map.get("field").toString().toLowerCase() : "";
//			String op = map.containsKey("op") ? map.get("op").toString().toLowerCase() : "";
//			String value = map.containsKey("value") ? map.get("value").toString().toLowerCase() : "";
//
//			if (StringUtility.isNullOrEmpty(_label))
//				sql += String.format(cypher, label.toLowerCase(), field.toLowerCase(), op.toLowerCase(), value);
//			else
//				sql += String.format(cypher, _label.toLowerCase(), field.toLowerCase(), op.toLowerCase(), value);
//		}
		return this.transform(label, formula, args);
	}

	public String doOptionalMatch(String startLabel, String midLabel, String endLabel) {
		if (StringUtils.isBlank(midLabel) && StringUtils.isBlank(endLabel)) {
			return "";
		}
		String mid;
		if (StringUtils.isBlank(midLabel)) {
			mid = "[r1]";
		} else {
			mid = String.format("[r1] - (%s:%s) - [r2]", midLabel.toLowerCase(), midLabel);
		}
		String optionalMatch = " MATCH (%s) - " + mid + " - (%s:%s)";
		return String.format(optionalMatch, startLabel.toLowerCase(), endLabel.toLowerCase(), endLabel);
	}

	public String doWhere(Map<String, Object> args) {
		if (args.isEmpty())
			return "";
		Object[] collect = args.entrySet().stream().map(entry -> entry.getKey() +" "+ entry.getValue()).toArray();

		String where = StringUtils.join(collect, " and ");
		return " WHERE " + where;
	}

	/**
	 * 拼接cypherSql返回串儿
	 * 
	 * @param label
	 *            实体类型
	 * @param list
	 *            cypher语句中包含的实体的集合
	 */
	public String doReturn(String label, List<String> list) {
		return list == null || list.isEmpty() ? " RETURN " + label.toLowerCase() : " RETURN " + StringUtils.join(list.toArray(), ',');
	}

	/**
	 * 拼接分页字符串
	 * 
	 * @param skip
	 *            分页开始位置
	 * @param limit
	 *            分页大小
	 * @return
	 */
	public String doPage(int skip, int limit) {
		return " skip " + skip + " limit " + limit;
	}

	@Override
	public String doCount(String label, List<Map<String, Object>> args, String formula, int skip, int limit) {
		String match = this.doMatch(label);
		String optional = this.doOptionalMatch(label, args);
		String where = this.doWhere(label, args, formula);
		return match + optional + where + " RETURN COUNT(*) AS total";
	}

	/**
	 * 按照给定的表达式计算
	 * @param label 检索节点
	 * @param expression 1*2*(3+4)+5
	 * @param args 参数数组
	 * @return
	 */
	private String transform(String label,String expression, List<Map<String, Object>> args) {
//		StringBuffer res = new StringBuffer();
		String res = "";
		String cp = "";
		try {
			
//			res.append(" WHERE 1=1 ");
			
			Stack<Object> resultStack = new Stack<Object>();
			
			// 准备表达式  后缀式栈
			Stack<String> postfixStack = prepare(expression);
			// 将后缀式栈反转
			Collections.reverse(postfixStack);

			if (postfixStack.isEmpty() || postfixStack.size() <= 0) {
				return res.toString();
			}

			if (!postfixStack.isEmpty() && postfixStack.size() == 1) {
				String currentValue = postfixStack.pop();
				//表达式中只包含一个
//				res.append(calculate_current_other(currentValue,args,label));
				res = calculate_current_other(currentValue,args,label);
			} else {
				Object firstValue, secondValue;
				String currentValue;
				// 参与计算的第一个值，第二个值和算术运算符
				while (!postfixStack.isEmpty()) {
					currentValue = postfixStack.pop();
					if (!isOperator(currentValue.charAt(0))) {
						// 如果不是运算符则存入操作数栈中 //
						resultStack.push(currentValue);
					} else {
						// 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
						secondValue = resultStack.pop();
						firstValue = resultStack.pop();
//						res.append(calculate_current(firstValue, secondValue, currentValue.charAt(0), args, label));
						res = calculate_current(firstValue, secondValue, currentValue.charAt(0), args, label);
						//组串
						resultStack.push(res);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		if(res.toString() != ""){
			cp = " WHERE 1=1 AND " + res.toString();
		}	
		return cp;
	}
	
	/**
	 * 按照给定的表达式计算
	 * @return String
	 */
	private String calculate_current(Object first, Object second,char op,List<Map<String,Object>> args,String label) throws Exception{
		StringBuffer res = new StringBuffer();
		try {
			if(args == null || args.size() <= 0){
				return null;
			}
			
			Map<String,Object> map1 = null;
			Map<String,Object> map2 = null;
			
			StringBuffer res1 = new StringBuffer();
			StringBuffer res2 = new StringBuffer();
			
			try {
				Integer.parseInt(first.toString());
			} catch (Exception e) {
				res1.append(first.toString());
			}
			
			try {
				Integer.parseInt(second.toString());
			} catch (Exception e) {
				res2.append(second.toString());
			}
			
			for (Map<String, Object> map : args) {
				String id = map.containsKey("id") ? map.get("id").toString() : "";
				
				if(StringUtils.isBlank(res1.toString())) {
					if(id.equals(first)){
						map1 = map;
					}
				}
				
				if(StringUtils.isBlank(res2.toString())) {
					if(id.equals(second)){
						map2 = map;
					}
				}
			}
			
			if(map1 != null ) {
				res1.append(formatWhere(map1, label));
			}
			
			if(map2 != null ) {
				res2.append(formatWhere(map2, label));
			}
			
			switch(op){
				case '*':
					res.append(res1.toString());
					res.append(" AND ");
					res.append(res2.toString());
					break;
				case '+':
					res.append(" ( ");
					res.append(res1.toString());
					res.append(" OR ");
					res.append(res2.toString());
					res.append(" ) ");
					break;
				default:
					res.append(res1.toString());
					res.append(" AND ");
					res.append(res2.toString());
					break;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return res.toString();
	}
	
	/**
	 * 按照给定的表达式计算
	 * @param index 索引
	 * @return QueryBuilder
	 */
	private String calculate_current_other(String index,List<Map<String,Object>> args, String label) throws Exception{
		String sql = "";
		
		if(args == null || args.size() <= 0){
			return sql;
		}
		
		for (Map<String, Object> map : args) {
			String id = map.containsKey("id") ? map.get("id").toString() : "";
			if(index.equals(id)) {
				sql = formatWhere(map,label);
			}
		}
		return sql;
	}
	
	/**
	 * 组装where语句
	 * @param map
	 * @param label
	 * @return
	 * @throws Exception
	 */
	private String formatWhere(Map<String,Object> map,String label) throws Exception{
		StringBuffer sql = new StringBuffer();
		String cypher = " %s.%s %s '%s' ";
		
		if(map == null ) return sql.toString();
		String _label = map.containsKey("label") ? map.get("label").toString().toLowerCase() : "";
		String field = map.containsKey("field") ? map.get("field").toString().toLowerCase() : "";
		String op = map.containsKey("op") ? map.get("op").toString().toLowerCase() : "";
		String value = map.containsKey("value") ? map.get("value").toString().toLowerCase() : "";

		if (StringUtils.isBlank(_label))
			sql.append(String.format(cypher, label.toLowerCase(), field.toLowerCase(), op.toLowerCase(), value));
		else
			sql.append(String.format(cypher, _label.toLowerCase(), field.toLowerCase(), op.toLowerCase(), value));
		return sql.toString();
	}

	/**
	 * 数据准备阶段将表达式转换成为后缀式栈
	 * @param expression
	 */
	private Stack<String> prepare(String expression) {
		
		Stack<Character> opStack = new Stack<Character>();
		Stack<String> postfixStack = new Stack<String>();
		
		char[] arr = expression.toCharArray();
		
		//当前字符的位置
		int currentIndex = 0;
		//上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
		int count = 0;
		//当前操作符和栈顶操作符
		char currentOp, peekOp;
		
		//运算符放入栈底元素逗号，此符号优先级最低
		opStack.push(',');
		
		for (int i = 0; i < arr.length; i++) {
			currentOp = arr[i];
			if (isOperator(currentOp)) {// 如果当前字符是运算符
				if (count > 0) {
					postfixStack.push(new String(arr, currentIndex, count));// 取两个运算符之间的数字
				}
				peekOp = opStack.peek();
				if (currentOp == ')') {// 遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
					while (opStack.peek() != '(') {
						postfixStack.push(String.valueOf(opStack.pop()));
					}
					opStack.pop();
				} else {
					while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
						postfixStack.push(String.valueOf(opStack.pop()));
						peekOp = opStack.peek();
					}
					opStack.push(currentOp);
				}
				count = 0;
				currentIndex = i + 1;
			} else {
				count++;
			}
		}
		if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
			postfixStack.push(new String(arr, currentIndex, count));
		}

		while (opStack.peek() != ',') {
			postfixStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
		}
		return postfixStack;
	}

	/**
	 * 判断是否为算术符号
	 * 
	 * @param c
	 * @return
	 */
	private boolean isOperator(char c) {
		return c == '+' || c == '*' || c == '(' || c == ')';
	}

	/**
	 * 利用ASCII码-40做下标去算术符号优先级
	 * @param cur
	 * @param peek
	 * @return
	 */
	public boolean compare(char cur, char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
		boolean result = false;
		int[] operatPriority = new int[] { 0, 3, 2, 1, -1, 1, 0, 2 };// 运用运算符ASCII码-40做索引的运算符优先级
		if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
			result = true;
		}
		return result;
	}
}
