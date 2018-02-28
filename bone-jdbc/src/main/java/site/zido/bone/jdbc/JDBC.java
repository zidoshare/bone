package site.zido.bone.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import site.zido.core.beans.BoneContext;
import site.zido.core.props.DataSourceProps;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBC {
    private static Connection conn;
    private static ComboPooledDataSource ds = new ComboPooledDataSource();
    public static void init(){
        DataSourceProps dsProps = BoneContext.getInstance().getBean(DataSourceProps.class);
        try {
            ds.setDriverClass(dsProps.getDriverName());
            ds.setJdbcUrl(dsProps.getJdbcUrl());
            ds.setUser(dsProps.getUsername());
            ds.setPassword(dsProps.getPassword());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
