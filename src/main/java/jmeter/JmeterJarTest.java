package jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.sql.*;

public class JmeterJarTest extends AbstractJavaSamplerClient {

    private String url = "jdbc:mysql://172.16.249.2:4000/test";
    private String username = "root";
    private String password = "";
    private String sql = "select * from t1 where id = ?;";

    private Connection c;
    private PreparedStatement ps;

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        try {

            password = javaSamplerContext.getParameter("password");
            Class.forName("com.mysql.jdbc.Driver");
            c.setAutoCommit(false);
            c = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        sampleResult.setDataEncoding("UTF-8");
        ResultSet resultSet = null;
        try {
            ps = c.prepareStatement(sql);
            ps.setObject(1, "1");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("c1"));
            }
            c.commit();
            resultSet.close();
            ps.close();
        } catch (SQLException throwables) {
            sampleResult.setSuccessful(false);
            throwables.printStackTrace();
        }
        sampleResult.setSuccessful(true);
        sampleResult.sampleEnd();
        return sampleResult;
    }

    @Override
    public void teardownTest(JavaSamplerContext javaSamplerContext) {
        try {
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("url", "");
        arguments.addArgument("userName", "");
        arguments.addArgument("password", "");
        return arguments;
    }
}
