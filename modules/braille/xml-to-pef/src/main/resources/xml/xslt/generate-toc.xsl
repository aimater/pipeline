<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:pf="http://www.daisy.org/ns/pipeline/functions"
                xmlns:f="http://www.daisy.org/ns/pipeline/internal-functions"
                version="2.0"
                exclude-result-prefixes="#all">
  
  <!-- stylesheet defaults to names used in HTML if no other grammar is detected -->
  
  <xsl:param name="toc-depth" as="xs:string" select="'0'"/>
  <xsl:param name="heading-names" select="''"/>
  
  <xsl:include href="http://www.daisy.org/pipeline/modules/common-utils/generate-id.xsl"/>
  <xsl:include href="http://www.daisy.org/pipeline/modules/file-utils/library.xsl"/>
  
  <xsl:variable name="depth" as="xs:integer" select="if ($toc-depth) then xs:integer($toc-depth) else 6"/>
  
  <xsl:variable name="root-base-uri" as="xs:anyURI" select="base-uri(/*)"/>
  
  <xsl:variable name="included-heading-names" as="xs:string*"
                select="if ($heading-names) then tokenize($heading-names,' ') else f:html-headers()"/>
  
  <xsl:template match="/*" priority="1">
    <xsl:call-template name="pf:next-match-with-generated-ids">
      <xsl:with-param name="prefix" select="'h_'"/>
      <xsl:with-param name="for-elements" select="//*[local-name()=$included-heading-names]
                                                     [self::*:h1|self::*:h2|self::*:h3|self::*:h4|self::*:h5|self::*:h6]
                                                     [not(@id|@xml:id)]"/>
      <xsl:with-param name="in-use" select="//@id|//@xml:id"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template match="/*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:if test="$depth &gt; 0">
        <xsl:variable name="list" as="element()*">
          <xsl:for-each-group select="//*[local-name()=$included-heading-names]" group-starting-with="*:h1">
            <xsl:element name="{f:list-item-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
              <xsl:call-template name="list-item-attributes"/>
              <xsl:if test="current-group()/self::*:h1">
                <xsl:element name="{f:link-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                  <xsl:call-template name="link-attributes">
                    <xsl:with-param name="header-element" select="current-group()/self::*:h1"/>
                  </xsl:call-template>
                  <xsl:apply-templates select="current-group()/self::*:h1/child::node()"/>
                </xsl:element>
              </xsl:if>
              <xsl:if test="$depth &gt; 1">
                <xsl:variable name="list" as="element()*">
                  <xsl:for-each-group select="current-group()[not(self::*:h1)]" group-starting-with="*:h2">
                    <xsl:element name="{f:list-item-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                      <xsl:call-template name="list-item-attributes"/>
                      <xsl:if test="current-group()/self::*:h2">
                        <xsl:element name="{f:link-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                          <xsl:call-template name="link-attributes">
                            <xsl:with-param name="header-element" select="current-group()/self::*:h2"/>
                          </xsl:call-template>
                          <xsl:apply-templates select="current-group()/self::*:h2/child::node()"/>
                        </xsl:element>
                      </xsl:if>
                      <xsl:if test="$depth &gt; 2">
                        <xsl:variable name="list" as="element()*">
                          <xsl:for-each-group select="current-group()[not(self::*:h2)]" group-starting-with="*:h3">
                            <xsl:element name="{f:list-item-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                              <xsl:call-template name="list-item-attributes"/>
                              <xsl:if test="current-group()/self::*:h3">
                                <xsl:element name="{f:link-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                  <xsl:call-template name="link-attributes">
                                    <xsl:with-param name="header-element" select="current-group()/self::*:h3"/>
                                  </xsl:call-template>
                                  <xsl:apply-templates select="current-group()/self::*:h3/child::node()"/>
                                </xsl:element>
                              </xsl:if>
                              <xsl:if test="$depth &gt; 3">
                                <xsl:variable name="list" as="element()*">
                                  <xsl:for-each-group select="current-group()[not(self::*:h3)]" group-starting-with="*:h4">
                                    <xsl:element name="{f:list-item-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                      <xsl:call-template name="list-item-attributes"/>
                                      <xsl:if test="current-group()/self::*:h4">
                                        <xsl:element name="{f:link-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                          <xsl:call-template name="link-attributes">
                                            <xsl:with-param name="header-element" select="current-group()/self::*:h4"/>
                                          </xsl:call-template>
                                          <xsl:apply-templates select="current-group()/self::*:h4/child::node()"/>
                                        </xsl:element>
                                      </xsl:if>
                                      <xsl:if test="$depth &gt; 4">
                                        <xsl:variable name="list" as="element()*">
                                          <xsl:for-each-group select="current-group()[not(self::*:h4)]" group-starting-with="*:h5">
                                            <xsl:element name="{f:list-item-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                              <xsl:call-template name="list-item-attributes"/>
                                              <xsl:if test="current-group()/self::*:h5">
                                                <xsl:element name="{f:link-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                                  <xsl:call-template name="link-attributes">
                                                    <xsl:with-param name="header-element" select="current-group()/self::*:h5"/>
                                                  </xsl:call-template>
                                                  <xsl:apply-templates select="current-group()/self::*:h5/child::node()"/>
                                                </xsl:element>
                                              </xsl:if>
                                              <xsl:if test="$depth &gt; 5">
                                                <xsl:variable name="list" as="element()*">
                                                  <xsl:for-each select="current-group()/self::*:h6">
                                                    <xsl:element name="{f:list-item-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                                      <xsl:call-template name="list-item-attributes"/>
                                                      <xsl:element name="{f:link-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                                        <xsl:call-template name="link-attributes">
                                                          <xsl:with-param name="header-element" select="."/>
                                                        </xsl:call-template>
                                                        <xsl:apply-templates select="node()"/>
                                                      </xsl:element>
                                                    </xsl:element>
                                                  </xsl:for-each>
                                                </xsl:variable>
                                                <xsl:if test="exists($list)">
                                                  <xsl:element name="{f:list-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                                    <xsl:call-template name="list-attributes"/>
                                                    <xsl:sequence select="$list"/>
                                                  </xsl:element>
                                                </xsl:if>
                                              </xsl:if>
                                            </xsl:element>
                                          </xsl:for-each-group>
                                        </xsl:variable>
                                        <xsl:if test="exists($list)">
                                          <xsl:element name="{f:list-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                            <xsl:call-template name="list-attributes"/>
                                            <xsl:sequence select="$list"/>
                                          </xsl:element>
                                        </xsl:if>
                                      </xsl:if>
                                    </xsl:element>
                                  </xsl:for-each-group>
                                </xsl:variable>
                                <xsl:if test="exists($list)">
                                  <xsl:element name="{f:list-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                                    <xsl:call-template name="list-attributes"/>
                                    <xsl:sequence select="$list"/>
                                  </xsl:element>
                                </xsl:if>
                              </xsl:if>
                            </xsl:element>
                          </xsl:for-each-group>
                        </xsl:variable>
                        <xsl:if test="exists($list)">
                          <xsl:element name="{f:list-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                            <xsl:call-template name="list-attributes"/>
                            <xsl:sequence select="$list"/>
                          </xsl:element>
                        </xsl:if>
                      </xsl:if>
                    </xsl:element>
                  </xsl:for-each-group>
                </xsl:variable>
                <xsl:if test="exists($list)">
                  <xsl:element name="{f:list-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
                    <xsl:call-template name="list-attributes"/>
                    <xsl:sequence select="$list"/>
                  </xsl:element>
                </xsl:if>
              </xsl:if>
            </xsl:element>
          </xsl:for-each-group>
        </xsl:variable>
        <xsl:if test="exists($list)">
          <xsl:element name="{f:list-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
            <xsl:call-template name="list-attributes"/>
            <xsl:attribute name="id" select="'generated-document-toc'"/>
            <xsl:sequence select="$list"/>
          </xsl:element>
          <xsl:element name="{f:list-name(namespace-uri(/*))}" namespace="{namespace-uri(/*)}">
            <xsl:call-template name="list-attributes"/>
            <xsl:attribute name="id" select="'generated-volume-toc'"/>
            <xsl:sequence select="$list"/>
          </xsl:element>
        </xsl:if>
      </xsl:if>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="*:h1|*:h2|*:h3|*:h4|*:h5|*:h6">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:if test="$depth &gt; 0 and not(@id|@xml:id)">
        <xsl:variable name="generated-id" as="xs:string">
          <xsl:call-template name="pf:generate-id"/>
        </xsl:variable>
        <xsl:attribute name="xml:id" select="$generated-id"/>
      </xsl:if>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <!-- ========================================= -->
  <!-- Format-specific tag names and attributes: -->
  <!-- ========================================= -->
  
  <xsl:function name="f:html-headers">
    <xsl:sequence select="for $i in 1 to $depth return concat('h',$i)"/>
  </xsl:function>
  
  <xsl:function name="f:list-name">
    <xsl:param name="namespace-uri"/>
    <xsl:choose>
      <xsl:when test="$namespace-uri = 'http://www.daisy.org/z3986/2005/dtbook/'">
        <xsl:value-of select="'list'"/>
      </xsl:when>
      <xsl:when test="$namespace-uri = 'http://www.daisy.org/ns/z3998/authoring/'">
        <xsl:value-of select="'list'"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="'ol'"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>
  
  <xsl:template name="list-attributes">
    <xsl:choose>
      <xsl:when test="namespace-uri(/*) = 'http://www.daisy.org/z3986/2005/dtbook/'">
        <xsl:attribute name="type" select="'ol'"/>
      </xsl:when>
      <xsl:when test="namespace-uri(/*) = 'http://www.daisy.org/ns/z3998/authoring/'">
        <xsl:attribute name="type" select="'ordered'"/>
      </xsl:when>
      <xsl:otherwise/>
    </xsl:choose>
  </xsl:template>
  
  <xsl:function name="f:list-item-name">
    <xsl:param name="namespace-uri"/>
    <xsl:value-of select="'li'"/>
  </xsl:function>
  
  <xsl:template name="list-item-attributes"/>
  
  <xsl:function name="f:link-name">
    <xsl:param name="namespace-uri"/>
    <xsl:value-of select="'a'"/>
  </xsl:function>
  
  <xsl:template name="link-attributes">
    <xsl:param name="header-element" as="element()" required="yes"/>
    <xsl:apply-templates mode="link-attributes" select="$header-element"/>
  </xsl:template>
  
  <xsl:template mode="link-attributes"
                match="*:h1|*:h2|*:h3|*:h4|*:h5|*:h6">
    <xsl:variable name="id" as="xs:string">
      <xsl:choose>
        <xsl:when test="@id|@xml:id">
          <xsl:sequence select="@id|@xml:id"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="pf:generate-id"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:attribute name="href" select="concat('#',encode-for-uri($id))"/>
    <xsl:if test="not(@xml:base)">
      <xsl:variable name="header-base-uri" as="xs:anyURI" select="base-uri(.)"/>
      <xsl:if test="not($header-base-uri=$root-base-uri)">
        <xsl:attribute name="xml:base" select="$header-base-uri"/>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  
</xsl:stylesheet>
