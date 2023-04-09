package DeliveryManagerSystem;

import java实验.Report1;
import java实验.Report2;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static oracle.jdbc.OracleTypes.CURSOR;
import static oracle.jdbc.OracleTypes.FLOAT;

public class Oracle {
    private Connection conn;
    private Statement statm;
    private CallableStatement proCallId;        // proc_查找_u_id
    private CallableStatement proCallDate;      // proc_查找_date
    private CallableStatement procCallStat;     // proc_销售金额
    private CallableStatement procCallIvtry;    // proc_送货清单
    private CallableStatement procCallUsers;    // proc_用户表
    private SimpleDateFormat simpleDateFormat;
    private ResultSet rs;
    private int o_id;

    public Oracle()throws Exception
    {
        init();
    }

    // 初始化
    private void init()throws Exception
    {
        Properties pro = new Properties();
        try{
            pro.load(new FileInputStream("config.properties"));//相对路径
        }catch (Exception e){
            throw e;
        }
        try{
            connect(pro.getProperty("host"),pro.getProperty("port"),pro.getProperty("db"),pro.getProperty("user"),pro.getProperty("passwd"));
            statm=conn.createStatement();
            proCallId=conn.prepareCall("call proc_查找_u_id(?,?)");
            proCallDate=conn.prepareCall("call proc_查找_date(?,?)");
            procCallStat=conn.prepareCall("call proc_销售金额(?,?,?)");
            procCallIvtry=conn.prepareCall("call proc_送货清单(?)");
            procCallUsers=conn.prepareCall("call proc_用户表(?)");
            ResultSet res = statm.executeQuery("select * from (select o_id from ord order by o_id desc) where rownum=1");
            res.next();
            o_id=res.getInt(1);
            res.close();
            simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }catch (SQLException sqlE){
            throw sqlE;
        }
    }

    /**
     * @param host 数据库服务器IP
     * @param port 数据库服务器端口
     * @param db 数据库名
     * @param user 数据库用户
     * @param passwd 数据库用户密码
     * */
    public void connect(String host,String port,String db,String user,String passwd) throws SQLException
    {
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@%s:%s:%s".formatted(host,port,db), user,passwd);
        }catch (SQLException e) {
            throw e;
        }
    }

    public List<Object[]> sqlQuery(String sql)throws SQLException
    {
        try {
            rs = statm.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            List<Object[]> listSet = new ArrayList<Object[]>();
            Object[] objRow = new Object[md.getColumnCount()];
            for (int index = 0; index < objRow.length; index++) {
                objRow[index] = md.getColumnName(index + 1);
            }
            listSet.add(objRow);
            while (rs.next()) {
                objRow = new Object[md.getColumnCount()];
                for (int index = 0; index < objRow.length; index++) {
                    objRow[index] = rs.getObject(index + 1);
                }
                listSet.add(objRow);
            }
            rs.close();
            return listSet;
        } catch (SQLException sqlE) {
            throw sqlE;
        }
    }

    /**
     * @param id 输入用户ID
     * @return 返回值第一个元素是表头，其余为行数据
     * */
    public List<Object[]> searchById(String id)throws SQLException
    {
        try {
            proCallId.setString(1, id);
            proCallId.registerOutParameter(2, CURSOR);
            proCallId.execute();
            ResultSet rs= (ResultSet)proCallId.getObject(2);
            List<Object[]> set=RsToSet(rs);
            rs.close();
            return set;
        }catch (SQLException sqlE){
            throw sqlE;
        }
    }

    /**
     * @param date 为时间单位为月 格式：yyyy-mm
     * @return 返回值第一个元素是表头，其余为行数据
     * */
    public List<Object[]> searchByDate(String date)throws SQLException
    {
        try {
            proCallDate.setString(1, date);
            proCallDate.registerOutParameter(2, CURSOR);
            proCallDate.execute();
            ResultSet rs= (ResultSet)proCallDate.getObject(2);
            List<Object[]> set=RsToSet(rs);
            rs.close();
            return set;
        }catch (SQLException sqlE){
            throw sqlE;
        }
    }

    public List<Object[]> inventory()throws SQLException
    {
        try{
            procCallIvtry.registerOutParameter(1,CURSOR);
            procCallIvtry.execute();
            ResultSet rs= (ResultSet)procCallIvtry.getObject(1);
            List<Object[]> set=RsToSet(rs);
            rs.close();
            return set;
        }catch (SQLException sqlE){
            throw sqlE;
        }
    }

    public List<Object[]> usersTable()throws SQLException
    {
        try{
            procCallUsers.registerOutParameter(1,CURSOR);
            procCallUsers.execute();
            ResultSet rs= (ResultSet)procCallUsers.getObject(1);
            List<Object[]> set=RsToSet(rs);
            rs.close();
            return set;
        }catch (SQLException sqlE){
            throw sqlE;
        }
    }

    public float statistics(String start,String end)throws SQLException
    {
        try{
            procCallStat.setString(1,start);
            procCallStat.setString(2,end);
            procCallStat.registerOutParameter(3,FLOAT);
            procCallStat.execute();
            return procCallStat.getFloat(3);
        }catch (SQLException sqlE){
            throw sqlE;
        }
    }

    /**
     * @param u_id 用户ID
     * @param goods 键/值 为 商品ID/发货时间
     * @param quantity 键/值 为 商品ID/商品数量
     * */
    public void insert(String u_id, Map<Integer,String> goods, Map<Integer,Integer> quantity)throws SQLException
    {
        String time=simpleDateFormat.format(new Date());
        ResultSet rs;
        try {
            statm.executeUpdate("insert into ord(o_id,u_id,o_date) values(%d,%s,to_date('%s','yyyy-mm-dd hh24:mi:ss'))".formatted(++o_id, u_id, time));
            Integer[] keys = goods.keySet().toArray(new Integer[0]);
            for (int key : keys) {
                statm.executeUpdate("insert into ord_detail(o_id,g_id,quant,o_d_date) values(%d,%d,%d,to_date('%s','yyyy-mm-dd hh24:mi:ss'))".formatted(o_id,key,quantity.get(key),goods.get(key)));
            }
        }catch (SQLException sqlE){
            throw sqlE;
        }
    }

    public void delete(String o_id)throws SQLException
    {
        statm.executeUpdate("delete from ord_detail where o_id=%d".formatted(o_id));
        statm.executeUpdate("delete from ord where o_id=%d".formatted(o_id));
    }

    public void disconnect()throws SQLException
    {
        try{
            conn.close();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    private List<Object[]> RsToSet (ResultSet rs)throws SQLException
    {
        List<Object[]> set=new ArrayList<Object[]>();
        try{
            ResultSetMetaData md = rs.getMetaData();
            Object[] objRow=new Object[md.getColumnCount()];
            for (int index = 0; index < objRow.length; index++) {
                objRow[index] = md.getColumnName(index + 1);
            }
            set.add(objRow);
            while(rs.next()){
                objRow=new Object[md.getColumnCount()];
                for(int index=0;index<objRow.length;index++){
                    objRow[index]=rs.getObject(index+1);
                }
                set.add(objRow);
            }
        }catch (SQLException sqlE){
            throw sqlE;
        }
        return set;
    }
}
