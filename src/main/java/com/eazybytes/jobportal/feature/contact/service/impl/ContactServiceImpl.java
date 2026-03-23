package com.eazybytes.jobportal.feature.contact.service.impl;

import com.eazybytes.jobportal.config.security.util.ApplicationUtil;
import com.eazybytes.jobportal.feature.contact.dto.ContactResponseDto;
import com.eazybytes.jobportal.feature.contact.repository.ContactRepository;
import com.eazybytes.jobportal.feature.contact.service.IContactService;
import com.eazybytes.jobportal.feature.contact.dto.ContactRequestDto;
import com.eazybytes.jobportal.feature.contact.entity.Contact;
import com.eazybytes.jobportal.util.TransformationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.eazybytes.jobportal.constants.AppConstants.NEW_STATUS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @CacheEvict(value = "contacts", allEntries = true)
    @Override
    @Transactional
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        boolean result = false;
        Contact contact = contactRepository.save(TransformationUtil.transformContactDtoToContact(contactRequestDto));
        if (contact != null && contact.getId() != null) {
            result = true;
        }
        return result;
    }

    @CacheEvict(value = "contacts", allEntries = true)
    @Override
    @Transactional
    public boolean updateContactMsgStatus(Long id, String status) {
        int updatedRows = contactRepository.updateStatusById(status, id, ApplicationUtil.getLoggedInUser());
        return updatedRows > 0;
    }

    @Cacheable(value = "contacts", key = "'normal'")
    @Override
    public List<ContactResponseDto> fetchNewContactMsg() {
        List<Contact> contacts = contactRepository.findContactsByStatus(NEW_STATUS);
        return contacts.stream().map(TransformationUtil::transformContactToContactDto).toList();
    }

    @Cacheable(value = "contacts", key = "'sorted'")
    @Override
    public List<ContactResponseDto> fetchNewContactMsgWithSort(String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        List<Contact> contacts = contactRepository.findContactsByStatus(NEW_STATUS, sort);
        return contacts.stream().map(TransformationUtil::transformContactToContactDto).toList();
    }

    @Cacheable(value = "contacts", key = "'paged'")
    @Override
    public Page<ContactResponseDto> fetchNewContactMsgWithSortAndPagination(String sortBy, String sortDir, Integer pageNumber, Integer pageSize) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Contact> contactsPage = contactRepository.findContactsByStatus(NEW_STATUS, pageable);
        return contactsPage.map(TransformationUtil::transformContactToContactDto);
    }

}
