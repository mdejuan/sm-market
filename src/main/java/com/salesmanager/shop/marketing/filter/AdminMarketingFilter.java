package com.salesmanager.shop.marketing.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.utils.CacheUtils;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.filter.AdminFilter;

public class AdminMarketingFilter extends HandlerInterceptorAdapter{
private static final Logger LOGGER = LoggerFactory.getLogger(AdminFilter.class);
	
	@Inject
	private CacheUtils cache;
	
	
	public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		@SuppressWarnings("unchecked")
		Map<String,Menu> menus = (Map<String,Menu>) cache.getFromCache("MENUMAP");
		Object updated = cache.getFromCache("promoUpdatedMenu");

		if(menus!=null && updated == null) {
			InputStream in = null;
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			try {
				in =
					(InputStream) this.getClass().getClassLoader().getResourceAsStream("admin/marketingMenu.json");

				Map<String,Object> data = mapper.readValue(in, Map.class);
	
				List objects = (List)data.get("menus");
				for(Object object : objects) {
					Menu m = getMenu(object);
					menus.put(m.getCode(),m);
				}

				cache.putInCache(menus,"MENUMAP");
				cache.putInCache(true, "promoUpdatedMenu");
				

			} catch (JsonParseException e) {
				LOGGER.error("Error while updating menu", e);
			} catch (JsonMappingException e) {
				LOGGER.error("Error while updating menu", e);
			} catch (IOException e) {
				LOGGER.error("Error while updating menu", e);
			} finally {
				if(in !=null) {
					try {
						in.close();
					} catch (Exception ignore) {
						// TODO: handle exception
					}
				}
			}
			List<Menu> list = new ArrayList<Menu>(menus.values());

			request.setAttribute("MENULIST", list);

			
			
			request.setAttribute("MENUMAP", menus);
			response.setCharacterEncoding("UTF-8");
			
		} 
				
		
		return true;
	}
	
	
	private Menu getMenu(Object object) {
		
		Map o = (Map)object;
		Map menu = (Map)o.get("menu");
		
		Menu m = new Menu();
		m.setCode((String)menu.get("code"));
		
		
		m.setUrl((String)menu.get("url"));
		m.setIcon((String)menu.get("icon"));
		m.setRole((String)menu.get("role"));
		
		List menus = (List)menu.get("menus");
		if(menus!=null) {
			for(Object oo : menus) {
				
				Menu mm = getMenu(oo);
				m.getMenus().add(mm);
			}
			
		}
		
		return m;
		
	}

}
