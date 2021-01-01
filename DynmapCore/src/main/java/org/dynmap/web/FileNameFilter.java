package org.dynmap.web;

import org.dynmap.DynmapCore;
import org.dynmap.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileNameFilter implements Filter {
    private DynmapCore core;

    public FileNameFilter(DynmapCore core) {
        this.core = core;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse)response;
        String path = request.getServletContext().getContextPath();
        // Filter unneeded file requests
        if (path.toLowerCase().endsWith(".php")) {
            resp.sendError(404);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() { }
}
