package sv.edu.udb.www.cuponera.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

@Service
public class SalesService {
	
	@Autowired
	private SalesRepository salesRepository;
	@Autowired
	ServletContext context; 
	@PersistenceContext
    private EntityManager entityManager;
	@Autowired
	DataSource dataSource;
	@Autowired
	ResourceLoader resourceLoader;

	private List<Promotions> promotions = new ArrayList<Promotions>();
		
	public Page<Promotions> findPaginated(Pageable pageable){	
		
		promotions = salesRepository.listPromotionsAvailable();
		
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Promotions> list;
		
		if(promotions.size() < startItem) {
			list = Collections.emptyList();
		}else {
			int toIndex = Math.min(startItem + pageSize, promotions.size());
			list = promotions.subList(startItem, toIndex);
		}
		
		Page<Promotions> promoPage = new PageImpl<Promotions>(list, PageRequest.of(currentPage, pageSize),promotions.size());
		return promoPage;
		
	}
	
	public String count(String id) 
	{
		
		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("count_sales");
		
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		
		query.setParameter(1, id);
	
		query.execute();
		
		return (query.getResultList().size() > 0)? query.getResultList().get(0).toString() : "0";
	}
	
	public Boolean insertSales(Sales sale) {

        //"login" this is the name of your procedure
        StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("insert_sales"); 

        //Declare the parameters in the same order
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.OUT);

        //Pass the parameter values
        query.setParameter(1, sale.getCouponCode());
        query.setParameter(2, sale.getPromotion().getId());
        query.setParameter(3, sale.getClient().getId());
        query.setParameter(4, sale.getState().getId());

        //Execute query
        query.execute();

        //Get output parameters
        Integer rowCount = (Integer) query.getOutputParameterValue(5);

        return rowCount > 0; //enter your condition
    }
	
	public void generarPdf(List<String> cuponCodes)  {
        String tipo = "pdf";
        
        try {
        	
        	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    		
    		ServletOutputStream out = attr.getResponse().getOutputStream();
        	
            Connection conexion = this.dataSource.getConnection();
            
            String codes = "";
            boolean first = true;
            
            for (String val : cuponCodes)
            {
            	codes += (first)? "'"+val+"'": ",'"+val+"'";
                first = false;
            }
            
            /*
            ClassPathResource reportResource = new ClassPathResource("static/jasper/FacturaCupones.jasper");
            
            InputStream employeeReportStream = getClass().getResourceAsStream();
            JasperReport reporte = JasperCompileManager.compileReport(employeeReportStream);
            */
            
            final InputStream reportInputStream = getClass().getResourceAsStream("/jasper/FacturaCupones.jrxml");
            final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);

            JasperReport reporte = (JasperReport) JasperCompileManager.compileReport(jasperDesign);
            
            Map<String, Object> parameters = new HashMap();
            parameters.put("codes", codes);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,parameters, conexion);
            JRExporter exporter = null;

            if (tipo.equals("pdf")) {
                attr.getResponse().setContentType("application/pdf");
                exporter = new JRPdfExporter();
            } else {
            	attr.getResponse().setContentType("text/html;charset=UTF-8");
                exporter = new JRHtmlExporter();
            }
            
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,this.context.getContextPath() + "/jasperImage?rnd=" + Math.random() + "&image=");

            exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);

            attr.getRequest().getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
            
            conexion.close();
            exporter.exportReport();
            
            out.close();
        }
        catch (Exception e)
        {
        	System.out.println("Error JASPER: " + e.getMessage());
	        e.printStackTrace();
        }
    }
	
	 public JasperPrint exportPdfFile(List<String> cuponCodes) throws Exception {
		  Connection conn = this.dataSource.getConnection();

		  String path = resourceLoader.getResource("classpath:jasper/FacturaCupones.jrxml").getURI().getPath();

		  JasperReport jasperReport = JasperCompileManager.compileReport(path);

		  // Parameters for report
		  Map<String, Object> parameters = new HashMap<String, Object>();

		  String codes = "";
          boolean first = true;
          
          for (String val : cuponCodes)
          {
        	  codes += (first)? "'"+val+"'": ",'"+val+"'";
              first = false;
          }
		  
          parameters.put("codes", codes);
          
		  JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);

		  return print;
	 }
}
