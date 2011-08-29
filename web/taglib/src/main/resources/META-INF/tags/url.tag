<%-- 
    Document   : url.tag
    Created on : 10.11.2010, 11:56:35
    Author     : nico.rehwaldt
--%>
<%@tag description="Produces an url relative to the current web resource (does not use absolute path as c:url does)"
       pageEncoding="UTF-8"%>

<%@tag import="de.nixis.commons.web.taglib.core.TagUtil" %>
<%@tag import="javax.servlet.jsp.PageContext" %>
<%@tag trimDirectiveWhitespaces="true" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute required="true" type="java.lang.String" name="value" %>
<%@attribute required="false" type="java.lang.String" name="var" %>

<%
    String url = (String) jspContext.getAttribute("value");
    String var = (String) jspContext.getAttribute("var");
    
    String rewrittenUrl = TagUtil.rewriteURL(request, response, url);
    
    if (var != null) {
        jspContext.setAttribute(var, rewrittenUrl, PageContext.REQUEST_SCOPE);
    } else {
        jspContext.getOut().print(rewrittenUrl);
    }
%>