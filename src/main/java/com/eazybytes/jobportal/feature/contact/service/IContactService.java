package com.eazybytes.jobportal.feature.contact.service;

import com.eazybytes.jobportal.feature.contact.dto.ContactRequestDto;
import com.eazybytes.jobportal.feature.contact.dto.ContactResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IContactService {

    boolean saveContact(ContactRequestDto contactRequestDto);

    boolean updateContactMsgStatus(Long id, String status);

    List<ContactResponseDto> fetchNewContactMsg();

    List<ContactResponseDto> fetchNewContactMsgWithSort(String sortBy, String sortDir);

    Page<ContactResponseDto> fetchNewContactMsgWithSortAndPagination(String sortBy, String sortDir, Integer pageNumber, Integer pageSize);

}
