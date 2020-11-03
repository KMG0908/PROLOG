package com.spring.prolog.back;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.spring.prolog.dao.DAO;

public class SessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		HttpSession session = se.getSession();
		System.out.println(session.getId());
		session.setMaxInactiveInterval(60 * 20);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		HttpSession session = se.getSession();
		String id = (String)session.getAttribute("id");
		if(id != null) {
			DAO dao = new DAO();
			dao.deleteTable(id);
			System.out.println(id + " 세션 만료");
		}
	}

}
