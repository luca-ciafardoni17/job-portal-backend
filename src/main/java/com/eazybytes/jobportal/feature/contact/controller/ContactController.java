package com.eazybytes.jobportal.feature.contact.controller;

import com.eazybytes.jobportal.constants.AppConstants;
import com.eazybytes.jobportal.feature.contact.dto.ContactResponseDto;
import com.eazybytes.jobportal.feature.contact.service.IContactService;
import com.eazybytes.jobportal.feature.contact.dto.ContactRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService contactService;

    @PostMapping("/public")
    public ResponseEntity<String> saveContactMsg(@RequestBody @Valid ContactRequestDto contactRequestDto) {
        boolean isSaved = this.contactService.saveContact(contactRequestDto);
        if (isSaved) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Request processing successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request processing failed");
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgs() {
        List<ContactResponseDto> contactResponseDtoList = contactService.fetchNewContactMsg();
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtoList);
    }

    @GetMapping("/sort/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgsWithSort(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        List<ContactResponseDto> contactResponseDtoList = contactService.fetchNewContactMsgWithSort(sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtoList);
    }

    @GetMapping("/page/admin")
    public ResponseEntity<Page<ContactResponseDto>> fetchNewContactMsgsWithSortAndPagination(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Page<ContactResponseDto> contactResponseDtoList = contactService
                .fetchNewContactMsgWithSortAndPagination(sortBy, sortDir, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtoList);
    }

    @PatchMapping("/{id}/status/admin")
    public ResponseEntity<String> updateContactMsg(@PathVariable Long id) {
        boolean isUpdated = contactService.updateContactMsgStatus(id, AppConstants.CLOSED_STATUS);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("Contact msg update successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Contact msg update failed");
        }
    }


}
