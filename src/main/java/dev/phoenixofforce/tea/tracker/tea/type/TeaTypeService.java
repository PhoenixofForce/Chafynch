package dev.phoenixofforce.tea.tracker.tea.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeaTypeService {

    private final TeaTypeRepository teaTypeRepository;

    public List<TeaTypeDto> findAll() {
        return TeaTypeDto.from(teaTypeRepository.findAll());
    }

}