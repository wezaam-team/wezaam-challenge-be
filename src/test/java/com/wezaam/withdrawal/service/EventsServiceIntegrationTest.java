package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.verification.After;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventsServiceIntegrationTest {

    @MockBean
    TestService testService;

    @MockBean
    EventRepository eventRepository;

    @SpyBean
    EventsService eventsService;


    @Test
    void shouldRetry() {
        when(testService.test()).thenThrow(RuntimeException.class).thenThrow(RuntimeException.class).thenReturn(true);

        eventsService.send(new Withdrawal());

        verify(testService, new After(300, VerificationModeFactory.times(3))).test();
    }

    @Test
    void shouldRecover() {
        when(testService.test()).thenThrow(RuntimeException.class).thenThrow(RuntimeException.class).thenThrow(RuntimeException.class);

        eventsService.send(new Withdrawal());

        verify(testService, new After(300, VerificationModeFactory.times(3))).test();
        verify(eventsService, new After(100, VerificationModeFactory.times(1))).recover(any(), any(Withdrawal.class));
    }

}
