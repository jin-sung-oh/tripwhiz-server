package com.example.demo.faq.service;

import com.example.demo.common.domain.CategoryEntity;
import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.common.repository.CategoryRepository;
import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.dto.FAQListDTO;
import com.example.demo.faq.dto.FAQModifyDTO;
import com.example.demo.faq.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class FAQService {

    private final FAQRepository faqRepository;

    private final CategoryRepository categoryRepository;

    // list
    @Transactional
    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("fno").descending());

        Page<FAQEntity> result = faqRepository.findAll(pageable);

        List<FAQListDTO> dtoList = result.get().map(FAQEntity -> {

            FAQListDTO dto = FAQListDTO.builder()
                    .fno(FAQEntity.getFno())
                    .question(FAQEntity.getQuestion())
                    .answer(FAQEntity.getAnswer())
                    .viewCnt(FAQEntity.getViewCnt())
                    .delFlag(false)
                    .build();

            return dto;

        }).collect(Collectors.toUnmodifiableList());

        long total = result.getTotalElements();

        return PageResponseDTO.<FAQListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(total)
                .build();

    }

    // add
    @Transactional
    public FAQEntity addFaq(FAQEntity faq) {

        // FAQ 저장
        FAQEntity savedFaq = faqRepository.save(faq);
        return savedFaq; // 저장된 FAQ 반환

    }



    // delete
    @Transactional
    public void softDeleteFAQ(Long fno) {

        int updatedRows = faqRepository.softDeleteByFno(fno);

        // fno가 0일때 삭제 안되게 처리
        if (updatedRows == 0) {
            throw new IllegalArgumentException("FAQ not found with fno: " + fno);
        }

    }

    // existsById 추가
    @Transactional(readOnly = true)
    public boolean existsById(Long fno) {
        return faqRepository.existsById(fno);
    }
}
