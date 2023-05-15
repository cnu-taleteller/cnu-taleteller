package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.tool.domain.Tool;
import com.cnu.taleteller.backend.domain.tool.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;

}
