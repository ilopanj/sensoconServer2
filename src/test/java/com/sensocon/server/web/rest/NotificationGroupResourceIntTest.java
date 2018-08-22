package com.sensocon.server.web.rest;

import com.sensocon.server.SensoconServer2App;

import com.sensocon.server.domain.NotificationGroup;
import com.sensocon.server.repository.NotificationGroupRepository;
import com.sensocon.server.service.NotificationGroupService;
import com.sensocon.server.service.dto.NotificationGroupDTO;
import com.sensocon.server.service.mapper.NotificationGroupMapper;
import com.sensocon.server.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.sensocon.server.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NotificationGroupResource REST controller.
 *
 * @see NotificationGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensoconServer2App.class)
public class NotificationGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private NotificationGroupRepository notificationGroupRepository;
    @Mock
    private NotificationGroupRepository notificationGroupRepositoryMock;

    @Autowired
    private NotificationGroupMapper notificationGroupMapper;
    
    @Mock
    private NotificationGroupService notificationGroupServiceMock;

    @Autowired
    private NotificationGroupService notificationGroupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificationGroupMockMvc;

    private NotificationGroup notificationGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationGroupResource notificationGroupResource = new NotificationGroupResource(notificationGroupService);
        this.restNotificationGroupMockMvc = MockMvcBuilders.standaloneSetup(notificationGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationGroup createEntity(EntityManager em) {
        NotificationGroup notificationGroup = new NotificationGroup()
            .name(DEFAULT_NAME);
        return notificationGroup;
    }

    @Before
    public void initTest() {
        notificationGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationGroup() throws Exception {
        int databaseSizeBeforeCreate = notificationGroupRepository.findAll().size();

        // Create the NotificationGroup
        NotificationGroupDTO notificationGroupDTO = notificationGroupMapper.toDto(notificationGroup);
        restNotificationGroupMockMvc.perform(post("/api/notification-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the NotificationGroup in the database
        List<NotificationGroup> notificationGroupList = notificationGroupRepository.findAll();
        assertThat(notificationGroupList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationGroup testNotificationGroup = notificationGroupList.get(notificationGroupList.size() - 1);
        assertThat(testNotificationGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createNotificationGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationGroupRepository.findAll().size();

        // Create the NotificationGroup with an existing ID
        notificationGroup.setId(1L);
        NotificationGroupDTO notificationGroupDTO = notificationGroupMapper.toDto(notificationGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationGroupMockMvc.perform(post("/api/notification-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationGroup in the database
        List<NotificationGroup> notificationGroupList = notificationGroupRepository.findAll();
        assertThat(notificationGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNotificationGroups() throws Exception {
        // Initialize the database
        notificationGroupRepository.saveAndFlush(notificationGroup);

        // Get all the notificationGroupList
        restNotificationGroupMockMvc.perform(get("/api/notification-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    public void getAllNotificationGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        NotificationGroupResource notificationGroupResource = new NotificationGroupResource(notificationGroupServiceMock);
        when(notificationGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restNotificationGroupMockMvc = MockMvcBuilders.standaloneSetup(notificationGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restNotificationGroupMockMvc.perform(get("/api/notification-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(notificationGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllNotificationGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        NotificationGroupResource notificationGroupResource = new NotificationGroupResource(notificationGroupServiceMock);
            when(notificationGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restNotificationGroupMockMvc = MockMvcBuilders.standaloneSetup(notificationGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restNotificationGroupMockMvc.perform(get("/api/notification-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(notificationGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getNotificationGroup() throws Exception {
        // Initialize the database
        notificationGroupRepository.saveAndFlush(notificationGroup);

        // Get the notificationGroup
        restNotificationGroupMockMvc.perform(get("/api/notification-groups/{id}", notificationGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notificationGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingNotificationGroup() throws Exception {
        // Get the notificationGroup
        restNotificationGroupMockMvc.perform(get("/api/notification-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationGroup() throws Exception {
        // Initialize the database
        notificationGroupRepository.saveAndFlush(notificationGroup);

        int databaseSizeBeforeUpdate = notificationGroupRepository.findAll().size();

        // Update the notificationGroup
        NotificationGroup updatedNotificationGroup = notificationGroupRepository.findById(notificationGroup.getId()).get();
        // Disconnect from session so that the updates on updatedNotificationGroup are not directly saved in db
        em.detach(updatedNotificationGroup);
        updatedNotificationGroup
            .name(UPDATED_NAME);
        NotificationGroupDTO notificationGroupDTO = notificationGroupMapper.toDto(updatedNotificationGroup);

        restNotificationGroupMockMvc.perform(put("/api/notification-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationGroupDTO)))
            .andExpect(status().isOk());

        // Validate the NotificationGroup in the database
        List<NotificationGroup> notificationGroupList = notificationGroupRepository.findAll();
        assertThat(notificationGroupList).hasSize(databaseSizeBeforeUpdate);
        NotificationGroup testNotificationGroup = notificationGroupList.get(notificationGroupList.size() - 1);
        assertThat(testNotificationGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationGroup() throws Exception {
        int databaseSizeBeforeUpdate = notificationGroupRepository.findAll().size();

        // Create the NotificationGroup
        NotificationGroupDTO notificationGroupDTO = notificationGroupMapper.toDto(notificationGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restNotificationGroupMockMvc.perform(put("/api/notification-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationGroup in the database
        List<NotificationGroup> notificationGroupList = notificationGroupRepository.findAll();
        assertThat(notificationGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotificationGroup() throws Exception {
        // Initialize the database
        notificationGroupRepository.saveAndFlush(notificationGroup);

        int databaseSizeBeforeDelete = notificationGroupRepository.findAll().size();

        // Get the notificationGroup
        restNotificationGroupMockMvc.perform(delete("/api/notification-groups/{id}", notificationGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NotificationGroup> notificationGroupList = notificationGroupRepository.findAll();
        assertThat(notificationGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationGroup.class);
        NotificationGroup notificationGroup1 = new NotificationGroup();
        notificationGroup1.setId(1L);
        NotificationGroup notificationGroup2 = new NotificationGroup();
        notificationGroup2.setId(notificationGroup1.getId());
        assertThat(notificationGroup1).isEqualTo(notificationGroup2);
        notificationGroup2.setId(2L);
        assertThat(notificationGroup1).isNotEqualTo(notificationGroup2);
        notificationGroup1.setId(null);
        assertThat(notificationGroup1).isNotEqualTo(notificationGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationGroupDTO.class);
        NotificationGroupDTO notificationGroupDTO1 = new NotificationGroupDTO();
        notificationGroupDTO1.setId(1L);
        NotificationGroupDTO notificationGroupDTO2 = new NotificationGroupDTO();
        assertThat(notificationGroupDTO1).isNotEqualTo(notificationGroupDTO2);
        notificationGroupDTO2.setId(notificationGroupDTO1.getId());
        assertThat(notificationGroupDTO1).isEqualTo(notificationGroupDTO2);
        notificationGroupDTO2.setId(2L);
        assertThat(notificationGroupDTO1).isNotEqualTo(notificationGroupDTO2);
        notificationGroupDTO1.setId(null);
        assertThat(notificationGroupDTO1).isNotEqualTo(notificationGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(notificationGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(notificationGroupMapper.fromId(null)).isNull();
    }
}
