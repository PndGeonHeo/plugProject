package com.common;

import com.controller.Common;
import com.fr.third.fasterxml.jackson.core.type.TypeReference;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import com.fr.third.org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.sql.Driver;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

public class JdbcUtils {
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String PATH_SEPARATOR = "/";
    private static final String EXT_XML = ".xml";

    private static Properties props = Common.getProperties("conf/app.properties");

    private static String DRIVER   = props.getProperty("jdbc.driver");
    private static String URL      = props.getProperty("jdbc.url");
    private static String USER     = props.getProperty("jdbc.user");
    private static String PASSWORD = props.getProperty("jdbc.password");

    private NamedParameterJdbcTemplate jdbcTemplate;

    private boolean useSqlMap = false; // true: query in sqlmap.xml (default), false: parameter string

    public void setUseSqlMap(boolean useSqlMap) {
        this.useSqlMap = useSqlMap;
    }

    private String sqlFilename = "";

    public void setSqlFilename(String sqlFilename) {
        this.sqlFilename = sqlFilename;
    }

    public JdbcUtils() throws Exception {
        DataSource ds = dataSource();
        this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    public JdbcUtils(boolean useSqlMap) throws Exception {
        DataSource ds = dataSource();
        this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.useSqlMap = useSqlMap;
    }

    public JdbcUtils(String filename) throws Exception {
        DataSource ds = dataSource();
        this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.useSqlMap = true;
        this.sqlFilename = filename;
    }

    public DataSource dataSource() throws Exception {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<? extends Driver>) Class.forName(DRIVER));
        dataSource.setUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);

        return dataSource;
    }

    public <T> T queryForObject(String str, Class<T> t) throws Exception {
        try {
            return queryForObject(str, null, t);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T queryForObject(String str, Map<String, Object> paramMap, Class<T> t) throws Exception {
        try {
            String sql = sql(str, paramMap);
            return jdbcTemplate.queryForObject(sql, paramMap, t);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> queryForMap(String str) throws Exception {
        try {
            return queryForMap(str, null);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> queryForMap(String str, Map<String, Object> paramMap) throws Exception {
        try {
            String sql = sql(str, paramMap);
            return jdbcTemplate.queryForMap(sql, paramMap);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map<String, Object>> queryForList(String str) throws Exception {
        return queryForList(str, null);
    }

    public List<Map<String, Object>> queryForList(String str, Map<String, Object> paramMap) throws Exception {
        String sql = sql(str, paramMap);
        return jdbcTemplate.queryForList(sql, paramMap);
    }

    public int update(String str, Object record) throws Exception {
        Map<String, Object> paramMap = new ObjectMapper().convertValue(record, new TypeReference<Map<String, Object>>() {});
        return update(str, paramMap);
    }

    public int update(String str, Map<String, Object> paramMap) throws Exception {
        String sql = sql(str, paramMap);
        return jdbcTemplate.update(sql, paramMap);
    }

    public int[] batchUpdate(String str, List<?> list) throws Exception {
        List<Map<String, ?>> paramMaps = new ObjectMapper().convertValue(list, new TypeReference<List<Map<String, ?>>>() {});
        String sql = sql(str, null);
        return jdbcTemplate.batchUpdate(sql, createBatchValues(paramMaps));
    }

    public SqlParameterSource[] createBatchValues(List<Map<String, ?>> valueMaps) {
        SqlParameterSource[] batchValues = new SqlParameterSource[valueMaps.size()];

        for (int i=0; i<valueMaps.size(); i++) {
            batchValues[i] = new MapSqlParameterSource(valueMaps.get(i));
        }

        return batchValues;
    }

    public String sql(String str, Map<String, Object> paramMap) throws Exception {
        if (this.useSqlMap && StringUtils.isNotEmpty(str))
            return parseSql(str, paramMap);

        return str;
    }

    private Document document;
    private XPath xpath;

    public String parseSql(String str, Map<String, Object> paramMap) throws Exception {
        String filename = "";
        String queryid = "";
        if (StringUtils.isNotEmpty(this.sqlFilename)) {
            filename = this.sqlFilename;
            queryid = str;

        } else {
            filename = str.split("\\.")[0] + EXT_XML;
            queryid = str.split("\\.")[1];
        }

        String classPath = JdbcUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll(PATH_SEPARATOR, Matcher.quoteReplacement(FILE_SEPARATOR));
        String path = classPath + "sql" + FILE_SEPARATOR + filename;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        document = documentBuilder.parse(path);

        XPathFactory xPathfactory = XPathFactory.newInstance();
        xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile("//query[@id='" + queryid +"']");
        Node query = (Node) expr.evaluate(document, XPathConstants.NODE);

        if (query == null)
            throw new Exception("invalid element : " + queryid);

        StringBuilder sb = new StringBuilder();
        sb.append(getSql(queryid, query, paramMap));

        if (sb.length() < 1)
            throw new Exception("query is null : " + queryid);

        return sb.toString();
    }

    public String getSql(String queryid, Node node, Map<String, Object> paramMap) throws Exception {
        StringBuilder sb = new StringBuilder();

        NodeList childs = node.getChildNodes();
        for (int i=0; i<childs.getLength(); i++) {
            Node child = childs.item(i);

            if (child.getNodeType() == Node.TEXT_NODE) {
                sb.append(child.getTextContent());

            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) child;
                String nodeName = elem.getNodeName();
                String property = elem.getAttribute("property");

                if (StringUtils.isEmpty(property))
                    throw new Exception("isNotEmpty property not exist : " + queryid);

                if ("isEmpty".equals(nodeName)) {
                    XPathExpression expr = xpath.compile("//query[@id='" + queryid +"']/isEmpty[@property='" + property + "']");
                    Node isEmpty = (Node) expr.evaluate(document, XPathConstants.NODE);

                    if (paramMap == null || paramMap.get(property) == null) {
                        sb.append(getSql(queryid, isEmpty, paramMap));

                    } else if (paramMap != null && paramMap.get(property) != null) {
                        if (paramMap.get(property) instanceof String && !"".equals(paramMap.get(property)))
                            continue;
                        if (paramMap.get(property) instanceof List && ((List) paramMap.get(property)).size() > 0)
                            continue;
                        if (paramMap.get(property).getClass().isArray() && ((Object[]) paramMap.get(property)).length > 0)
                            continue;

                        sb.append(getSql(queryid, isEmpty, paramMap));
                    }

                } else if ("isNotEmpty".equals(nodeName)) {
                    XPathExpression expr = xpath.compile("//query[@id='" + queryid +"']/isNotEmpty[@property='" + property + "']");
                    Node isNotEmpty = (Node) expr.evaluate(document, XPathConstants.NODE);

                    if (paramMap != null && paramMap.get(property) != null) {
                        if (paramMap.get(property) instanceof String && "".equals(paramMap.get(property)))
                            continue;
                        if (paramMap.get(property) instanceof List && ((List) paramMap.get(property)).size() < 1)
                            continue;
                        if (paramMap.get(property).getClass().isArray() && ((Object[]) paramMap.get(property)).length < 1)
                            continue;

                        sb.append(getSql(queryid, isNotEmpty, paramMap));
                    }

                } else if ("isEqual".equals(nodeName)) {
                    String compareValue = elem.getAttribute("compareValue");

                    if (StringUtils.isEmpty(property))
                        throw new Exception("isEqual compareValue not exist : " + queryid);

                    XPathExpression expr = xpath.compile("//query[@id='" + queryid +"']/isEqual[@property='" + property + "'][@compareValue='" + compareValue + "']");
                    Node isEqual = (Node) expr.evaluate(document, XPathConstants.NODE);

                    if (paramMap != null) {
                        String param = String.valueOf(paramMap.get(property));
                        if (compareValue.equals(param)) {
                            sb.append(getSql(queryid, isEqual, paramMap));
                        }
                    }

                } else if ("isNotEqual".equals(nodeName)) {
                    String compareValue = elem.getAttribute("compareValue");

                    if (StringUtils.isEmpty(property))
                        throw new Exception("isEqual compareValue not exist : " + queryid);

                    XPathExpression expr = xpath.compile("//query[@id='" + queryid +"']/isNotEqual[@property='" + property + "'][@compareValue='" + compareValue + "']");
                    Node isNotEqual = (Node) expr.evaluate(document, XPathConstants.NODE);

                    if (paramMap != null) {
                        String param = (String) paramMap.get(property);
                        if (!compareValue.equals(param)) {
                            sb.append(getSql(queryid, isNotEqual, paramMap));
                        }
                    }
                }
            }
        }

        return sb.toString();
    }
}
