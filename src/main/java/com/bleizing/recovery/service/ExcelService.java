package com.bleizing.recovery.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.StringBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bleizing.recovery.enumeration.OperationEnum;
import com.bleizing.recovery.dto.model.ColumnDto;
import com.bleizing.recovery.dto.model.ConditionDto;
import com.bleizing.recovery.dto.model.GenerateSqlDto;
import com.bleizing.recovery.dto.model.OperationDto;
import com.bleizing.recovery.dto.model.TableDto;
import com.bleizing.recovery.dto.request.UploadRequest;
import com.bleizing.recovery.helper.ExcelHelper;

@Service
public class ExcelService {

	private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

	private static final String SPACE = " ";
	private static final String DOT = ".";
	private static final String SEMICOLON = ";";
	private static final String APOSTROPHE = "'";
	private static final String FROM = SPACE + "FROM" + SPACE;
	private static final String WHERE = SPACE + "WHERE" + SPACE;
	private static final String COMMA = "," + SPACE;
	private static final String SET = SPACE + "SET" + SPACE;
	private static final String EQUALS = SPACE + "=" + SPACE;
	private static final String AND = SPACE + "AND" + SPACE;

	@Autowired
	PdfService pdfService;
	
	@Autowired
	ExcelHelper excelHelper;
	
	public String extractFile(MultipartFile file, UploadRequest req, String filename) {
		for (OperationDto operation : req.getOperations()) {
			for (TableDto table : operation.getTables()) {
				try {
					GenerateSqlDto generateSqlDto = new GenerateSqlDto();
					generateSqlDto.setRowTotal(0);
					excelHelper.convertExcel(file.getInputStream(), operation.getValue(), table, generateSqlDto);
					List<String> sqls = generateSql(req, generateSqlDto);
					pdfService.createPdfDoc(sqls, filename);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					throw new RuntimeException("fail to store excel data: " + e.getMessage());
				}
			}
		}
		return filename;
	}
	
	private List<String> generateSql(UploadRequest req, GenerateSqlDto generateSqlDto) {
		List<String> sqls = new ArrayList<>();
		
		for (OperationDto operation : req.getOperations()) {
			for (TableDto table : operation.getTables()) {
				for(int i = 0; i < generateSqlDto.getRowTotal(); i++) {
					List<ColumnDto> columns = generateSqlDto.getColumnsMap().get(i);
					List<ConditionDto> conditions = generateSqlDto.getConditionsMap().get(i);
					boolean isFirst = true;
					boolean isSelect = operation.getValue().equals(OperationEnum.SELECT.toString());
					StringBuilder sql = new StringBuilder();
					StringBuilder query = new StringBuilder();
					
					if (isSelect) {
						sql.append(OperationEnum.SELECT.toString());
						sql.append(SPACE);
					} else {
						sql.append(OperationEnum.UPDATE.toString());
						sql.append(SPACE);
						sql.append(operation.getSchema());
						sql.append(DOT);
						sql.append(table.getValue());
						sql.append(SET);
					}
					
					for (ColumnDto column : columns) {
						if (!isFirst) {
							query.append(COMMA);
						}
						
						query.append(column.getName());
						
						if (!isSelect) {
							query.append(EQUALS);
							query.append(APOSTROPHE);
							query.append(column.getValue());
							query.append(APOSTROPHE);
						}
						
						isFirst = false;
					}
					sql.append(query);
					
					if (isSelect) {
						sql.append(FROM);
						sql.append(operation.getSchema());
						sql.append(DOT);
						sql.append(table.getValue());
					}
					
					sql.append(WHERE);

					query = new StringBuilder();
					isFirst = true;
					
					for (ConditionDto condition : conditions) {						
						if (!isFirst) {
							query.append(AND);
						}
						
						query.append(condition.getName());
						query.append(EQUALS);
						query.append(APOSTROPHE);
						query.append(condition.getValue());
						query.append(APOSTROPHE);
						
						isFirst = false;
					}
					sql.append(query);
					
					sql.append(SEMICOLON);
					
					sqls.add(sql.toString());
				}
			}
		}
		
		return sqls;
	}
}
