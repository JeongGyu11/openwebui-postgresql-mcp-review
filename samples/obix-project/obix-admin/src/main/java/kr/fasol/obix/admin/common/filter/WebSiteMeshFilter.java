package kr.fasol.obix.admin.common.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.tagrules.html.DivExtractingTagRuleBundle;
import org.springframework.context.annotation.Configuration;

/**
 * 프로그램명 : SiteMesh Filter 설정
 * 설명 :
 *
 * @Class  WebSiteMeshFilter.java
 * @author  정영탁
 * @since   2023. 3. 9.
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2023. 3. 9.        정영탁          최초생성
 */
@Configuration
public class WebSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        // Decorator 정의
        builder
                .setDecoratorPrefix("/WEB-INF/templates/")
                // 기본 템플릿
                .addDecoratorPath("*.frag", "template-fragment.jsp")

                // Admin
                .addDecoratorPath("/admin/*.view", "admin/template-admin-default.jsp")
                .addDecoratorPath("/admin/main/*.view", "admin/template-admin-main.jsp")
                .addDecoratorPath("/admin/cm/so/*.view", "admin/template-admin-login.jsp")

                // Decorator 예외 정의
                .addExcludedPath("/include/*")
                .addExcludedPath("/common/*")
                .addExcludedPath("/cmn/*")
                .addExcludedPath("*.html")
                .addExcludedPath("*.jsp");

        // add div tag rule
        builder.addTagRuleBundles(new DivExtractingTagRuleBundle());
    }

}
