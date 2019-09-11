package com.aa.mtg.game.turn.action.life;

import com.aa.TestUtilsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.aa.mtg.game.turn.action.life")
@Import(TestUtilsConfiguration.class)
public class LifeTestConfiguration {

}
