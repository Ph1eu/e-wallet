package com.project.Service;

import com.project.Repository.BalanaceInformationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional

public class TransactionHistoryService {
    @Autowired
    BalanaceInformationRepository balanaceInformationRepository;

}
