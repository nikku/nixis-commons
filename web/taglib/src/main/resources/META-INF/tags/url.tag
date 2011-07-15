<%-- 
    Document   : url.tag
    Created on : 10.11.2010, 11:56:35
    Author     : nico.rehwaldt
--%>
<%@tag description="Produces an url relative to the current web resource (does not use absolute path as c:url does)"
       pageEncoding="UTF-8"%>

<%@tag import="de.nixis.commons.web.taglib.core.TagUtil"%>

<%@tag trimDirectiveWhitespaces="true" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute required="true" type="java.lang.String" name="value" %>

<%
    String url = (String) jspContext.getAttribute("value");
    jspContext.setAttribute("rewrittenUrl", TagUtil.rewriteURL(request, response, url));
%>

${rewrittenUrl}