/**
 * 
 */
package com.ss.UtopiaAirlines.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ronh
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_XML);
  }
}
