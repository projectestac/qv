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
  <xsl:param name="color"/>
  <!-- fletxa enrere -->
  <!-- <xsl:param name="clicJarBase" select="/applets/jclic/jars/"/> -->
  <!--xsl:output method="html"/-->
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
  <xsl:template match="*[local-name()='item']">
    <xsl:variable name="it_ident">
      <xsl:value-of select="@ident"/>
    </xsl:variable>
    <A>
      <xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/>
      </xsl:attribute>
    </A>
    <TABLE width="100%" height="60" border="0" cellspacing="0" cellpadding="0" class="item">
      <TR>
        <TD>
          <xsl:call-template name="getAssessment">
            <xsl:with-param name="ident_item">
              <xsl:value-of select="$it_ident"/>
            </xsl:with-param>
          </xsl:call-template>
        </TD>
      </TR>
      <TR>
        <TD>
          <xsl:call-template name="getItemAdditionalMaterial">
            <xsl:with-param name="ident_item">
              <xsl:value-of select="$it_ident"/>
            </xsl:with-param>
          </xsl:call-template>
        </TD>
      </TR>
      <TR>
        <TD>
          <xsl:call-template name="getResponses">
            <xsl:with-param name="ident_item">
              <xsl:value-of select="$it_ident"/>
            </xsl:with-param>
          </xsl:call-template>
        </TD>
      </TR>
      <TR>
        <TD>
          <xsl:call-template name="getDisplayFeedback">
            <xsl:with-param name="ident_item">
              <xsl:value-of select="$it_ident"/>
            </xsl:with-param>
            <xsl:with-param name="feedback">
              <xsl:value-of select="$displayfeedback"/>
            </xsl:with-param>
          </xsl:call-template>
        </TD>
      </TR>
    </TABLE>
    <BR/>
  </xsl:template>
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
  <!-- INTERACTION -->
  <!--xsl:template name="showInteraction">
  <xsl:choose>
   <xsl:when test="//assessment/assessmentcontrol[@feedbackswitch='No']"> false </xsl:when>
   <xsl:when test="itemcontrol[@interactionswitch='No']"> false </xsl:when>
   <xsl:when test="itemmetadata/qtimetadata/qtimetadatafield[@fieldlabel='showInteraction']">
    <xsl:value-of select="itemmetadata/qtimetadata/qtimetadatafield[@fieldlabel='showInteraction']/@fieldentry"/>
   </xsl:when>
   <xsl:otherwise> true </xsl:otherwise>
  </xsl:choose>
 </xsl:template-->
  <xsl:template name="showInteraction">
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
  <xsl:template name="interactions">
    <!-- Si existeix alguna interaccio a mostrar per l'item amb ident=item_ident es mostra-->
    <xsl:param name="item_ident"/>
    <xsl:param name="index"/>
    <xsl:variable name="showInteraction">
      <xsl:call-template name="showInteraction">
        <xsl:with-param name="item_ident">
          <xsl:value-of select="$item_ident"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <tr>
      <td>&#160;</td>
      <td>
        <table width="98%" border="0" cellspacing="1" cellpadding="0" class="interaction" align="left">
          <tr>
            <xsl:if test="$writingEnabled='true' and normalize-space($showInteraction)!='false'">
              <td width="30" class="forum" align="left" valign="top">
                <a href="#" onmouseout="MM_swapImgRestore()">
                  <xsl:attribute name="onClick"> if (doSubmit2(false)) {
                      document.forms[0].interaction.value='<xsl:value-of select="@ident"/>';
                    document.forms[0].submit(); } </xsl:attribute>
                  <xsl:attribute name="onMouseOver">MM_swapImage('im_int_<xsl:value-of
                      select="@ident"/>','','<xsl:value-of select="$path_imatges"/>questio_on.gif',1)</xsl:attribute>
                  <img title="Afegir una nova intervenci&#243; a la zona de di&#224;leg"
                    alt="Afegir una nova intervenci&#243; a la zona de di&#224;leg"
                    align="left" border="0">
                    <xsl:attribute name="src">
                      <xsl:value-of select="$path_imatges"/>questio_off.gif</xsl:attribute>
                    <xsl:attribute name="name">im_int_<xsl:value-of select="@ident"/>
                    </xsl:attribute>
                  </img>
                </a>
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
      </td>
    </tr>
  </xsl:template>
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
          <xsl:if test="$req_view='teacher'">
            <xsl:attribute name="class">intervencioDocent</xsl:attribute>
          </xsl:if>
          <xsl:choose>
            <xsl:when test="$interaction_clean='|'">
              <!-- Ha demanat posar aquesta interaccio -->
              <TD colspan="2">
                <xsl:if test="$view='teacher'">
                  <xsl:attribute name="class">intervencioDocent</xsl:attribute>
                </xsl:if>
                <table border="0" cellspacing="0" cellpadding="0">
                  <TR>
                    <TD class="forum" width="100%">
                      <input type="text" style="width: 98%;">
                        <xsl:attribute name="name">interaccio_<xsl:value-of
                            select="$item_ident"/>_<xsl:value-of select="$index"/>_view<xsl:value-of select="$view"/>
                        </xsl:attribute>
                      </input>
                    </TD>
                    <TD>
                      <a onmouseout="MM_swapImgRestore()">
                        <xsl:attribute name="onClick">if (doSubmit2(<xsl:value-of
                          select="$needResponse"/>)) {javascript:document.forms[0].wantCorrect.value='false';document.forms[0].submit();}</xsl:attribute>
                        <xsl:attribute
                            name="onMouseOver">MM_swapImage('btnEnviarInt','','<xsl:value-of select="$path_imatges"/>enviar_on.gif',1)</xsl:attribute>
                        <img name="btnEnviarInt" align="left" border="0" alt="enviar">
                          <xsl:attribute name="src">
                            <xsl:value-of select="$path_imatges"/>enviar_off.gif</xsl:attribute>
                        </img>
                      </a>
                    </TD>
                  </TR>
                </table>
              </TD>
            </xsl:when>
            <xsl:otherwise>
              <!-- La interaccio ja estava posada -->
              <TD width="30" class="forum">
                <xsl:choose>
                  <xsl:when test="$req_view='candidate'">alumne</xsl:when>
                  <xsl:when test="$req_view='teacher'">profe</xsl:when>
                </xsl:choose>
              </TD>
              <TD class="forum">
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
        <TD colspan="2" class="forum">&#160;</TD>
      </xsl:when>
    </xsl:choose>
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
      <xsl:variable name="params">'+ 'NAME=<xsl:value-of select="$ident"/>;'+
          'url_base=<xsl:value-of select="$QTIUrl"/>/../;'+
        <!--'image_src=<xsl:value-of select="$QTIUrl"/>/../<xsl:value-of select="ancestor::presentation//matimage/@uri"/>;'+-->

        <!--'image_src=<xsl:call-template name="putResourceSource2"><xsl:with-param name="uri"><xsl:value-of select="ancestor::presentation//matimage/@uri"/></xsl:with-param></xsl:call-template>;'+-->
          'serverUrl=<xsl:value-of select="$serverUrl"/>;'+ 'image_src=<xsl:value-of
        select="ancestor::presentation//matimage/@uri"/>;'+ 'orientation=<xsl:value-of
        select="@orientation"/>;'+ 'size=<xsl:value-of select="@size"/>;'+ 'font=<xsl:value-of
        select="@font"/>;'+ 'shuffle=<xsl:value-of select="@shuffle"/>;'+ 'disabled=<xsl:value-of
        select="$noModify"/>;'+ 'INITPARAM=<xsl:value-of
        select="substring-after(substring-before(substring-after($initialselection,$ident),'#'),'=')"/>;'+ <xsl:choose>
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
                <xsl:for-each select="attribute::*"> 'T<xsl:value-of
                    select="$num_param"/>_<xsl:value-of select="local-name(.)"/>=<xsl:value-of
                  select="."/>;'+ </xsl:for-each> 'T<xsl:value-of
                  select="$num_param"/>_text=<xsl:value-of select="normalize-space(.)"/>;'+ </xsl:for-each>
              <xsl:call-template name="putDragDropSources"/>
            </xsl:for-each>
          </xsl:otherwise>
        </xsl:choose> '</xsl:variable>
      <SCRIPT>
        <xsl:choose>
          <xsl:when test="$cardinality='ordered'"> writeJavaApplet('QVOrdering.class', './appl',
            'QVOrdering.jar', '<xsl:value-of select="$params"/>', '<xsl:call-template
            name="getOrderingWidth"/>', '<xsl:call-template name="getOrderingHeight"/>' , null,
              null,'qvApplet','<xsl:value-of select="$ident"/>',true); </xsl:when>
          <xsl:otherwise> writeJavaApplet('QVDragDrop.class', './appl', 'QVDragDrop.jar',
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
      <xsl:variable name="backSlashQuote">�</xsl:variable>
      <xsl:for-each select=".//attribute::*"> 'S<xsl:value-of select="$num_param"/>_<xsl:value-of
          select="local-name(.)"/>=<xsl:value-of select="."/>;'+ </xsl:for-each> 'S<xsl:value-of
        select="$num_param"/>_text=<xsl:value-of
      select="translate(normalize-space(.),$singleQuote, $backSlashQuote)"/>;'+ </xsl:for-each>
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
          <xsl:attribute name="name">
            <xsl:value-of select="$ident"/>
          </xsl:attribute>
          <xsl:attribute name="size">
            <xsl:value-of select="normalize-space(ancestor-or-self::render_fib/@columns)"/>
          </xsl:attribute>
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
    <xsl:choose>
      <xsl:when test="contains(@texttype,'html')">
        <xsl:copy-of select="."/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="*[local-name()='mathtml']">
    <xsl:copy-of select="."/>
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
    <xsl:apply-templates select="*[local-name()='matclic']"/>
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
  <xsl:template match="*[local-name()='matclic']">
    <SCRIPT LANGUAGE="JavaScript"> setJarBase('http://localhost:8080/qv/<xsl:value-of
      select="$clicJarBase"/>'); setLanguage('<xsl:value-of select="@language"/>'); <xsl:choose>
        <xsl:when test="normalize-space(@skin)=''"/>
        <xsl:otherwise> setSkin('<xsl:value-of select="@skin"/>'); </xsl:otherwise>
      </xsl:choose>
      <!-- setReporter('TCPReporter','ip=127.0.0.1;port=5510'); -->
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
    </xsl:if>
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
      <param name="movie">
        <xsl:attribute name="VALUE">
          <xsl:value-of select="$uri"/>
        </xsl:attribute>
      </param>
      <param name="quality" value="high"/>
      <embed quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash">
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
            'url_base=<xsl:value-of select="$QTIUrl"/>/../;'+ 'serverUrl=<xsl:value-of
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
          writeJavaApplet('QVRenderHotspotAppletJS.class', './appl', 'QVHotspotAppletJS.jar',
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
    <table border="0" align="center">
      <tr>
        <td>
          <object ID="RVOCX" classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA">
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
    <xsl:if test="local-name()='flow'">
      <xsl:call-template name="insertFlow"/>
    </xsl:if>
  </xsl:template>
  <xsl:template match="*[local-name()='material']">
    <xsl:for-each select="child">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="*[local-name()='mattext']">
    <xsl:choose>
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
    <xsl:call-template name="add-assessment-url">
      <xsl:with-param name="text">
        <xsl:value-of select="$text"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <xsl:template name="add-assessment-url">
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
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
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
        <xsl:otherwise>audio/x-pn-realaudio-plugin</xsl:otherwise>
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
      <object CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA">
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
        <param NAME="console" VALUE="Clip1"/>
        <param NAME="autostart" VALUE="false"/>
        <param NAME="src">
          <xsl:attribute name="VALUE">
            <xsl:value-of select="$uri"/>
          </xsl:attribute>
        </param>
        <embed CONSOLE="Clip1" CONTROLS="ImageWindow" AUTOSTART="false">
          <xsl:attribute name="src">
            <xsl:value-of select="$uri"/>
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
      <object CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" HEIGHT="60">
        <xsl:attribute name="id">
          <xsl:value-of select="$id"/>
        </xsl:attribute>
        <xsl:attribute name="width">
          <xsl:value-of select="$width"/>
        </xsl:attribute>
        <param NAME="controls" VALUE="ControlPanel,StatusBar"/>
        <param NAME="console" VALUE="Clip1"/>
        <embed CONSOLE="Clip1" CONTROLS="ControlPanel,StatusBar" HEIGHT="60" AUTOSTART="false">
          <xsl:attribute name="type">
            <xsl:value-of select="$videotype"/>
          </xsl:attribute>
          <xsl:attribute name="width">
            <xsl:value-of select="$width"/>
          </xsl:attribute>
        </embed>
      </object>
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
  <xsl:template name="putScripts">
    <SCRIPT SRC="web/scripts/qti_functions.js" TYPE="text/javascript"/>
    <SCRIPT SRC="web/scripts/qti_timer.js" TYPE="text/javascript"/>
    <SCRIPT SRC="web/scripts/javaplugin.js" TYPE="text/javascript"/>
    <SCRIPT src="web/scripts/mm.js" type="text/javascript"/>
  </xsl:template>
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
  <xsl:template name="go_section">
    <xsl:param name="section_number_to_go"/>
    <xsl:variable name="url"> if (doSubmit2(<xsl:value-of select="$needResponse"/>))
        {javascript:document.forms[0].next.value=<xsl:value-of
      select="$section_number_to_go"/>;document.forms[0].submit();} </xsl:variable>
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
    </xsl:call-template>
  </xsl:template>
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
        <xsl:when test="$is_preview='false' and $view='candidate' and //assignment_states/lliurat='false' and //assignment_states/corregit='false'">true</xsl:when>
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
  <xsl:template name="get_action_url">
    <xsl:variable name="url">
      <xsl:value-of select="$serverUrl"/>?<xsl:choose>
        <xsl:when test="$assignacioId!='' and $assignacioId!='null'">assignacioId=<xsl:value-of select="$assignacioId"/>
        </xsl:when>
        <xsl:otherwise>quadernURL=<xsl:value-of select="$QTIUrl"/>
        </xsl:otherwise>
      </xsl:choose>&amp;quadernXSL=<xsl:value-of
        select="$quadernXSL"/>&amp;currentSect=<xsl:value-of select="$section_number"/>#int </xsl:variable>
    <xsl:value-of select="normalize-space($url)"/>
  </xsl:template>
</xsl:stylesheet>
