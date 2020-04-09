package com.matag.admin;

import com.matag.cards.CardsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CardsConfiguration.class, MatagAdminProdConfiguration.class})
public class MatagAdminConfiguration {

}
