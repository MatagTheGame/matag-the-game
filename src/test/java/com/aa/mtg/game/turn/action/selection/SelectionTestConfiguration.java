package com.aa.mtg.game.turn.action.selection;

import com.aa.TestUtilsConfiguration;
import com.aa.mtg.game.turn.action.permanent.PermanentService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("com.aa.mtg.game.turn.action.selection")
@Import(TestUtilsConfiguration.class)
public class SelectionTestConfiguration {

    @Bean
    @Primary
    public PermanentService permanentService() {
        return Mockito.mock(PermanentService.class);
    }
}