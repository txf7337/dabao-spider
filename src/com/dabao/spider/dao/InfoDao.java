package com.dabao.spider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.dabao.spider.bean.BaseColumn;
import com.dabao.spider.bean.Info;
import com.dabao.spider.log.WarnLog;

public class InfoDao {
	private JdbcTemplate jdbcTemplate;

	public Map<String, String> findTable(String tableName) {
		try {
			final Map<String, String> map = new HashMap<String, String>();
			String sql = "DESC " + tableName;
			jdbcTemplate.query(sql, new RowCallbackHandler() {

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					String key = rs.getString("field");
					String value = rs.getString("type");
					map.put(key, value);
				}
			});
			return map;
		} catch (DataAccessException e) {
			WarnLog.logger(e.getMessage());
			return null;
		}
	}

	public boolean dropColumn(String tableName, List<String> drop_column) {
		try {
			StringBuffer sql = new StringBuffer("ALTER TABLE " + tableName);
			for (String column : drop_column) {
				sql.append(" DROP COLUMN " + column + ",");
			}
			jdbcTemplate.execute(sql.substring(0, sql.length() - 1));
			return true;
		} catch (DataAccessException e) {
			WarnLog.logger(e.getMessage());
			return false;
		}
	}

	public boolean addColumn(String tableName, Map<String, String> add_column) {
		try {
			StringBuffer sql = new StringBuffer("ALTER TABLE " + tableName);
			for (Iterator<String> it = add_column.keySet().iterator(); it
					.hasNext();) {
				String column = it.next();
				sql.append(" ADD COLUMN " + column + " "
						+ add_column.get(column) + ",");
			}
			jdbcTemplate.execute(sql.substring(0, sql.length() - 1));
			return true;
		} catch (DataAccessException e) {
			WarnLog.logger(e.getMessage());
			return false;
		}
	}

	public boolean createTable(String tableName, Map<String, String> column_type) {
		try {
			StringBuffer sql = new StringBuffer("CREATE TABLE " + tableName);
			sql.append("(");
			for (Iterator<String> it = column_type.keySet().iterator(); it
					.hasNext();) {
				String column = it.next();
				sql.append(column + " " + column_type.get(column) + ",");
			}
			jdbcTemplate.execute(sql.substring(0, sql.length() - 1) + ")");
			return true;
		} catch (DataAccessException e) {
			WarnLog.logger(e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public int findInfoIdByUrl(String table, String url) throws Exception {
		List<Integer> list = jdbcTemplate.queryForList("SELECT "
				+ BaseColumn.KEY_SPIDER_ID + " t FROM " + table + " WHERE "
				+ BaseColumn.KEY_SPIDER_URL + "=?", new Object[] { url },
				Integer.class);
		return list.size() == 0 ? 0 : list.get(0);
	}

	public void updateInfoById(String table, int id, Info info)
			throws Exception {
		jdbcTemplate.update("UPDATE " + table + " SET " + info.getUpdateKeys()
				+ " WHERE " + BaseColumn.KEY_SPIDER_ID + "=" + id, info
				.getUpdateParams());
	}

	public void saveInfo(String table, Info info) throws Exception {
		jdbcTemplate.update("INSERT INTO " + table + "(" + info.getKeys()
				+ ") values(" + info.getQuestions() + ")", info.getParams());
	}

	public Info findInfo(String table, int id, final String[] columns) {
		final Info info = new Info();
		jdbcTemplate.query("SELECT " + BaseColumn.KEY_SPIDER_URL + ","
				+ StringUtils.join(columns, ",") + " FROM " + table + " WHERE "
				+ BaseColumn.KEY_SPIDER_ID + "=" + id,
				new RowCallbackHandler() {

					@Override
					public void processRow(ResultSet rs) throws SQLException {
						info.put(BaseColumn.KEY_SPIDER_URL, rs
								.getString(BaseColumn.KEY_SPIDER_URL), true);
						for (String column : columns) {
							info.put(column, rs.getString(column), true);
							info.put(column, rs.getString(column), true);
						}
					}
				});
		return info;
	}

	public void saveColumnValue(String table, int id, String column,
			String value) {
		jdbcTemplate.update("UPDATE " + table + " SET " + column + "=? WHERE "
				+ BaseColumn.KEY_SPIDER_ID + "=" + id, new Object[] { value });
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
