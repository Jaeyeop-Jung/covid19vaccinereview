package com.teamproject.covid19vaccinereview.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
public class NginxControllerTest {

    @Test
    public void real_profile_이_조회된다(){
        //given
        String expectedProfile = "real";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        NginxController nginxController = new NginxController(env);

        //when
        String profile = nginxController.nginxConfig();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void real_profile이_없으면_첫번째가_조회된다() {
        //given
        String expectedProfile = "ouath";
        MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        NginxController nginxController = new NginxController(env);

        //when
        String profile = nginxController.nginxConfig();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void active_profile이_없으면_default가_조회된다(){
        //given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        NginxController nginxController = new NginxController(env);

        //when
        String profile = nginxController.nginxConfig();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}
