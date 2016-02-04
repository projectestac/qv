<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:param name="assignacioId"/>
  <xsl:param name="QTIUrl" select="'unknown'"/>
  <xsl:param name="quadernXSL"/>
  <xsl:param name="userId" select="''"/>
  <xsl:param name="sUserId" select="''"/>
  <xsl:param name="displayfeedback" select="''"/>
  <xsl:param name="displayinteraction" select="''"/>
  <xsl:param name="initialselection" select="''"/>
  <xsl:param name="view" select="''"/>
  <xsl:param name="serverUrl" select="''"/>
  <xsl:param name="section_number" select="1"/>
  <xsl:param name="section_max_number" select="1"/>
  <xsl:param name="section_limit" select="-1"/>
  <xsl:param name="section_max_limit" select="-1"/>
  <xsl:param name="returnPage" select="''"/>
  <xsl:param name="es_correccio" select="'false'"/>
  <xsl:param name="noModify" select="'false'"/>
  <xsl:param name="estat_lliurament"/>
  <xsl:param name="finishPage" select="''"/>
  <!--si no es pot modificar:true -->
  <xsl:param name="canCorrect" select="'true'"/>
  <xsl:param name="writingEnabled" select="'true'"/>
  <xsl:param name="scoring" select="''"/>
  <xsl:param name="needResponse" select="true"/>
  <!-- representacio de les puntuacions i la correctesa de les respostes -->
  <xsl:param name="date" select="''"/>
  <!-- data en que es va fer el darrer lliurament -->
  <!-- Temps -->
  <xsl:param name="notebookTime" select="'00:00:00'"/>
  <xsl:param name="sectionTime" select="'00:00:00'"/>
  <!-- Clic -->
  <xsl:param name="clicJarBase" select="'applets/jclic/jars/'"/>
  <xsl:param name="sessionKey" select="'qv'"/>
  <xsl:param name="sessionContext" select="''"/>
  <xsl:param name="clicReporter" select="''"/>
  <xsl:param name="clicIP" select="''"/>
  <xsl:param name="clicPort" select="''"/>
  <xsl:param name="reportServerURL" select="'http://127.0.0.1:9000/main?PH=activityReports&amp;projects=-1&amp;edit=false'"/>
  <!-- Aspecte -->
  <xsl:param name="bgimage" select="''"/>
  <xsl:param name="askInteractionImage" select="'web/imatges/question.gif'"/>
  <xsl:param name="askInteractionImageOver" select="'web/imatges/question2.gif'"/>
  <xsl:param name="lliurarImage" select="'web/imatges/lliurar.gif'"/>
  <xsl:param name="backImage" select="'web/imatges/tornar.gif'"/>
  <xsl:param name="color" select="blau"/>
  <xsl:param name="default_skin"/>	
	
	
  <!-- fletxa enrere -->
  <!-- <xsl:param name="clicJarBase" select="/applets/jclic/jars/"/> -->
  <!--xsl:output method="html"/-->
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>

  <xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html"/>
  <!-- <xsl:variable name="urlBase"><xsl:value-of select="questestinterop/assessment/@urlBase"/></xsl:variable> -->
  <xsl:variable name="urlBase">
    <xsl:value-of select="$QTIUrl"/>/../</xsl:variable>
  <xsl:variable name="css">
    <xsl:value-of select="//assessment/qtimetadata/qtimetadatafield[fieldlabel[text()='css']]/fieldentry[text()]"/>
  </xsl:variable>
  <!-- Seleccionar localitzacio imatges -->
  <xsl:variable name="path_imatges">
    <xsl:choose>
      <xsl:when test="$color='rosa'">web/imatges/rosa/</xsl:when>
      <xsl:when test="$color='verd'">web/imatges/verd/</xsl:when>
      <xsl:otherwise>web/imatges/blau/</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  
  <!-- FEEDBACK -->
  <xsl:template match="*[local-name()='itemfeedback']">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template name="getDisplayFeedback">
    <xsl:param name="ident_item"/>
    <xsl:param name="feedback"/>
    <xsl:if test="contains($feedback, $ident_item)">
      <xsl:variable name="ident_feed">
        <xsl:value-of select="substring-before(substring-after($feedback,$ident_item),'#')"/>
      </xsl:variable>
      <xsl:if test="itemfeedback[@ident=$ident_feed]!=''">
        <xsl:variable name="filtered_html">
          <xsl:call-template name="filter-html">
            <xsl:with-param name="text">
              <xsl:value-of select="itemfeedback[@ident=$ident_feed]" disable-output-escaping="yes"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="$filtered_html" disable-output-escaping="yes"/>
        <!--xsl:value-of select="itemfeedback[@ident=$ident_feed]"/-->
        <BR/>
      </xsl:if>
      <xsl:call-template name="getDisplayFeedback">
        <xsl:with-param name="ident_item">
          <xsl:value-of select="$ident_item"/>
        </xsl:with-param>
        <xsl:with-param name="feedback">
          <xsl:value-of select="substring-after($feedback,$ident_item)"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
	
  <xsl:template match="*[local-name()='solution']">
	  
  </xsl:template>
	
  <!-- RESPONSES -->
  <xsl:template match="*[local-name()='render_choice']">
    <xsl:choose>
      <xsl:when test="@display='LIST'">
        <SELECT>
          <xsl:attribute name="name">
            <xsl:call-template name="get_response_ident"/>
          </xsl:attribute>
          <OPTION value="" id="0" name="0"/>
          <xsl:for-each select="descendant::response_label">
            <xsl:call-template name="choice"/>
          </xsl:for-each>
        </SELECT>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
        <!--xsl:apply-templates select="material|response_label|flow_label"/-->
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="*[local-name()='render_fib']">
    <xsl:apply-templates/>
    <!--xsl:call-template name="fib"/-->
  </xsl:template>
	
  <xsl:template name="get_url_base"><xsl:value-of select="$QTIUrl"/>/../</xsl:template>
	
  <xsl:template match="*[local-name()='render_draw']">
	  <xsl:variable name="ident"><xsl:call-template name="get_response_ident"/></xsl:variable>
	  <xsl:variable name="init"><xsl:value-of select="substring-after(substring-before(substring-after($initialselection,$ident),'#'),'=')"></xsl:value-of></xsl:variable>
	  <xsl:variable name="width"><xsl:choose><xsl:when test="@width!=''"><xsl:value-of select="@width"/></xsl:when><xsl:otherwise>500</xsl:otherwise></xsl:choose></xsl:variable>
	  <xsl:variable name="height"><xsl:choose><xsl:when test="@height!=''"><xsl:value-of select="@height"/></xsl:when><xsl:otherwise>400</xsl:otherwise></xsl:choose></xsl:variable>

      <xsl:variable name="params">'+ 'NAME=<xsl:value-of select="$ident"/>;'+
          'url_base=<xsl:call-template name="get_url_base"/>;'+
          'serverUrl=<xsl:value-of select="$serverUrl"/>;'+
		  'image_src=<xsl:value-of select="material/matimage/@uri"/>;'+ 
		  'disabled=<xsl:value-of select="$noModify"/>;'+
		  'num_colors=<xsl:value-of select="@num_colors"/>;'+
  		  <xsl:choose><xsl:when test="$init!=''">'INITPARAM=<xsl:value-of select="$init"/>;'+</xsl:when></xsl:choose>'
	  </xsl:variable>
	  <xsl:variable name="applet">
			writeJavaApplet('edu.xtec.qv.applet.QVDrawer.class', getAppletsBase(),
				'QVDrawer.jar,svg_generat.jar', '<xsl:value-of select="$params"/>', '<xsl:value-of select="$width"/>', '<xsl:value-of select="$height"/>' , null,
				  null,'qvApplet','<xsl:value-of select="$ident"/>',true);
	  </xsl:variable>
	  <INPUT type="hidden"><xsl:attribute name="name"><xsl:value-of select="$ident"/></xsl:attribute></INPUT>
  	  <SCRIPT> 
		  //alert('<xsl:value-of select="$init"/>');
		  <xsl:value-of select="normalize-space($applet)"/>
	  </SCRIPT>		  
  </xsl:template>
  <xsl:template match="*[local-name()='ims_render_object']">
    <span style="background:#FFFFFF;">
      <xsl:variable name="ident">
        <xsl:call-template name="get_dragdrop_response_ident"/>
      </xsl:variable>
      <INPUT TYPE="HIDDEN">
        <xsl:attribute name="name">
          <xsl:value-of select="$ident"/>
        </xsl:attribute>
      </INPUT>
      <xsl:variable name="cardinality">
        <xsl:value-of select="translate(../../@rcardinality,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
        <!-- manera de passar a minuscules a xsl... -->
      </xsl:variable>
      <xsl:variable name="WIDTH">
        <xsl:call-template name="getHotspotWidth"/>
      </xsl:variable>
      <xsl:variable name="HEIGHT">
        <xsl:call-template name="getHotspotHeight"/>
      </xsl:variable>
      <xsl:variable name="params">'+ 'NAME=<xsl:value-of select="$ident"/>$$'+
          'url_base=<xsl:call-template name="get_url_base"/>$$'+
          'serverUrl=<xsl:value-of select="$serverUrl"/>$$'+ 
		  'image_src=<xsl:value-of select="ancestor::presentation//matimage/@uri"/>$$'+ 
		  'orientation=<xsl:value-of select="@orientation"/>$$'+ 
		  'size=<xsl:value-of select="@size"/>$$'+ 
		  'font=<xsl:value-of select="@font"/>$$'+ 
		  'shuffle=<xsl:value-of select="@shuffle"/>$$'+ 
		  'inside=<xsl:value-of select="@inside"/>$$'+ 
		  'style=<xsl:value-of select="@style"/>$$'+ 
		  'showTargets=<xsl:value-of select="@showTargets"/>$$'+ 
		  'disabled=<xsl:value-of select="$noModify"/>$$'+ 
		  'align=<xsl:value-of select="@align"/>$$'+ 
  		  'Snratio=<xsl:value-of select="@nratio"/>$$'+ 
  		  'Snrotate=<xsl:value-of select="@nrotate"/>$$'+ 
  		  'enable_rotating=<xsl:value-of select="@enable_rotating"/>$$'+ 
  		  'enable_scaling=<xsl:value-of select="@enable_scaling"/>$$'+ 
  		  'INITPARAM=<xsl:value-of select="substring-after(substring-before(substring-after($initialselection,$ident),'#'),'=')"/>$$'+ <xsl:choose>

         <xsl:when test="$cardinality='ordered'">
            <!-- ordenacio -->
            <xsl:for-each select="ancestor::render_extension">
              <xsl:call-template name="putDragDropSources"/>
            </xsl:for-each>
          </xsl:when>
          <xsl:otherwise>
            <!-- drag drop -->
            <xsl:for-each select="ancestor::presentation">
              <xsl:for-each select=".//response_label[@rarea!='']">
                <xsl:variable name="num_param">
                  <xsl:number/>
                </xsl:variable>
                <xsl:for-each select="attribute::*">
					 'T<xsl:value-of select="$num_param"/>_<xsl:value-of select="local-name(.)"/>=<xsl:value-of select="."/>$$'+ 
				</xsl:for-each> 'T<xsl:value-of select="$num_param"/>_text=<xsl:value-of select="normalize-space(.)"/>$$'+ 
			  </xsl:for-each>
              <xsl:call-template name="putDragDropSources"/>
            </xsl:for-each>
          </xsl:otherwise>
        </xsl:choose> '</xsl:variable>
      <SCRIPT>
        <xsl:choose>
          <xsl:when test="$cardinality='ordered'"> writeJavaApplet('QVOrdering.class', getAppletsBase(),
            'QVOrdering.jar', '<xsl:value-of select="$params"/>', '<xsl:call-template
            name="getOrderingWidth"/>', '<xsl:call-template name="getOrderingHeight"/>' , null,
              null,'qvApplet','<xsl:value-of select="$ident"/>',true); </xsl:when>
          <xsl:otherwise> writeJavaApplet('QVDragDrop.class', getAppletsBase(), 'QVDragDrop.jar',
              '<xsl:value-of select="$params"/>', '<xsl:call-template name="getHotspotWidth"/>',
              '<xsl:call-template name="getHotspotHeight"/>' , null, null,'qvApplet','<xsl:value-of
            select="$ident"/>',true); </xsl:otherwise>
        </xsl:choose>
      </SCRIPT>
    </span>
  </xsl:template>
  <xsl:template name="get_dragdrop_response_ident">
    <xsl:value-of select="ancestor-or-self::item/@ident"/>
    <xsl:text>--&gt;</xsl:text>
    <xsl:value-of select="ancestor::response_grp/@ident"/>
    <!-- POTSER ES UN ALTRE TIPUS DE RESPONSE... -->
    <xsl:value-of select="ancestor::response_lid/@ident"/>
    <!-- POTSER ES UN ALTRE TIPUS DE RESPONSE... -->
  </xsl:template>
  <xsl:template name="putDragDropSources">
    <xsl:for-each select=".//response_label[not(@rarea)]">
      <xsl:variable name="num_param">
        <xsl:number/>
      </xsl:variable>
      <!--xsl:for-each select=".//attribute::*"> 'S<xsl:value-of select="$num_param"/>_<xsl:value-of
     select="local-name(.)"/>=<xsl:value-of select="."/>;'+ </xsl:for-each> 'S<xsl:value-of
    select="$num_param"/>_text=<xsl:value-of select="normalize-space(.)"/>;'+ </xsl:for-each-->
      <xsl:variable name="singleQuote">&#39;</xsl:variable>
      <xsl:variable name="backSlash">&#92;</xsl:variable>
      <xsl:variable name="backSlashQuote">´</xsl:variable>
      <xsl:for-each select=".//attribute::*"> 'S<xsl:value-of select="$num_param"/>_<xsl:value-of
          select="local-name(.)"/>=<xsl:value-of select="."/>$$'+ </xsl:for-each> 'S<xsl:value-of
        select="$num_param"/>_text=<xsl:value-of
      select="translate(normalize-space(.),$singleQuote, $backSlashQuote)"/>$$'+ </xsl:for-each>
  </xsl:template>
  <xsl:template match="*[local-name()='render_slider']">
    <!-- <xsl:apply-templates/> -->
    <xsl:variable name="ident">
      <!-- <xsl:value-of select="ancestor::response_num/@ident"/>  NO TE PER QUE SER NUM!!!!-->
      <xsl:call-template name="get_response_ident"/>
    </xsl:variable>
    <xsl:variable name="width">
      <xsl:choose>
        <xsl:when test="normalize-space(@orientation)='Vertical'">80</xsl:when>
        <xsl:otherwise>200</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="height">
      <xsl:choose>
        <xsl:when test="normalize-space(@orientation)='Vertical'">200</xsl:when>
        <xsl:otherwise>80</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" type="qvApplet" codebase="http://java.sun.com/products/plugin/1.3/jinstall-13-win32.cab#Version=1,3,0,0">
      <xsl:attribute name="WIDTH">
        <xsl:value-of select="$width"/>
      </xsl:attribute>
      <xsl:attribute name="HEIGHT">
        <xsl:value-of select="$height"/>
      </xsl:attribute>
      <xsl:attribute name="NAME">
        <xsl:value-of select="$ident"/>
      </xsl:attribute>
      <PARAM NAME="type" VALUE="application/x-java-applet;version=1.3"/>
      <PARAM NAME="scriptable" VALUE="true"/>
      <PARAM NAME="CODE" VALUE="qvRenderSliderApplet.class"/>
      <PARAM NAME="ARCHIVE" VALUE="qvRenderSliderApplet.jar"/>
      <PARAM NAME="NAME">
        <xsl:attribute name="VALUE">
          <xsl:value-of select="$ident"/>
        </xsl:attribute>
      </PARAM>
      <PARAM NAME="lowerBound">
        <xsl:attribute name="value">
          <xsl:value-of select="@lowerbound"/>
        </xsl:attribute>
      </PARAM>
      <PARAM NAME="upperBound">
        <xsl:attribute name="value">
          <xsl:value-of select="@upperbound"/>
        </xsl:attribute>
      </PARAM>
      <xsl:variable name="step">
        <xsl:value-of select="@step"/>
      </xsl:variable>
      <xsl:if test="normalize-space($step)!=''">
        <!-- aquest render_slider te atribut step -->
        <PARAM name="step">
          <xsl:attribute name="value">
            <xsl:value-of select="normalize-space($step)"/>
          </xsl:attribute>
        </PARAM>
      </xsl:if>
      <xsl:variable name="step_label">
        <xsl:value-of select="@steplabel"/>
      </xsl:variable>
      <xsl:if test="normalize-space($step_label)!=''">
        <!-- aquest render_slider te atribut steplabel -->
        <PARAM name="stepLabel">
          <xsl:attribute name="value">
            <xsl:value-of select="normalize-space($step_label)"/>
          </xsl:attribute>
        </PARAM>
      </xsl:if>
      <xsl:variable name="start_val">
        <xsl:value-of select="@startval"/>
      </xsl:variable>
      <xsl:if test="normalize-space($start_val)!=''">
        <!-- aquest render_slider te atribut startval -->
        <PARAM name="startVal">
          <xsl:attribute name="value">
            <xsl:value-of select="normalize-space($start_val)"/>
          </xsl:attribute>
        </PARAM>
      </xsl:if>
      <xsl:variable name="orientation">
        <xsl:value-of select="@orientation"/>
      </xsl:variable>
      <xsl:if test="normalize-space($orientation)!=''">
        <!-- aquest render_slider te atribut orientation -->
        <PARAM name="orientation">
          <xsl:attribute name="value">
            <xsl:value-of select="normalize-space($orientation)"/>
          </xsl:attribute>
        </PARAM>
      </xsl:if>
      <PARAM NAME="INITPARAM">
        <xsl:attribute name="VALUE">
          <xsl:value-of select="substring-after(substring-before(substring-after($initialselection,$ident),'#'),'=')"/>
        </xsl:attribute>
      </PARAM>
    </OBJECT>
  </xsl:template>
  <xsl:template match="*[local-name()='flow_label']">
    <P>
      <xsl:apply-templates/>
    </P>
  </xsl:template>
  <xsl:template match="*[local-name()='flow_mat']">
    <xsl:apply-templates/>
    <BR/>
    <BR/>
  </xsl:template>
  <xsl:template match="*[local-name()='response_label']">
    <xsl:call-template name="process_response"/>
  </xsl:template>
  <xsl:template name="process_response">
    <!-- Estem a response_label -->
    <xsl:variable name="type">
      <xsl:call-template name="get_render_type"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$type='choice'">
        <xsl:call-template name="choice"/>
      </xsl:when>
      <xsl:when test="$type='fib'">
        <xsl:call-template name="fib"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="response_label">
    <xsl:choose>
      <xsl:when test="@display='LIST'">
        <SELECT class="response_label_list">
          <xsl:attribute name="NAME">
            <xsl:call-template name="get_response_ident"/>
            <!-- <xsl:value-of select="parent::*/@ident"/> -->
          </xsl:attribute>
          <OPTION value="" id="0" name="0">
            <!-- <xsl:attribute name="id"><xsl:value-of select="@ident"/></xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="@ident"/></xsl:attribute> -->
          </OPTION>
          <xsl:apply-templates/>
        </SELECT>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="response_label|flow_label"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="choice">
    <xsl:variable name="card">
      <!-- potser no en te, cal mirar la per defecte -->
      <xsl:value-of select="ancestor::*/@rcardinality"/>
    </xsl:variable>
    <xsl:variable name="display">
      <xsl:value-of select="ancestor::*/@display"/>
    </xsl:variable>
    <xsl:if test="$card='Single' or normalize-space($card)=''">
      <xsl:choose>
        <xsl:when test="$display='LIST'">
          <OPTION>
            <xsl:attribute name="value">
              <xsl:value-of select="@ident"/>
            </xsl:attribute>
            <xsl:attribute name="id">
              <xsl:value-of select="@ident"/>
            </xsl:attribute>
            <xsl:attribute name="name">
              <xsl:value-of select="@ident"/>
            </xsl:attribute>
            <xsl:apply-templates/>
          </OPTION>
        </xsl:when>
        <xsl:otherwise>
          <INPUT TYPE="RADIO">
            <xsl:attribute name="NAME">
              <xsl:call-template name="get_response_ident"/>
            </xsl:attribute>
            <xsl:attribute name="ID">
              <xsl:value-of select="@ident"/>
            </xsl:attribute>
            <xsl:attribute name="value">
              <xsl:value-of select="@ident"/>
            </xsl:attribute>
            <xsl:apply-templates/>
          </INPUT>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
    <xsl:if test="$card='Multiple'">
      <INPUT TYPE="CHECKBOX">
        <xsl:attribute name="NAME">
          <xsl:call-template name="get_response_ident"/>
        </xsl:attribute>
        <xsl:attribute name="ID">
          <xsl:value-of select="@ident"/>
        </xsl:attribute>
        <xsl:apply-templates/>
      </INPUT>
    </xsl:if>
  </xsl:template>
  <xsl:template name="fib">
    <!-- Hem pasat per render_fib i pot ser que encara hi siguem (si no hi ha response_label) o que estem a response_label -->
    <xsl:variable name="rows">
      <xsl:value-of select="normalize-space(ancestor-or-self::render_fib/@rows)"/>
    </xsl:variable>
    <xsl:variable name="check_field_type_script">
      <xsl:call-template name="check_field_type">
        <xsl:with-param name="type">
          <xsl:value-of select="normalize-space(ancestor-or-self::render_fib/@fibtype)"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="ident">
      <!-- <xsl:value-of select="ancestor-or-self::response_str/@ident"/>  NO TE PER QUE SER STR!!!!-->
      <xsl:call-template name="get_response_ident"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="not($rows='') and not($rows='1')">
        <TEXTAREA>
		  <xsl:if test="$noModify='true' or $estat_lliurament='corregit'">
		   <xsl:attribute name="readonly"></xsl:attribute>
		  </xsl:if>
          <xsl:attribute name="name">
            <xsl:value-of select="$ident"/>
          </xsl:attribute>
          <xsl:attribute name="cols">
            <xsl:value-of select="normalize-space(ancestor-or-self::render_fib/@columns)"/>
          </xsl:attribute>
          <xsl:attribute name="rows">
            <xsl:value-of select="$rows"/>
          </xsl:attribute>
          <xsl:attribute name="onChange">
            <!-- Podria posar-ho nomes si no es buit-->
            <xsl:value-of select="$check_field_type_script"/>
          </xsl:attribute>
          <xsl:call-template name="fib_att"/>
        </TEXTAREA>
      </xsl:when>
      <xsl:otherwise>
        <INPUT TYPE="TEXT" class="fib">
		  <xsl:if test="$noModify='true' or $estat_lliurament='corregit'">
		   <xsl:attribute name="readonly"></xsl:attribute>
		  </xsl:if>
          <xsl:attribute name="name">
            <xsl:value-of select="$ident"/>
          </xsl:attribute>
          <xsl:attribute name="size">
            <xsl:value-of select="normalize-space(ancestor-or-self::render_fib/@columns)"/>
          </xsl:attribute>
          <xsl:if test="normalize-space(ancestor-or-self::render_fib/@columns)='1'">
           <xsl:attribute name="style">width:15</xsl:attribute>
          </xsl:if>
          <xsl:attribute name="onChange">
            <!-- Podria posar-ho nomes si no es buit-->
            <xsl:value-of select="$check_field_type_script"/>
            <!-- if (!(<xsl:value-of select="$check_field_type_script"/>)) document.forms[0].<xsl:value-of select="$ident"/>.value=''; -->
          </xsl:attribute>
          <xsl:call-template name="fib_att"/>
        </INPUT>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="fib_att">
    <xsl:variable name="length">
      <xsl:value-of select="ancestor-or-self::render_fib/@maxchars"/>
    </xsl:variable>
    <xsl:if test="boolean(normalize-space($length))">
      <xsl:attribute name="maxlength">
        <xsl:value-of select="$length"/>
      </xsl:attribute>
      <xsl:attribute name="size">
        <xsl:value-of select="$length"/>
      </xsl:attribute>
      <xsl:attribute name="value">
        <xsl:variable name="prompt">
          <xsl:value-of select="normalize-space(ancestor-or-self::render_fib/@prompt)"/>
        </xsl:variable>
        <xsl:if test="boolean($prompt)">
          <xsl:variable name="symbol">
            <xsl:choose>
              <xsl:when test="$prompt='Dashline'">-</xsl:when>
              <xsl:when test="$prompt='Asterisk'">*</xsl:when>
              <xsl:when test="$prompt='Underline'">_</xsl:when>
              <xsl:when test="$prompt='Box'"/>
            </xsl:choose>
          </xsl:variable>
          <xsl:call-template name="loop_print">
            <xsl:with-param name="i">1</xsl:with-param>
            <xsl:with-param name="max">
              <xsl:value-of select="$length"/>
            </xsl:with-param>
            <xsl:with-param name="symbol">
              <xsl:value-of select="normalize-space($symbol)"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:if>
      </xsl:attribute>
    </xsl:if>
  </xsl:template>
  <xsl:template match="*[local-name()='matbreak']">
    <BR/>
  </xsl:template>
  <xsl:template match="*[local-name()='mattext']">
        <xsl:call-template name="putMattext"/>   
<!--    <xsl:choose>
      <xsl:when test="contains(@texttype,'html')">
        <xsl:copy-of select="."/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
      </xsl:otherwise>
    </xsl:choose>
-->    
  </xsl:template>
  <xsl:template match="*[local-name()='mathtml']">
    <xsl:call-template name="putMattext"/>   
<!--    <xsl:copy-of select="."/>-->
  </xsl:template>
  <xsl:template match="*[local-name()='matemtext']">
    <b>
      <xsl:apply-templates/>
    </b>
  </xsl:template>
  <xsl:template match="*[local-name()='matapplet']">
    <SCRIPT LANGUAGE="JavaScript"> writeJavaPlugin('<xsl:value-of
        select="@uri"/>','http://pie.xtec.es/~bferran','<xsl:value-of
        select="@classpath"/>','<xsl:value-of select="@params"/>','<xsl:value-of
        select="@width"/>','<xsl:value-of select="@height"/>'); </SCRIPT>
  </xsl:template>
  <xsl:template match="*[local-name()='mat_extension']">
    <xsl:apply-templates select="*[local-name()='matjclic']"/>
    <xsl:apply-templates select="*[local-name()='matlatex']"/>
    <xsl:apply-templates select="*[local-name()='matflash']"/>
    <xsl:apply-templates select="*[local-name()='matlink']"/>
    <xsl:apply-templates select="*[local-name()='mathtml']"/>
  </xsl:template>
  <xsl:template match="*[local-name()='matlink']">
    <A>
      <xsl:attribute name="HREF">javascript:openWindow('<xsl:value-of select="@href"/>','600','400')</xsl:attribute>
      <xsl:choose>
        <xsl:when test="normalize-space(@text)!=''">
          <xsl:value-of select="@text"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@href"/>
        </xsl:otherwise>
      </xsl:choose>
    </A>
  </xsl:template>
  <xsl:template match="*[local-name()='matjclic']">
	<xsl:variable name="jclic_uri">
		<xsl:choose>
			<xsl:when test="starts-with(@uri,'http')"><xsl:value-of select="@uri"/></xsl:when>
			<xsl:otherwise>../<xsl:value-of select="@uri"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>  
	  
	<script language="JavaScript" SRC="http://clic.xtec.cat/dist/jclic/jclicplugin.js" type="text/javascript"></script>
	  <script language="JavaScript"> 
		  setJarBase('http://clic.xtec.cat/dist/jclic'); 
		  setLanguage('ca'); 
		  writePlugin(getJClicURL('<xsl:value-of select="$jclic_uri"/>'), '<xsl:value-of select="@width"/>', '<xsl:value-of select="@height"/>'); 
	  </script>
	
    <!--SCRIPT LANGUAGE="JavaScript"> setJarBase('http://localhost:8080/qv/<xsl:value-of
      select="$clicJarBase"/>'); setLanguage('<xsl:value-of select="@language"/>'); <xsl:choose>
        <xsl:when test="normalize-space(@skin)=''"/>
        <xsl:otherwise> setSkin('<xsl:value-of select="@skin"/>'); </xsl:otherwise>
      </xsl:choose>
      /*setReporter('TCPReporter','ip=127.0.0.1;port=5510'); */
      <xsl:choose>
        <xsl:when test="normalize-space($clicIP)!='' and $userId!=-1 "> setReporter('<xsl:value-of
            select="$clicReporter"/>','ip=<xsl:value-of select="$clicIP"/>;port=<xsl:value-of
            select="$clicPort"/>;user=<xsl:value-of select="$userId"/>;key=<xsl:value-of
            select="$sessionKey"/>;context=<xsl:value-of select="$sessionContext"/>'); </xsl:when>
      </xsl:choose> setSystemSounds('<xsl:value-of select="@sounds"/>');
        setCompressImages('<xsl:value-of select="@compressImages"/>'); writePlugin('<xsl:value-of
      select="@project"/>', '<xsl:value-of select="@width"/>', '<xsl:value-of select="@height"/>'); </SCRIPT>
    <xsl:if test="$userId=-1">
      <BR/>
      <FONT COLOR="#ffffff">
        <A target="_blank">
          <xsl:attribute name="HREF">
            <xsl:value-of select="$reportServerURL"/>&amp;session_context=<xsl:value-of select="$sessionContext"/>
          </xsl:attribute> Resultats </A>
      </FONT>
    </xsl:if-->
  </xsl:template>

  <xsl:template match="*[local-name()='matlatex']">
	<xsl:variable name="latex_equation">
		<xsl:choose>
			<xsl:when test="starts-with(@uri,'http')"><xsl:value-of select="@uri"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$urlBase"/><xsl:value-of select="@uri"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>  
	  
    <xsl:variable name="params">equation=<xsl:value-of select="."/>$$</xsl:variable>
	  
    <script language="JavaScript"> 
		writeJavaApplet('dHotEqn.class', getAppletsBase(), 'HotEqn.jar','<xsl:value-of select="$params"/>', '<xsl:value-of select="@width"/>', '<xsl:value-of select="@height"/>', null, null,'qvApplet', 'hotEqnApplet', true);
	</script>
  </xsl:template>	
	
  <xsl:template match="*[local-name()='matflash']">
    <xsl:call-template name="matflash"/>
  </xsl:template>
  <xsl:template name="matflash">
    <xsl:variable name="uri">
      <xsl:call-template name="putResourceSource2">
        <xsl:with-param name="uri">
          <xsl:value-of select="@uri"/>
        </xsl:with-param>
      </xsl:call-template>
      <!--xsl:value-of select="@uri"/-->
    </xsl:variable>
    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0">
      <xsl:if test="normalize-space(@width)!=''">
        <!-- no te width-->
        <xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>
      </xsl:if>
      <xsl:if test="normalize-space(@height)!=''">
        <!-- no te width-->
        <xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>
      </xsl:if>
      <param name="movie">
        <xsl:attribute name="VALUE">
          <xsl:value-of select="$uri"/>
        </xsl:attribute>
      </param>
      <param name="quality" value="high"/>
      <embed quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash">
        <xsl:if test="normalize-space(@width)!=''">
          <!-- no te width-->
          <xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>
        </xsl:if>
        <xsl:if test="normalize-space(@height)!=''">
          <!-- no te width-->
          <xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>
        </xsl:if>
        <xsl:attribute name="SRC">
          <xsl:value-of select="$uri"/>
        </xsl:attribute>
      </embed>
    </object>
  </xsl:template>
  <xsl:template match="*[local-name()='matimage']">
    <xsl:variable name="temp">
      <!-- <xsl:for-each select="ancestor::presentation"> -->
      <xsl:for-each select="ancestor::response_lid">
        <xsl:value-of select="local-name(.//render_hotspot)"/>
      </xsl:for-each>
    </xsl:variable>
    <xsl:variable name="temp2">
      <xsl:for-each select="ancestor::presentation">
        <xsl:value-of select="local-name(.//ims_render_object)"/>
      </xsl:for-each>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="normalize-space($temp)=''">
        <!-- aquest presentation no te node render_hotspot-->
        <xsl:if test="$temp2='' and @uri!=''">
          <xsl:choose>
            <xsl:when test="normalize-space(@imagtype)='application/x-shockwave-flash'">
              <CENTER>
                <xsl:call-template name="matflash"/>
              </CENTER>
            </xsl:when>
            <xsl:otherwise>
              <xsl:variable name="align">
                <xsl:value-of select="normalize-space(@align)"/>
              </xsl:variable>
              <IMG>
                <xsl:if test="normalize-space(@width)!=''">
                  <!-- no te width-->
                  <xsl:attribute name="width">
                    <xsl:value-of select="@width"/>
                  </xsl:attribute>
                </xsl:if>
                <xsl:if test="normalize-space(@height)!=''">
                  <!-- no te width-->
                  <xsl:attribute name="height">
                    <xsl:value-of select="@height"/>
                  </xsl:attribute>
                </xsl:if>
                <xsl:if test="normalize-space($align)!=''">
                  <!-- no te align-->
                  <xsl:attribute name="align">
                    <xsl:value-of select="$align"/>
                  </xsl:attribute>
                </xsl:if>
                <xsl:call-template name="putResourceSource">
                  <xsl:with-param name="uri">
                    <xsl:value-of select="@uri"/>
                  </xsl:with-param>
                </xsl:call-template>
                <!-- <xsl:attribute name="src">
					<xsl:value-of select="@uri"/>
				</xsl:attribute> -->
              </IMG>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:if>
      </xsl:when>
      <xsl:otherwise>
        <xsl:variable name="ident">
          <xsl:call-template name="get_hotspot_response_ident"/>
        </xsl:variable>
        <INPUT TYPE="HIDDEN">
          <xsl:attribute name="name">
            <xsl:value-of select="$ident"/>
          </xsl:attribute>
        </INPUT>
        <!-- -->
        <xsl:variable name="temp2">
          <xsl:value-of select="@width"/>
        </xsl:variable>
        <xsl:variable name="WIDTH">
          <xsl:choose>
            <xsl:when test="normalize-space($temp2)=''">500</xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$temp2"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="temp3">
          <xsl:value-of select="@height"/>
        </xsl:variable>
        <xsl:variable name="HEIGHT">
          <xsl:choose>
            <xsl:when test="normalize-space($temp3)=''">350</xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$temp3"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="cardinality">
          <xsl:for-each select="ancestor::presentation">
            <xsl:value-of select=".//@rcardinality"/>
          </xsl:for-each>
        </xsl:variable>
        <xsl:variable name="init_param_tmp">
          <xsl:value-of select="substring-after(substring-before(substring-after($initialselection,$ident),'#'),'=')"/>
        </xsl:variable>
        <xsl:variable name="init_param">
          <xsl:choose>
            <xsl:when test="normalize-space($init_param_tmp)=''">
              <xsl:value-of select="ancestor::render_hotspot/@initparam"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$init_param_tmp"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="params">'+ 'NAME=<xsl:value-of select="$ident"/>;'+
            'url_base=<xsl:call-template name="get_url_base"/>;'+ 'serverUrl=<xsl:value-of
          select="$serverUrl"/>;'+

          <!--'INITPARAM=<xsl:value-of select="substring-after(substring-before(substring-after($initialselection,$ident),'#'),'=')"/>;'+-->
            'INITPARAM=<xsl:value-of select="normalize-space($init_param)"/>;'+
            'disabled=<xsl:value-of select="$noModify"/>;'+ 'rcardinality=<xsl:value-of
          select="$cardinality"/>;'+ 'image_src=<xsl:value-of select="@uri"/>;'+
          <!--'image_src=<xsl:call-template name="putResourceSource2"><xsl:with-param name="uri"><xsl:value-of select="@uri"/></xsl:with-param></xsl:call-template>;'+-->
          <xsl:for-each select="ancestor::presentation">
            <xsl:for-each select=".//render_hotspot//response_label">
              <xsl:variable name="num_param">
                <xsl:number/>
              </xsl:variable>
              <xsl:for-each select="attribute::*"> 'P<xsl:value-of
                  select="$num_param"/>_<xsl:value-of select="local-name(.)"/>=<xsl:value-of
                select="."/>;'+ </xsl:for-each> 'P<xsl:value-of
                select="$num_param"/>_text=<xsl:value-of select="normalize-space(.)"/>;'+ </xsl:for-each>
            <xsl:variable name="showdraw">
              <xsl:value-of select=".//render_hotspot/@showdraw"/>
            </xsl:variable>
            <xsl:if test="normalize-space($showdraw)!=''"> 'showDraw=<xsl:value-of
              select="normalize-space($showdraw)"/>;'+ </xsl:if>
            <xsl:variable name="showoptions">
              <xsl:value-of select=".//render_hotspot/@showoptions"/>
            </xsl:variable>
            <xsl:if test="normalize-space($showoptions)!=''"> 'showOptions=<xsl:value-of
              select="normalize-space($showoptions)"/>;'+ </xsl:if>
            <xsl:variable name="transp">
              <xsl:value-of select=".//render_hotspot/@transp"/>
            </xsl:variable>
            <xsl:if test="normalize-space($transp)!=''"> 'transp=<xsl:value-of
              select="normalize-space($transp)"/>;'+ </xsl:if>
            <xsl:variable name="style">
              <xsl:value-of select=".//render_hotspot/@style"/>
            </xsl:variable>
            <xsl:if test="normalize-space($style)!=''"> 'style=<xsl:value-of
              select="normalize-space($style)"/>;'+ </xsl:if>
            <xsl:variable name="maxnumber">
              <xsl:value-of select=".//render_hotspot/@maxnumber"/>
            </xsl:variable>
            <xsl:if test="normalize-space($maxnumber)!=''"> 'maxNumber=<xsl:value-of
              select="normalize-space($maxnumber)"/>;'+ </xsl:if>
          </xsl:for-each> '</xsl:variable>
        <SCRIPT>

          <!-- writeJavaPlugin('QVRenderHotspotAppletJS.class', '.', 'QVHotspotAppletJS.jar', '<xsl:value-of select="$params"/>', '<xsl:value-of select="$WIDTH"/>', '<xsl:value-of select="$HEIGHT"/>',
null, null,'qvApplet','<xsl:value-of select="$ident"/>',true); -->
          writeJavaApplet('QVRenderHotspotAppletJS.class', getAppletsBase(), 'QVHotspotAppletJS.jar',
            '<xsl:value-of select="$params"/>', '<xsl:value-of select="$WIDTH"/>', '<xsl:value-of
          select="$HEIGHT"/>', null, null,'qvApplet','<xsl:value-of select="$ident"/>',true);
          <!-- writeJavaPlugin('qvRenderHotspotApplet.class', '.', 'qvRenderHotspotApplet.jar', '<xsl:value-of select="$params"/>', '<xsl:value-of select="$WIDTH"/>', '<xsl:value-of select="$HEIGHT"/>',
null, null,'qvApplet','<xsl:value-of select="$ident"/>',true); -->
        </SCRIPT>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="getHotspotWidth">
    <xsl:variable name="temp2">
      <xsl:value-of select="ancestor::presentation//matimage/@width"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="normalize-space($temp2)=''">500</xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$temp2"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="getHotspotHeight">
    <xsl:variable name="temp3">
      <xsl:value-of select="ancestor::presentation//matimage/@height"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="normalize-space($temp3)=''">350</xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$temp3"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="getOrderingWidth">
    <xsl:variable name="temp2">
      <xsl:value-of select="translate(@orientation,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="@width!=''">
        <xsl:value-of select="@width"/>
      </xsl:when>
      <!-- <xsl:when test="$temp2='row'">600</xsl:when> -->
      <xsl:otherwise>600</xsl:otherwise>
      <!-- es troba indicada -->
    </xsl:choose>
  </xsl:template>
  <xsl:template name="getOrderingHeight">
    <xsl:variable name="temp2">
      <xsl:value-of select="translate(@orientation,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="@height!=''">
        <xsl:value-of select="@height"/>
      </xsl:when>
      <xsl:when test="descendant::mattext">
        <xsl:choose>
          <xsl:when test="$temp2='column'">
            <xsl:value-of select="count(response_label)*16.5"/>
            <!--xsl:number level="any" count="response_label"/-->
          </xsl:when>
          <xsl:otherwise>20</xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <!--xsl:when test="$temp2='row'">200</xsl:when-->
      <xsl:otherwise>350</xsl:otherwise>
      <!-- es troba indicada -->
    </xsl:choose>
  </xsl:template>
  <xsl:template name="putResourceSource">
    <!-- Es rep el param. uri i es retorna la url absoluta que servira per localitzar el
	recurs. En el cas que uri no contingui // es concatena a uri el valor que tingui
	atrib urlBase del node assessment del quadern. -->
    <xsl:param name="uri"/>
    <xsl:attribute name="src">
      <xsl:choose>
        <xsl:when test="contains($uri,'//')">
          <xsl:value-of select="$uri"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$urlBase"/>
          <xsl:value-of select="$uri"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
  </xsl:template>
  <xsl:template name="getResourceSource">
    <xsl:param name="uri"/>
    <xsl:choose>
      <xsl:when test="contains($uri,'//')">
        <xsl:value-of select="$uri"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$urlBase"/>
        <xsl:value-of select="$uri"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="putResourceSource2">
    <!-- IGUAL QUE ABANS PERO SENSE ATTRIBUTE -->
    <xsl:param name="uri"/>
    <xsl:choose>
      <xsl:when test="contains($uri,'//')">
        <xsl:value-of select="$uri"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$urlBase"/>
        <xsl:value-of select="$uri"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="get_hotspot_response_ident">
    <xsl:value-of select="ancestor-or-self::item/@ident"/>
    <xsl:text>--&gt;</xsl:text>
    <xsl:value-of select="ancestor::response_lid/@ident"/>
  </xsl:template>
  <!--xsl:template name="get_hotspot_response_ident">
  <xsl:value-of select="ancestor-or-self::item/@ident"/>
  <xsl:text>- -&gt;</xsl:text>
  <xsl:for-each select="ancestor::presentation">
   <xsl:for-each select=".//render_hotspot">
    <xsl:value-of select="ancestor::response_lid/@ident"/>
   </xsl:for-each>
  </xsl:for-each>
 </xsl:template-->
  <xsl:template name="get_response_ident">
    <xsl:value-of select="ancestor-or-self::item/@ident"/>
    <xsl:text>--&gt;</xsl:text>
    <xsl:value-of select="ancestor-or-self::response_lid/@ident"/>
    <xsl:value-of select="ancestor-or-self::response_str/@ident"/>
    <xsl:value-of select="ancestor-or-self::response_num/@ident"/>
    <xsl:value-of select="ancestor-or-self::response_xy/@ident"/>
  </xsl:template>
  <xsl:template match="*[local-name()='mataudio']">
    <object ID="RVOCX" CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" HEIGHT="40" WIDTH="200">
      <param name="SRC">
        <xsl:attribute name="value">
          <xsl:call-template name="getResourceSource">
            <xsl:with-param name="uri">
              <xsl:value-of select="@uri"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:attribute>
      </param>
      <!-- PARAM NAME="CONSOLE" VALUE="one"/ -->
      <EMBED NOJAVA="true" CONTROLS="ControlPanel" CONSOLE="one" HEIGHT="40" WIDTH="200">
        <xsl:call-template name="putResourceSource">
          <xsl:with-param name="uri">
            <xsl:value-of select="@uri"/>
          </xsl:with-param>
        </xsl:call-template>
      </EMBED>
      <noembed>
        <a>
          <xsl:attribute name="href">
            <xsl:call-template name="getResourceSource">
              <xsl:with-param name="uri">
                <xsl:value-of select="@uri"/>
              </xsl:with-param>
            </xsl:call-template>
          </xsl:attribute>
        </a>
      </noembed>
    </object>
    <!--EMBED VOLUME="128" WIDTH="150" HEIGHT="70" AUTOPLAY="false" HIDDEN="false" LOOP="false">
		<xsl:call-template name="putResourceSource">
			<xsl:with-param name="uri"><xsl:value-of select="@uri"/></xsl:with-param>
		</xsl:call-template-->
    <!-- <xsl:attribute name="src">
			<xsl:value-of select="@uri"/>
		</xsl:attribute> -->
    <!-- /EMBED -->
  </xsl:template>
  <xsl:template match="*[local-name()='matvideo']">
	<xsl:variable name="classid">
		<xsl:choose>
			<xsl:when test="normalize-space(@videotype)!='video/x-ms-wmv'">clsid:22d6f312-b0f6-11d0-94ab-0080c74c7e95</xsl:when>
			<xsl:when test="normalize-space(@videotype)!='application/x-mplayer2'">CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95</xsl:when>
			<xsl:otherwise>clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
    <table border="0" align="center">
      <tr>
        <td>
          <object ID="RVOCX">
  		    <xsl:attribute name="classid">
			  <xsl:value-of select="normalize-space($classid)"/>
		    </xsl:attribute>
            <xsl:if test="normalize-space(@width)!=''">
              <!-- no te width-->
              <xsl:attribute name="width">
                <xsl:value-of select="@width"/>
              </xsl:attribute>
            </xsl:if>
            <xsl:if test="normalize-space(@height)!=''">
              <!-- no te width-->
              <xsl:attribute name="height">
                <xsl:value-of select="@height"/>
              </xsl:attribute>
            </xsl:if>
            <param name="SRC">
              <xsl:attribute name="value">
                <xsl:call-template name="getResourceSource">
                  <xsl:with-param name="uri">
                    <xsl:value-of select="@uri"/>
                  </xsl:with-param>
                </xsl:call-template>
              </xsl:attribute>
            </param>
            <param name="CONTROLS" value="ImageWindow"/>
            <param name="CONSOLE" value="one"/>
            <param name="AUTOSTART" value="false"/>
            <embed nojava="true" controls="ImageWindow" console="one" autostart="false" pluginspage="http://get.real.com/RCH1/195.141.101.151/20a4e60630d83dd90d05/windows/mrkt/RN30TD/RealPlayer10GOLD_bb.exe">
              <xsl:call-template name="putResourceSource">
                <xsl:with-param name="uri">
                  <xsl:value-of select="@uri"/>
                </xsl:with-param>
              </xsl:call-template>
              <xsl:if test="normalize-space(@width)!=''">
                <!-- no te width-->
                <xsl:attribute name="width">
                  <xsl:value-of select="@width"/>
                </xsl:attribute>
              </xsl:if>
              <xsl:if test="normalize-space(@height)!=''">
                <!-- no te width-->
                <xsl:attribute name="height">
                  <xsl:value-of select="@height"/>
                </xsl:attribute>
              </xsl:if>
              <xsl:if test="normalize-space(@videotype)!=''">
                <xsl:attribute name="type">
                  <xsl:value-of select="@videotype"/>
                </xsl:attribute>
              </xsl:if>
            </embed>
            <noembed>
              <a>
                <xsl:attribute name="href">
                  <xsl:call-template name="getResourceSource">
                    <xsl:with-param name="uri">
                      <xsl:value-of select="@uri"/>
                    </xsl:with-param>
                  </xsl:call-template>
                </xsl:attribute>
              </a>
            </noembed>
          </object>
        </td>
      </tr>
    <xsl:if test="normalize-space(@videotype)!='video/x-ms-wmv' and normalize-space(@videotype)!='application/x-mplayer2'">
      <tr>
        <td>
          <object ID="RVOCX" CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" HEIGHT="40">
            <xsl:if test="normalize-space(@width)!=''">
              <!-- no te width-->
              <xsl:attribute name="width">
                <xsl:value-of select="@width"/>
              </xsl:attribute>
            </xsl:if>
            <param name="SRC">
              <xsl:attribute name="value">
                <xsl:call-template name="getResourceSource">
                  <xsl:with-param name="uri">
                    <xsl:value-of select="@uri"/>
                  </xsl:with-param>
                </xsl:call-template>
              </xsl:attribute>
            </param>
            <PARAM NAME="CONSOLE" VALUE="one"/>
            <EMBED NOJAVA="true" CONTROLS="ControlPanel" CONSOLE="one" HEIGHT="40">
              <xsl:call-template name="putResourceSource">
                <xsl:with-param name="uri">
                  <xsl:value-of select="@uri"/>
                </xsl:with-param>
              </xsl:call-template>
              <xsl:if test="normalize-space(@width)!=''">
                <!-- no te width-->
                <xsl:attribute name="width">
                  <xsl:value-of select="@width"/>
                </xsl:attribute>
              </xsl:if>
            </EMBED>
          </object>
        </td>
      </tr>
	 </xsl:if>
    </table>
    <BR/>
    <!--xsl:if test="normalize-space(@width)!=''">
			<xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>
		</xsl:if>
		<xsl:if test="normalize-space(@height)!=''">
			<xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>
		</xsl:if>
		
		<xsl:call-template name="putResourceSource">
			<xsl:with-param name="uri"><xsl:value-of select="@uri"/></xsl:with-param>
		</xsl:call-template -->
    <!-- <xsl:attribute name="src">
			<xsl:value-of select="@uri"/>
		</xsl:attribute> -->
    <!-- /EMBED -->
  </xsl:template>
  <xsl:template match="*[local-name()='qticomment']"/>
  <xsl:template name="get_render_type">
    <xsl:for-each select="ancestor::*">
      <xsl:variable name="temp">
        <xsl:value-of select="local-name(.)"/>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="$temp='render_choice'">choice</xsl:when>
        <xsl:when test="$temp='render_fib'">fib</xsl:when>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="*[local-name()='sectionfeedback']">
    <xsl:apply-templates/>
    <BR/>
  </xsl:template>
  <xsl:template name="initParamQVApplet">
    <xsl:param name="item_ident"/>
    <xsl:value-of select="substring-before(substring-after($initialselection,$item_ident),'#')"/>
  </xsl:template>
  <xsl:template name="check_field_type">
    <!-- Retorna javascript que comprovara que en els camps de text
	s'escriguin valors valids segons el fibtype -->
    <xsl:param name="type"/>
    <xsl:choose>
      <xsl:when test="$type='Integer'">verify_integer(this.value) </xsl:when>
      <xsl:when test="$type='Decimal'">verify_decimal(this.value) </xsl:when>
      <xsl:when test="$type='Scientific'">verify_scientific(this.value) </xsl:when>
      <xsl:when test="$type='Boolean'">verify_boolean(this.value) </xsl:when>
      <xsl:otherwise/>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="put_num_item">
    <xsl:param name="num"/>
    <TABLE WIDTH="20" HEIGHT="20" BGCOLOR="#0000ff" BORDER="1">
      <TR>
        <TD ALIGN="CENTER">
          <FONT SIZE="10" COLOR="#ffffff">
            <xsl:value-of select="$num"/>
          </FONT>
        </TD>
      </TR>
    </TABLE>
  </xsl:template>
  <xsl:template name="loop_print">
    <xsl:param name="i"/>
    <xsl:param name="max"/>
    <xsl:param name="symbol"/>
    <xsl:if test="$i &lt;= $max">
      <!--    body of the loop goes here    -->
      <xsl:value-of select="$symbol"/>
    </xsl:if>
    <xsl:if test="$i &lt;= $max">
      <xsl:call-template name="loop_print">
        <xsl:with-param name="i">
          <xsl:value-of select="$i + 1"/>
        </xsl:with-param>
        <xsl:with-param name="max">
          <xsl:value-of select="$max"/>
        </xsl:with-param>
        <xsl:with-param name="symbol">
          <xsl:value-of select="$symbol"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  <xsl:template name="getSectionAdditionalMaterial">
    <xsl:for-each select="child::*">
      <xsl:choose>
        <xsl:when test="local-name()='flow_mat'">
          <xsl:call-template name="getSectionAdditionalMaterial"/>
        </xsl:when>
        <xsl:when test="local-name()='material'">
          <xsl:apply-templates select="."/>
        </xsl:when>
      </xsl:choose>
    </xsl:for-each>
    <xsl:if test="local-name()='flow_mat'">
      <xsl:call-template name="insertFlow"/>
    </xsl:if>
  </xsl:template>
  <xsl:template name="getAssessment">
    <xsl:param name="ident_item"/>
    <xsl:apply-templates select="//item[@ident=$ident_item]/presentation/*/material[position()=1]/mattext[position()=1]"/>
  </xsl:template>
  <xsl:template name="item-attrs">
  </xsl:template>
  <xsl:template name="item-assessment-attrs">
  </xsl:template>
  <xsl:template name="getItemAdditionalMaterial">
    <xsl:param name="ident_item"/>
    <xsl:for-each select="//item[@ident=$ident_item]/presentation/*/material[position()=1]/child::*">
      <xsl:if test="position()!=1">
        <xsl:call-template name="putAdditionalMaterial"/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="getResponses">
    <xsl:param name="ident_item"/>
    <xsl:for-each select="//item[@ident=$ident_item]/presentation/child::*">
      <xsl:choose>
        <xsl:when test="local-name()='flow'">
          <xsl:for-each select="child::*">
            <xsl:if test="not(local-name()='material' and position()=1)">
              <xsl:call-template name="getFlowResponses"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:if test="not(local-name()='material' and position()=1)">
            <xsl:call-template name="getFlowResponses"/>
          </xsl:if>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>
	
  <xsl:template name="getHintMaterial">
    <xsl:param name="item_ident"/>
    <xsl:for-each select="//item[@ident=$item_ident]/itemfeedback/hint/hintmaterial/*/child::*">
        <xsl:call-template name="putAdditionalMaterial"/>
    </xsl:for-each>
  </xsl:template>
	
  <xsl:template name="getSolutionMaterial">
    <xsl:param name="item_ident"/>
    <xsl:for-each select="//item[@ident=$item_ident]/itemfeedback/solution/solutionmaterial/*/child::*">
        <xsl:call-template name="putAdditionalMaterial"/>
    </xsl:for-each>
  </xsl:template>
	
  <xsl:template name="insertFlow">
    <BR/>
  </xsl:template>
  <xsl:template name="response_lid">
    <xsl:if test="@rcardinality='Ordered'">
      <xsl:for-each select="child::*[local-name()='material']">
        <xsl:for-each select="descendant::*">
          <xsl:choose>
            <xsl:when test="local-name()='matimage'">
              <xsl:call-template name="putMatimage"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates select="."/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:for-each>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>
  <xsl:template name="getFlowResponses">
    <!-- Afegit apply-templates per pregunta equivalents-->
    <xsl:if test="local-name()='response_lid'">
      <xsl:call-template name="response_lid"/>
    </xsl:if>
    <xsl:for-each select="child::*">
      <xsl:choose>
        <xsl:when test="local-name()='flow'">
          <xsl:call-template name="getFlowResponses"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="."/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
    <xsl:if test="local-name()='flow' and not (contains(material/mattext, '&lt;table ') or contains(material/mattext, '&lt;thead')  or contains(material/mattext, '&lt;tbody') or contains(material/mattext, '&lt;tr') or contains(material/mattext, '&lt;td') or contains(material/mattext, '&lt;/tr') or contains(material/mattext, '&lt;td') or contains(material/mattext, '&lt;/td'))">
      <xsl:call-template name="insertFlow"/>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="*[local-name()='material']">
   <xsl:apply-templates/>
    <!--xsl:for-each select="child">
      <xsl:apply-templates select="."/>
    </xsl:for-each-->
  </xsl:template>
  
  <xsl:template match="*[local-name()='mattext']">
        <xsl:call-template name="putMattext"/>   
<!--    <xsl:choose>
      <xsl:when test="contains(@texttype,'html')">
        <xsl:variable name="html">
          <xsl:call-template name="escape-apos">
            <xsl:with-param name="string">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:variable>
        <SCRIPT LANGUAGE="JavaScript"> filter('<xsl:value-of select="$html"/>'); </SCRIPT>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
      </xsl:otherwise>
    </xsl:choose>
-->    
  </xsl:template>
  <!--xsl:template match="*[local-name()='matimage']">
  <xsl:call-template name="putMatimage"/>
 </xsl:template-->
  <xsl:template match="*[local-name()='matvideo']">
    <xsl:call-template name="putMatvideo"/>
  </xsl:template>
  <xsl:template match="*[local-name()='mataudio']">
    <xsl:call-template name="putMataudio"/>
  </xsl:template>
  <xsl:template match="*[local-name()='matbreak']">
    <xsl:call-template name="putMatbreak"/>
  </xsl:template>
  <xsl:template name="putAdditionalMaterial">
    <xsl:choose>
      <xsl:when test="local-name()='mattext'">
        <xsl:call-template name="putMattext"/>
      </xsl:when>
      <xsl:when test="local-name()='matimage'">
        <xsl:call-template name="putMatimage"/>
      </xsl:when>
      <xsl:when test="local-name()='matvideo'">
        <xsl:call-template name="putMatvideo"/>
      </xsl:when>
      <xsl:when test="local-name()='mataudio'">
        <xsl:call-template name="putMataudio"/>
      </xsl:when>
      <xsl:when test="local-name()='matbreak'">
        <xsl:call-template name="putMatbreak"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="putMatbreak">
    <BR/>
  </xsl:template>
  <xsl:template name="replace-string">
    <xsl:param name="text"/>
    <xsl:param name="old"/>
    <xsl:param name="new"/>
    <xsl:choose>
      <xsl:when test="contains($text, $old)">
        <xsl:variable name="before" select="substring-before($text, $old)"/>
        <xsl:variable name="after" select="substring-after($text, $old)"/>
        <xsl:variable name="prefix" select="concat($before, $new)"/>
        <xsl:value-of select="$before"/>
        <xsl:value-of select="$new"/>
        <xsl:call-template name="replace-string">
          <xsl:with-param name="text" select="$after"/>
          <xsl:with-param name="old" select="$old"/>
          <xsl:with-param name="new" select="$new"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="filter-html">
    <xsl:param name="text"/>
    <xsl:variable name="filtered-href">
	    <xsl:call-template name="add-assessment-url">
	      <xsl:with-param name="text">
	        <xsl:value-of select="$text"/>
	      </xsl:with-param>
	    </xsl:call-template>
	</xsl:variable>
    <xsl:call-template name="add-assessment-url-img">
      <xsl:with-param name="text">
        <xsl:value-of select="$filtered-href"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="add-assessment-url">
    <xsl:param name="text"/>
    <xsl:choose>
      <xsl:when test="contains($text, '&lt;a ')">
        <xsl:variable name="href">
          <xsl:variable name="href_quot">
            <xsl:value-of select="substring-before(substring-after(substring-after($text,'&lt;a '),'href=&quot;'),'&quot;')"/>
          </xsl:variable>
          <xsl:choose>
            <xsl:when test="$href_quot!=''">
              <xsl:value-of select="$href_quot"/>
            </xsl:when>
            <xsl:otherwise>#160;</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <!-- replace href link-->
        <xsl:variable name="new_href">
          <xsl:choose>
            <xsl:when test="starts-with($href, 'http:')">
              <xsl:value-of select="$href"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$QTIUrl"/>/../<xsl:value-of select="$href"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="html_text">
          <xsl:call-template name="replace-string">
            <xsl:with-param name="text">
              <xsl:value-of select="substring-before($text,'&lt;/a&gt;')"/>&lt;/a&gt;</xsl:with-param>
            <xsl:with-param name="old">&lt;a </xsl:with-param>
            <xsl:with-param name="new">&lt;a target="_blank" </xsl:with-param>
          </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="replace-string">
          <xsl:with-param name="text">
            <xsl:value-of select="$html_text"/>
          </xsl:with-param>
          <xsl:with-param name="old">
            <xsl:value-of select="$href"/>
          </xsl:with-param>
          <xsl:with-param name="new">
            <xsl:value-of select="$new_href"/>
          </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="add-assessment-url">
          <xsl:with-param name="text">
            <xsl:value-of select="substring-after($text,'&lt;/a&gt;')"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="add-assessment-url-img">
    <xsl:param name="text"/>
    <xsl:choose>
      <xsl:when test="contains($text, '&lt;img ')">
        <xsl:variable name="src">
            <xsl:value-of select="substring-before(substring-after(substring-after($text,'&lt;img'),'src=&quot;'),'&quot;')"/>
        </xsl:variable>
        <!-- replace src link-->
        <xsl:variable name="new_src">
          <xsl:choose>
            <xsl:when test="starts-with($src, 'http:')">
              <xsl:value-of select="$src"/>
            </xsl:when>
            <xsl:when test="starts-with($src, '/')">
              http://clic.xtec.cat<xsl:value-of select="$src"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$QTIUrl"/>/../<xsl:value-of select="$src"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:value-of select="substring-before($text,'&lt;img')"/>&lt;img
        <xsl:call-template name="replace-string">
          <xsl:with-param name="text">
            <xsl:value-of select="substring-before(substring-after($text,'&lt;img'),'&gt;')"/>
          </xsl:with-param>
          <xsl:with-param name="old">
            <xsl:value-of select="$src"/>
          </xsl:with-param>
          <xsl:with-param name="new">
            <xsl:value-of select="$new_src"/>
          </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="add-assessment-url-img">
          <xsl:with-param name="text">
            &gt;<xsl:value-of select="substring-after(substring-after($text,'&lt;img'),'&gt;')"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--xsl:template name="add-assessment-url">
    <xsl:param name="text"/>
    <xsl:choose>
      <xsl:when test="contains($text, '&lt;a')">
        <xsl:variable name="href">
          <xsl:variable name="href_quot">
            <xsl:value-of select="substring-before(substring-after(substring-after($text,'&lt;a'),'href=&quot;'),'&quot;')"/>
          </xsl:variable>
          <xsl:choose>
            <xsl:when test="$href_quot!=''">
              <xsl:value-of select="$href_quot"/>
            </xsl:when>
            <xsl:otherwise>#160;</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="new_href">
          <xsl:choose>
            <xsl:when test="starts-with($href, 'http:')">
              <xsl:value-of select="$href"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$QTIUrl"/>/../<xsl:value-of select="$href"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="html_text">
          <xsl:call-template name="replace-string">
            <xsl:with-param name="text">
              <xsl:value-of select="substring-before($text,'&lt;/a&gt;')"/>&lt;/a&gt;</xsl:with-param>
            <xsl:with-param name="old">&lt;a</xsl:with-param>
            <xsl:with-param name="new">&lt;a target="_blank" </xsl:with-param>
          </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="replace-string">
          <xsl:with-param name="text">
            <xsl:value-of select="$html_text"/>
          </xsl:with-param>
          <xsl:with-param name="old">
            <xsl:value-of select="$href"/>
          </xsl:with-param>
          <xsl:with-param name="new">
            <xsl:value-of select="$new_href"/>
          </xsl:with-param>
        </xsl:call-template>
        
        <xsl:call-template name="add-assessment-url">
          <xsl:with-param name="text">
            <xsl:value-of select="substring-after($text,'&lt;/a&gt;')"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains($text, '&lt;img')">
        <xsl:variable name="src">
          <xsl:variable name="src_quot">
           <xsl:value-of select="substring-before(substring-after(substring-after($text,'&lt;img'),'src=&quot;'),'&quot;')"/>
          </xsl:variable>
          <xsl:choose>
            <xsl:when test="$src_quot!=''">
              <xsl:value-of select="$src_quot"/>
            </xsl:when>
            <xsl:otherwise>#160;</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="new_src">
          <xsl:choose>
            <xsl:when test="starts-with($src, 'http:')">
              <xsl:value-of select="$src"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$QTIUrl"/>/../<xsl:value-of select="$src"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:call-template name="replace-string">
          <xsl:with-param name="text">
            <xsl:value-of select="substring-before($text,'&gt;')"/>
          </xsl:with-param>
          <xsl:with-param name="old">
            <xsl:value-of select="$src"/>
          </xsl:with-param>
          <xsl:with-param name="new">
            <xsl:value-of select="$new_src"/>
          </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="add-assessment-url">
          <xsl:with-param name="text">
            <xsl:value-of select="substring-after($text,'&gt;')"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template-->
  <xsl:template name="putMattext">
    <xsl:choose>
      <xsl:when test="contains(@texttype,'html')">
        <!--xsl:value-of select="." disable-output-escaping="yes"/-->
        <xsl:variable name="filtered_html">
          <xsl:call-template name="filter-html">
            <xsl:with-param name="text">
              <xsl:value-of select="." disable-output-escaping="yes"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="$filtered_html" disable-output-escaping="yes"/>
        <!--xsl:copy-of select="."/-->
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="."/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="putMatimage">
    <xsl:choose>
      <xsl:when test="normalize-space(@imagtype)='application/x-shockwave-flash'">
        <CENTER>
          <xsl:call-template name="matflash"/>
        </CENTER>
      </xsl:when>
      <xsl:otherwise>
        <IMG>
          <xsl:attribute name="src">
            <xsl:call-template name="getURL">
              <xsl:with-param name="uri">
                <xsl:value-of select="@uri"/>
              </xsl:with-param>
            </xsl:call-template>
          </xsl:attribute>
          <xsl:if test="normalize-space(@width)!=''">
            <xsl:attribute name="width">
              <xsl:value-of select="@width"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:if test="normalize-space(@height)!=''">
            <xsl:attribute name="height">
              <xsl:value-of select="@height"/>
            </xsl:attribute>
          </xsl:if>
        </IMG>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="putMataudio">
    <xsl:variable name="align">
      <xsl:choose>
        <xsl:when test="normalize-space(@align)!=''">
          <xsl:value-of select="normalize-space(@align)"/>
        </xsl:when>
        <xsl:otherwise>left</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <SPAN>
      <xsl:attribute name="align">
        <xsl:value-of select="normalize-space($align)"/>
      </xsl:attribute>
      <object CLASSID="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" HEIGHT="18" WIDTH="200">
        <xsl:attribute name="id">
          <xsl:value-of select="generate-id()"/>
        </xsl:attribute>
        <param name="SRC">
          <xsl:attribute name="value">
            <xsl:call-template name="getResourceSource">
              <xsl:with-param name="uri">
                <xsl:value-of select="@uri"/>
              </xsl:with-param>
            </xsl:call-template>
          </xsl:attribute>
        </param>
        <param name="AUTOSTART" value="false"/>
        <!-- PARAM NAME="CONSOLE" VALUE="one"/ -->
        <EMBED NOJAVA="true" CONTROLS="ControlPanel" CONSOLE="one" HEIGHT="18" WIDTH="200"
          autostart="false" autoplay="false">
          <xsl:call-template name="putResourceSource">
            <xsl:with-param name="uri">
              <xsl:value-of select="@uri"/>
            </xsl:with-param>
          </xsl:call-template>
        </EMBED>
        <noembed>
          <a>
            <xsl:attribute name="href">
              <xsl:call-template name="getResourceSource">
                <xsl:with-param name="uri">
                  <xsl:value-of select="@uri"/>
                </xsl:with-param>
              </xsl:call-template>
            </xsl:attribute>
          </a>
        </noembed>
      </object>
      <IMG src="web/imatges/blanc.gif" width="5"/>
    </SPAN>
  </xsl:template>
  <xsl:template name="putMatvideo">
    <xsl:variable name="id">
      <xsl:value-of select="generate-id()"/>
    </xsl:variable>
    <xsl:variable name="uri">
      <xsl:call-template name="getURL">
        <xsl:with-param name="uri">
          <xsl:value-of select="@uri"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="videotype">
      <xsl:choose>
        <xsl:when test="normalize-space(@videotype)!=''">
          <xsl:value-of select="normalize-space(@videotype)"/>
        </xsl:when>
        <xsl:otherwise>
			<xsl:choose>
				<xsl:when test="substring-before(substring-after($uri,'.'),'wmv')">video/x-ms-wmv</xsl:when>
				<xsl:otherwise>audio/x-pn-realaudio-plugin</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
	<xsl:variable name="classid">
		<xsl:choose>
			<xsl:when test="normalize-space($videotype)='video/x-ms-wmv'">clsid:22d6f312-b0f6-11d0-94ab-0080c74c7e95</xsl:when>
			<xsl:when test="normalize-space($videotype)='application/x-mplayer2'">CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95</xsl:when>
			<xsl:otherwise>clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
    <xsl:variable name="width">
      <xsl:choose>
        <xsl:when test="normalize-space(@width)!=''">
          <xsl:value-of select="normalize-space(@width)"/>
        </xsl:when>
        <xsl:otherwise>250</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="height">
      <xsl:choose>
        <xsl:when test="normalize-space(@height)!=''">
          <xsl:value-of select="normalize-space(@height)"/>
        </xsl:when>
        <xsl:otherwise>210</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <CENTER>
      <object>
	    <xsl:attribute name="classid">
		  <xsl:value-of select="normalize-space($classid)"/>
		</xsl:attribute>
        <xsl:attribute name="ID">
          <xsl:value-of select="$id"/>
        </xsl:attribute>
        <xsl:attribute name="WIDTH">
          <xsl:value-of select="$width"/>
        </xsl:attribute>
        <xsl:attribute name="HEIGHT">
          <xsl:value-of select="$height"/>
        </xsl:attribute>
        <param NAME="controls" VALUE="ImageWindow"/>
        <param NAME="autostart" VALUE="false"/>
        <param NAME="console">
          <xsl:attribute name="VALUE">
            <xsl:value-of select="$id"/>
          </xsl:attribute>
        </param>
        <param NAME="src">
          <xsl:attribute name="VALUE">
            <xsl:value-of select="$uri"/>
          </xsl:attribute>
        </param>
        <embed CONTROLS="ImageWindow" AUTOSTART="false">
          <xsl:attribute name="src">
            <xsl:value-of select="$uri"/>
          </xsl:attribute>
          <xsl:attribute name="console">
            <xsl:value-of select="$id"/>
          </xsl:attribute>
          <xsl:attribute name="type">
            <xsl:value-of select="$videotype"/>
          </xsl:attribute>
          <xsl:attribute name="width">
            <xsl:value-of select="$width"/>
          </xsl:attribute>
          <xsl:attribute name="heigth">
            <xsl:value-of select="$height"/>
          </xsl:attribute>
        </embed>
      </object>
      <br/>
    <xsl:if test="normalize-space($classid)='clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA'">
      <object CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" HEIGHT="60">
        <xsl:attribute name="id">
          <xsl:value-of select="$id"/>
        </xsl:attribute>
        <xsl:attribute name="width">
          <xsl:value-of select="$width"/>
        </xsl:attribute>
        <param NAME="controls" VALUE="ControlPanel,StatusBar"/>
        <param NAME="console">
          <xsl:attribute name="VALUE">
            <xsl:value-of select="$id"/>
          </xsl:attribute>
        </param>
        <embed CONTROLS="ControlPanel,StatusBar" HEIGHT="60" AUTOSTART="false">
          <xsl:attribute name="type">
            <xsl:value-of select="$videotype"/>
          </xsl:attribute>
          <xsl:attribute name="console">
            <xsl:value-of select="$id"/>
          </xsl:attribute>
          <xsl:attribute name="width">
            <xsl:value-of select="$width"/>
          </xsl:attribute>
        </embed>
      </object>
	 </xsl:if>
    </CENTER>
  </xsl:template>
  <xsl:template name="getURL">
    <xsl:param name="uri"/>
    <xsl:choose>
      <xsl:when test="contains($uri,'//')">
        <xsl:value-of select="$uri"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$urlBase"/>
        <xsl:value-of select="$uri"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="escape-apos">
    <xsl:param name="string"/>
    <xsl:choose>
      <xsl:when test="contains($string, &quot;&apos;&quot;)">
        <xsl:value-of select="substring-before($string, &quot;&apos;&quot;)"/>
        <xsl:text>\'</xsl:text>
        <xsl:call-template name="escape-apos">
          <xsl:with-param name="string" select="substring-after($string, &quot;&apos;&quot;)"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$string"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="getAssessmentURL">
    <xsl:value-of select="$QTIUrl"/>/.. </xsl:template>
  <xsl:template name="isPreviewMode">
    <xsl:choose>
      <xsl:when test="//assignment">
        <xsl:choose>
          <xsl:when test="//assignment/@id='null' or //assignment/@id=''">true</xsl:when>
          <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="normalize-space($assignacioId)=''">true</xsl:when>
      <xsl:otherwise>false</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!-- #################### UTIL ################# -->
  <xsl:template name="get_action_url">
    <xsl:variable name="url">
      <xsl:value-of select="$serverUrl"/>?<xsl:choose>
        <xsl:when test="$assignacioId!='' and $assignacioId!='null'">assignacioId=<xsl:value-of select="$assignacioId"/>
        </xsl:when>
        <xsl:otherwise>quadernURL=<xsl:value-of select="$QTIUrl"/>
        </xsl:otherwise>
      </xsl:choose>&amp;quadernXSL=<xsl:value-of
        select="$quadernXSL"/>&amp;currentSect=<xsl:value-of select="$section_number"/></xsl:variable>
    <xsl:value-of select="normalize-space($url)"/>
  </xsl:template>
  
<!-- #################### SECTION ################# -->
 <xsl:template match="*[local-name()='questestinterop']">
  <HTML>
   <xsl:call-template name="html_head"/>
   <BODY onload="window.focus();" >
     <xsl:call-template name="html_body_attrs"/>
     <!-- Declare FORM -->
     <SCRIPT>setScoring('<xsl:value-of select="$scoring"/>');</SCRIPT>
     <FORM method="post">
      <xsl:attribute name="onSubmit">return(doSubmit(<xsl:value-of select="$needResponse"/>))</xsl:attribute>
       <xsl:attribute name="action"><xsl:call-template name="get_action_url"/></xsl:attribute>
      <INPUT type="HIDDEN" name="es_correccio">
       <!-- Aquest camp ocult indica si estem mostrant la correccio -->
       <xsl:attribute name="value">
        <xsl:value-of select="$es_correccio"/>
       </xsl:attribute>
      </INPUT>
      <INPUT type="HIDDEN" name="param"/>
      <INPUT type="HIDDEN" name="interaction"/>
      <INPUT type="HIDDEN" name="time"/>
      <INPUT type="HIDDEN" name="next" value="-1"/>
      <!-- full que vol passar a visualitzar -->
      <INPUT type="HIDDEN" name="wantCorrect"/>
      <!-- vol veure la correccio -->
      <INPUT type="HIDDEN" name="QTIUrl">
       <!-- Aquest camp ocult indica el param de la URL on es troba l'XML original -->
       <xsl:attribute name="value">
        <xsl:value-of select="$QTIUrl"/>
       </xsl:attribute>
      </INPUT>
          
      <xsl:call-template name="questestinterop_body"/>

     </FORM>
    <SCRIPT> 
    	select_responses(document.forms[0],"<xsl:value-of select="$initialselection"/>");
    	ordenaItems(<xsl:value-of select="count(//section[position()=$section_number]/item)"/>,<xsl:call-template name="getRandomizableItems"></xsl:call-template>); <!-- Albert -->
    </SCRIPT>
    <xsl:if test="$noModify='true' or $estat_lliurament='corregit'">
     <SCRIPT> disableAll(); </SCRIPT>
    </xsl:if>
   </BODY>
  </HTML>
 </xsl:template>

  <xsl:template match="*[local-name()='assessment']">
    <xsl:apply-templates select="section[position()=$section_number]|*[local-name()='item']"/>
  </xsl:template>
  
  <xsl:template match="*[local-name()='section']">
    <xsl:apply-templates select="*[local-name()='presentation_material']"/>
    <xsl:apply-templates select="*[local-name()='section']|*[local-name()='item']"/>
    <xsl:if test="$noModify='false'">
      <SCRIPT> startTimerISO("<xsl:value-of
          select="ancestor::assessment/@duration"/>","<xsl:value-of select="$notebookTime"/>",
          "<xsl:value-of select="@duration"/>","<xsl:value-of select="$sectionTime"/>"); </SCRIPT>
    </xsl:if>
  </xsl:template>

 <xsl:template match="*[local-name()='presentation_material']">
  <TABLE width="100%" border="0" cellspacing="10" cellpadding="0" class="section-additional-material">
   <TR>
    <TD>
     <xsl:call-template name="getSectionAdditionalMaterial"/>
    </TD>
   </TR>
  </TABLE>
  <BR/>
 </xsl:template>
 <xsl:template match="*[local-name()='item']">
  <xsl:variable name="it_ident">
   <xsl:value-of select="@ident"/>
  </xsl:variable>

  <xsl:variable name="showInteraction">
   <xsl:call-template name="show_interaction">
    <xsl:with-param name="ident_item"><xsl:value-of select="$it_ident"/></xsl:with-param>
   </xsl:call-template>
  </xsl:variable>

  <A>
   <xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/>
   </xsl:attribute>
  </A>
  <TABLE width="100%" height="60" border="0" cellspacing="0" cellpadding="0" class="item">
   <xsl:call-template name="item-attrs"/>   
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="0">
      <TR>
       <TD class="item-assessment">
        <xsl:call-template name="item-assessment-attrs"/>
        <xsl:call-template name="getAssessment">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>
       </TD>
      </TR>
     </TABLE>
    </TD>
   </TR>
   <xsl:if test="count(//item[@ident=$it_ident]/presentation/*/material[position()=1]/child::*)>1">
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="0" class="item-additional-material">
      <TR>
       <TD>
        <xsl:call-template name="getItemAdditionalMaterial">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>
       </TD>
      </TR>
     </TABLE>
    </TD>
   </TR>
   </xsl:if>
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="5" cellspacing="0" class="item-response">
      <TR>
       <TD>
        <xsl:call-template name="getResponses">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>
       </TD>
      </TR>
     </TABLE>
    </TD>
   </TR>
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="10" class="item-feedback">
      <TR>
       <xsl:call-template name="getItemFeedback">
        <xsl:with-param name="item_ident">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
      </TR>
     </TABLE>
    </TD>
   </TR>
   <xsl:if test="normalize-space($showInteraction)='true'">
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="10" class="item-interaction">
      <TR>
       <TD>
        <xsl:call-template name="interactions">
         <xsl:with-param name="item_ident">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
         <xsl:with-param name="index">1</xsl:with-param>
        </xsl:call-template>
       </TD>
      </TR>
     </TABLE>
    </TD>
   </TR>
   </xsl:if>
  </TABLE>
  <br/>
 </xsl:template>

<!-- #################### INTERACTION ################# -->
  <xsl:template name="interaction">
    <!-- Si existeix alguna interaccio a mostrar per l'item amb ident=item_ident es mostra-->
    <xsl:param name="item_ident"/>
    <xsl:param name="index"/>
    <!-- A item_ident_num deixo el nom de l'ident + el numero d'interaccio que busco -->
    <xsl:variable name="item_ident_num">
      <xsl:value-of select="$item_ident"/>_<xsl:value-of select="$index"/>_view</xsl:variable>
    <xsl:variable name="interaction_to_show">
      <!-- forma: (item_ident)_(index)_view(autor)=(valor) -->
      <xsl:value-of select="substring-before(substring-after($displayinteraction,$item_ident_num),'#')"/>
    </xsl:variable>
    <xsl:variable name="req_view">
      <xsl:value-of select="substring-before($interaction_to_show,'=')"/>
    </xsl:variable>
    <!-- Qui ho ha escrit -->
    <xsl:variable name="interaction_clean">
      <xsl:value-of select="substring-after($interaction_to_show,'=')"/>
    </xsl:variable>
    <!-- text que ha escrit -->
    <xsl:choose>
      <xsl:when test="normalize-space($interaction_to_show)!=''">
        <!-- Si existeix alguna interaccio per aquesta interaccio es mostra i tambe es mira si hi ha mes interaccions. -->
        <tr>
          <xsl:choose>
           <xsl:when test="$req_view='teacher'"><xsl:attribute name="class">item-teacher-interaction</xsl:attribute></xsl:when>
           <xsl:otherwise><xsl:attribute name="class">item-candidate-interaction</xsl:attribute></xsl:otherwise>
          </xsl:choose>
          <xsl:choose>
            <xsl:when test="$interaction_clean='|'">
              <!-- Ha demanat posar aquesta interaccio -->
              <TD colspan="2">
                <xsl:if test="$view='teacher'">
                  <xsl:attribute name="class">item-teacher-interaction</xsl:attribute>
                </xsl:if>
                <table border="0" cellspacing="0" cellpadding="0">
                  <TR>
                  	<TD>
                     <xsl:choose>
                     	<xsl:when test="$view='teacher'"><xsl:attribute name="class">item-teacher-interaction</xsl:attribute>professor</xsl:when>
                     	<xsl:when test="$view='candidate'"><xsl:attribute name="class">item-candidate-interaction</xsl:attribute>alumne</xsl:when>
                     </xsl:choose>
					 <IMG width="30" height="1" border="0"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/blanc.gif</xsl:attribute></IMG>
                  	</TD>
                    <TD class="item-interaction" width="100%">
                      <input type="text" style="width: 98%;">
                        <xsl:attribute name="name">interaccio_<xsl:value-of
                            select="$item_ident"/>_<xsl:value-of select="$index"/>_view<xsl:value-of select="$view"/>
                        </xsl:attribute>
                      </input>
                    </TD>
                    <TD>
                     <xsl:call-template name="put_send_interaction_button"/>
                    </TD>
                  </TR>
                </table>
              </TD>
            </xsl:when>
            <xsl:otherwise>
              <!-- La interaccio ja estava posada -->
              <TD width="30" class="item-interaction-title">
                <xsl:choose>
                  <xsl:when test="$req_view='candidate'">alumne</xsl:when>
                  <xsl:when test="$req_view='teacher'">professor</xsl:when>
                </xsl:choose>
              </TD>
              <TD>
                <xsl:value-of select="$interaction_clean"/>
                <BR/>
              </TD>
            </xsl:otherwise>
          </xsl:choose>
        </tr>
        <xsl:call-template name="interaction">
          <xsl:with-param name="item_ident">
            <xsl:value-of select="$item_ident"/>
          </xsl:with-param>
          <xsl:with-param name="index">
            <xsl:value-of select="$index + 1"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$index=1">
        <TD colspan="2" class="item-interaction">&#160;</TD>
      </xsl:when>
    </xsl:choose>
  </xsl:template>


 <xsl:template name="put_add_interaction_button">
  <xsl:variable name="show_interaction_button"><xsl:call-template name="show_interaction_button"/></xsl:variable>  
  <A title="Afegir una nova intervenci&#243; a la zona de di&#224;leg">
     <xsl:attribute name="href">#item_<xsl:value-of select="@ident"/></xsl:attribute>
     <xsl:attribute name="onClick"><xsl:if test="$show_interaction_button='true'"><xsl:call-template name="add_interaction"/></xsl:if></xsl:attribute>
     afegir intervenció
  </A> 
 </xsl:template>
 
 <xsl:template name="put_send_interaction_button">
  <A title="Envia la nova intervenció">
     <xsl:attribute name="href">#item_<xsl:value-of select="@ident"/></xsl:attribute>
     <xsl:attribute name="onClick"><xsl:call-template name="send_interaction"/></xsl:attribute>
     enviar intervenció
  </A> 
 </xsl:template>
 
<!-- #################### COMMONS - SHOW ################# -->
  <xsl:template name="show_correct_all_button">
    <xsl:variable name="is_preview">
      <xsl:call-template name="isPreviewMode"/>
    </xsl:variable>
    <xsl:variable name="show">
      <xsl:choose>
        <xsl:when test="$is_preview='false' and $view!='candidate' and //assignment_states/corregit='false'">true</xsl:when>
        <xsl:otherwise>false</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:value-of select="normalize-space($show)"/>
  </xsl:template>
  
  <xsl:template name="show_deliver_all_button">
    <xsl:variable name="is_preview">
      <xsl:call-template name="isPreviewMode"/>
    </xsl:variable>
    <xsl:variable name="show">
      <xsl:choose>
        <!--xsl:when test="$is_preview='false' and $view='candidate' and //assignment_states/lliurat='false' and //assignment_states/corregit='false'">true</xsl:when-->
        <xsl:when test="$is_preview='false' and $view='candidate' and ( 0>//assignment/max_delivery or //assignment/limit>0 ) and //assignment_states/corregit='false'">true</xsl:when>
        <xsl:otherwise>false</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:value-of select="normalize-space($show)"/>
  </xsl:template>
  
  <xsl:template name="show_start_button">
    <xsl:variable name="show">
      <xsl:choose>
        <xsl:when test="$view='candidate' and //assignment_states/corregit='false'">true</xsl:when>
        <xsl:when test="$view='teacher' and //assignment_states/corregit='false'">true</xsl:when>
        <xsl:otherwise>false</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:value-of select="normalize-space($show)"/>
  </xsl:template>

 <xsl:template name="show_correct_button">
   <xsl:param name="is_preview"/>
   <xsl:variable name="b_is_preview">
    <xsl:choose>
     <xsl:when test="$is_preview=''"><xsl:call-template name="isPreviewMode"/></xsl:when>
     <xsl:otherwise><xsl:value-of select="$is_preview"/></xsl:otherwise>
    </xsl:choose>
   </xsl:variable>
   <xsl:variable name="show">
     <xsl:choose>
       <xsl:when test="normalize-space($b_is_preview)='true' or ($view='teacher' and $estat_lliurament!='corregit' and $estat_lliurament!='')">true</xsl:when>
       <xsl:otherwise>false</xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
   <xsl:value-of select="normalize-space($show)"/>  
  </xsl:template>
 
 <xsl:template name="show_deliver_button">
   <xsl:param name="is_preview"/>
   <xsl:variable name="b_is_preview">
    <xsl:choose>
     <xsl:when test="$is_preview=''"><xsl:call-template name="isPreviewMode"/></xsl:when>
     <xsl:otherwise><xsl:value-of select="$is_preview"/></xsl:otherwise>
    </xsl:choose>
   </xsl:variable>
   <xsl:variable name="show">
     <xsl:choose>
       <xsl:when test="normalize-space($b_is_preview)!='true' and $view='candidate' and $estat_lliurament!='corregit' and ( 0>$section_max_limit or $section_max_limit>$section_limit )">true</xsl:when>
       <xsl:otherwise>false</xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
   <xsl:value-of select="normalize-space($show)"/>  
  </xsl:template>
 
 <xsl:template name="show_save_button">
   <xsl:param name="is_preview"/>
   <xsl:variable name="b_is_preview">
    <xsl:choose>
     <xsl:when test="$is_preview=''"><xsl:call-template name="isPreviewMode"/></xsl:when>
     <xsl:otherwise><xsl:value-of select="$is_preview"/></xsl:otherwise>
    </xsl:choose>
   </xsl:variable>
   <xsl:variable name="show">
     <xsl:choose>
       <xsl:when test="normalize-space($b_is_preview)!='true' and $view='candidate' and $estat_lliurament!='corregit' and ( 0>$section_max_limit or $section_max_limit>$section_limit )">true</xsl:when>
       <xsl:otherwise>false</xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
   <xsl:value-of select="normalize-space($show)"/>  
  </xsl:template>
  
  <xsl:template name="show_interaction">
    <xsl:param name="ident_item"/>
    <xsl:variable name="iteminteraction">
      <xsl:value-of select="//item[@ident=$ident_item]/itemcontrol/@interactionswitch"/>
    </xsl:variable>
    <xsl:variable name="sectioninteraction">
      <xsl:value-of select="//item[@ident=$ident_item]/ancestor::section/sectioncontrol/@interactionswitch"/>
    </xsl:variable>
    <xsl:variable name="assessmentinteraction">
      <xsl:value-of select="//item[@ident=$ident_item]/ancestor::assessment/assessmentcontrol/@interactionswitch"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="normalize-space($assignacioId)=''">false</xsl:when>
      <xsl:otherwise>
        <xsl:choose>
          <xsl:when test="normalize-space($iteminteraction)!=''">
            <xsl:choose>
              <xsl:when test="normalize-space($iteminteraction)='No'">false</xsl:when>
              <xsl:otherwise>true</xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="normalize-space($sectioninteraction)!=''">
                <xsl:choose>
                  <xsl:when test="normalize-space($sectioninteraction)='No'">false</xsl:when>
                  <xsl:otherwise>true</xsl:otherwise>
                </xsl:choose>
              </xsl:when>
              <xsl:otherwise>
                <xsl:choose>
                  <xsl:when test="normalize-space($assessmentinteraction)='No'">false</xsl:when>
                  <xsl:otherwise>true</xsl:otherwise>
                </xsl:choose>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="show_interaction_button">
   <xsl:variable name="new_interaction_index">
    <xsl:value-of select="substring-after(substring(substring-before($displayinteraction,'_view=|'), (string-length(substring-before($displayinteraction,'_view=|'))-3)),'_')"/>
   </xsl:variable>
   <xsl:variable name="show">
    <xsl:choose>
       <xsl:when test="not (contains($displayinteraction, concat(@ident,concat('_',concat($new_interaction_index,'_view=|')))))">true</xsl:when>
       <xsl:otherwise>false</xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
   <xsl:value-of select="normalize-space($show)"/>  
  </xsl:template>
  

<!-- #################### COMMONS - ACTIONS ################# -->

  <xsl:template name="go_section"><!-- TODO ALLR: Comprovar la navegació amb servidor -->
    <xsl:param name="section_number_to_go"/>
    <xsl:param name="diff"/><!-- Albert -->
    <xsl:param name="randomizable"/><!-- Albert -->
    <!--Albert xsl:variable name="url">if (!isSubmitted()) {document.forms[0].next.value=<xsl:value-of select="$section_number_to_go"/>;   -->
    <xsl:variable name="url">if (!isSubmitted()) {document.forms[0].next.value=<xsl:value-of select="$section_number_to_go + $diff"/>; <!--Albert-->
       doSubmit(<xsl:value-of select="$needResponse"/>);}</xsl:variable>
    <xsl:value-of select="normalize-space($url)"/>
  </xsl:template>
  <xsl:template name="go_section_from_assignment">
    <xsl:param name="section_number_to_go"/>
    <xsl:variable name="url">
      <xsl:if test="ancestor::assignment/@servlet!=''">
        <xsl:value-of select="ancestor::assignment/@servlet"/>?</xsl:if>
      <xsl:choose>
        <xsl:when
            test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/>
        </xsl:when>
        <xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/>
        </xsl:otherwise>
      </xsl:choose>&#38;quadernXSL=<xsl:value-of
        select="ancestor::assignment/@quadernXSL"/>&#38;full=<xsl:value-of select="$section_number_to_go"/>
    </xsl:variable>
    <xsl:value-of select="normalize-space($url)"/>
  </xsl:template>
  <xsl:template name="go_home">
    <xsl:call-template name="go_section">
      <xsl:with-param name="section_number_to_go">0</xsl:with-param>
      <xsl:with-param name="diff">0</xsl:with-param><!--Albert -->
      <xsl:with-param name="randomizable"><xsl:call-template name="getRandomizableSectionsInItem"></xsl:call-template></xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="do_submit">
   <xsl:param name="p_want_correct">true</xsl:param>
   <xsl:variable name="url">javascript:if (!isSubmitted()) {document.forms[0].wantCorrect.value='<xsl:value-of select="$p_want_correct"/>'; document.forms[0].action+='#item_<xsl:value-of select="@ident"/>';doSubmit(<xsl:value-of select="$needResponse"/>);}</xsl:variable>
   <xsl:value-of select="normalize-space($url)"/>
  </xsl:template>
  
  <xsl:template name="save_section">
   <xsl:call-template name="do_submit"><xsl:with-param name="p_want_correct">false</xsl:with-param></xsl:call-template>
  </xsl:template>
  
  <xsl:template name="save_section_teacher"><!--Albert-->
   <xsl:call-template name="do_submit"><xsl:with-param name="p_want_correct">false</xsl:with-param></xsl:call-template>
  </xsl:template>
  
  <xsl:template name="correct_section">
   <xsl:call-template name="do_submit"/>
  </xsl:template>
  
  <xsl:template name="deliver_section">
   <xsl:call-template name="do_submit"/>
  </xsl:template>
  
 <xsl:template name="add_interaction">
  <xsl:variable name="action">javascript:if (!isSubmitted()) { document.forms[0].interaction.value='<xsl:value-of select="@ident"/>'; document.forms[0].action+='#item_<xsl:value-of select="@ident"/>';doSubmit(false); }</xsl:variable>
  <xsl:value-of select="normalize-space($action)"/>
 </xsl:template>
 
 <xsl:template name="send_interaction">
  <xsl:variable name="action">javascript:if (!isSubmitted()) {document.forms[0].wantCorrect.value='false';document.forms[0].action+='#item_<xsl:value-of select="@ident"/>';doSubmit(<xsl:value-of select="$needResponse"/>);}</xsl:variable>
  <xsl:value-of select="normalize-space($action)"/>
 </xsl:template>

<!-- #################### COMMONS - UTIL ################# -->
 <xsl:template name="put_scripts">
    <SCRIPT type="text/javascript"><xsl:attribute name="src"><xsl:call-template name="get_scripts_path"/>/qv.js</xsl:attribute></SCRIPT>
    <SCRIPT type="text/javascript">
      includeScript('qti_functions.js');
      includeScript('qti_timer.js');
      includeScript('javaplugin.js');
      includeScript('mm.js');
      includeScript('order.js');<!-- Albert -->
    </SCRIPT>
    <!-- SCRIPT type="text/javascript"><xsl:attribute name="src"><xsl:call-template name="get_scripts_path"/>/qti_functions.js</xsl:attribute></SCRIPT>
    <SCRIPT type="text/javascript"><xsl:attribute name="src"><xsl:call-template name="get_scripts_path"/>/qti_timer.js</xsl:attribute></SCRIPT>
    <SCRIPT type="text/javascript"><xsl:attribute name="src"><xsl:call-template name="get_scripts_path"/>/javaplugin.js</xsl:attribute></SCRIPT>
    <SCRIPT type="text/javascript"><xsl:attribute name="src"><xsl:call-template name="get_scripts_path"/>/mm.js</xsl:attribute></SCRIPT-->
    <xsl:call-template name="put_specific_scripts"/>
  </xsl:template> 
  
  <xsl:template name="put_specific_scripts">
  </xsl:template>
  
  <xsl:template name="get_scripts_path">skins/common/scripts</xsl:template>
  
 
<!-- #################### REDEFINITIONS ################# -->
 <xsl:template name="html_head">
   <HEAD>
    <title><xsl:call-template name="html_head_title"/></title>
    <xsl:call-template name="put_scripts"/>
    <xsl:call-template name="put_css"/>
    <LINK rel="icon" type="image/x-icon">
     <xsl:attribute name="href"><xsl:value-of select="$common_images_path"/>/favicon.ico</xsl:attribute>
    </LINK>
    <LINK rel="shortcut icon" type="image/x-icon">
     <xsl:attribute name="href"><xsl:value-of select="$common_images_path"/>/favicon.ico</xsl:attribute>
    </LINK>
   </HEAD>  
 </xsl:template>

 <xsl:template name="html_head_title">
    Quaderns Virtuals
 </xsl:template>

 <xsl:template name="put_css">
 </xsl:template>
 
 <xsl:template name="questestinterop_body">
     <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
 </xsl:template>
 
 <xsl:template name="html_body_attrs">
 </xsl:template>
 
 <xsl:template name="interactions">
  <!-- Si existeix alguna interaccio a mostrar per l'item amb ident=item_ident es mostra-->
  <xsl:param name="item_ident"/>
  <xsl:param name="index"/>
  <xsl:variable name="showInteraction">
   <xsl:call-template name="show_interaction">
    <xsl:with-param name="ident_item"><xsl:value-of select="$item_ident"/></xsl:with-param>
   </xsl:call-template>
  </xsl:variable>
  
  <table width="98%" border="0" cellspacing="0" cellpadding="0">
   <tr>
    <xsl:if test="$writingEnabled='true' and normalize-space($showInteraction)!='false'">
     <td width="30" align="left" valign="top">
      <xsl:call-template name="put_add_interaction_button"/>
     </td>
     <td>
      <table width="100%" height="35" border="0" cellspacing="0" cellpadding="5" bgcolor="#FFFFFF">
       <xsl:call-template name="interaction">
        <xsl:with-param name="item_ident">
         <xsl:value-of select="$item_ident"/>
        </xsl:with-param>
        <xsl:with-param name="index">1</xsl:with-param>
       </xsl:call-template>
      </table>
     </td>
    </xsl:if>
   </tr>
  </table>
 </xsl:template>
 
 <xsl:template name="getRandomizableSectionsInItem"><!-- Albert: Per fer l'index es fa servir un xml generat que no és el del QTI -->
 	<xsl:text>",</xsl:text>
 	<xsl:for-each select="//assessment/selection_ordering//selection_metadata[@mdname='ident']">
		<xsl:variable name="element_id"><xsl:value-of select="."/></xsl:variable>
		<xsl:for-each select="//assessment//section">
			<xsl:variable name="section_id"><xsl:value-of select="@ident"/></xsl:variable>
			<xsl:if test="$element_id=$section_id"><xsl:value-of select="position()"/>,</xsl:if>
		</xsl:for-each>
	</xsl:for-each>
	<xsl:text>"</xsl:text>
 </xsl:template>
 
 <xsl:template name="getRandomizableItems"><!-- Albert -->
 	<xsl:text>",</xsl:text>
 	<xsl:for-each select="//section[position()=$section_number]/selection_ordering//selection_metadata[@mdname='ident']">
		<xsl:variable name="element_id"><xsl:value-of select="."/></xsl:variable>
		<xsl:for-each select="//section[position()=$section_number]//item">
			<xsl:variable name="item_id"><xsl:value-of select="@ident"/></xsl:variable>
			<xsl:if test="$element_id=$item_id"><xsl:value-of select="position()"/>,</xsl:if>
		</xsl:for-each>
	</xsl:for-each>
	<xsl:text>"</xsl:text>
 </xsl:template>
  
<xsl:template name="getRandomizableSections"><!-- Albert: Per fer l'index es fa servir un xml generat que no és el del QTI, haurem de trobar la propietat a l'atribut que s'ha afegit a la section no QTI -->
 	<xsl:text>",</xsl:text>
 	<xsl:for-each select="//section"><xsl:if test="@randomizable='true'"><xsl:value-of select="position()"/>,</xsl:if></xsl:for-each>
	<xsl:text>"</xsl:text>
 </xsl:template>
  
  
</xsl:stylesheet>
