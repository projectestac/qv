<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="qti.xsl"/>
 <xsl:import href="generic.xsl"/>	

 <!--xsl:import href="Q:/dev/qv/qv_viewer/web/dist/skins/common/qti.xsl"/>
 <xsl:import href="Q:/dev/qv/qv_viewer/web/dist/skins/generic/generic.xsl"/-->	
 <!--xsl:import href="/oracle/product/ias10/10.1.2/j2ee/e13_qv/applications/e13_qv_editor/e13_qv_editor/dist/skins/common/qti.xsl"/>
 <xsl:import href="/oracle/product/ias10/10.1.2/j2ee/e13_qv/applications/e13_qv_editor/e13_qv_editor/dist/skins/generic/generic.xsl"/-->	

 
 <xsl:variable name="specific_images_path">css/<xsl:value-of select="$skin_name"/>/image/</xsl:variable>
 <xsl:variable name="specific_css_path">css/<xsl:value-of select="$skin_name"/></xsl:variable>
	
 <xsl:template name="get_section_path">
	 javascript:gotoSectionRandomizable('<xsl:value-of select="@num"/>',<xsl:value-of select="$section_max_number"/>,0,<xsl:call-template name="getRandomizableSectionsInItem"></xsl:call-template>);<!--Albert -->
 </xsl:template>	
	
	
  <xsl:template name="go_section">
    <xsl:param name="section_number_to_go"/>
    <xsl:param name="diff"/><!--Albert -->
    <xsl:param name="randomizable"/><!--Albert -->
    <xsl:variable name="section_to_go">
    	<xsl:choose>
    		<xsl:when test="$section_number_to_go>$section_max_number">0</xsl:when>
    		<xsl:otherwise><xsl:value-of select="$section_number_to_go"/></xsl:otherwise>
    	</xsl:choose>
    </xsl:variable>
    <xsl:variable name="url">if (!isSubmitted()) {gotoSectionRandomizable('<xsl:value-of select="normalize-space($section_to_go)"/>',<xsl:value-of select="$section_max_number"/>,<xsl:value-of select="normalize-space($diff)"/>,<xsl:value-of select="normalize-space($randomizable)"/>);}</xsl:variable>
    <xsl:value-of select="normalize-space($url)"/>
  </xsl:template>
  
 <!-- xsl:template name="get_section_path">
	 javascript:sendWithQueryParams('section_<xsl:value-of select="@num"/>.htm');
 </xsl:template>	
 
  <xsl:template name="go_section">
    <xsl:param name="section_number_to_go"/>
	<xsl:variable name="page">
		<xsl:choose>
			<xsl:when test="$section_number_to_go=0">index.htm</xsl:when>
			<xsl:when test="$section_number_to_go>$section_max_number">index.htm</xsl:when>
			<xsl:otherwise>section_<xsl:value-of select="$section_number_to_go"/>.htm</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
    <xsl:variable name="url">if (!isSubmitted()) {sendWithQueryParams('<xsl:value-of select="normalize-space($page)"/>');}</xsl:variable>
    <xsl:value-of select="normalize-space($url)"/>
  </xsl:template-->  
	
  <xsl:template name="get_url_base">/../../</xsl:template>
	
 <xsl:template name="put_specific_scripts">
    <SCRIPT type="text/javascript">
      includeScript('qv_local.js');
      includeScript('qv_messages_<xsl:value-of select="$lang"/>.js');
      includeScript('prototype.js');
	  includeScript('dhtmlwindow.js');
    </SCRIPT>
    <SCRIPT type="text/javascript">
	    <xsl:attribute name="src"><xsl:value-of select="$specific_scripts_path"/>/<xsl:value-of select="$skin_name"/>.js</xsl:attribute>
    </SCRIPT>
    
    <!-- SCRIPT type="text/javascript"><xsl:attribute name="src"><xsl:call-template name="get_scripts_path"/>/qv_local.js</xsl:attribute></SCRIPT>
    <SCRIPT type="text/javascript"><xsl:attribute name="src"><xsl:call-template name="get_scripts_path"/>/qv_messages_<xsl:value-of select="$lang"/>.js</xsl:attribute></SCRIPT>
	<SCRIPT type="text/javascript" src="scripts/prototype.js"></SCRIPT-->
	<script type="text/javascript">initMessages();</script>
	<script type="text/javascript">
		setTeacher("<xsl:value-of select="$teacher"/>");
		setAssessment("<xsl:value-of select="$assessment"/>");
	</script>
 </xsl:template>
 
  <xsl:template name="get_scripts_path">scripts</xsl:template>
	
  <xsl:template name="save_section">javascript:saveSection(lang);</xsl:template>
  <xsl:template name="save_section_teacher">javascript:saveSectionTeacher(lang);</xsl:template>
  <xsl:template name="deliver_section">javascript:deliverSection();</xsl:template>
  <xsl:template name="correct_section">javascript:if (isTeacher()){correctTeacherSection();}else{correctSection();}</xsl:template>
	
 <xsl:template name="footer">
	<script type="text/javascript">
		writeCorrectQVApplet('<xsl:value-of select="$assessment"/>', '<xsl:value-of select="//section[position()=$section_number]/@ident"/>', '<xsl:value-of select="$section_number"/>');
		window.onload=init;
	</script>
 </xsl:template>

 <xsl:template name="footer_sections">
	<script type="text/javascript">
		if (getApplet('app_1')==null) writeCorrectQVApplet('<xsl:value-of select="$assessment"/>', 'index', '0');
		window.onload=initSections;
		ordenaSections(<xsl:value-of select="count(//section)"/>,<xsl:call-template name="getRandomizableSections"></xsl:call-template>); <!-- Albert -->
	</script>
 </xsl:template>		
	
	
</xsl:stylesheet>
