package kr.fasol.obix.core.config.db.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

import java.io.StringReader;
import java.sql.*;

@MappedJdbcTypes({JdbcType.CLOB, JdbcType.NCLOB})
public class CustomClobTypeHandler extends BaseTypeHandler<String> implements TypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
		if (jdbcType == JdbcType.CLOB || jdbcType == JdbcType.NCLOB) {
			StringReader reader = new StringReader(parameter);
			ps.setCharacterStream(i, reader, parameter.length());
		} else {
			ps.setString(i, parameter);
		}
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Clob clob = rs.getClob(columnName);
		try {
			return toString(clob);
		} finally {
			if (clob != null) {
				clob.free();
			}
		}
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Clob clob = rs.getClob(columnIndex);
		try {
			return toString(clob);
		} finally {
			if (clob != null) {
				clob.free();
			}
		}
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Clob clob = cs.getClob(columnIndex);
		try {
			return toString(clob);
		} finally {
			if (clob != null) {
				clob.free();
			}
		}
	}

	private String toString(Clob clob) throws SQLException {
		return clob == null ? null : clob.getSubString(1, (int) clob.length());
	}
}
