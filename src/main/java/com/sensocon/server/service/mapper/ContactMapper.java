package com.sensocon.server.service.mapper;

import com.sensocon.server.domain.*;
import com.sensocon.server.service.dto.ContactDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contact and its DTO ContactDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {


    @Mapping(target = "notificationGroups", ignore = true)
    Contact toEntity(ContactDTO contactDTO);

    default Contact fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }
}
