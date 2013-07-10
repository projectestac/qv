<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

 <xsl:param name="teacher"/> 
 <xsl:param name="assessment"/> 

					
 <xsl:variable name="lang">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>ca</xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>
 <xsl:variable name="css">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='css']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='css']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise></xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>
 <xsl:variable name="skin_name">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='skin']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='skin']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>
			 <xsl:choose>
				 <xsl:when test="$default_skin!=''"><xsl:value-of select="$default_skin"/></xsl:when>
				 <xsl:otherwise>default</xsl:otherwise>
			 </xsl:choose>
		 </xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>

 <xsl:variable name="generic_skin">generic</xsl:variable>
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/<xsl:value-of select="$generic_skin"/>/css/<xsl:value-of select="$skin_name"/>/image/</xsl:variable>
 <xsl:variable name="specific_images_lang_path"><xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="$lang"/>/</xsl:variable>
 <xsl:variable name="specific_css_path">skins/<xsl:value-of select="$generic_skin"/>/css/<xsl:value-of select="$skin_name"/></xsl:variable>
 <xsl:variable name="specific_scripts_path">skins/<xsl:value-of select="$generic_skin"/>/scripts/<xsl:value-of select="$skin_name"/></xsl:variable>

 <xsl:variable name="header_title">
  <xsl:choose>
    <xsl:when test="//assignment/@name!=''"><xsl:value-of select="//assignment/@name"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
   </xsl:choose>
 </xsl:variable>
 

 <xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html" doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd"/>

<!-- #################### INDEX ################# -->
 <xsl:template match="quadern_assignments">
 <HTML>
   <HEAD>
    <title><xsl:call-template name="html_head_title"/></title>
    <xsl:call-template name="put_scripts"/>
    <xsl:call-template name="put_css"/>
   </HEAD>
   <BODY onload="window.focus();" >
	   <xsl:call-template name="put_header"/>
	   <xsl:apply-templates select="assignment/sections"/>
   </BODY>
  </HTML>
 </xsl:template> 
	
 <xsl:template match="sections">
  <xsl:variable name="is_preview"><xsl:call-template name="isPreviewMode"/></xsl:variable>
  <xsl:variable name="puntuation">
   <xsl:choose>
    <xsl:when test="//assignment/puntuation!=''"><xsl:value-of select="//assignment/puntuation"/></xsl:when>
    <xsl:otherwise>-</xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <xsl:variable name="assignment_date">
    <xsl:value-of select="//assignment/assignment_date"/>
  </xsl:variable>	 
	 
  <div id="content">
	<div id="index">
		<div id="index-information">
			<div id="index-information-title">
				<table cellpadding="5" cellspacing="0">
				<tr>
					<td><b><script type="text/javascript">document.write(getMessage('score'));</script>:</b></td>
					<td id="assignment-score"><xsl:value-of select="$puntuation"/></td>
				</tr>
				<tr>
					<td><b><script type="text/javascript">document.write(getMessage('sectiontime'));</script>:</b></td>
					<td id="assignment-time">-</td>
				</tr>
				</table>
			</div>
			<div id="index-information-table">
				<table cellpadding="0" cellspacing="0">
				<thead>
					<th class="section-title"><script type="text/javascript">document.write(getMessage('sections'));</script></th>
				  <xsl:if test="$is_preview='false'">
					<th><div class="section-info"><script type="text/javascript">document.write(getMessage('state'));</script></div></th>
					<th><div class="section-info"><script type="text/javascript">document.write(getMessage('delivers'));</script></div></th>
					<th><div class="section-info"><script type="text/javascript">document.write(getMessage('score'));</script></div></th>
					<!-- Albert -->
					<th width="0"><div class="section-info"><span id="section_header_time"></span></div></th>
					<script type="text/javascript">if(isWorkingWithServer()){writeSectionTimeHeaderCell()}</script>
					<!-- script type="text/javascript">
					if(isWorkingWithServer()){
						writeSectionTimeHeaderCell();
					}
					</script-->
				  </xsl:if>
				</thead>
				<tbody>
				 
				 
				 <xsl:for-each select="section">
				   <xsl:variable name="section_name">
					<xsl:choose>
					 <xsl:when test="@name=''">getMessage('section')+" <xsl:value-of select="@num"/>"
					 </xsl:when>
					 <xsl:otherwise>"<xsl:value-of select="@name"/>"</xsl:otherwise>
					</xsl:choose>
				   </xsl:variable>
				   <xsl:variable name="section_title">
					<xsl:choose>
					 <xsl:when test="@name=''"><xsl:value-of select="@num"/>
					 </xsl:when>
					 <xsl:otherwise>
					  <xsl:value-of select="@name"/>
					 </xsl:otherwise>
					</xsl:choose>
				   </xsl:variable>
				   <xsl:variable name="section_path">
					 <xsl:call-template name="get_section_path"/>
					 <!--xsl:value-of select="ancestor::assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;quadernXSL=<xsl:value-of select="ancestor::assignment/@quadernXSL"/>&#38;full=<xsl:value-of select="@num"/-->
				   </xsl:variable>
				   <xsl:variable name="section_limit">
					<xsl:apply-templates select="section_delivery"/>
					<xsl:if test="ancestor::assignment/max_delivery!='-1'">/<xsl:apply-templates select="ancestor::assignment/max_delivery"/></xsl:if>
				   </xsl:variable>
				   <xsl:variable name="section_puntuation">
					<xsl:choose>
					 <xsl:when test="section_puntuation"><xsl:apply-templates select="section_puntuation"/></xsl:when>
					 <xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				   </xsl:variable>
					 
					<xsl:variable name="class">
					   <xsl:choose>
						<xsl:when test="@num mod 2=0">even-row</xsl:when>
						<xsl:otherwise>odd-row</xsl:otherwise>
					   </xsl:choose>						
					</xsl:variable>

					 
				<tr>
				  <xsl:attribute name="class"><xsl:value-of select="normalize-space($class)"/></xsl:attribute>

				  
				  <td class="section-title">
						<xsl:value-of select="@num"/>. 
						<span>
							<xsl:attribute name="id">section_title_opt<xsl:number/></xsl:attribute>
				        	<a>
				          		<xsl:attribute name="title"><xsl:value-of select="$section_title"/></xsl:attribute>
				          		<!--xsl:attribute name="href">javascript:gotoSectionRandomizable(getSectionRandomValueRandomizable('<xsl:number/>',<xsl:value-of select="count(//section)"/>,<xsl:call-template name="getRandomizableSections"></xsl:call-template>),<xsl:value-of select="count(//section)"/>,0,<xsl:call-template name="getRandomizableSections"></xsl:call-template>);</xsl:attribute-->
				          		<xsl:attribute name="href">javascript:gotoSectionRandomizable(getRealValueRandomizable('<xsl:number/>',<xsl:value-of select="count(//section)"/>,<xsl:call-template name="getRandomizableSections"></xsl:call-template>),<xsl:value-of select="count(//section)"/>,0,<xsl:call-template name="getRandomizableSections"></xsl:call-template>);</xsl:attribute>
				          		
				          			<!-- Albert <xsl:attribute name="href"><xsl:value-of select="normalize-space($section_path)"/></xsl:attribute-->
				          		
				          		<script type="text/javascript">document.write(<xsl:value-of select="normalize-space($section_name)"/>);</script> 
				        	</a>
				        </span>
				  </td>
				  <xsl:if test="$is_preview='false'">
					<td><!--Albert xsl:attribute name="id"><xsl:value-of select="@id"/>_state</xsl:attribute--><!--xsl:attribute name="class">section-info state <xsl:value-of select="normalize-space($class)"/></xsl:attribute--><xsl:attribute name="class">section-info <xsl:value-of select="normalize-space($class)"/></xsl:attribute>
						<span>
							<xsl:attribute name="id">section_state_opt<xsl:number/></xsl:attribute>
							<!-- Albert div-->
							<div class="state"><!-- Albert -->
								<xsl:attribute name="id"><xsl:value-of select="@id"/>_state</xsl:attribute>
							<div class="section-info">&#160;</div><div class="section-info-state-txt"><xsl:attribute name="id"><xsl:value-of select="@id"/>_state_txt</xsl:attribute>-1</div>
							</div>
						</span>
					</td>
					<td class="section-info"><!--Albert xsl:attribute name="id"><xsl:value-of select="@id"/>_attempts</xsl:attribute-->
						<span>
							<xsl:attribute name="id">section_attempts_opt<xsl:number/></xsl:attribute>
							<div class="section-info">
<xsl:attribute name="id"><xsl:value-of select="@id"/>_attempts</xsl:attribute><!--Albert--><xsl:value-of select="$section_limit"/>&#160;</div>
						</span>
					</td>
					<td class="section-info"><!--Albert xsl:attribute name="id"><xsl:value-of select="@id"/>_score</xsl:attribute-->
						<span>
							<xsl:attribute name="id">section_score_opt<xsl:number/></xsl:attribute>
							<div class="section-info"><xsl:attribute name="id"><xsl:value-of select="@id"/>_score</xsl:attribute><!--Albert--><xsl:value-of select="$section_puntuation"/></div>
						</span>
					</td>
					<script type="text/javascript">
						if(isWorkingWithServer()){
							writeSectionTimeCell('<xsl:number/>','<xsl:value-of select="@id"/>');
						}
					</script>
				  </xsl:if>

				  
				  		  	
				</tr>

				</xsl:for-each>	 
				
				
				</tbody>
				<tfoot>
				<tr>
					<!-- td colspan="4"></td-->
					<script type="text/javascript">
					if(isWorkingWithServer()){
						writeColspan(5);//Time column
					} else {
						writeColspan(4);
					}
					</script>
				</tr>
				
				
				</tfoot>					
				</table>
			</div>
		</div>
		<div id="index-footer"></div>
	</div>
  </div>
  <div id="footer">
  	<script>if (typeof setFooter == 'function'){setFooter();}</script>
  </div>
  <xsl:call-template name="footer_sections"/>   	   
 </xsl:template>	

 <xsl:template name="footer_sections">	 
 </xsl:template>	 
	
	
 <xsl:template name="get_section_path">
	 <xsl:value-of select="ancestor::assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;quadernXSL=<xsl:value-of select="ancestor::assignment/@quadernXSL"/>&#38;full=<xsl:value-of select="@num"/>
 </xsl:template>	
	
 <xsl:template name="put_header">
  <xsl:variable name="section_name">
   <xsl:value-of select="//section[position()=$section_number]/@title"/>
  </xsl:variable>
	 
  <div id="header">
	 <a>
		<xsl:attribute name="href">javascript:gotoSectionRandomizable('0',<xsl:value-of select="$section_max_number"/>,0,<xsl:call-template name="getRandomizableSectionsInItem"></xsl:call-template>);</xsl:attribute>
		<div id="header-logo"></div>
	 </a><!--Albert -->
	 <div id="header-title">
		<h1> <xsl:value-of select="$header_title"/> </h1>
        <xsl:choose>
        	<xsl:when test="$section_name!=''">
        		<h3><xsl:value-of select="$section_name"/>&#160;<div id="state" class="">&#160;&#160;&#160;&#160;</div></h3>
        	</xsl:when>
        	<xsl:when test="false">
		        <h3><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='materia']/fieldentry"/></h3>
		        <h3><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='level']/fieldentry"/></h3>
		    </xsl:when>
        	<xsl:otherwise>
        	</xsl:otherwise>
        </xsl:choose>
        <h3 id="username"><script type="text/javascript">writeUserName();</script></h3>
	 </div>
	 <xsl:if test="//assessment!=''">
		  <div id="header-puntuation">
		  <!--Albert div id="header-puntuation-section"><script type="text/javascript">writeMessage('section');</script>: <h1><xsl:value-of select="$section_number"/></h1>/<xsl:value-of select="$section_max_number"/></div-->
		  <div id="header-puntuation-section"><script type="text/javascript">writeMessage('section');</script>: <h1><script type="text/javascript">writeSectionNumRandomizable(<xsl:value-of select="$section_number"/>,<xsl:value-of select="normalize-space($section_max_number)"/>,<xsl:call-template name="getRandomizableSectionsInItem"></xsl:call-template>);</script></h1>/<xsl:value-of select="$section_max_number"/></div><!--Albert-->
		  
		   <!-- ATTEMPTS and SCORE-->
		   <div id="header-puntuation-attempts"><script type="text/javascript">writeMessage('attempts');</script>: <span id="attempts"> - </span></div>
		   <div style="visibility: hidden;" id="header-puntuation-score"><script type="text/javascript">writeMessage('score');</script>: 
			 <span id="score">
		   <xsl:choose>
			<xsl:when test="$es_correccio='true'">
			 <script>writeItemScore('<xsl:value-of select="assessment/section[position()=$section_number]/@ident"/>');</script>
			</xsl:when>
			<xsl:otherwise> - </xsl:otherwise>
		   </xsl:choose>  
		  </span>
		  </div>

		  <!-- span id="header-puntuation-time"><script type="text/javascript">writeMessage('sectiontime');</script>: <span id="section-time"> - </span></span-->
		  <div style="visibility: hidden;" id="header-puntuation-time"><script type="text/javascript">writeMessage('sectiontime');</script>:
			  <span id="section-time"> - </span>
		  </div>

		 </div>
	 </xsl:if>
  </div>
 </xsl:template>

<!-- #################### SECTION ################# -->
 
<xsl:template name="getItemFeedback">
  <!-- Si existeix algun feedback a mostrar per l'item amb ident=item_ident es mostra-->
  <xsl:param name="item_ident"/>
  <!--xsl:variable name="showFeedback"><xsl:value-of select="itemmetadata/qtimetadata/qtimetadatafield[@fieldlabel='showFeedback']/@fieldentry" /></xsl:variable-->
  <xsl:variable name="showFeedback">
   <xsl:value-of select="itemcontrol/@feedbackswitch"/>
  </xsl:variable>
  <xsl:if test="normalize-space($showFeedback)!='No'">
	<a><!--Albert-->
		<xsl:attribute name="onClick">changeCorrect('<xsl:value-of select="$item_ident"/>');</xsl:attribute><!--Albert-->
  
		<div>
			<xsl:attribute name="id">feedback_<xsl:value-of select="$item_ident"/></xsl:attribute>	  
			<xsl:if test="$es_correccio='true' and contains($initialselection,$item_ident)">
				<script> if (isItemCorrect("<xsl:value-of select="$item_ident"/>")) { <![CDATA[
						document.write("<div class='item-feedback-ok'>");
					]]> }else { <![CDATA[
						document.write("<div class='item-feedback-ko'>");
					]]> } 
				</script>
				<xsl:call-template name="getDisplayFeedback">
					<xsl:with-param name="ident_item">
						<xsl:value-of select="$item_ident"/>
					</xsl:with-param>
					<xsl:with-param name="feedback">
						<xsl:value-of select="$displayfeedback"/>
					</xsl:with-param>
				</xsl:call-template>
				<script><![CDATA[document.write("</div>");]]></script>
			</xsl:if>
		</div>
	</a><!--Albert-->
  </xsl:if>
 </xsl:template>

<!-- #################### REDEFINITIONS ################# -->
 <xsl:template name="html_head_title">
    QV - <xsl:value-of select="$header_title"/>
 </xsl:template>

 <xsl:template name="html_body_attrs">
 </xsl:template>	
	
 <xsl:template name="put_css">
	<script type="text/javascript">includeCSS('<xsl:value-of select="$skin_name"/>', '<xsl:value-of select="$lang"/>');</script>     
 </xsl:template>
 
 <xsl:template name="put_specific_scripts">
    <SCRIPT type="text/javascript">
	    <xsl:attribute name="src"><xsl:value-of select="$specific_scripts_path"/>/<xsl:value-of select="$skin_name"/>.js</xsl:attribute>
    </SCRIPT>
	<script language="JavaScript" src="http://clic.xtec.net/qv/dist/launchQV.js" type="text/javascript"></script>
 </xsl:template>
 
  
 <xsl:template name="questestinterop_body">	
	<a name="top"/>	 
	<xsl:call-template name="put_header"/>
	 
	<div id="content">
		<div id="messages"></div>
		<xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
	</div>	 

	<div id="content-footer"></div>
	 
	<div id="bottom-navigation-bar">
		<div id="navigation-buttons">
			<a id="back-bt" class="button">
				<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">back</xsl:with-param></xsl:call-template></xsl:attribute>
			    <!--Albert xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number -1"/></xsl:with-param></xsl:call-template></xsl:attribute-->
			    <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number"/></xsl:with-param><xsl:with-param name="diff">-1</xsl:with-param><xsl:with-param name="randomizable"><xsl:call-template name="getRandomizableSectionsInItem"></xsl:call-template></xsl:with-param></xsl:call-template></xsl:attribute><!--Albert-->
				<div><div id="back-txt" class="bt-txt"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">back</xsl:with-param></xsl:call-template></div></div>
			</a>
			<script type="text/javascript">$('back-bt').title=getMessage('back');Element.update('back-txt', getMessage('back'));</script>
			<a id="home-bt" class="button">
				<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">home</xsl:with-param></xsl:call-template></xsl:attribute>
			    <xsl:attribute name="href">javascript:<xsl:call-template name="go_home"/></xsl:attribute>
				<div><div id="home-txt" class="bt-txt"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">home</xsl:with-param></xsl:call-template></div></div>
				<xsl:if test="false"><span><xsl:call-template name="get_lang_name"><xsl:with-param name="id">home</xsl:with-param></xsl:call-template></span></xsl:if>
			</a>
			<script type="text/javascript">$('home-bt').title=getMessage('home');Element.update('home-txt', getMessage('home'));</script>
			<a id="next-bt" class="button">
				<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">next</xsl:with-param></xsl:call-template></xsl:attribute>
			    <!--Albert xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number +1"/></xsl:with-param></xsl:call-template></xsl:attribute-->
			    <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number"/></xsl:with-param><xsl:with-param name="diff">1</xsl:with-param><xsl:with-param name="randomizable"><xsl:call-template name="getRandomizableSectionsInItem"></xsl:call-template></xsl:with-param></xsl:call-template></xsl:attribute><!--Albert-->
				<div><div id="next-txt" class="bt-txt"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">next</xsl:with-param></xsl:call-template></div></div>
				<xsl:if test="false"><span><xsl:call-template name="get_lang_name"><xsl:with-param name="id">home</xsl:with-param></xsl:call-template></span></xsl:if>
			</a>
			<script type="text/javascript">$('next-bt').title=getMessage('next');Element.update('next-txt', getMessage('next'));</script>
		</div>
		<div id="action-buttons">
			<script type="text/javascript">
			var lang='<xsl:value-of select="$lang"/>';
			if (notNull(getQueryParam('server'))){
				if (isTeacher()){
					document.writeln('<a id="correct-bt" class="button">
										<xsl:attribute name="title">'+getMessage('correct')+'</xsl:attribute>
										<xsl:attribute name="href"><xsl:call-template name="replace-string"><xsl:with-param name="text"><xsl:call-template name="correct_section"/></xsl:with-param><xsl:with-param name="old">'</xsl:with-param><xsl:with-param name="new">\'</xsl:with-param></xsl:call-template></xsl:attribute>'+
										'<div>'+
										'<div id="correct-txt" class="bt-txt">'+getMessage('correct_bt')+'</div>'+
										'</div>'+
									  '</a>');
					document.writeln('<a id="save-bt" class="button">
										<xsl:attribute name="title">'+getMessage('save')+'</xsl:attribute>
										<xsl:attribute name="href"><xsl:call-template name="replace-string"><xsl:with-param name="text"><xsl:call-template name="save_section_teacher"/></xsl:with-param><xsl:with-param name="old">'</xsl:with-param><xsl:with-param name="new">\'</xsl:with-param></xsl:call-template></xsl:attribute>'+
										'<div>'+
										'<div id="save-txt" class="bt-txt">'+getMessage('save_bt')+'</div>'+
										'</div>'+
									  '</a>');
				}else{
					document.writeln('<a id="save-bt" class="button">
										<xsl:attribute name="title">'+getMessage('save')+'</xsl:attribute>
										<xsl:attribute name="href"><xsl:call-template name="replace-string"><xsl:with-param name="text"><xsl:call-template name="save_section"/></xsl:with-param><xsl:with-param name="old">'</xsl:with-param><xsl:with-param name="new">\'</xsl:with-param></xsl:call-template></xsl:attribute>'+
										'<div>'+
										'<div id="save-txt" class="bt-txt">'+getMessage('save_bt')+'</div>'+
										'</div>'+
									  '</a>');
	
					document.writeln('<a id="deliver-bt" class="button">
										<xsl:attribute name="title">'+getMessage('deliver')+'</xsl:attribute>
										<xsl:attribute name="href"><xsl:call-template name="replace-string"><xsl:with-param name="text"><xsl:call-template name="deliver_section"/></xsl:with-param><xsl:with-param name="old">'</xsl:with-param><xsl:with-param name="new">\'</xsl:with-param></xsl:call-template></xsl:attribute>'+
										'<div>'+
										'<div id="deliver-txt" class="bt-txt">'+getMessage('deliver_bt')+'</div>'+
										'</div>'+
									  '</a>');
				}
			}else{
				document.writeln('<a id="correct-bt" class="button">
									<xsl:attribute name="title">'+getMessage('correct')+'</xsl:attribute>
									<xsl:attribute name="href"><xsl:call-template name="replace-string"><xsl:with-param name="text"><xsl:call-template name="correct_section"/></xsl:with-param><xsl:with-param name="old">'</xsl:with-param><xsl:with-param name="new">\'</xsl:with-param></xsl:call-template></xsl:attribute>'+
									'<div>'+
									'<div id="correct-txt" class="bt-txt">'+getMessage('correct_bt')+'</div>'+
									'</div>'+
								  '</a>');
			}
			</script>
			
		</div>
	</div>
	 
   <xsl:call-template name="footer"/>
	 
 </xsl:template>

 <xsl:template name="footer">
 </xsl:template>
	
 <xsl:template match="*[local-name()='section']">
    <xsl:apply-templates select="*[local-name()='presentation_material']"/>
    <ol class="section-number"><xsl:apply-templates select="*[local-name()='section']|*[local-name()='item']"/></ol>
    <xsl:if test="$noModify='false'">
      <SCRIPT> startTimerISO("<xsl:value-of
          select="ancestor::assessment/@duration"/>","<xsl:value-of select="$notebookTime"/>",
          "<xsl:value-of select="@duration"/>","<xsl:value-of select="$sectionTime"/>"); </SCRIPT>
    </xsl:if>
  </xsl:template>	
	
 
 <xsl:template match="*[local-name()='presentation_material']">
     <div class="section-additional-material"><xsl:call-template name="getSectionAdditionalMaterial"/></div>
 </xsl:template>

<xsl:template match="*[local-name()='item']">
  <xsl:variable name="it_ident">
   <xsl:value-of select="@ident"/>
  </xsl:variable>
 
  <span> <!-- Albert -->
     <xsl:attribute name="id">item_opt<xsl:number/></xsl:attribute> <!-- Albert -->                
            
  <A><xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/></xsl:attribute></A>
  <div class="item">
	<li>
    <!-- Assessment -->
	<div class="assessment">
	   <!--xsl:value-of select="position()"/>.-->
       <xsl:call-template name="getAssessment">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
	</div>
    <!-- Additional material -->
	<div class="item-additional-material">
        <xsl:call-template name="getItemAdditionalMaterial">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>
    </div>

	<!-- Responses -->
	<div class="responses">
       <xsl:call-template name="getResponses">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
	</div>

   <div class="hint-solution">
	<!-- Hint -->				
	<div class="hint">
		<xsl:attribute name="id">hint_<xsl:value-of select="$it_ident"/></xsl:attribute>
		<xsl:call-template name="getHintMaterial">
			<xsl:with-param name="item_ident">
			 <xsl:value-of select="$it_ident"/>
			</xsl:with-param>
		</xsl:call-template>
	</div>
    <div class="hint-bt">
		<xsl:attribute name="id">hintbt_<xsl:value-of select="$it_ident"/></xsl:attribute>
		<a href="#">
			<xsl:attribute name="onClick">openPopup('hint','hint_<xsl:value-of select="$it_ident"/>');</xsl:attribute>
			<script type="text/javascript">writeMessage('hint_bt');</script>
		</a>
	</div>
	<script type="text/javascript">updateHintVisibility('<xsl:value-of select="$it_ident"/>');</script>
		
	<!-- Solution -->				
	<div class="solution">
		<xsl:attribute name="id">solution_<xsl:value-of select="$it_ident"/></xsl:attribute>
		<xsl:call-template name="getSolutionMaterial">
			<xsl:with-param name="item_ident">
			 <xsl:value-of select="$it_ident"/>
			</xsl:with-param>
		</xsl:call-template>
	</div>
    <div class="solution-bt">
		<xsl:attribute name="id">solutionbt_<xsl:value-of select="$it_ident"/></xsl:attribute>
		<a href="#">
			<xsl:attribute name="onClick">openPopup('solution','solution_<xsl:value-of select="$it_ident"/>');</xsl:attribute>
			<script type="text/javascript">writeMessage('solution_bt');</script>
		</a>
	</div>
	<!--script type="text/javascript">updateSolutionVisibility('<xsl:value-of select="$it_ident"/>', !(getQueryParam('showcorrection')=='1'));</script-->
	<script type="text/javascript">updateSolutionVisibility('<xsl:value-of select="$it_ident"/>', true);</script>
  </div>		
		
	<!-- Feedback -->
	<div class="item-feedback">
		<xsl:call-template name="getItemFeedback">
			<xsl:with-param name="item_ident">
			 <xsl:value-of select="$it_ident"/>
			</xsl:with-param>
		</xsl:call-template>
		
		<!--Albert -->
		<div class="item-puntuation-score" style="visibility: hidden;">
			<xsl:attribute name="id">divscore_<xsl:value-of select="$it_ident"/></xsl:attribute>
			<script type="text/javascript">writeMessage('score');</script>: 
			<span>
				<xsl:attribute name="id">score_<xsl:value-of select="$it_ident"/></xsl:attribute>
				<input type="text">
					<xsl:attribute name="id">txt_score_<xsl:value-of select="$it_ident"/></xsl:attribute>
					<xsl:attribute name="name">txt_score_<xsl:value-of select="$it_ident"/></xsl:attribute>
				</input>
			</span>
		</div>
		<!--Albert -->
	</div>

	<!--Albert script type="text/javascript">if (getQueryParam('showcorrection')=='0'){Element.hide($('feedback_<xsl:value-of select="$it_ident"/>'));}</script-->
	<script type="text/javascript">if (getQueryParam('showcorrection')=='0'){Element.hide($('feedback_<xsl:value-of select="$it_ident"/>')); Element.hide($('divscore_<xsl:value-of select="$it_ident"/>'));}</script>
	<!-- Interaction -->
	<div class="item-interaction">
		<xsl:attribute name="id">interaction_<xsl:value-of select="$it_ident"/></xsl:attribute>
		<div class="item-interaction-bt">
			<A class="button" id="add-interaction-bt" >
				<xsl:attribute name="href">javascript:addInteraction('int_msg_<xsl:value-of select="$it_ident"/>');</xsl:attribute>
				<div><xsl:attribute name="alt"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">add_interaction</xsl:with-param></xsl:call-template></xsl:attribute></div>
			</A>
		</div>
		<div class="item-interaction-msg">
			<xsl:attribute name="id">int_msg_<xsl:value-of select="$it_ident"/></xsl:attribute>
			<br/><br/>
		</div>
	</div>
	<script type="text/javascript">if (!notNull(getQueryParam('server')) || getQueryParam('showinteraction')=='0'){Element.hide($('interaction_<xsl:value-of select="$it_ident"/>'));}</script>
	  
	</li>  	  
  </div>
  
  </span> <!-- Albert -->
  
 </xsl:template> 
   
  <xsl:template match="*[local-name()='flow_mat']">
    <xsl:apply-templates/>
    <BR/>
  </xsl:template>
  
	  
<!-- #################### COMMONS ################# -->

<xsl:template name="get_lang_name">
  <xsl:param name="id"/>
  <xsl:choose>
  	<xsl:when test="$id='add_interaction'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Añadir intervención</xsl:when>
  			<xsl:otherwise>Afegeix intervenció</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='attempts'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Intentos</xsl:when>
  			<xsl:otherwise>Lliuraments</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='assignment_date'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Fecha de asignación</xsl:when>
  			<xsl:otherwise>Data assignació</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='back'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Página anterior</xsl:when>
  			<xsl:otherwise>Pàgina anterior</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>	  
  	<xsl:when test="$id='correct'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Corregir esta hoja</xsl:when>
  			<xsl:otherwise>Corregeix aquest full</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>	  
  	<xsl:when test="$id='deliver'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Entregas</xsl:when>
  			<xsl:otherwise>Lliuraments</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>	  
  	<xsl:when test="$id='home'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Página principal</xsl:when>
  			<xsl:otherwise>Pàgina principal</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>	  
  	<xsl:when test="$id='load_section_title'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Ver hoja</xsl:when>
  			<xsl:otherwise>Veure full</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='next'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Página siguiente</xsl:when>
  			<xsl:otherwise>Pàgina següent</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='puntuation'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Puntuación</xsl:when>
  			<xsl:otherwise>Puntuació</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='section'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Hoja</xsl:when>
  			<xsl:otherwise>Full</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='sections'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Hojas</xsl:when>
  			<xsl:otherwise>Fulls</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='state'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Estado</xsl:when>
  			<xsl:otherwise>Estat</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:otherwise>
		<xsl:value-of select="$id"/>
  	</xsl:otherwise>
  </xsl:choose>	
</xsl:template>

</xsl:stylesheet>
