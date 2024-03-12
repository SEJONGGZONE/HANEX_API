package com.gzonesoft.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;

public class PdfUtil {

	public static void htmlToPdf(String strHtml, String strFontPath, File saveFile) throws IOException {
	    //ConverterProperties : htmlconverter의 property 설정(한글폰트 적용)
		String strFont = strFontPath + File.separatorChar + "malgun.ttf";
	    ConverterProperties properties = new ConverterProperties();
	    FontProvider fontProvider = new DefaultFontProvider(false, false, false);
	    FontProgram fontProgram = FontProgramFactory.createFont(strFont);
	    fontProvider.addFont(fontProgram);
	    properties.setFontProvider(fontProvider);

		//pdf 페이지 크기를 조정
		List<IElement> elements = HtmlConverter.convertToElements(strHtml, properties);
	    PdfDocument pdf = new PdfDocument(new PdfWriter(saveFile));
	    Document document = new Document(pdf); //크기 조정 가능
	    	//setMargins 매개변수순서 : 상, 우, 하, 좌
	        document.setMargins(10, 10, 10, 10);
	        for (IElement element : elements) {
	            document.add((IBlockElement) element);
	        }
	        document.close();
	}	
}
