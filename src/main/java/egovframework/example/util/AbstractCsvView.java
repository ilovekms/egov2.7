package egovframework.example.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public abstract class AbstractCsvView extends AbstractView {

/*	@Override
	protected void renderMergedOutputModel(Map<String, Object> arg0,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		// TODO Auto-generated method stub
		
	}*/
	
	private String fileName;

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	protected void prepareResponse(HttpServletRequest request,
			HttpServletResponse response) {
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				fileName);
		response.setContentType("text/csv");
		response.setHeader(headerKey, headerValue);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);

		buildCsvDocument(csvWriter, model);
		csvWriter.close();
	}

	/**
	 * The concrete view must implement this method. 
	 */
	protected abstract void buildCsvDocument(ICsvBeanWriter csvWriter,
			Map<String, Object> model) throws IOException;	

}
