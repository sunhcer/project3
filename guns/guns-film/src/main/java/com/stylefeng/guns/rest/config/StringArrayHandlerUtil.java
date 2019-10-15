package com.stylefeng.guns.rest.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@MappedTypes(String[].class)
public class StringArrayHandlerUtil implements TypeHandler<String[]> {
    ObjectMapper objectMapper=new ObjectMapper();

    @Override
    //String[]转String到数据库
    public void setParameter(PreparedStatement preparedStatement, int index, String[] strings, JdbcType jdbcType) throws SQLException {
        String jsonArray = null;
        try {
            jsonArray = objectMapper.writeValueAsString(strings);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        preparedStatement.setString(index,jsonArray);
    }

    @Override
    //数据区的json数组字符串转String[]
    public String[] getResult(ResultSet resultSet, String paramName) throws SQLException {
        String value=resultSet.getString(paramName);
        return parseStringToStringArray(value);
    }

    private String[] parseStringToStringArray(String value) {
        String[] strings = new String[0];
        if (value==null){
            return strings;
        }
        try {
            strings=objectMapper.readValue(value,String[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    @Override
    public String[] getResult(ResultSet resultSet, int index) throws SQLException {
        String value = resultSet.getString(index);
        return parseStringToStringArray(value);
    }

    @Override
    public String[] getResult(CallableStatement callableStatement, int index) throws SQLException {
        String value = callableStatement.getString(index);
        return parseStringToStringArray(value);
    }
}
