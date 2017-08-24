package com.salesmanager.shop.configuration.marketing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@Configuration
@EntityScan(basePackages = {"com.salesmanager.marketing.model"})
@ComponentScan({"com.salesmanager.shop.marketing.controller","com.salesmanager.business.marketing.services"})
@EnableAutoConfiguration
@ImportResource("classpath:/spring/marketing-shop-context.xml")
@PropertySource("classpath:bundles/marketing.properties")
@EnableWebSecurity
@Component
public class MarketingConfiguration extends WebMvcConfigurerAdapter{
	private String resourcePathPatternJSP;
	private String resourcePathPatternJS;
	private String resourcePathPatternCSS;
	private String resourcePathPatternProperties;
    private String jarFile;
    private String destination;
    private String destinationProperties;
    File destinationFolder;
    @Inject
    private ServletContext servletContext;
    

	public MarketingConfiguration() {
		super();
		this.resourcePathPatternJSP = ".jsp";
		this.resourcePathPatternJS = ".js";
		this.resourcePathPatternCSS = ".css";
		this.resourcePathPatternProperties = "marketing.properties";
        this.jarFile = "sm-market-0.0.1-SNAPSHOT.jar";
        this.destination = "pages/admin/marketing";
        this.destinationProperties = "/resources/bundles";
	
	}
	
	
    /** 
     * Extracts the resource files found in the specified jar file into the destination path
     * 
     * @throws IOException
     *             If an IO error occurs when reading the jar file
     * @throws FileNotFoundException
     *             If the jar file cannot be found
     */
    @EventListener
    public void extractFiles(ContextRefreshedEvent event) throws IOException {
        try {
        	
            String path = servletContext.getRealPath("/WEB-INF/lib/" + jarFile);
            JarFile jarFile = new JarFile(path);
            Path realPath = null;
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (resourcePathPatternJSP.equals(entry.getName().substring(entry.getName().length() - 4)) || 
                		resourcePathPatternJS.equals(entry.getName().substring(entry.getName().length() - 3)) || 
                		resourcePathPatternCSS.equals(entry.getName().substring(entry.getName().length() - 4)) || 
                		(entry.getName().length() > 19 && resourcePathPatternProperties.equals(entry.getName().substring(entry.getName().length() - 20))) ){
                	if(resourcePathPatternJS.equals(entry.getName().substring(entry.getName().length() - 3)))
                	{
                		destination = destination.replace("pages", "resources/js");
                		realPath = Paths.get(servletContext.getRealPath(destination));
                	}
                	else if(resourcePathPatternCSS.equals(entry.getName().substring(entry.getName().length() - 4)))
                	{
                		destination = destination.replace("js", "css");
                		realPath = Paths.get(servletContext.getRealPath(destination));
                	}
                	else if(entry.getName().length() > 19 && resourcePathPatternProperties.equals(entry.getName().substring(entry.getName().length() - 20)))
                	{
                		realPath = Paths.get(servletContext.getRealPath(destinationProperties).replace("/webapp", ""));
                	}
                	else if(resourcePathPatternJSP.equals(entry.getName().substring(entry.getName().length() - 4)))
                	{
                		destination = "pages/admin/marketing";
                		realPath = Paths.get(servletContext.getRealPath(destination));
                	}
                	
                		
                	try {
                	    Files.createDirectories(realPath);
                	} catch (IOException e) {
                	    System.err.println("Cannot create directories - " + e);
                	}
                	if(entry.getName().length() > 19 && resourcePathPatternProperties.equals(entry.getName().substring(entry.getName().length() - 20)))
                	{
                		destinationFolder = new File(servletContext.getRealPath(destinationProperties).replace("/webapp", ""));
                	}
                	else
                	{
                		destinationFolder = new File(servletContext.getRealPath(destination));
                	}
                	
                	
                	
                    InputStream inputStream = jarFile.getInputStream(entry);
                    File materializedJsp = new File(destinationFolder, entry.getName().substring(entry.getName().lastIndexOf("/"), entry.getName().length()));
                    FileOutputStream outputStream = new FileOutputStream(materializedJsp);
                    copyAndClose(inputStream, outputStream);
                }
            }

        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Cannot find jar file in libs: " + jarFile);
        }
        catch (IOException e) {
            throw new IOException("IOException while moving resources.", e);
        }
    }


    public static int IO_BUFFER_SIZE = 8192;

    private static void copyAndClose(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] b = new byte[IO_BUFFER_SIZE];
            int read;
            while ((read = in.read(b)) != -1) {
                out.write(b, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }
    
    
	
}

