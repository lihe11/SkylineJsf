package skyline.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.platform.form.config.SystemAttributeNames;
import pub.platform.security.OperatorManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by zhanrui on 2015/6/11.
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"*.xhtml"})
public class LoginFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession session = req.getSession(false);

            String uri = req.getRequestURI();
            //logger.info("uri:[" + uri + "]");

            if (uri.contains("/login.xhtml")
                    || session == null
                    || uri.contains("/pages/")
                    || uri.contains("javax.faces.resource")) {
                chain.doFilter(request, response);
            } else {
                OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                if (om != null && om.isLogin()) {
                    chain.doFilter(request, response);
                } else
                    resp.sendRedirect(req.getContextPath() + "/login.xhtml");
            }
        } catch (Exception e) {
            logger.error("»œ÷§¥ÌŒÛ", e);
        }
    }

    @Override
    public void destroy() {

    }
}
