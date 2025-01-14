package com.bleizing.recovery.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bleizing.recovery.enumeration.OperationEnum;
import com.bleizing.recovery.dto.model.BaseReqDto;
import com.bleizing.recovery.dto.model.ColumnDto;
import com.bleizing.recovery.dto.model.ConditionDto;
import com.bleizing.recovery.dto.model.GenerateSqlDto;
import com.bleizing.recovery.dto.model.TableDto;

@Service
public class ExcelHelper {

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public void convertExcel(InputStream is, String operation, TableDto table, GenerateSqlDto generateSqlDto) {
		try {
			Workbook workbook = new XSSFWorkbook(is);
			
			HashMap<Integer, List<ColumnDto>> columnsMap = new HashMap<>();
			HashMap<Integer, List<ConditionDto>> conditionsMap = new HashMap<>();
			
			List<ColumnDto> columns = null;
			List<ConditionDto> conditions = null;
			
			Integer rowTotal = generateSqlDto.getRowTotal();
			
			for (int i = 1; i > 0 ; i--) {				
				Sheet sheet = workbook.getSheetAt(workbook.getNumberOfSheets() - i);
				
				Iterator<Row> rows = sheet.iterator();
				int rowNumber = 0;

				while (rows.hasNext()) {
					columns = new ArrayList<>();
					conditions = new ArrayList<>();
					
					Row currentRow = rows.next();

					// skip header
					if (rowNumber == 0) {
						rowNumber++;
						continue;
					}
					
					if (currentRow.getCell(0) == null || getFirstCol(currentRow.getCell(0)).equals("")) {
						break;
					}
					
					for (ColumnDto column : table.getColumns()) {
						ColumnDto col = new ColumnDto();
						col.setName(column.getName());
						if (operation.equals(OperationEnum.UPDATE.toString())) {
							col.setValue(getValue(column, currentRow));
						}
						columns.add(col);
					}
					columnsMap.put(rowTotal, columns);
					
					for (ConditionDto condition : table.getConditions()) {
						ConditionDto cond = new ConditionDto();
						cond.setName(condition.getName());
						String val = getValue(condition, currentRow);
						if (val.contains(".0")) {
							val = val.substring(0, val.length() - 2);
							cond.setNumber(true);
						}
						cond.setValue(val);
						conditions.add(cond);
					}
					conditionsMap.put(rowTotal, conditions);
					
					++rowTotal;
				}
				
				generateSqlDto.setRowTotal(rowTotal);
				generateSqlDto.setColumnsMap(columnsMap);
				generateSqlDto.setConditionsMap(conditionsMap);
			}

			workbook.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
	
	private String getValue(BaseReqDto obj, Row row) {
		String value = "";
		
		Cell cell = row.getCell(Integer.parseInt(obj.getValue()));
		
		if (cell != null) {
			switch (cell.getCellType()) {
				case STRING:
					value = cell.getRichStringCellValue().getString();
					break;
				case NUMERIC:
					value = cell.getNumericCellValue() + "";
					break;
				default:
					break;
			}
		}
		
		return value;
	}
	
	private String getFirstCol(Cell cell) {
		String value = "";
		
		switch (cell.getCellType()) {
			case STRING:
				value = cell.getRichStringCellValue().getString();
				break;
			case NUMERIC:
				value = cell.getNumericCellValue() + "";
				break;
			default:
				break;
		}
		
		return value;
	}
}
