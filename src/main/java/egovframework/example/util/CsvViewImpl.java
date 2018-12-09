package egovframework.example.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.supercsv.io.ICsvBeanWriter;

public class CsvViewImpl extends AbstractCsvView {

	@Override
	protected void buildCsvDocument(ICsvBeanWriter csvWriter,
			Map<String, Object> model) throws IOException {

		List<Book> listBooks = (List<Book>) model.get("csvData");
		String[] header = (String[]) model.get("csvHeader");

		csvWriter.writeHeader(header);

		for (Book aBook : listBooks) {
			csvWriter.write(aBook, header);
		}
	}
}
