package egovframework.example.util;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.servlet.view.document.AbstractExcelView;


/**
 * 		String[] headColumns = {"사건종류", "사건구분", "사건번호", "제목", "최초처리기관", "발신처", "발송일시", "접수일시", "상태"};
		Integer[] columnSize = {10*256, 10*256, 10*256, 30*256, 20*256, 20*256, 20*256, 20*256, 10*256};

		model.addAttribute("excelTitle",		"상황실접수목록"									);
		model.addAttribute("excelDv",			"SttnRptRcptLst"									);
		model.addAttribute("excelHeadColumn",	headColumns											);
		model.addAttribute("excelColumnSize",	columnSize											);
		model.addAttribute("excelBodyList" ,	smsSttnMngService.searchListSttnRcptExcel(condVo)	);

		return "CmmExcelView";
 * @author kms_note
 *
 */
public class CmmExcelView extends AbstractExcelView {
	
	/** log */
	//private  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void buildExcelDocument(	Map<String, Object> model,
										HSSFWorkbook wb, 
										HttpServletRequest request, 
										HttpServletResponse response) 	throws Exception {
		
		  /** 엑셀 서식 설정 */
		  makeFormlaStyle(wb);
		  
		 HSSFSheet sheet = wb.createSheet();
		 int rowIdx = 0;

		 String excelTitle = (String)model.get("excelTitle");
		 
		 //LOGGER.debug("excelTitle:"+excelTitle);
		  
		  /** Head Column 설정 */
		  String[] headColumns = (String[])model.get("excelHeadColumn");	  //String[] titleList = { "아이디", "이름", "EMAIL","연락처","등록일","최종접속"};
		  
		  /** 2번째 row에 2번재 cell부터 제목 표시 시작 */
		  rowIdx++;
		  HSSFRow titleRow = sheet.createRow(rowIdx++); 
		  
		  HSSFCell cellTitle = titleRow.createCell(1);
		  cellTitle.setCellValue(new HSSFRichTextString(excelTitle));
		  cellTitle.setCellStyle(makeTitleStyle(wb));
		  
		  // Merges the cells
		  CellRangeAddress cellRangeAddress = new CellRangeAddress(1, 1, 1, headColumns.length);
		  sheet.addMergedRegion(cellRangeAddress);
		  /** 2번째 row에 2번재 cell부터 제목 표시 끝 */


		  /** 4번째 row에 2번재 cell부터 HEAD 항목  표시 시작 */
		  rowIdx++;
		  HSSFRow headerRow = sheet.createRow(rowIdx++);
		  
		  int headCellIdx = 0;
		  for (int i = 1; i < headColumns.length +1 ; i++) {
			  HSSFCell cell = headerRow.createCell(i);
			  cell.setCellValue(new HSSFRichTextString(headColumns[headCellIdx++]));
			  cell.setCellStyle(makeHeadColumnStyle(wb));
		  }
		  /** 4번째 row에 2번재 cell부터 HEAD 항목  표시 끝 */
		
		  
		  String  excelDv = (String)model.get("excelDv");
		  
		  /* 통제보고 통계 시작 */
		  if("CnrlRptStats".equals(excelDv)) {
			  getCnrlRptStats(wb , sheet, model,  rowIdx);
	      /* 정기보고 통계 */
		  } else if ("FdrmRptStats".equals(excelDv)) {
			  getFdrmRptStats(wb , sheet, model,  rowIdx);
		  /* 수시보고 통계 */
		  } else if ("AnytmRptStats".equals(excelDv)) {
				  getAnytmRptStats(wb , sheet, model,  rowIdx);		 
		  /* 반려건수통계 */
		  } else if ("RtnCoStats".equals(excelDv)) {
				  getRtnCoStats(wb , sheet, model,  rowIdx);				  
			  
			  /* 사건접수추적*/  
		  } else if ("IcdtRcptTrc".equals(excelDv)) {
			  getIcdtRcptTrc(wb , sheet, model,  rowIdx);
			  /* 상황보고추적*/  
		  } else if ("SttnRptTrc".equals(excelDv)) {
			  getSttnRptTrc(wb , sheet, model,  rowIdx);
		  /* 조치보고추적*/  
		  } else if ("MangtRptTrc".equals(excelDv)) {
				  getMangtRptTrc(wb , sheet, model,  rowIdx);	    
			  /* 사용자접속로그 */	 
		  } else if("EmplyrCnn".equals(excelDv)) {
			  
			  /*
			  List<SysLgMngVO> list = (List<SysLgMngVO>)model.get("excelBodyList");
				 for (SysLgMngVO vo : list) {
					int cellIdx = 1;
				    HSSFRow dataRow = sheet.createRow(rowIdx++);
				    HSSFCell cell1 = dataRow.createCell(cellIdx++);
				    cell1.setCellValue(new HSSFRichTextString(vo.getUserNm()+"("+vo.getUserId()+")"));
//				    cell1.setCellStyle(makeDataStyle(wb));
				    
				    HSSFCell cell2 = dataRow.createCell(cellIdx++);
				    
				    cell2.setCellValue(new HSSFRichTextString(vo.getIpAddr()));
//				    cell2.setCellStyle(makeDataStyle(wb));
				    
				    HSSFCell cell3 = dataRow.createCell(cellIdx++);
				    cell3.setCellValue(new HSSFRichTextString(vo.getLgnDt()));
//				    cell3.setCellStyle(makeDataStyle(wb));

				 }
			  
		  } else if("PrnLg".equals(excelDv)) {
			  List<SysLgPrnVO> list = (List<SysLgPrnVO>)model.get("excelBodyList");
				 for (SysLgPrnVO vo : list) {
					int cellIdx = 1;
				    HSSFRow dataRow = sheet.createRow(rowIdx++);
				    HSSFCell cell1 = dataRow.createCell(cellIdx++);
				    cell1.setCellValue(new HSSFRichTextString(vo.getUserNm()+"("+vo.getUserId()+")"));
//				    cell1.setCellStyle(makeDataStyle(wb));
				    
				    HSSFCell cell2 = dataRow.createCell(cellIdx++);
				    
				    cell2.setCellValue(new HSSFRichTextString(vo.getDocNm()));
//				    cell2.setCellStyle(makeDataStyle(wb));
				    
				    HSSFCell cell3 = dataRow.createCell(cellIdx++);
				    cell3.setCellValue(new HSSFRichTextString(vo.getPrnDt()));
//				    cell3.setCellStyle(makeDataStyle(wb));

				 }
		*/
		  } else if("MnuAcces".equals(excelDv)) {
			  /*
			  List<SysLgMnuVO> list = (List<SysLgMnuVO>)model.get("excelBodyList");
				 for (SysLgMnuVO vo : list) {
					int cellIdx = 1;
				    HSSFRow dataRow = sheet.createRow(rowIdx++);
				    HSSFCell cell1 = dataRow.createCell(cellIdx++);
				    cell1.setCellValue(new HSSFRichTextString(vo.getUserNm()+"("+vo.getUserId()+")"));
//				    cell1.setCellStyle(makeDataStyle(wb));
				    
				    HSSFCell cell2 = dataRow.createCell(cellIdx++);
				    
				    cell2.setCellValue(new HSSFRichTextString(vo.getMnuNm()+"("+vo.getMnuId()+")"));
//				    cell2.setCellStyle(makeDataStyle(wb));
				    
				    HSSFCell cell3 = dataRow.createCell(cellIdx++);
				    cell3.setCellValue(new HSSFRichTextString(vo.getUseDt()));
//				    cell3.setCellStyle(makeDataStyle(wb));

				 }
				 */
		  } else if ( "SttnRptRcptLst".equals(excelDv) ) {	// 상황보고 접수목록
			  getSttnRptRcptLst(wb, sheet, model, rowIdx);
		  } else if("AnytmRptSmrzStats".equals(excelDv)) {	// 수시보고총괄통계
			  getAnytmRptSmrzStats(wb, sheet, model, rowIdx);
		  } else if ("CnctlorgAnytmRptStats".equals(excelDv)) {	// 중통수시보고통계
			  getCnctlorgAnytmRptStats(wb, sheet, model, rowIdx);
		  } else if("XXXX".equals(excelDv)) {
			//추가는 여기에...	
				/* 새로운 엑셀 정보 끝  */
			  
		  }

		  /** 컬럼 사이즈 조정 시작  */
		  Integer[] columSize = (Integer[])model.get("excelColumnSize");
		  
		  if(columSize==null || columSize.length <=0) {
			  for (int i = 1; i < headColumns.length+1 ; i++) {
				  sheet.autoSizeColumn((short)i);
			  }
		  } else {
			  headCellIdx = 0;
			  for (int i = 1; i < headColumns.length+1; i++) {
				  sheet.setColumnWidth((short)i, columSize[headCellIdx++]);
			  }
		  }
		  /** 컬럼 사이즈 조정 끝  */

		 
		/*  파일 다운로드 시작 */
		 excelTitle=URLEncoder.encode(excelTitle,"UTF-8");
		response.setContentType("Application/Msexcel");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + createFileName(excelTitle) + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		/*  파일 다운로드 끝 */
		  
	 }
	
	/**
	 * 엑셀 파일의 이름을 생성한다.
	 * @param excelTitle 엑셀파일 제목
	 * @return 다운로드 엑셀 파일 이름
	 */
	 private String createFileName(String excelTitle) {
		  SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

		  return new StringBuilder().append(excelTitle)
		    .append("_").append(fileFormat.format(new Date())).append(".xls").toString();
	 }
		 
		 
	 /**
		 * Head Column 서식을 생성한다.
		 * @param  wb  HSSFWorkbook
		 * @return CellStyle
		 */	 
	 public CellStyle makeHeadColumnStyle(HSSFWorkbook wb) {
			 
			 HSSFFont headerFont = wb.createFont();
			  headerFont.setBoldweight(headerFont.BOLDWEIGHT_BOLD);
			  headerFont.setFontHeightInPoints((short) 10);
			  
			  CellStyle headerStyle = wb.createCellStyle();
			  headerStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			  headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
			  headerStyle.setFont(headerFont);
			  headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			  return headerStyle;
	 }
	
	 /**
	  * 제목 서식을 생성한다.
	  * @param wb HSSFWorkbook
	  * @return CellStyle
	  */
	 public CellStyle makeTitleStyle(HSSFWorkbook wb) {
			 
		 
			 HSSFFont headerFont = wb.createFont();
			  headerFont.setBoldweight(headerFont.BOLDWEIGHT_BOLD);
			  headerFont.setFontHeightInPoints((short) 12);
		  
			  CellStyle titleStyle = wb.createCellStyle();
			  titleStyle.setFillBackgroundColor(IndexedColors.BROWN.getIndex());
			  titleStyle.setAlignment(titleStyle.ALIGN_CENTER);
			  titleStyle.setFont(headerFont);
			  
			  //titleStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			  return titleStyle;
		 }
		
	 /**
	  * 엑셀 데이타 부분 서식을 생성한다.
	  * @param wb HSSFWorkbook
	  * @return CellStyle
	  */
	 public CellStyle makeDataStyle(HSSFWorkbook wb) {
			 
			 
			 HSSFFont headerFont = wb.createFont();
			 headerFont.setFontHeightInPoints((short) 10);
		  
			  CellStyle titleStyle = wb.createCellStyle();
			  titleStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			  titleStyle.setAlignment(titleStyle.ALIGN_LEFT);
			  titleStyle.setFont(headerFont);
			  
			  titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			  return titleStyle;
	 }		
		
	 /**
	  * 엑셀 데이타 부분 서식을 생성한다.
	  * @param wb HSSFWorkbook
	  * @return CellStyle
	  */
	 public CellStyle makeDataStyleRight(HSSFWorkbook wb) {

		 HSSFFont headerFont = wb.createFont();
		 headerFont.setFontHeightInPoints((short) 10);
  
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		titleStyle.setAlignment(titleStyle.ALIGN_RIGHT);
		titleStyle.setFont(headerFont);
	
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		return titleStyle;
	}
		
	 /**
	  * 엑셀 데이타 부분 서식을 생성한다.
	  * @param wb HSSFWorkbook
	  * @return CellStyle
	  */
	 public CellStyle makeDataStyleCenter(HSSFWorkbook wb) {

		 HSSFFont headerFont = wb.createFont();
		 headerFont.setFontHeightInPoints((short) 10);

		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		titleStyle.setAlignment(titleStyle.ALIGN_CENTER);
		titleStyle.setFont(headerFont);
	
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		return titleStyle;
	}

		 
		 
		  
	 /**
	  * 엑셀 데이타 Formula 서식을 지정한다.
	  * @param wb HSSFWorkbook
	  * @return 
	  */	 
	 public void makeFormlaStyle(HSSFWorkbook wb) {
			 
			  HSSFCellStyle numStyle = wb.createCellStyle();
			  numStyle.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
			  HSSFCellStyle percentStyle = wb.createCellStyle();
			  percentStyle.setDataFormat(wb.createDataFormat().getFormat("0%"));
			  HSSFCellStyle dateStyle = wb.createCellStyle();
			  dateStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy/mm/dd"));
			 // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	 }
	 
	 /**
	  * 일일통제보고 통계 엑셀 정보를 설정한다.
	  * @param wb HSSFWorkbook
	  * @return 
	  */	 
	 public void getCnrlRptStats(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {
		/*
		 List<CnrlRptStatsVO> list = (List<CnrlRptStatsVO>)model.get("excelBodyList");
		 for (CnrlRptStatsVO vo : list) {
			int cellIdx = 1;
		    HSSFRow dataRow = sheet.createRow(rowIdx++);
		    HSSFCell cell1 = dataRow.createCell(cellIdx++);
		    cell1.setCellValue(new HSSFRichTextString(vo.getInstNm()));
		    cell1.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell2 = dataRow.createCell(cellIdx++);
		    
		    cell2.setCellValue(new HSSFRichTextString(DateUtil.changeDateFormat(vo.getStdDt(),"-")));
		    cell2.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell3 = dataRow.createCell(cellIdx++);
		    cell3.setCellValue(new HSSFRichTextString(vo.getTrnsAt()));
		    cell3.setCellStyle(makeDataStyle(wb));

		    HSSFCell cell4 = dataRow.createCell(cellIdx++);
		    cell4.setCellValue(new HSSFRichTextString(vo.getRcptAt()));
		    cell4.setCellStyle(makeDataStyle(wb));
		 }
		 */
	 }
	 
	 /**
	  * 정기보고 통계 엑셀 정보를 설정한다.
	  * @param wb HSSFWorkbook
	  * @return 
	  */
	 public void getFdrmRptStats(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {

		  /*
		  List<FdrmRptStatsVO> list = (List<FdrmRptStatsVO>)model.get("excelBodyList");
		 for (FdrmRptStatsVO vo : list) {
			int cellIdx = 1;
		    HSSFRow dataRow = sheet.createRow(rowIdx++);
		    HSSFCell cell1 = dataRow.createCell(cellIdx++);
		    cell1.setCellValue(new HSSFRichTextString(vo.getInstNm()));
		    cell1.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell2 = dataRow.createCell(cellIdx++);
		    
		    cell2.setCellValue(new HSSFRichTextString(DateUtil.changeDateFormat(vo.getStdDt(),"-")));
		    cell2.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell3 = dataRow.createCell(cellIdx++);
		    
		    cell3.setCellValue(new HSSFRichTextString(vo.getStdTmNm()));
		    cell3.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell4 = dataRow.createCell(cellIdx++);
		    cell4.setCellValue(new HSSFRichTextString(vo.getTrnsAt()));
		    cell4.setCellStyle(makeDataStyle(wb));

		    HSSFCell cell5 = dataRow.createCell(cellIdx++);
		    cell5.setCellValue(new HSSFRichTextString(vo.getRcptAt()));
		    cell5.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell6 = dataRow.createCell(cellIdx++);
		    cell6.setCellValue(new HSSFRichTextString(vo.getLstRcptAt()));
		    cell6.setCellStyle(makeDataStyle(wb));
		 }
		 */
	 }

	 
	 /**
	  * 수시보고 통계 엑셀 정보를 설정한다.
	  * @param wb HSSFWorkbook
	  * @return 
	  */
	 public void getAnytmRptStats(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {

		  /*
		  List<EvtAnytmRptStatsVO> list = (List<EvtAnytmRptStatsVO>)model.get("excelBodyList");
		 for (EvtAnytmRptStatsVO vo : list) {
			int cellIdx = 1;
		    HSSFRow dataRow = sheet.createRow(rowIdx++);
		    HSSFCell cell1 = dataRow.createCell(cellIdx++);
		    cell1.setCellValue(new HSSFRichTextString(vo.getRcvInstCdNm()));
		    cell1.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell2 = dataRow.createCell(cellIdx++);
		    
		    cell2.setCellValue(new HSSFRichTextString(vo.getOccDt()));
		    cell2.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell3 = dataRow.createCell(cellIdx++);
		    cell3.setCellValue(vo.getSttnAllCo());
		    cell3.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell4 = dataRow.createCell(cellIdx++);
		    cell4.setCellValue(vo.getSttnTrnsCo());
		    cell4.setCellStyle(makeDataStyle(wb));

		    HSSFCell cell5 = dataRow.createCell(cellIdx++);
		    cell5.setCellValue(vo.getSttnTmpCo());
		    cell5.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell6 = dataRow.createCell(cellIdx++);
		    cell6.setCellValue(vo.getMangtAllCo());
		    cell6.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell7 = dataRow.createCell(cellIdx++);
		    cell7.setCellValue(vo.getMangtTrnsCo());
		    cell7.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell8 = dataRow.createCell(cellIdx++);
		    cell8.setCellValue(vo.getLess6h());
		    cell8.setCellStyle(makeDataStyle(wb));	
		    
		    HSSFCell cell9 = dataRow.createCell(cellIdx++);
		    cell9.setCellValue(vo.getOver6h());
		    cell9.setCellStyle(makeDataStyle(wb));	

		    HSSFCell cell10 = dataRow.createCell(cellIdx++);
		    cell10.setCellValue(vo.getMangtTmpCo());
		    cell10.setCellStyle(makeDataStyle(wb));	
		 }
		 */
	 } 
	 
	 
	 /**
	  * 반려건수 통계 엑셀 정보를 설정한다.(미완성)
	  * @param wb HSSFWorkbook
	  * @return 
	  */
	 public void getRtnCoStats(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {
/*
		  List<EvtRtnCoStatsVO> list = (List<EvtRtnCoStatsVO>)model.get("excelBodyList");
		 for (EvtRtnCoStatsVO vo : list) {
			 	int cellIdx = 1;
				HSSFRow dataRow = sheet.createRow(rowIdx++);
				HSSFCell cell1 = dataRow.createCell(cellIdx++);
				cell1.setCellValue(new HSSFRichTextString(vo.getInstCdNm()));
				cell1.setCellStyle(makeDataStyle(wb));
				
				HSSFCell cell2 = dataRow.createCell(cellIdx++);
				cell2.setCellValue( vo.getMangtSprdAllCo()  );
				cell2.setCellStyle(makeDataStyle(wb));
				
				
				HSSFCell cell3 = dataRow.createCell(cellIdx++);
				cell3.setCellValue(vo.getMangtSprdRtnCo());
				cell3.setCellStyle(makeDataStyle(wb));
				
				HSSFCell cell4 = dataRow.createCell(cellIdx++);
				cell4.setCellValue(vo.getMangtSprdRtnCo2());
				cell4.setCellStyle(makeDataStyle(wb));

				HSSFCell cell5 = dataRow.createCell(cellIdx++);
				cell5.setCellValue(vo.getPercent());
				cell5.setCellStyle(makeDataStyle(wb));
		 }
		 */
	 } 
	 
	 
	 
	 
	 /**
	  * 사건접수추적 엑셀 정보를 설정한다.
	  * @param wb HSSFWorkbook
	  * @return 
	  */
	 public void getIcdtRcptTrc(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {

		  /*
		  List<EvtIcdtRcptTrcVO> list = (List<EvtIcdtRcptTrcVO>)model.get("excelBodyList");
		 for (EvtIcdtRcptTrcVO vo : list) {
			int cellIdx = 1;
		    HSSFRow dataRow = sheet.createRow(rowIdx++);
		    HSSFCell cell1 = dataRow.createCell(cellIdx++);
		    cell1.setCellValue(new HSSFRichTextString(vo.getAllIcdtNo()));
		    cell1.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell2 = dataRow.createCell(cellIdx++);
		    
		    cell2.setCellValue(new HSSFRichTextString(DateUtil.changeDateFormat(vo.getSj(),"-")));
		    cell2.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell3 = dataRow.createCell(cellIdx++);
		    
		    cell3.setCellValue(new HSSFRichTextString(vo.getOccDt()));
		    cell3.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell4 = dataRow.createCell(cellIdx++);
		    cell4.setCellValue(new HSSFRichTextString(vo.getRcvInstCdNm()));
		    cell4.setCellStyle(makeDataStyle(wb));

		    HSSFCell cell5 = dataRow.createCell(cellIdx++);
		    cell5.setCellValue(new HSSFRichTextString(vo.getRcvDt()));
		    cell5.setCellStyle(makeDataStyle(wb));

		 }
		 */
	 }
	 
	  /* 상황보고추적 엑셀 정보를 설정한다.
	  * @param wb HSSFWorkbook
	  * @return 
	  */
	 public void getSttnRptTrc(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {

		  /*
		  List<EvtIcdtRcptTrcVO> list = (List<EvtIcdtRcptTrcVO>)model.get("excelBodyList");
		 for (EvtIcdtRcptTrcVO vo : list) {
			int cellIdx = 1;
		    HSSFRow dataRow = sheet.createRow(rowIdx++);
		    HSSFCell cell1 = dataRow.createCell(cellIdx++);
		    cell1.setCellValue(new HSSFRichTextString(vo.getAllIcdtNo()));
		    cell1.setCellStyle(makeDataStyle(wb));
		    
		    HSSFCell cell2 = dataRow.createCell(cellIdx++);
		    
		    cell2.setCellValue(new HSSFRichTextString(DateUtil.changeDateFormat(vo.getSj(),"-")));
		    cell2.setCellStyle(makeDataStyle(wb));
		    
		    
		    HSSFCell cell3 = dataRow.createCell(cellIdx++);
		    cell3.setCellValue(new HSSFRichTextString(vo.getRcvInstCdNm()));
		    cell3.setCellStyle(makeDataStyle(wb));

		    HSSFCell cell4 = dataRow.createCell(cellIdx++);
		    cell4.setCellValue(new HSSFRichTextString(vo.getRcvDt()));
		    cell4.setCellStyle(makeDataStyle(wb));
		    
		    // <%-- 상위처리기관, 보고기관, 정부종합상황실 --%>
		    if(StringUtil.isNotEmpty(vo.getRptInstCdNm1())  && 
		    					StringUtil.isNotEmpty(vo.getRptInstCdNm2())  &&
		    									StringUtil.isNotEmpty(vo.getRptInstCdNm3()) ) {
			    HSSFCell cell5 = dataRow.createCell(cellIdx++);
			    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm1()));
			    cell5.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell6 = dataRow.createCell(cellIdx++);
			    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt1()));
			    cell6.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell7 = dataRow.createCell(cellIdx++);
			    cell7.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm2()));
			    cell7.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell8 = dataRow.createCell(cellIdx++);
			    cell8.setCellValue(new HSSFRichTextString(vo.getRptRcptDt2()));
			    cell8.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell9 = dataRow.createCell(cellIdx++);
			    cell9.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm3()));
			    cell9.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell10 = dataRow.createCell(cellIdx++);
			    cell10.setCellValue(new HSSFRichTextString(vo.getRptRcptDt3()));
			    cell10.setCellStyle(makeDataStyle(wb));
			//    <%-- 상위처리기관 X , 보고기관, 정부종합상황실 --%>
		    } else if(StringUtil.isEmpty(vo.getRptInstCdNm1())  && StringUtil.isNotEmpty(vo.getRptInstCdNm2())  && StringUtil.isNotEmpty(vo.getRptInstCdNm3()) ) {
			    HSSFCell cell5 = dataRow.createCell(cellIdx++);
			    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm2()));
			    cell5.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell6 = dataRow.createCell(cellIdx++);
			    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt2()));
			    cell6.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell7 = dataRow.createCell(cellIdx++);
			    cell7.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm3()));
			    cell7.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell8 = dataRow.createCell(cellIdx++);
			    cell8.setCellValue(new HSSFRichTextString(vo.getRptRcptDt3()));
			    cell8.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell9 = dataRow.createCell(cellIdx++);
			    cell9.setCellValue(new HSSFRichTextString(""));
			    cell9.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell10 = dataRow.createCell(cellIdx++);
			    cell10.setCellValue(new HSSFRichTextString(""));
			    cell10.setCellStyle(makeDataStyle(wb));
			 //   <%-- 상위처리기관 X , 보고기관 X , 정부종합상황실 --%>
		    } else if(StringUtil.isEmpty(vo.getRptInstCdNm1())  && StringUtil.isEmpty(vo.getRptInstCdNm2())  && StringUtil.isNotEmpty(vo.getRptInstCdNm3()) ) {
			    HSSFCell cell5 = dataRow.createCell(cellIdx++);
			    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm3()));
			    cell5.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell6 = dataRow.createCell(cellIdx++);
			    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt3()));
			    cell6.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell7 = dataRow.createCell(cellIdx++);
			    cell7.setCellValue(new HSSFRichTextString(""));
			    cell7.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell8 = dataRow.createCell(cellIdx++);
			    cell8.setCellValue(new HSSFRichTextString(""));
			    cell8.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell9 = dataRow.createCell(cellIdx++);
			    cell9.setCellValue(new HSSFRichTextString(""));
			    cell9.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell10 = dataRow.createCell(cellIdx++);
			    cell10.setCellValue(new HSSFRichTextString(""));
			    cell10.setCellStyle(makeDataStyle(wb));
			 //    <%-- 상위처리기관 X , 보고기관  , 정부종합상황실 X --%>
		    } else if(StringUtil.isEmpty(vo.getRptInstCdNm1())  && StringUtil.isEmpty(vo.getRptInstCdNm2())  && StringUtil.isNotEmpty(vo.getRptInstCdNm3()) ) {
			    HSSFCell cell5 = dataRow.createCell(cellIdx++);
			    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm2()));
			    cell5.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell6 = dataRow.createCell(cellIdx++);
			    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt2()));
			    cell6.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell7 = dataRow.createCell(cellIdx++);
			    cell7.setCellValue(new HSSFRichTextString(""));
			    cell7.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell8 = dataRow.createCell(cellIdx++);
			    cell8.setCellValue(new HSSFRichTextString(""));
			    cell8.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell9 = dataRow.createCell(cellIdx++);
			    cell9.setCellValue(new HSSFRichTextString(""));
			    cell9.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell10 = dataRow.createCell(cellIdx++);
			    cell10.setCellValue(new HSSFRichTextString(""));
			    cell10.setCellStyle(makeDataStyle(wb));			    
		    } else {
			    HSSFCell cell5 = dataRow.createCell(cellIdx++);
			    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm2()));
			    cell5.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell6 = dataRow.createCell(cellIdx++);
			    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt2()));
			    cell6.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell7 = dataRow.createCell(cellIdx++);
			    cell7.setCellValue(new HSSFRichTextString(""));
			    cell7.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell8 = dataRow.createCell(cellIdx++);
			    cell8.setCellValue(new HSSFRichTextString(""));
			    cell8.setCellStyle(makeDataStyle(wb));
			    
			    HSSFCell cell9 = dataRow.createCell(cellIdx++);
			    cell9.setCellValue(new HSSFRichTextString(""));
			    cell9.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell10 = dataRow.createCell(cellIdx++);
			    cell10.setCellValue(new HSSFRichTextString(""));
			    cell10.setCellStyle(makeDataStyle(wb));
		    }
		 }
		 */
	 }
		 
		  /* 조치보고추적 엑셀 정보를 설정한다.
		  * @param wb HSSFWorkbook
		  * @return 
		  */
		 public void getMangtRptTrc(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {
/*
			  
			  List<EvtIcdtRcptTrcVO> list = (List<EvtIcdtRcptTrcVO>)model.get("excelBodyList");
			 for (EvtIcdtRcptTrcVO vo : list) {
				int cellIdx = 1;
			    HSSFRow dataRow = sheet.createRow(rowIdx++);
			    HSSFCell cell1 = dataRow.createCell(cellIdx++);
			    cell1.setCellValue(new HSSFRichTextString(vo.getAllIcdtNo()));
			    cell1.setCellStyle(makeDataStyle(wb));
			    
			    HSSFCell cell2 = dataRow.createCell(cellIdx++);
			    
			    cell2.setCellValue(new HSSFRichTextString(DateUtil.changeDateFormat(vo.getSj(),"-")));
			    cell2.setCellStyle(makeDataStyle(wb));
			    
			    
			    HSSFCell cell3 = dataRow.createCell(cellIdx++);
			    cell3.setCellValue(new HSSFRichTextString(vo.getRcvInstCdNm()));
			    cell3.setCellStyle(makeDataStyle(wb));

			    HSSFCell cell4 = dataRow.createCell(cellIdx++);
			    cell4.setCellValue(new HSSFRichTextString(vo.getRcvDt()));
			    cell4.setCellStyle(makeDataStyle(wb));
			    
			    // <%-- 상위처리기관, 보고기관, 정부종합상황실 --%>
			    if(StringUtil.isNotEmpty(vo.getRptInstCdNm1())  && 
			    					StringUtil.isNotEmpty(vo.getRptInstCdNm2())  &&
			    									StringUtil.isNotEmpty(vo.getRptInstCdNm3()) ) {
				    HSSFCell cell5 = dataRow.createCell(cellIdx++);
				    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm1()));
				    cell5.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell6 = dataRow.createCell(cellIdx++);
				    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt1()));
				    cell6.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell7 = dataRow.createCell(cellIdx++);
				    cell7.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm2()));
				    cell7.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell8 = dataRow.createCell(cellIdx++);
				    cell8.setCellValue(new HSSFRichTextString(vo.getRptRcptDt2()));
				    cell8.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell9 = dataRow.createCell(cellIdx++);
				    cell9.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm3()));
				    cell9.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell10 = dataRow.createCell(cellIdx++);
				    cell10.setCellValue(new HSSFRichTextString(vo.getRptRcptDt3()));
				    cell10.setCellStyle(makeDataStyle(wb));
				//    <%-- 상위처리기관 X , 보고기관, 정부종합상황실 --%>
			    } else if(StringUtil.isEmpty(vo.getRptInstCdNm1())  && StringUtil.isNotEmpty(vo.getRptInstCdNm2())  && StringUtil.isNotEmpty(vo.getRptInstCdNm3()) ) {
				    HSSFCell cell5 = dataRow.createCell(cellIdx++);
				    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm2()));
				    cell5.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell6 = dataRow.createCell(cellIdx++);
				    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt2()));
				    cell6.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell7 = dataRow.createCell(cellIdx++);
				    cell7.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm3()));
				    cell7.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell8 = dataRow.createCell(cellIdx++);
				    cell8.setCellValue(new HSSFRichTextString(vo.getRptRcptDt3()));
				    cell8.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell9 = dataRow.createCell(cellIdx++);
				    cell9.setCellValue(new HSSFRichTextString(""));
				    cell9.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell10 = dataRow.createCell(cellIdx++);
				    cell10.setCellValue(new HSSFRichTextString(""));
				    cell10.setCellStyle(makeDataStyle(wb));
				 //   <%-- 상위처리기관 X , 보고기관 X , 정부종합상황실 --%>
			    } else if(StringUtil.isEmpty(vo.getRptInstCdNm1())  && StringUtil.isEmpty(vo.getRptInstCdNm2())  && StringUtil.isNotEmpty(vo.getRptInstCdNm3()) ) {
				    HSSFCell cell5 = dataRow.createCell(cellIdx++);
				    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm3()));
				    cell5.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell6 = dataRow.createCell(cellIdx++);
				    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt3()));
				    cell6.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell7 = dataRow.createCell(cellIdx++);
				    cell7.setCellValue(new HSSFRichTextString(""));
				    cell7.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell8 = dataRow.createCell(cellIdx++);
				    cell8.setCellValue(new HSSFRichTextString(""));
				    cell8.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell9 = dataRow.createCell(cellIdx++);
				    cell9.setCellValue(new HSSFRichTextString(""));
				    cell9.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell10 = dataRow.createCell(cellIdx++);
				    cell10.setCellValue(new HSSFRichTextString(""));
				    cell10.setCellStyle(makeDataStyle(wb));
				 //    <%-- 상위처리기관 X , 보고기관  , 정부종합상황실 X --%>
			    } else if(StringUtil.isEmpty(vo.getRptInstCdNm1())  && StringUtil.isEmpty(vo.getRptInstCdNm2())  && StringUtil.isNotEmpty(vo.getRptInstCdNm3()) ) {
				    HSSFCell cell5 = dataRow.createCell(cellIdx++);
				    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm2()));
				    cell5.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell6 = dataRow.createCell(cellIdx++);
				    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt2()));
				    cell6.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell7 = dataRow.createCell(cellIdx++);
				    cell7.setCellValue(new HSSFRichTextString(""));
				    cell7.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell8 = dataRow.createCell(cellIdx++);
				    cell8.setCellValue(new HSSFRichTextString(""));
				    cell8.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell9 = dataRow.createCell(cellIdx++);
				    cell9.setCellValue(new HSSFRichTextString(""));
				    cell9.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell10 = dataRow.createCell(cellIdx++);
				    cell10.setCellValue(new HSSFRichTextString(""));
				    cell10.setCellStyle(makeDataStyle(wb));			    
			    } else {
				    HSSFCell cell5 = dataRow.createCell(cellIdx++);
				    cell5.setCellValue(new HSSFRichTextString(vo.getRptInstCdNm2()));
				    cell5.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell6 = dataRow.createCell(cellIdx++);
				    cell6.setCellValue(new HSSFRichTextString(vo.getRptRcptDt2()));
				    cell6.setCellStyle(makeDataStyle(wb));
				    
				    
				    HSSFCell cell7 = dataRow.createCell(cellIdx++);
				    cell7.setCellValue(new HSSFRichTextString(""));
				    cell7.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell8 = dataRow.createCell(cellIdx++);
				    cell8.setCellValue(new HSSFRichTextString(""));
				    cell8.setCellStyle(makeDataStyle(wb));
				    
				    HSSFCell cell9 = dataRow.createCell(cellIdx++);
				    cell9.setCellValue(new HSSFRichTextString(""));
				    cell9.setCellStyle(makeDataStyle(wb));

				    HSSFCell cell10 = dataRow.createCell(cellIdx++);
				    cell10.setCellValue(new HSSFRichTextString(""));
				    cell10.setCellStyle(makeDataStyle(wb));
			    }
			 }  //end of for...
			 */
		 
	 } // end of getMangtRptTrc
	  

	/* 상황보고접수목록 엑셀 정보를 설정한다.
	 * @param wb HSSFWorkbook
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public void getSttnRptRcptLst(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {
		
		/*
		List<SmsSttnMngVO> list = (List<SmsSttnMngVO>) model.get("excelBodyList");
		for (SmsSttnMngVO vo : list) {
			int cellIdx = 1;
			HSSFRow dataRow = sheet.createRow(rowIdx++);

			HSSFCell cell1 = dataRow.createCell(cellIdx++);
			cell1.setCellValue(new HSSFRichTextString(CmmCdUtil.getCodeNameByCode("07", vo.getIcdtKndCd())));
			cell1.setCellStyle(makeDataStyle(wb));

			HSSFCell cell2 = dataRow.createCell(cellIdx++);
			cell2.setCellValue(new HSSFRichTextString(CmmCdUtil.getCodeNameByCode("08", vo.getIcdtDvCd())));
			cell2.setCellStyle(makeDataStyle(wb));

			HSSFCell cell3 = dataRow.createCell(cellIdx++);
			cell3.setCellValue(new HSSFRichTextString(vo.getIcdtNo()));
			cell3.setCellStyle(makeDataStyle(wb));

			HSSFCell cell4 = dataRow.createCell(cellIdx++);
			cell4.setCellValue(new HSSFRichTextString(vo.getSj()));
			cell4.setCellStyle(makeDataStyle(wb));

			HSSFCell cell5 = dataRow.createCell(cellIdx++);
			cell5.setCellValue(new HSSFRichTextString(vo.getFstInstNm()));
			cell5.setCellStyle(makeDataStyle(wb));

			HSSFCell cell6 = dataRow.createCell(cellIdx++);
			cell6.setCellValue(new HSSFRichTextString(vo.getTrnsInstNm()));
			cell6.setCellStyle(makeDataStyle(wb));

			HSSFCell cell7 = dataRow.createCell(cellIdx++);
			cell7.setCellValue(new HSSFRichTextString(vo.getTrnsDt()));
			cell7.setCellStyle(makeDataStyle(wb));

			HSSFCell cell8 = dataRow.createCell(cellIdx++);
			cell8.setCellValue(new HSSFRichTextString(vo.getRcvDt()));
			cell8.setCellStyle(makeDataStyle(wb));

			HSSFCell cell9 = dataRow.createCell(cellIdx++);
			cell9.setCellValue(new HSSFRichTextString(CmmCdUtil.getCodeNameByCode("17", vo.getSprdStsCd())));
			cell9.setCellStyle(makeDataStyle(wb));
		}
		*/
	}

	/* 수시보고총괄통계 엑셀 정보를 설정한다.
	 * @param wb HSSFWorkbook
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public void getAnytmRptSmrzStats(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {
		
		
		
/*		List<AnytmRptSmrzSttsVO> list = (List<AnytmRptSmrzSttsVO>) model.get("excelBodyList");
		for (AnytmRptSmrzSttsVO vo : list) {
			int cellIdx = 1;
			HSSFRow dataRow = sheet.createRow(rowIdx++);

			HSSFCell cell1 = dataRow.createCell(cellIdx++);
			cell1.setCellValue(new HSSFRichTextString(vo.getInstClsNm()));
			cell1.setCellStyle(makeDataStyle(wb));

			HSSFCell cell2 = dataRow.createCell(cellIdx++);
			cell2.setCellValue(vo.getsCnt1());
			cell2.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell3 = dataRow.createCell(cellIdx++);
			cell3.setCellValue(vo.getsCnt2());
			cell3.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell4 = dataRow.createCell(cellIdx++);
			cell4.setCellValue(vo.getsCnt3());
			cell4.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell5 = dataRow.createCell(cellIdx++);
			cell5.setCellValue(vo.getjCnt1());
			cell5.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell6 = dataRow.createCell(cellIdx++);
			cell6.setCellValue(vo.getjCnt2());
			cell6.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell7 = dataRow.createCell(cellIdx++);
			cell7.setCellValue(vo.getjCnt3());
			cell7.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell8 = dataRow.createCell(cellIdx++);
			cell8.setCellValue(vo.getjCnt4());
			cell8.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell9 = dataRow.createCell(cellIdx++);
			cell9.setCellValue(vo.getjCnt5());
			cell9.setCellStyle(makeDataStyleRight(wb));
		}
		
		
		
		
		
		List<EvtAnytmRptStatsVO> list = (List<EvtAnytmRptStatsVO>) model.get("excelBodyList");
		for (EvtAnytmRptStatsVO vo : list) {
			int cellIdx = 1;
			HSSFRow dataRow = sheet.createRow(rowIdx++);

			HSSFCell cell1 = dataRow.createCell(cellIdx++);
			cell1.setCellValue(new HSSFRichTextString(vo.getInstClsCdNm()));
			cell1.setCellStyle(makeDataStyle(wb));

			HSSFCell cell2 = dataRow.createCell(cellIdx++);
			cell2.setCellValue(vo.getSttnAllCo());
			cell2.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell3 = dataRow.createCell(cellIdx++);
			cell3.setCellValue(vo.getSttnTrnsCo());
			cell3.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell4 = dataRow.createCell(cellIdx++);
			cell4.setCellValue(vo.getSttnTmpCo());
			cell4.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell5 = dataRow.createCell(cellIdx++);
			cell5.setCellValue(vo.getMangtAllCo());
			cell5.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell6 = dataRow.createCell(cellIdx++);
			cell6.setCellValue(vo.getMangtTrnsCo());
			cell6.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell7 = dataRow.createCell(cellIdx++);
			cell7.setCellValue(vo.getLess6h());
			cell7.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell8 = dataRow.createCell(cellIdx++);
			cell8.setCellValue(vo.getOver6h());
			cell8.setCellStyle(makeDataStyleRight(wb));

			HSSFCell cell9 = dataRow.createCell(cellIdx++);
			cell9.setCellValue(vo.getMangtTmpCo());
			cell9.setCellStyle(makeDataStyleRight(wb));
		}
		*/
	}

	/* 중통수시보고통계 엑셀 정보를 설정한다.
	 * @param wb HSSFWorkbook
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public void getCnctlorgAnytmRptStats(HSSFWorkbook wb ,HSSFSheet sheet, Map<String, Object> model, int rowIdx) {
		
		/*
		List<CnctlorgAnytmRptStatsVO> list = (List<CnctlorgAnytmRptStatsVO>) model.get("excelBodyList");
		for (CnctlorgAnytmRptStatsVO vo : list) {
			int cellIdx = 1;
			HSSFRow dataRow = sheet.createRow(rowIdx++);

			HSSFCell cell1 = dataRow.createCell(cellIdx++);
			cell1.setCellValue(new HSSFRichTextString(vo.getIcdtNo()));
			cell1.setCellStyle(makeDataStyle(wb));

			HSSFCell cell2 = dataRow.createCell(cellIdx++);
			cell2.setCellValue(new HSSFRichTextString(vo.getRptInstNm()));
			cell2.setCellStyle(makeDataStyle(wb));

			HSSFCell cell3 = dataRow.createCell(cellIdx++);
			cell3.setCellValue(vo.getRptCnt1());
			cell3.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell4 = dataRow.createCell(cellIdx++);
			cell4.setCellValue(vo.getRptCnt2());
			cell4.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell5 = dataRow.createCell(cellIdx++);
			cell5.setCellValue(vo.getRptCnt3());
			cell5.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell6 = dataRow.createCell(cellIdx++);
			cell6.setCellValue(new HSSFRichTextString(vo.getRelateInstNm()));
			cell6.setCellStyle(makeDataStyle(wb));

			HSSFCell cell7 = dataRow.createCell(cellIdx++);
			cell7.setCellValue(vo.getRelateCnt1());
			cell7.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell8 = dataRow.createCell(cellIdx++);
			cell8.setCellValue(vo.getRelateCnt2());
			cell8.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell9 = dataRow.createCell(cellIdx++);
			cell9.setCellValue(vo.getRelateCnt3());
			cell9.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell10 = dataRow.createCell(cellIdx++);
			cell10.setCellValue(new HSSFRichTextString(vo.getPrcsInstNm()));
			cell10.setCellStyle(makeDataStyle(wb));

			HSSFCell cell11 = dataRow.createCell(cellIdx++);
			cell11.setCellValue(vo.getPrcsCnt1());
			cell11.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell12 = dataRow.createCell(cellIdx++);
			cell12.setCellValue(vo.getPrcsCnt2());
			cell12.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell13 = dataRow.createCell(cellIdx++);
			cell13.setCellValue(vo.getPrcsCnt3());
			cell13.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell14 = dataRow.createCell(cellIdx++);
			cell14.setCellValue(vo.getRtnCnt1());
			cell14.setCellStyle(makeDataStyleCenter(wb));

			HSSFCell cell15 = dataRow.createCell(cellIdx++);
			cell15.setCellValue(vo.getRtnCnt2());
			cell15.setCellStyle(makeDataStyleCenter(wb));
		}
		*/
	}
}
