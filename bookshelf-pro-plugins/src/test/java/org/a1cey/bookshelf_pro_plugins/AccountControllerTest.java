package org.a1cey.bookshelf_pro_plugins;

import java.util.UUID;

import org.a1cey.bookshelf_pro_application.account.CreateAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.DeleteAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.UpdateAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.result.CreateAccountResult;
import org.a1cey.bookshelf_pro_plugins.rest.account.AccountController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private CreateAccountUseCase createAccountUseCase;
    @Mock
    private DeleteAccountUseCase deleteAccountUseCase;
    @Mock
    private UpdateAccountUseCase updateAccountUseCase;

    @InjectMocks
    private AccountController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createAccount() throws Exception {
        var id = UUID.randomUUID();
        when(createAccountUseCase.execute(any())).thenReturn(new CreateAccountResult(id));

        mockMvc.perform(post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"alice\",\"password\":\"secret\",\"email\":\"a@b.com\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.accountId").value(id.toString()));
    }

    @Test
    void createAccountWithoutEmail() throws Exception {
        var id = UUID.randomUUID();
        when(createAccountUseCase.execute(any())).thenReturn(new CreateAccountResult(id));

        mockMvc.perform(post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"alice\",\"password\":\"secret\"}"
                            ))
               .andExpect(status().isOk());
    }

    @Test
    void updateAccount() throws Exception {
        var id = UUID.randomUUID();
        doNothing().when(updateAccountUseCase).execute(any());

        mockMvc.perform(patch("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"newName\":\"bob\"}"))
               .andExpect(status().isOk());

        verify(updateAccountUseCase).execute(any());
    }
}
