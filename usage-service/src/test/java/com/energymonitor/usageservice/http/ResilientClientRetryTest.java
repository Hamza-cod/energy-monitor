package com.energymonitor.usageservice.http;

import com.energymonitor.common.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Verifies the @Retryable proxy actually fires. A compile check would not catch
 * a wrapper bean that was never proxied, nor a filter that matches nothing.
 */
@SpringBootTest(classes = ResilientClientRetryTest.TestApp.class)
@TestPropertySource(properties = {
        "spring.http.clients.connect-timeout=2s",
        "spring.http.clients.read-timeout=5s"
})
class ResilientClientRetryTest {

    @SpringBootApplication
    @EnableResilientMethods
    @ComponentScan(basePackageClasses = ResilientUserClient.class,
            includeFilters = @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE, classes = ResilientUserClient.class),
            useDefaultFilters = false)
    static class TestApp {
    }

    @Autowired
    private ResilientUserClient resilientUserClient;

    @MockitoBean
    private UserClient userClient;

    @Test
    void retriesUntilSuccessOnConnectionFailure() {
        UserDto expected = new UserDto();
        given(userClient.getUserById("u1"))
                .willThrow(new ResourceAccessException("boom", new ConnectException()))
                .willThrow(new ResourceAccessException("boom", new ConnectException()))
                .willReturn(expected);

        assertThat(resilientUserClient.getUserById("u1")).isSameAs(expected);
        verify(userClient, org.mockito.Mockito.times(3)).getUserById("u1");
    }

    @Test
    void doesNotRetryClientErrors() {
        given(userClient.getUserById("u2"))
                .willThrow(HttpClientErrorException.create(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "not found", null, null, null));

        assertThatThrownBy(() -> resilientUserClient.getUserById("u2"))
                .isInstanceOf(HttpClientErrorException.class);
        verify(userClient, org.mockito.Mockito.times(1)).getUserById("u2");
    }
}
