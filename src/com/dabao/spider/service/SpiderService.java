package com.dabao.spider.service;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.dabao.spider.bean.BaseColumn;
import com.dabao.spider.bean.ColumnRule;
import com.dabao.spider.bean.Info;
import com.dabao.spider.dao.InfoDao;
import com.dabao.spider.log.InfoLog;
import com.dabao.spider.log.WarnLog;
import com.dabao.spider.util.RandomUtil;

public class SpiderService {
	private InfoDao infoDao;
	private BaseColumn baseColumn;

	public boolean checkTable(String tableName, List<ColumnRule> list) {
		Map<String, String> column_type = infoDao.findTable(tableName);
		if (column_type == null) {
			column_type = new HashMap<String, String>();
			for (ColumnRule cr : list) {
				column_type.put(cr.getName(), cr.getType());
				if (cr.getFile_column() != null) {
					column_type.put(cr.getFile_column(), "VARCHAR(1000)");
				}
				if (cr.getOcr_column() != null) {
					column_type.put(cr.getOcr_column(), "VARCHAR(1000)");
				}
			}
			column_type.putAll(baseColumn.getMap());
			InfoLog.logger("创建表" + tableName + "以及字段");
			return infoDao.createTable(tableName, column_type);
		} else {
			Map<String, String> add_column = new HashMap<String, String>();
			for (ColumnRule cr : list) {
				if (!column_type.containsKey(cr.getName())) {
					add_column.put(cr.getName(), cr.getType());
					if (cr.getFile_column() != null) {
						add_column.put(cr.getFile_column(), "VARCHAR(1000)");
					}
					if (cr.getOcr_column() != null) {
						add_column.put(cr.getOcr_column(), "VARCHAR(1000)");
					}
				}
			}
			if (add_column.size() > 0) {
				InfoLog.logger("添加表" + tableName + "的字段:"
						+ add_column.keySet().toString());
				return infoDao.addColumn(tableName, add_column);
			}
			return true;
		}
	}

	/**
	 * @param table
	 * @param info
	 * @return 表中的id
	 */
	@Transactional(rollbackFor = Exception.class)
	public int saveInfo(String table, Info info) throws Exception {
		info.put(BaseColumn.KEY_SPIDER_LAST_MODIFY_TIME, DateFormat
				.getDateTimeInstance().format(new Date()), true);
		try {
			int id = infoDao.findInfoIdByUrl(table, info.get(
					BaseColumn.KEY_SPIDER_URL).toString());
			if (id > 0) {
				infoDao.updateInfoById(table, id, info);
				InfoLog.logger("更新数据成功" + table + ":" + id);
				return id;
			} else {
				info.put(BaseColumn.KEY_UUID, String.valueOf(System
						.currentTimeMillis())
						+ RandomUtil.random(4), true);
				infoDao.saveInfo(table, info);
				id = infoDao.findInfoIdByUrl(table, info.get(
						BaseColumn.KEY_SPIDER_URL).toString());
				InfoLog.logger("保存数据成功" + table + ":" + id);
				return id;
			}
		} catch (Exception e) {
			WarnLog.logger(e.getMessage());
			return 0;
		}
	}

	public void setInfoDao(InfoDao infoDao) {
		this.infoDao = infoDao;
	}

	public void setBaseColumn(BaseColumn baseColumn) {
		this.baseColumn = baseColumn;
	}

}